
import argparse
import math
from typing import List, Dict, Tuple, Optional, Iterable
import pandas as pd
import os
import sys
from pathlib import Path

# Gap audit default (can be disabled with --no-gap-audit)
GAP_AUDIT_ENABLED_DEFAULT = True
# Skip the first linear sequence value per patient (setup line)
SKIP_FIRST_SEQ_DEFAULT = True
STARTUP_FILTER_ENABLED_DEFAULT = True
STARTUP_LOCK_K_DEFAULT = 5

# Aggregate gap stats across all patients in a run
GAP_STATS = {
    'patients': 0,
    'missing': 0,
    'extra': 0,
    'union_total': 0,
    'linear_total': 0,
    'recorder_total': 0,
    'linear_raw_total': 0,    # count of rows (after filters) in linear
    'recorder_raw_total': 0,  # count of rows (after filters) in recorder

    # New accounting fields
    'lin_duplicates_collapsed': 0,         # sum over patients: (len(linear_raw) - len(unique))
    'rec_duplicates_collapsed': 0,         # same for recorder
    'lin_skip_first_dropped': 0,           # counts of seq=1 dropped from linear
    'lin_startup_lock_dropped': 0,         # how many linear seq < lock_seq dropped
    'rec_startup_lock_dropped': 0,         # how many recorder seq < lock_seq dropped
    'burn_in_skipped_common': 0,           # how many common seq IDs skipped by --burn-in
}

def print_gap_summary():
    if 'GAP_AUDIT_ENABLED' in globals() and GAP_AUDIT_ENABLED:
        try:
            total_union = GAP_STATS.get('union_total', 0) or 0
            total_missing = GAP_STATS.get('missing', 0) or 0
            total_extra = GAP_STATS.get('extra', 0) or 0
            total_linear = GAP_STATS.get('linear_total', 0) or 0
            total_rec = GAP_STATS.get('recorder_total', 0) or 0
            total_pat = GAP_STATS.get('patients', 0) or 0

            def pct(n, d):
                return (100.0 * n / d) if d else 0.0

            overall_unmatched = total_missing + total_extra
            pct_unmatched_vs_union = pct(overall_unmatched, total_union)
            pct_missing_vs_linear = pct(total_missing, total_linear)
            pct_extra_vs_rec = pct(total_extra, total_rec)

            print("\\n— Gap summary —")
            print(f"{'Patients analyzed:':28s} {total_pat:10d}")
            print(f"{'Missing in recorder:':28s} {total_missing:10d} | {'Extra in recorder:':20s} {total_extra:10d}")
            print(f"{'Total unique seq IDs (union):':28s} {total_union:10d}")
            #print(f"{'Total rows (linear, non-unique):':28s} {GAP_STATS.get('linear_raw_total',0):10d}")
            #print(f"{'Total rows (recorder, non-unique):':28s} {GAP_STATS.get('recorder_raw_total',0):10d}")
            print(f"{'Unmatched vs union:':28s} {pct_unmatched_vs_union:9.2f}%")
            #print(f"{'Missing vs total linear:':28s} {pct_missing_vs_linear:9.2f}%")
            #print(f"{'Extra vs total recorder:':28s} {pct_extra_vs_rec:9.2f}%")

            # New detailed accounting section
#             print("\\n— Sequence accounting —")
#             print(f"{'Linear duplicates collapsed:':28s} {GAP_STATS.get('lin_duplicates_collapsed',0):10d}")
#             print(f"{'Recorder duplicates collapsed:':28s} {GAP_STATS.get('rec_duplicates_collapsed',0):10d}")
#             print(f"{'Linear skip-first dropped:':28s} {GAP_STATS.get('lin_skip_first_dropped',0):10d}")
#             print(f"{'Linear startup-lock dropped:':28s} {GAP_STATS.get('lin_startup_lock_dropped',0):10d}")
#             print(f"{'Recorder startup-lock dropped:':28s} {GAP_STATS.get('rec_startup_lock_dropped',0):10d}")
#             print(f"{'Burn-in skipped (common):':28s} {GAP_STATS.get('burn_in_skipped_common',0):10d}")
        except Exception:
            pass

# ---------- Bandwidth estimation ----------

def _detect_time_col(cols):
    """Prefer entryTimeMs (ms) in recorder; fallback to simTime (s)."""
    for c in cols:
        name = str(c).lower()
        if 'entrytimems' in name or 'entry_time_ms' in name:
            return c  # milliseconds
    for c in cols:
        if 'simtime' in str(c).lower():
            return c  # seconds
    return None

def _num(x):
    import pandas as pd
    return pd.to_numeric(x, errors='coerce')

def print_bandwidth_summary(recorder_path: str,
                            linear_paths: Optional[List[str]],
                            overhead_factor: float = 1.0):
    try:
        if not recorder_path or not os.path.exists(recorder_path):
            return
        size_bytes = os.path.getsize(recorder_path)
        rec = pd.read_csv(recorder_path, dtype=str, keep_default_na=False)
        if rec.empty:
            return
        rec.columns = [normalize_column(c) for c in rec.columns]
        time_col = _detect_time_col(rec.columns)
        if not time_col:
            return
        t = _num(rec[time_col])
        t = t[t.notna()]
        if t.empty:
            return
        name_lc = str(time_col).lower()
        if ('entrytimems' in name_lc) or ('entry_time_ms' in name_lc) or name_lc.endswith('ms'):
            duration = float((t.max() - t.min()) / 1000.0)
        else:
            duration = float(t.max() - t.min())
        if duration <= 0:
            return
        rows_total = len(rec)
        bps = (size_bytes * overhead_factor * 8.0) / duration
        Bps = (size_bytes * overhead_factor) / duration

        print("\\n— Bandwidth estimate (recorder) —")
        print(f"{'Recorder file size:':28s} {size_bytes:10d} bytes")
        print(f"{'Time span:':28s} {duration:10.3f} s")
        print(f"{'Avg bandwidth:':28s} {Bps/1_000_000:10.3f} MB/s  |  {bps/1_000_000:10.3f} Mb/s")
        print(f"{'Row rate:':28s} {rows_total/duration:10.3f} rows/s  (rows: {rows_total})")
    except Exception:
        pass

# ---------- Helpers ----------

def normalize_column(col: str) -> str:
    return col.strip().lower().replace(' ', '_')

def base_column_name(col: str) -> str:
    return normalize_column(col).split('_(')[0]

def find_column(cols: List[str], needle: str) -> Optional[str]:
    needle = needle.lower()
    for c in cols:
        if needle in c.lower():
            return c
    return None

def find_by_base(cols: List[str], base: str) -> Optional[str]:
    base = base.lower()
    for c in cols:
        if base_column_name(c) == base:
            return c
    return None

def get_patient_id_value(df: pd.DataFrame) -> Optional[str]:
    cand = find_column(df.columns.tolist(), 'patientid') or find_column(df.columns.tolist(), 'patient_id')
    if cand is None:
        return None
    vals = df[cand].dropna().astype(str).unique().tolist()
    if len(vals) == 0:
        return None
    return vals[0]

def sort_by_simtime_coerced(df: pd.DataFrame) -> Tuple[pd.DataFrame, str]:
    sim_col = next((c for c in df.columns if 'simtime' in c.lower()), None)
    if sim_col is None:
        raise ValueError('No simTime column found')
    sim_num = pd.to_numeric(df[sim_col], errors='coerce')
    return df.loc[sim_num.sort_values().index], sim_col

def match_column_maps(recorder_df: pd.DataFrame, linear_df: pd.DataFrame, skip_bases: Iterable[str]=()) -> Tuple[List[str], List[str]]:
    internal_skip = {"__orig_index__"}
    linear_col_map = {base_column_name(col): col for col in linear_df.columns}
    rec_cols, lin_cols = [], []
    for col in recorder_df.columns:
        b = base_column_name(col)
        if b.startswith("__") or b in internal_skip or b in skip_bases:
            continue
        if b in linear_col_map:
            rec_cols.append(col)
            lin_cols.append(linear_col_map[b])
    return rec_cols, lin_cols

def find_startup_lock(lin_set: set, rec_set: set, k: int = 5):
    if not lin_set or not rec_set or k <= 0:
        return None
    for s in sorted(lin_set):
        ok = True
        for i in range(k):
            t = s + i
            if (t not in lin_set) or (t not in rec_set):
                ok = False
                break
        if ok:
            return s
    return None

# ---------- Comparison ----------

def compare_pair_seq(recorder_df: pd.DataFrame,
                     linear_df: pd.DataFrame,
                     abs_tol: float,
                     linear_skip_rows: List[int],
                     recorder_offset_header: int,
                     linear_offset_header: int,
                     patient_id: str,
                     seq_base: str,
                     burn_in: int,
                     diff_dp: int) -> pd.DataFrame:

    if recorder_df.empty or linear_df.empty:
        return pd.DataFrame()

    recorder_df = recorder_df.copy()
    linear_df = linear_df.copy()
    recorder_df.columns = [normalize_column(c) for c in recorder_df.columns]
    linear_df.columns = [normalize_column(c) for c in linear_df.columns]

    recorder_df["__orig_index__"] = recorder_df.index
    linear_df["__orig_index__"] = linear_df.index

    rec_seq_col = find_by_base(recorder_df.columns.tolist(), seq_base)
    lin_seq_col = find_by_base(linear_df.columns.tolist(), seq_base)

    mismatches = []

    if rec_seq_col and lin_seq_col:
        rec_seq_all = pd.to_numeric(recorder_df[rec_seq_col], errors='coerce')
        lin_seq_all = pd.to_numeric(linear_df[lin_seq_col], errors='coerce')

        recorder_df = recorder_df[rec_seq_all.notna()]
        linear_df = linear_df[lin_seq_all.notna()]

        rec_seq = pd.to_numeric(recorder_df[rec_seq_col], errors='coerce').astype('int64')
        lin_seq = pd.to_numeric(linear_df[lin_seq_col], errors='coerce').astype('int64')

        # Duplicates collapsed accounting
        lin_unique_before = set(lin_seq.tolist())
        rec_unique_before = set(rec_seq.tolist())
        GAP_STATS['lin_duplicates_collapsed'] += (len(lin_seq) - len(lin_unique_before))
        GAP_STATS['rec_duplicates_collapsed'] += (len(rec_seq) - len(rec_unique_before))

        rec_first_idx = {}
        for i, s in zip(recorder_df["__orig_index__"].tolist(), rec_seq.tolist()):
            if s not in rec_first_idx:
                rec_first_idx[s] = i
        lin_first_idx = {}
        for i, s in zip(linear_df["__orig_index__"].tolist(), lin_seq.tolist()):
            if s not in lin_first_idx:
                lin_first_idx[s] = i

        rec_set = set(rec_seq.tolist())
        lin_set = set(lin_seq.tolist())

        # Keep copies for accounting

        lin_set_before_filters = set(lin_set)
        rec_set_before_filters = set(rec_set)

        # --- Skip-first (linear) ---
        if 'SKIP_FIRST_SEQ' in globals() and SKIP_FIRST_SEQ:
            if 1 in lin_set:
                lin_set.discard(1)
                GAP_STATS['lin_skip_first_dropped'] += 1

            # --- Startup stabilization ---
            if 'STARTUP_FILTER_ENABLED' in globals() and STARTUP_FILTER_ENABLED:
                try:
                    k = int(STARTUP_LOCK_K)
                except Exception:
                    k = 5
                lock_seq = find_startup_lock(lin_set, rec_set, k=k)
                if lock_seq is not None:
                    # Count how many got dropped by startup lock (beyond skip-first) on each side
                    lin_dropped = sum(1 for s in lin_set if s < lock_seq)
                    rec_dropped = sum(1 for s in rec_set if s < lock_seq)
                    GAP_STATS['lin_startup_lock_dropped'] += lin_dropped
                    GAP_STATS['rec_startup_lock_dropped'] += rec_dropped

                    rec_set = {s for s in rec_set if s >= lock_seq}
                    lin_set = {s for s in lin_set if s >= lock_seq}

        
        # ----- Composite seq support (patientId+seq) for gap/union math -----
        use_composite = globals().get('COMPOSITE_SEQ', False)
        if use_composite:
            # Build composite strings *after* filters are applied
            rec_set_comp = { f"{patient_id}:{s}" for s in rec_set }
            lin_set_comp = { f"{patient_id}:{s}" for s in lin_set }
            # Use composite sets for gap/union computations
            missing_in_rec = sorted(lin_set_comp - rec_set_comp)
            extra_in_rec = sorted(rec_set_comp - lin_set_comp)
            union_size = len(rec_set_comp | lin_set_comp)
        else:
            missing_in_rec = sorted(lin_set - rec_set)
            extra_in_rec = sorted(rec_set - lin_set)
            union_size = len(rec_set | lin_set)

        # Gaps after filters
        missing_in_rec = sorted(lin_set - rec_set)
        extra_in_rec = sorted(rec_set - lin_set)

        if 'GAP_AUDIT_ENABLED' in globals() and GAP_AUDIT_ENABLED:
            try:
                GAP_STATS['patients'] += 1
                GAP_STATS['missing'] += len(missing_in_rec)
                GAP_STATS['extra'] += len(extra_in_rec)
                GAP_STATS['union_total'] += union_size
                GAP_STATS['linear_total'] += len(lin_set)
                GAP_STATS['recorder_total'] += len(rec_set)
                GAP_STATS['linear_raw_total'] += len(lin_seq)
                GAP_STATS['recorder_raw_total'] += len(rec_seq)
            except Exception:
                pass

            for s in missing_in_rec:
                lin_idx = lin_first_idx.get(s, None)
                linear_row_number = (lin_idx + linear_offset_header) if lin_idx is not None else None
                mismatches.append({
                    "PatientId": patient_id,
                    "Column": f"{base_column_name(lin_seq_col)} (gap)",
                    "Expected (Linear File)": s,
                    "Actual (Recorder File)": "",
                    "Linear File Row": linear_row_number,
                    "Recorder File Row": None,
                    "Difference": None,
                    "Linear File": None,
                    "Recorder File": None,
                })
            for s in extra_in_rec:
                rec_idx = rec_first_idx.get(s, None)
                recorder_row_number = (rec_idx + recorder_offset_header) if rec_idx is not None else None
                mismatches.append({
                    "PatientId": patient_id,
                    "Column": f"{base_column_name(rec_seq_col)} (gap)",
                    "Expected (Linear File)": "",
                    "Actual (Recorder File)": s,
                    "Linear File Row": None,
                    "Recorder File Row": recorder_row_number,
                    "Difference": None,
                    "Linear File": None,
                    "Recorder File": None,
                })

        common_seq = sorted(rec_set.intersection(lin_set))
        if burn_in and burn_in > 0:
            # account burn-in skipped
            skipped = min(burn_in, len(common_seq))
            GAP_STATS['burn_in_skipped_common'] += skipped
            common_seq = common_seq[burn_in:]

        if len(common_seq) == 0:
            return pd.DataFrame(mismatches) if mismatches else pd.DataFrame()

        rec_by_seq = recorder_df.set_index(rec_seq.values, drop=False)
        lin_by_seq = linear_df.set_index(lin_seq.values, drop=False)

        skip_bases = set([seq_base])
        rec_cols, lin_cols = match_column_maps(recorder_df, linear_df, skip_bases=skip_bases)

        for seq in common_seq:
            rec_row = rec_by_seq.loc[seq].iloc[0] if isinstance(rec_by_seq.loc[seq], pd.DataFrame) else rec_by_seq.loc[seq]
            lin_row = lin_by_seq.loc[seq].iloc[0] if isinstance(lin_by_seq.loc[seq], pd.DataFrame) else lin_by_seq.loc[seq]

            for rec_col, lin_col in zip(rec_cols, lin_cols):
                rec_val_raw = rec_row[rec_col]
                lin_val_raw = lin_row[lin_col]

                rec_num = pd.to_numeric(pd.Series([rec_val_raw]), errors='coerce').iloc[0]
                lin_num = pd.to_numeric(pd.Series([lin_val_raw]), errors='coerce').iloc[0]

                if pd.notna(rec_num) and pd.notna(lin_num):
                    mismatch = not math.isclose(float(rec_num), float(lin_num), abs_tol=abs_tol)
                    diff = float(rec_num) - float(lin_num)
                    expect = float(lin_num)
                    actual = float(rec_num)
                else:
                    rec_str = str(rec_val_raw).strip()
                    lin_str = str(lin_val_raw).strip()
                    mismatch = rec_str != lin_str
                    diff = None
                    expect = lin_str
                    actual = rec_str

                if mismatch:
                    try:
                        original_rec_idx = int(rec_row["__orig_index__"])
                    except Exception:
                        original_rec_idx = None
                    try:
                        original_lin_idx = int(lin_row["__orig_index__"])
                    except Exception:
                        original_lin_idx = None

                    recorder_row_number = (original_rec_idx + recorder_offset_header) if original_rec_idx is not None else None
                    linear_row_number = (original_lin_idx + linear_offset_header) if original_lin_idx is not None else None

                    mismatches.append({
                        "PatientId": patient_id,
                        "Column": base_column_name(rec_col),
                        "Expected (Linear File)": expect,
                        "Actual (Recorder File)": actual,
                        "Linear File Row": linear_row_number,
                        "Recorder File Row": recorder_row_number,
                        "Difference": diff,
                        "Linear File": None,
                        "Recorder File": None,
                    })
        return pd.DataFrame(mismatches)
    else:
        linear_sorted, _ = sort_by_simtime_coerced(linear_df)

        rec_cols, lin_cols = match_column_maps(recorder_df, linear_sorted)
        min_len = min(len(recorder_df), len(linear_sorted))
        start = burn_in if (burn_in and burn_in > 0) else 0

        mismatches = []
        for i in range(start, min_len):
            for rec_col, lin_col in zip(rec_cols, lin_cols):
                rec_val_raw = recorder_df.iloc[i][rec_col]
                lin_val_raw = linear_sorted.iloc[i][lin_col]

                rec_num = pd.to_numeric(pd.Series([rec_val_raw]), errors='coerce').iloc[0]
                lin_num = pd.to_numeric(pd.Series([lin_val_raw]), errors='coerce').iloc[0]

                if pd.notna(rec_num) and pd.notna(lin_num):
                    mismatch = not math.isclose(float(rec_num), float(lin_num), abs_tol=abs_tol)
                    diff = float(rec_num) - float(lin_num)
                    expect = float(lin_num)
                    actual = float(rec_num)
                else:
                    rec_str = str(rec_val_raw).strip()
                    lin_str = str(lin_val_raw).strip()
                    mismatch = rec_str != lin_str
                    diff = None
                    expect = lin_str
                    actual = rec_str

                if mismatch:
                    try:
                        original_rec_idx = int(recorder_df.iloc[i]["__orig_index__"])
                    except Exception:
                        original_rec_idx = i
                    try:
                        original_lin_idx = int(linear_sorted.iloc[i]["__orig_index__"])
                    except Exception:
                        original_lin_idx = i

                    recorder_row_number = original_rec_idx + recorder_offset_header
                    linear_row_number = original_lin_idx + linear_offset_header

                    mismatches.append({
                        "PatientId": patient_id,
                        "Column": base_column_name(rec_col),
                        "Expected (Linear File)": expect,
                        "Actual (Recorder File)": actual,
                        "Linear File Row": linear_row_number,
                        "Recorder File Row": recorder_row_number,
                        "Difference": diff,
                        "Linear File": None,
                        "Recorder File": None,
                    })
        return pd.DataFrame(mismatches)

def compare_vital_signs_multi(recorder_path: str,
                              linear_paths: List[str],
                              abs_tol: float = 0.1,
                              skip_linear_rows: List[int] = [1],
                              burn_in: int = 1,
                              diff_dp: int = 9,
                              seq_col: str = 'heartrate') -> pd.DataFrame:
    if not os.path.exists(recorder_path):
        print(f'❌ File not found: {recorder_path}')
        sys.exit(1)

    recorder_df_parent = pd.read_csv(recorder_path, dtype=str, keep_default_na=False)
    if recorder_df_parent.empty:
        print('⚠️ Recorder file has no rows.')
        return pd.DataFrame()

    recorder_df_parent.columns = [normalize_column(c) for c in recorder_df_parent.columns]

    rec_pid_col = find_column(recorder_df_parent.columns.tolist(), 'patientid') or find_column(recorder_df_parent.columns.tolist(), 'patient_id')
    if rec_pid_col is None:
        raise ValueError('Combined recorder file has no patientId column.')

    all_mismatches = []

    for lin_path in linear_paths:
        if not os.path.exists(lin_path):
            print(f'❌ File not found: {lin_path}')
            sys.exit(1)

        linear_df = pd.read_csv(lin_path, dtype=str, keep_default_na=False)
        if linear_df.empty:
            print(f'⚠️ Linear file "{lin_path}" has no rows; skipping.')
            continue
        linear_df.columns = [normalize_column(c) for c in linear_df.columns]

        linear_df_for_pid = linear_df.drop(index=skip_linear_rows, errors='ignore')
        patient_id = get_patient_id_value(linear_df_for_pid)
        if patient_id is None:
            print(f'⚠️ Linear file "{lin_path}" has no usable patientId; skipping.')
            continue

        recorder_df = recorder_df_parent[recorder_df_parent[rec_pid_col].astype(str) == str(patient_id)]
        if recorder_df.empty:
            print(f'⚠️ No rows in recorder file for PatientId={patient_id} (from "{lin_path}"); skipping.')
            continue

        mism = compare_pair_seq(
            recorder_df=recorder_df,
            linear_df=linear_df,
            abs_tol=abs_tol,
            linear_skip_rows=skip_linear_rows,
            recorder_offset_header=2,
            linear_offset_header = 2,
            patient_id=str(patient_id),
            seq_base=seq_col.lower(),
            burn_in=burn_in,
            diff_dp=diff_dp
        )
        if not mism.empty:
            mism['Linear File'] = lin_path
            mism['Recorder File'] = recorder_path
            all_mismatches.append(mism)

    if not all_mismatches:
        print("✅ All rows match across all columns for all provided patientIds.")
        print_gap_summary()
        print_bandwidth_summary(
            recorder_path=recorder_path,
            linear_paths=linear_paths,
            overhead_factor=1.0,
        )
        return pd.DataFrame()
    else:
        print("❌ Mismatches found:")
        df = pd.concat(all_mismatches, ignore_index=True)

        if "Difference" in df.columns:
            def fmt_diff(x):
                if x is None or (isinstance(x, float) and (pd.isna(x))):
                    return ""
                try:
                    return f"{float(x):.{diff_dp}f}"
                except Exception:
                    return str(x)
            df["Difference"] = df["Difference"].apply(fmt_diff)

        view = df.copy()
        for col in ("Linear File", "Recorder File"):
            if col in view.columns:
                view[col] = view[col].apply(lambda p: Path(str(p)).name)

        order = ["PatientId","Column","Expected (Linear File)","Actual (Recorder File)",
                 "Linear File Row","Recorder File Row","Difference","Linear File","Recorder File"]
        view = view[[c for c in order if c in view.columns]]

        with pd.option_context(
            "display.max_rows", None,
            "display.width", 200,
            "display.max_colwidth", 1000,
            "display.colheader_justify", "left"
        ):
            print(view.to_string(index=False, justify="left"))

        print_gap_summary()
        print_bandwidth_summary(
            recorder_path=recorder_path,
            linear_paths=linear_paths,
            overhead_factor=1.0,
        )
        return df

def main():
    parser = argparse.ArgumentParser(description="Compare combined recorder output against one or more linear reference CSV files, grouped by patientId. Adds detailed sequence-drop accounting in the summary, with optional composite seq IDs (patientId+seq) for gap/union math.")
    parser.add_argument("paths", nargs="+", help="LINEAR... RECORDER (last arg is recorder). Or use --recorder/--linear.")
    parser.add_argument("--recorder", help="Path to combined recorder output CSV")
    parser.add_argument("--linear", nargs="+", help="One or more linear reference CSV files")
    parser.add_argument("--abs-tol", type=float, default=0.1, help="Absolute tolerance for numeric comparisons (default: 0.1)")
    parser.add_argument("--burn-in", type=int, default=1, help="Skip the first N data rows per patient before comparing (default: 1)")
    parser.add_argument("--diff-dp", type=int, default=9, help="Decimal places to print for Difference column (default: 9)")
    parser.add_argument("--no-gap-audit", action="store_true",
                        help="Disable the default gap audit that reports missing/extra sequence IDs per patient.")
    parser.add_argument("--no-skip-first-seq", action="store_true",
                        help="Do not skip the first sequence value from linear (default is to skip the setup row).")
    parser.add_argument("--no-startup-filter", action="store_true",
                        help="Disable startup stabilization filter (auto-lock after K consecutive matches).")
    parser.add_argument("--startup-lock-k", type=int, default=None,
                        help="Consecutive matches required to lock startup (default: 5).")
    parser.add_argument("--seq-col", type=str, default="heartrate", help="Base name of integer sequence column to align rows (default: heartrate).")
    parser.add_argument("--out", help="Optional path to write mismatches CSV")
    parser.add_argument("--composite-seq", action="store_true", help="Use composite sequence IDs (patientId+seq) for gap/union math.")
    parser.add_argument("--overhead", type=float, default=1.0,
                        help="Transport overhead multiplier for bandwidth estimate (e.g., 1.05 adds 5%%). Default: 1.0")
    args = parser.parse_args()

    global GAP_AUDIT_ENABLED
    GAP_AUDIT_ENABLED = GAP_AUDIT_ENABLED_DEFAULT and (not args.no_gap_audit)

    global SKIP_FIRST_SEQ
    SKIP_FIRST_SEQ = SKIP_FIRST_SEQ_DEFAULT and (not getattr(args, "no_skip_first_seq", False))

    global STARTUP_FILTER_ENABLED, STARTUP_LOCK_K
    STARTUP_FILTER_ENABLED = STARTUP_FILTER_ENABLED_DEFAULT and (not getattr(args, "no_startup_filter", False))
    STARTUP_LOCK_K = args.startup_lock_k if (getattr(args, "startup_lock_k", None) is not None) else STARTUP_LOCK_K_DEFAULT

    recorder_path: Optional[str] = args.recorder
    linear_paths: Optional[List[str]] = args.linear

    if recorder_path and linear_paths:
        pass
    else:
        if len(args.paths) < 2:
            parser.error("Need at least two paths (linear and recorder).")
        if len(args.paths) == 2 and not recorder_path and not linear_paths:
            linear_paths = [args.paths[0]]
            recorder_path = args.paths[1]
        else:
            linear_paths = args.paths[:-1]
            recorder_path = args.paths[-1]

    global COMPOSITE_SEQ
    COMPOSITE_SEQ = bool(getattr(args, 'composite_seq', False))

    mismatches_df = compare_vital_signs_multi(
        recorder_path=recorder_path,
        linear_paths=linear_paths,
        abs_tol=args.abs_tol,
        burn_in=args.burn_in,
        diff_dp=args.diff_dp,
        seq_col=args.seq_col
    )

    # If mismatches_df is empty (all match), the prints already happened inside
    if args.out and mismatches_df is not None and not mismatches_df.empty:
        mismatches_df.to_csv(args.out, index=False)
        print(f"↳ Wrote detailed mismatches to: {args.out}")

if __name__ == "__main__":
    main()
