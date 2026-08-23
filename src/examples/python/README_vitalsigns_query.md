# Vitals Load Generator & CSV Logger

A high‑throughput GraphQL client that sends synthetic **vital signs** updates at a precisely paced rate and logs every outbound payload to a CSV whose headers match an existing analytics workflow.

The tool supports:
- Fixed duration runs at a target **requests per second (RPS)**
- Multi‑threaded, scheduler‑paced requests with HTTP+GraphQL status capture
- **Deterministic heart rate (HR) stream** with **no duplicates per run**
- All other vitals change **every update** (to maximize bandwidth / change propagation)
- CSV output compatible with the provided analysis program
- Automatic creation of a patient row if one does not exist
- Detailed end‑of‑run metrics (latency percentiles, effective rate, HTTP status mix)

---

## 1) Quick Start

```bash
# from this folder (ensure Python 3.10+)
python vitalsigns_query.py --rps 100 --duration 10
```

Common options:
```bash
--patient 000d952b     # patient ID (also used in CSV)
--rps 600              # target requests per second
--duration 30          # how long to run (seconds)
--workers 18           # thread pool size (default: auto)
--lat-ms 20            # latency guess in ms (for auto workers)
--outfile myrun.csv    # explicit CSV path
--start-sim 16         # starting simTime value used in CSV
```

At the end you’ll see a summary and the path to the generated CSV, e.g.:
```
CSV log written to: outgoing_vitals_YYYYMMDD_HHMMSS.csv
```

---

## 2) Installation

```bash
python -m venv venv
# Windows PowerShell
.\venv\Scripts\Activate.ps1
# macOS/Linux
source venv/bin/activate

pip install -r requirements.txt  # if present
# or, minimally:
pip install requests
```

> The script uses only the standard library plus `requests`.

---

## 3) Configuration

The GraphQL endpoint is read from environment variable `GRAPHQL_ENDPOINT` (default: `http://localhost:8080/graphql`).

```bash
# Windows PowerShell
$env:GRAPHQL_ENDPOINT = "http://localhost:8080/graphql"
# macOS/Linux
export GRAPHQL_ENDPOINT="http://localhost:8080/graphql"
```

The tool will:
1. Probe the server with `query { __typename }` for a quick health/latency check.
2. Fetch `itemId` for the selected patient.
3. **Create** a minimal row if missing, then re‑fetch `itemId`.
4. Start the paced send loop.

---

## 4) What It Sends

Each update is a GraphQL mutation shaped like this:

```graphql
mutation VitalsUpdate {
  vitalSignsUpdate(vitalSigns: {
    patientId: "000d952b"
    itemIdentifier: { itemId: 0 }
    heartRate: <unique, monotonically increasing>
    diastolicBloodPressure: <changes every update>
    systolicBloodPressure:  <changes every update>
    peripheralOxygenSaturation: <changes every update>
    temperatureFahrenheit: <changes every update>
    respirationEndTidalCarbonDioxide: <changes every update>
    respirationRate: <changes every update>
  })
}
```

### Value rules (default scales match your reference data)
- **HR**: starts at 1 and increases by **1 per update**, **no duplicates per run** (thread‑safe counter).
- **DBP/SBP**: DBP then SBP order; both change each update using small periodic deltas.
- **SpO₂, Temp°F, EtCO₂, RR**: all change each update (smooth oscillators / tiny steps) to ensure the middleware treats every field as **changed**.
- **simTime**: increments based on `--rps` (e.g., at 10 Hz it rises by 0.01 per update).  
- **timeUnit**: the first row has literal **`MILLISECONDS`**; subsequent rows show cumulative milliseconds since the start (string), matching the analyzer’s expectations.

---

## 5) CSV Output

Headers (first eight match the analyzer’s schema; extra audit columns at the end are ignored by your analyzer):

```
patientId (HLAASCIIstring)
heartRate (Integer32BE)
diastolicBloodPressure (Integer32BE)
systolicBloodPressure (Integer32BE)
peripheralOxygenSaturation (FloatType32BE)
temperatureFahrenheit (FloatType32BE)
respirationEndTidalCarbonDioxide (FloatType32BE)
respirationRate (FloatType32BE)
simTime (HLAinteger64Time)
timeUnit (unitTimeEnum)
seq
status_code
ok
gql_error
elapsed_ms
```

Notes:
- `timeUnit` is **`MILLISECONDS`** only for the first row; afterwards it is the string form of cumulative milliseconds (e.g., `10`, `20`, …).
- `simTime` scales with `--rps`. Example: at `--rps 100`, `period=10 ms`, so `simTime += 0.001` per update.
- The CSV is **sorted by `seq`** before writing so rows are deterministic even with multiple workers.

---

## 6) Performance Metrics

After a run you’ll see:
- **Latency** percentiles measured per request (wall time client‑side).
- **Effective rate** = completed / (last_complete − first_submit).
- HTTP status breakdown (200 OK counts, etc.).
- “Behind‑schedule ticks” (if enabled in your version) count how many scheduler slots were already in the past when their work was queued—an indicator of pacing strain.

Expected behavior on a responsive local server:
- Low‑ms median latencies with occasional ~2‑second spikes if your server has a GC/lock/timer quirk (observed previously).
- Effective rate approaches target RPS as worker count rises up to the server’s capacity.

---

## 7) Tips & Troubleshooting

**I get `vitalSignsByPatientId: None`**  
The tool will create a row automatically—ensure your GraphQL backend permits the `vitalSignsUpdate` mutation with minimal fields.

**The analyzer needs exact header names**  
They’re already matched. Extra columns at the end are safe; the analyzer ignores unknown columns.

**I need every field to change each update**  
Already done. If you want larger swings, tune the small deltas/oscillator amplitudes in the send loop.

**I see repeated HR values**  
This version uses a **global thread‑safe counter** assigned before queuing each request, guaranteeing unique per‑run HRs even under heavy concurrency.

**Windows console errors about Unicode**  
The script prints ASCII‑only logs. If you add symbols (arrows, etc.), set `PYTHONIOENCODING=utf8` or run in a UTF‑8 terminal.

---

## 8) Development Notes

- Networking is done via a single shared `requests.Session` with an enlarged connection pool for reuse and throughput.
- If your GraphQL layer returns errors in the 200 OK envelope, they will be reported under `gql_error` and still logged to CSV.
- The pacing scheduler submits work at fixed intervals; with high `--rps`, increase `--workers` (rule of thumb: `workers ≈ rps * (lat_ms/1000) * 1.5`).

---

## 9) Example Runs

```bash
# 10 Hz for 60s, auto workers
python vitalsigns_query.py --patient 000d952b --rps 10 --duration 60

# 600 rps for 5s, 18 workers, custom CSV name
python vitalsigns_query.py --patient 000d952b --rps 600 --duration 5 --workers 18 --outfile myrun.csv

# Start simTime at 32.5
python vitalsigns_query.py --rps 100 --duration 10 --start-sim 32.5
```

---

## 10) License

Proprietary / internal testing tool. Adapt as needed for your environment.
