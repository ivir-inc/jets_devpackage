# analyze_recorder_multi_v14

Compare a **combined recorder CSV** against one or more **linear reference CSVs**, grouped by `patientId`.  
The tool aligns rows by an integer **sequence column** (default: `heartrate`) and reports:

- **Value mismatches** (with row numbers and diffs)  
- **Gap audit**: sequence IDs **missing** from recorder or **extra** in recorder  
- **Bandwidth estimate** for the recorder CSV (bytes/s, bits/s, and row rate)

> This README is based on the attached Python script.

---

## Quick start

```bash
# Basic (two files): LINEAR then RECORDER
python analyze_recorder_multi_v14.py linear_patient123.csv combined_recorder.csv

# Multiple linear files + explicit flags
python analyze_recorder_multi_v14.py   --linear linear_p123.csv linear_p456.csv   --recorder combined_recorder.csv   --seq-col heartrate   --abs-tol 0.1   --burn-in 1   --out mismatches.csv
```

If all rows match for all patients, the script prints a ✅ summary and bandwidth estimate.  
If mismatches exist, it prints a ❌ table and (optionally) writes `--out` as a CSV.

---

## Expected inputs

### Recorder CSV (combined)
- Must include a **patient ID** column: something containing `patientId` or `patient_id` (case/spacing doesn’t matter).
- For bandwidth estimation, a **time column** is auto-detected:
  - Prefer `entryTimeMs` (milliseconds)  
  - Fallback: `simTime` (seconds)

### Linear CSV(s)
- Each linear file should contain exactly one **patientId** value (the script derives it from the file).
- Must share comparable column **base names** with the recorder file.  
  Column names are normalized to `lowercase_with_underscores`; anything in parentheses is stripped from the base name (e.g., `HeartRate (bpm)` → base `heartrate`).

### Alignment column (sequence)
- Default **sequence base name** is `heartrate` (override with `--seq-col`).  
- This column must be **integer-valued** in both recorder and linear files after coercion.  
- If a proper sequence column is absent in either side, the tool falls back to **time-sorted** row-by-row comparison (using `simTime`).

---

## What the script does

1. **Normalize columns** (lowercase, underscores, trim suffixes like `_(...)`).  
2. **Group recorder rows by patient** and pair each **linear** file’s patient with its subset in the recorder.  
3. **Apply startup filters** (on the **linear** side, then both):
   - **Skip-first**: drop `seq == 1` from linear (treat as setup row). (Disable with `--no-skip-first-seq`.)  
   - **Startup lock**: auto-detect a lock sequence after **K consecutive matches** (default K=5) and drop earlier rows from both sides (disable with `--no-startup-filter`, tune with `--startup-lock-k`).  
4. **Burn-in**: skip the first **N common sequence IDs** (default `--burn-in 1`).  
5. **Compare values** across matched columns:
   - Numeric cells use `math.isclose` with **absolute tolerance** `--abs-tol` (default 0.1)  
   - Non-numeric cells compare as strings (trimmed)  
6. **Gap audit** (enabled by default): report **missing** (in recorder) and **extra** (in recorder) **sequence IDs** per patient, plus totals. (Disable with `--no-gap-audit`.)  
   - Optional **composite** mode counts gaps over `patientId:seq` pairs (`--composite-seq`).  
7. **Summaries printed** to stdout:
   - **Gap summary** (patients, missing/extra counts, union size, unmatched %)  
   - **Bandwidth estimate** for recorder (avg B/s, Mb/s, rows/s), factoring optional `--overhead` multiplier.  

---

## Command-line options

| Flag | Type | Default | Description |
|---|---:|:--:|---|
| `paths` | list | — | Positional form: `LINEAR... RECORDER` (last path is recorder). |
| `--recorder` | str | — | Recorder CSV path (use with `--linear`). |
| `--linear` | list[str] | — | One or more linear CSV paths. |
| `--abs-tol` | float | `0.1` | Absolute tolerance for numeric comparisons. |
| `--burn-in` | int | `1` | Skip first N **common seq IDs** per patient before comparing. |
| `--diff-dp` | int | `9` | Decimal places when printing numeric differences. |
| `--no-gap-audit` | flag | off | Disable gap reporting. |
| `--no-skip-first-seq` | flag | off | Do **not** drop `seq==1` from linear. |
| `--no-startup-filter` | flag | off | Disable startup lock filter. |
| `--startup-lock-k` | int | `5` | K consecutive matches required to establish lock. |
| `--seq-col` | str | `heartrate` | **Base name** of integer sequence column used to align rows. |
| `--out` | str | — | Write detailed mismatches to this CSV path. |
| `--composite-seq` | flag | off | Use `patientId:seq` composite IDs for gap/union math. |
| `--overhead` | float | `1.0` | Transport overhead multiplier for bandwidth estimate (e.g., `1.05`). |

> You may either supply `--recorder` and `--linear ...` or rely on positional `paths` where the **last** argument is the recorder file.

---

## Output details

### Console output (stdout)
- ✅ **All rows match** message (if no mismatches), followed by:
  - **Gap summary** (patients analyzed; missing/extra counts; union and unmatched %)  
  - **Bandwidth estimate** (size, time span, avg MB/s and Mb/s; rows/s)
- ❌ **Mismatches found** table with columns:
  - `PatientId`, `Column`, `Expected (Linear File)`, `Actual (Recorder File)`,  
    `Linear File Row`, `Recorder File Row`, `Difference`, `Linear File`, `Recorder File`  
  Row numbers reflect **original file line numbers including header offsets** (both default to `2` for human readability).

### CSV output (`--out`)
- Same columns as the console table (without shortening file names).  
- `Difference` is numeric (formatted to `--diff-dp` places) where applicable.

---

## Practical tips

- **Choose a reliable sequence column** (`--seq-col`). It must be integer-like after coercion in both datasets.  
- If **early transients** cause spurious diffs, increase `--burn-in` and/or tune `--startup-lock-k` (or disable with `--no-startup-filter`).  
- If line-by-line comparisons happen (no valid seq column), ensure both sides have a valid **time column** (e.g., `simTime`) so sorting is meaningful.  
- Use `--overhead` to approximate transport/protocol overhead when reporting bandwidth (e.g., `1.05` for +5%).

---

## Exit behavior & errors

- Missing files => prints a ❌ message and **exits** (`sys.exit(1)`).  
- Empty recorder/linear files => prints a warning and skips accordingly.  
- Missing `patientId` in the recorder => raises an error.

---

## Examples

### 1) One linear vs one recorder
```bash
python analyze_recorder_multi_v14.py   linear_p001.csv combined_recorder.csv   --seq-col heartrate --abs-tol 0.05 --burn-in 2
```

### 2) Many linear files, write mismatches, composite gaps
```bash
python analyze_recorder_multi_v14.py   --linear linear_p001.csv linear_p002.csv linear_p003.csv   --recorder rec_all.csv   --seq-col heartrate   --composite-seq   --out mismatches_all.csv
```

### 3) Disable startup filters; treat seq=1 normally
```bash
python analyze_recorder_multi_v14.py   linear_p010.csv rec_all.csv   --no-skip-first-seq --no-startup-filter
```

---

## License

Add your project’s license information here.
