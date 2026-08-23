# graphql.py
import os
import time
from datetime import datetime

import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

# Endpoint (override via env if you like)
URL = os.getenv("GRAPHQL_ENDPOINT", "http://localhost:8080/graphql")
DEFAULT_HEADERS = {"Content-Type": "application/json"}

# Single Session with a larger pool so calls reuse TCP/TLS connections
SESSION = requests.Session()
adapter = HTTPAdapter(pool_connections=100, pool_maxsize=100, max_retries=Retry(total=0))
SESSION.mount("http://", adapter)
SESSION.mount("https://", adapter)

def _ts() -> str:
    return datetime.now().isoformat(timespec="milliseconds")

# --- raw poster (returns the Response so callers can see status) ---
def _post_raw(payload: dict, headers: dict | None = None) -> requests.Response:
    hdrs = dict(DEFAULT_HEADERS)
    if headers:
        hdrs.update(headers)

    # pre-call log (keep-alive/pool in effect)
    approx_payload_bytes = len(str(payload))
    # print(f"{_ts()} | HTTP POST -> {URL} (payload bytes ~{approx_payload_bytes})")

    t0 = time.monotonic()
    resp = SESSION.post(URL, json=payload, headers=hdrs, timeout=(5, 20))
    total_ms = (time.monotonic() - t0) * 1000
    ttfb_ms = resp.elapsed.total_seconds() * 1000  # server think-time + network

    # post-call log (compact, grep-friendly)
    #print(
    #    f"{_ts()} | HTTP {resp.status_code} TTFB={ttfb_ms:.0f}ms total={total_ms:.0f}ms "
    #    f"len={len(resp.content)} conn={resp.headers.get('Connection','?')} "
    #    f"server={resp.headers.get('Server','?')}"
    #)
    return resp

# --- legacy helpers (raise on non-2xx, return only JSON) ---
def _post(payload: dict, headers: dict | None = None):
    resp = _post_raw(payload, headers=headers)
    resp.raise_for_status()
    return resp.json()

def execute_query(query: str, variables: dict | None = None):
    return _post({"query": query, "variables": variables or {}})

def execute_mutation(mutation: str, variables: dict | None = None):
    return _post({"query": mutation, "variables": variables or {}})

# --- new helpers that RETURN HTTP STATUS + timings (don’t raise) ---
def execute_query_status(query: str, variables: dict | None = None):
    resp = _post_raw({"query": query, "variables": variables or {}})
    try:
        body = resp.json()
    except Exception:
        body = None
    return {
        "status_code": resp.status_code,
        "ok": resp.ok,
        "json": body,
        "elapsed_ms": int(resp.elapsed.total_seconds() * 1000),  # TTFB from requests
        # total elapsed already printed above; add here if you prefer
    }

def execute_mutation_status(mutation: str, variables: dict | None = None):
    resp = _post_raw({"query": mutation, "variables": variables or {}})
    try:
        body = resp.json()
    except Exception:
        body = None
    return {
        "status_code": resp.status_code,
        "ok": resp.ok,
        "json": body,
        "elapsed_ms": int(resp.elapsed.total_seconds() * 1000),
    }
