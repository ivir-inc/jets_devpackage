# vitalsigns_query.py
import time
import argparse
from collections import Counter
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed
import csv

import graphql  # requires graphql.py from earlier (session-based client)

# ---------- tiny utils ----------
def ts() -> str:
    return datetime.now().isoformat(timespec="milliseconds")

def log(msg: str):
    print(f"{ts()} | {msg}")  # ASCII-safe

def clamp(v, lo, hi):
    return lo if v < lo else hi if v > hi else v

def percentile(values, p):
    if not values:
        return 0
    s = sorted(values)
    idx = max(0, min(len(s) - 1, int(round((p / 100.0) * (len(s) - 1)))))
    return s[idx]

def default_workers(rps: float, lat_ms_guess: float) -> int:
    # concurrency ≈ rps * latency; 50% headroom, clamp 4..1024
    return max(4, min(1024, int(rps * (lat_ms_guess / 1000.0) * 1.5) or 4))

# ---------- GraphQL bits ----------
LIGHT_QUERY = "query { __typename }"

GET_BY_PATIENT_ID_TPL = (
    'query VitalsByPatient { vitalSignsByPatientId (patientId:"%s"){ '
    'itemIdentifier { itemId instanceName } heartRate respirationRate } }'
)

# Create a new vitals row if patient is missing (attached-like baseline)
CREATE_IF_MISSING_TPL = (
    'mutation CreateVitals{ vitalSignsUpdate(vitalSigns:{ '
    'patientId: "%s" '
    'heartRate: 1 '
    'diastolicBloodPressure: 81 '      # DBP first
    'systolicBloodPressure: 120 '      # SBP second
    'peripheralOxygenSaturation: 0.98 ' # fraction scale like attached
    'temperatureFahrenheit: 98.6 '
    'respirationEndTidalCarbonDioxide: 0.055 '
    'respirationRate: 16.00 '
    '}) }'
)

# Mutation sends diastolic before systolic; values are already swapped/corrected
def build_update_mutation(patient_id: str, item_id: int, hr: int,
                          dbp: int, sbp: int, spo2: float, temp_f: float,
                          etco2: float, rr: float) -> str:
    return f"""
mutation VitalsUpdate {{
  vitalSignsUpdate(vitalSigns:{{
    patientId: "{patient_id}"
    itemIdentifier: {{ itemId: {item_id} }}
    heartRate: {hr}
    diastolicBloodPressure: {dbp}
    systolicBloodPressure: {sbp}
    peripheralOxygenSaturation: {spo2}
    temperatureFahrenheit: {temp_f}
    respirationEndTidalCarbonDioxide: {etco2}
    respirationRate: {rr}
  }})
}}
"""

# Does our graphql helper expose status-aware calls?
HAS_STATUS = hasattr(graphql, "execute_mutation_status") and hasattr(graphql, "execute_query_status")

def do_mutation(mutation: str):
    """
    Execute a mutation and return:
      dict(status_code:int, ok:bool, gql_error:bool, elapsed_ms:int, json:dict|None)
    """
    t0 = time.monotonic()
    if HAS_STATUS:
        res = graphql.execute_mutation_status(mutation)
        elapsed_ms = max(0, int((time.monotonic() - t0) * 1000))
        body = res.get("json")
        gql_error = bool(body and isinstance(body, dict) and body.get("errors"))
        return {
            "status_code": int(res.get("status_code", 0)),
            "ok": bool(res.get("ok", False)),
            "gql_error": gql_error,
            "elapsed_ms": elapsed_ms,
            "json": body,
        }
    else:
        try:
            body = graphql.execute_mutation(mutation)
            elapsed_ms = max(0, int((time.monotonic() - t0) * 1000))
            gql_error = bool(body and isinstance(body, dict) and body.get("errors"))
            return {"status_code": 200, "ok": not gql_error, "gql_error": gql_error, "elapsed_ms": elapsed_ms, "json": body}
        except Exception:
            elapsed_ms = max(0, int((time.monotonic() - t0) * 1000))
            return {"status_code": 0, "ok": False, "gql_error": True, "elapsed_ms": elapsed_ms, "json": None}

def extract_item_id(qres_json: dict) -> int:
    data = qres_json.get("data", {})
    node = data.get("vitalSignsByPatientId")
    if node is None:
        raise RuntimeError(f"No vitals found for patient. Full result: {qres_json}")
    if isinstance(node, list):
        if not node:
            raise RuntimeError("Vitals list is empty for patient.")
        node = node[0]
    ident = node.get("itemIdentifier") or {}
    if "itemId" not in ident:
        raise RuntimeError(f"itemIdentifier missing itemId. Node: {node}")
    return int(ident["itemId"])

def ensure_item_id(patient_id: str) -> int:
    """Return itemId for patient; create row if it doesn't exist yet."""
    get_query = GET_BY_PATIENT_ID_TPL % patient_id

    def _get_json():
        if HAS_STATUS:
            r = graphql.execute_query_status(get_query)
            return r.get("json")
        else:
            return graphql.execute_query(get_query)

    # First try
    j = _get_json()
    node = j.get("data", {}).get("vitalSignsByPatientId")
    if node is not None:
        return extract_item_id(j)

    # Missing → create
    log(f"No vitals for '{patient_id}', creating …")
    create_mut = CREATE_IF_MISSING_TPL % patient_id
    if HAS_STATUS:
        cres = graphql.execute_mutation_status(create_mut)
        log(f"Create HTTP {cres['status_code']} -> {cres.get('json')}")
    else:
        cres = graphql.execute_mutation(create_mut)
        log(f"Create -> {cres}")

    # Re-query with a few short retries to allow the row to appear
    for _ in range(20):
        time.sleep(0.05)  # 50ms
        j2 = _get_json()
        node2 = j2.get("data", {}).get("vitalSignsByPatientId")
        if node2 is not None:
            return extract_item_id(j2)

    raise RuntimeError(f"Created '{patient_id}' but could not fetch itemId after retries.")

# ---------- CSV (match attached headers; extra audit columns at end) ----------
CSV_HEADERS = [
    "patientId (HLAASCIIstring)",
    "heartRate (Integer32BE)",
    "diastolicBloodPressure (Integer32BE)",
    "systolicBloodPressure (Integer32BE)",
    "peripheralOxygenSaturation (FloatType32BE)",
    "temperatureFahrenheit (FloatType32BE)",
    "respirationEndTidalCarbonDioxide (FloatType32BE)",
    "respirationRate (FloatType32BE)",
    "simTime (HLAinteger64Time)",
    "timeUnit (unitTimeEnum)",
    # audit columns
    "seq", "status_code", "ok", "gql_error", "elapsed_ms",
]

def make_csv_row(patient_id, seq, hr, dbp, sbp, spo2, temp_f, etco2, rr,
                 sim_time_value, time_unit_field,
                 status_code, ok, gql_error, elapsed_ms):
    return {
        "patientId (HLAASCIIstring)": patient_id,
        "heartRate (Integer32BE)": hr,
        "diastolicBloodPressure (Integer32BE)": dbp,
        "systolicBloodPressure (Integer32BE)": sbp,
        "peripheralOxygenSaturation (FloatType32BE)": f"{spo2:.4f}".rstrip("0").rstrip("."),  # e.g., 0.98
        "temperatureFahrenheit (FloatType32BE)": f"{temp_f:.1f}",                               # e.g., 98.6
        "respirationEndTidalCarbonDioxide (FloatType32BE)": f"{etco2:.3f}",                     # e.g., 0.055
        "respirationRate (FloatType32BE)": f"{rr:.2f}",                                         # e.g., 16.01
        "simTime (HLAinteger64Time)": f"{sim_time_value:.2f}",                                  # e.g., 16.01
        "timeUnit (unitTimeEnum)": time_unit_field,                                             # 'MILLISECONDS' for first row
        "seq": seq,
        "status_code": status_code,
        "ok": ok,
        "gql_error": gql_error,
        "elapsed_ms": elapsed_ms,
    }

# ---------- main ----------
def main():
    ap = argparse.ArgumentParser(description="Multithreaded vitals sender (CSV matches attached format).")
    ap.add_argument("--patient", default="000d952b", help='Patient ID for CSV & mutation (default: "000d952b")')
    ap.add_argument("--rps", type=float, default=10.0, help="Target requests per second")
    ap.add_argument("--duration", type=float, default=60.0, help="Duration in seconds")
    ap.add_argument("--workers", type=int, default=None, help="Thread pool size (default: auto)")
    ap.add_argument("--lat-ms", type=float, default=20.0, help="Latency guess (ms) for auto workers")
    ap.add_argument("--outfile", default=None, help='CSV path (default: outgoing_vitals_<timestamp>.csv)')
    ap.add_argument("--start-sim", type=float, default=16.00, help="Starting simTime value (matches attached default)")
    args = ap.parse_args()

    if args.rps <= 0 or args.duration <= 0:
        raise SystemExit("--rps and --duration must be > 0")

    total = int(args.rps * args.duration)
    interval = 1.0 / args.rps
    period_ms = int(round(1000.0 / args.rps))
    workers = args.workers or default_workers(args.rps, args.lat_ms)

    outname = args.outfile or f"outgoing_vitals_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"
    log(
        f"Config: patient={args.patient} rps={args.rps} duration={args.duration}s total≈{total} "
        f"workers={workers} period={period_ms}ms csv_out={outname} start_sim={args.start_sim:.2f}"
    )

    # Probe
    log("Light probe (__typename)...")
    t0 = time.monotonic()
    if HAS_STATUS:
        probe = graphql.execute_query_status(LIGHT_QUERY)
        log(f"Probe HTTP {probe['status_code']} in {(time.monotonic()-t0)*1000:.0f} ms -> {probe['json']}")
    else:
        probe = graphql.execute_query(LIGHT_QUERY)
        log(f"Probe returned in {(time.monotonic()-t0)*1000:.0f} ms -> {probe}")

    # Resolve itemId (create if missing)
    log(f"Fetching itemId for '{args.patient}'...")
    t0 = time.monotonic()
    item_id = ensure_item_id(args.patient)
    log(f"Using itemId={item_id} (lookup/create-if-needed {(time.monotonic()-t0)*1000:.0f} ms)")

    # Baseline values (match attached scales)
    base_dbp, base_sbp = 81, 120                 # diastolic, systolic
    base_spo2 = 0.98                              # fraction
    base_temp_f = 98.6
    base_etco2 = 0.055
    base_rr = 16.00

    # Result storage
    results = []
    http_codes = Counter()
    gql_errs = 0
    behind = 0

    # Pacing + concurrency (unique HR derived from seq so no duplicates)
    start = time.monotonic()
    first_submit_ts = None
    last_complete_ts = None

    log(f"Starting paced loop at {args.rps} rps for {args.duration:.1f}s (interval={interval*1000:.1f} ms)")
    with ThreadPoolExecutor(max_workers=workers) as pool:
        futures = []

        for i in range(total):
            scheduled = start + i * interval
            now = time.monotonic()
            if now < scheduled:
                time.sleep(scheduled - now)
            else:
                if (now - scheduled) > 0.001:
                    behind += 1

            # ---- deterministic seq & unique HR per run ----
            seq = i + 1
            hr = 1 + (seq - 1)  # 1,2,3,... unique

            # All other vitals change every tick to force max payloads
            # small bounded oscillations / drifts to ensure a change each time
            sbp = base_sbp + ((i % 5) - 2)          # ±2
            dbp = base_dbp + (((i + 1) % 5) - 2)    # ±2, phase-shifted vs SBP
            spo2 = clamp(base_spo2 + (((i % 13) - 6) * 0.0005), 0.95, 1.00)
            temp_f = base_temp_f + (((i % 11) - 5) * 0.1)
            etco2 = clamp(base_etco2 + (((i % 9) - 4) * 0.001), 0.02, 0.08)
            rr = base_rr + (i * 0.01)               # +0.01 per sample

            # simTime: increments by period_ms/10000 (so 10ms -> +0.001)
            cum_ms = i * period_ms
            sim_time_value = args.start_sim + (cum_ms / 10000.0)

            # timeUnit: first row literal 'MILLISECONDS', subsequent rows show cumulative ms
            time_unit_field = "MILLISECONDS" if seq == 1 else (str(cum_ms) if cum_ms > 0 else "")

            mutation = build_update_mutation(args.patient, item_id, hr, dbp, sbp, spo2, temp_f, etco2, rr)

            def task(_seq=seq, _hr=hr, _dbp=dbp, _sbp=sbp, _spo2=spo2, _temp=temp_f, _etco2=etco2, _rr=rr,
                     _sim=sim_time_value, _tu=time_unit_field, _mutation=mutation):
                submit_ts = time.monotonic()
                res = do_mutation(_mutation)
                complete_ts = time.monotonic()
                return {
                    "seq": _seq,
                    "status_code": res["status_code"],
                    "ok": res["ok"],
                    "gql_error": res["gql_error"],
                    "elapsed_ms": res["elapsed_ms"],
                    "submitted_ts": submit_ts,
                    "completed_ts": complete_ts,
                    "csv_row": make_csv_row(args.patient, _seq, _hr, _dbp, _sbp, _spo2, _temp, _etco2, _rr,
                                            _sim, _tu, res["status_code"], res["ok"], res["gql_error"], res["elapsed_ms"]),
                }

            futures.append(pool.submit(task))

        # drain completions
        for idx, fut in enumerate(as_completed(futures), 1):
            r = fut.result()
            results.append(r)
            code = r.get("status_code", 0)
            http_codes[code] += 1
            if r.get("gql_error"):
                gql_errs += 1
            if first_submit_ts is None or r["submitted_ts"] < first_submit_ts:
                first_submit_ts = r["submitted_ts"]
            if last_complete_ts is None or r["completed_ts"] > last_complete_ts:
                last_complete_ts = r["completed_ts"]

    # Sort by seq so CSV is deterministic even with concurrency
    results.sort(key=lambda x: int(x["seq"]))
    csv_rows = [r["csv_row"] for r in results]

    # Write CSV
    outname = outname  # keep same variable name
    with open(outname, "w", newline="", encoding="utf-8") as f:
        w = csv.DictWriter(f, fieldnames=CSV_HEADERS)
        w.writeheader()
        w.writerows(csv_rows)

    # Summary/timings
    wall = (last_complete_ts - first_submit_ts) if (first_submit_ts is not None and last_complete_ts is not None) else 0.0
    eff_rate = (len(results) / wall) if wall > 0 else float("inf")
    lat = [int(r["elapsed_ms"]) for r in results]
    p50, p95, p99 = percentile(lat, 50), percentile(lat, 95), percentile(lat, 99)

    log("Run complete.\n")
    print("=== Summary ===")
    print(f"Requested: {total}  Completed: {len(results)}  Workers: {workers}")
    print(f"Wall (first submit -> last complete): {wall:.3f}s  Effective rate: {eff_rate:.2f} req/sec")
    print(f"Latency (ms): p50={p50}  p95={p95}  p99={p99}  min={min(lat) if lat else 0}  max={max(lat) if lat else 0}")
    print(f"Behind-schedule ticks: {behind}\n")

    if http_codes:
        tot = sum(http_codes.values())
        ok200 = http_codes.get(200, 0)
        print("HTTP status breakdown:")
        print(f"  200 OK: {ok200}/{tot} ({(ok200/tot*100):.1f}%)")
    print(f"GraphQL responses with errors[]: {gql_errs}\n")

    print(f"CSV log written to: {outname}")

if __name__ == "__main__":
    main()
