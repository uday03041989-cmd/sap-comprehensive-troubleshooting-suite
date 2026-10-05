from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import List, Optional

app = FastAPI(
    title="SAP Troubleshooting Suite",
    version="1.0.0",
    description="A diagnostic engine for SAP ABAP, Java, BOBJ, BODS, HANA, and ASE troubleshooting",
)


class DiagnoseRequest(BaseModel):
    component: str = Field(..., description="One of: abap, java, bobj, bods, hana, ase")
    issue: str = Field(..., description="User-described issue or error message")
    environment: str = Field(default="production", description="Environment type")


class DiagnosticCheck(BaseModel):
    title: str
    description: str


class DiagnosticResponse(BaseModel):
    component: str
    issue: str
    severity: str
    root_cause: str
    recommendations: List[str]
    checks: List[DiagnosticCheck]


AVAILABLE_COMPONENTS = [
    "abap",
    "java",
    "bobj",
    "bods",
    "hana",
    "ase",
]


RULES = {
    "abap": {
        "conditions": ["dump", "short dump", "runtime error", "authorization", "batch job", "performance"],
        "severity": "high",
        "root_cause": "ABAP runtime or authorization issue likely caused by a coding defect, failed transaction context, or stale/incorrect authorization setup.",
        "recommendations": [
            "Check ST22 dumps for the exact exception and source line.",
            "Review SU53 or auth trace for missing authorizations.",
            "Validate the user context, RFC destination, and batch job parameters.",
            "Check long-running SQL and database performance counters."
        ],
        "checks": [
            {"title": "Dump analysis", "description": "Inspect ST22 for exception type, program, and source location."},
            {"title": "Authorization review", "description": "Verify SU53 and object-level authorization checks."},
            {"title": "Job and lock review", "description": "Check SM37, SM12, and SM13 for job and lock-related bottlenecks."}
        ]
    },
    "java": {
        "conditions": ["memory", "heap", "thread", "deadlock", "connection", "startup", "exception"],
        "severity": "high",
        "root_cause": "Java application failure likely caused by memory pressure, thread contention, or dependency/service connectivity issues.",
        "recommendations": [
            "Inspect JVM heap, GC logs, and thread dumps.",
            "Review application logs for connection or dependency failures.",
            "Check CPU saturation and DB connection pool usage.",
            "Verify service startup configuration and environment variables."
        ],
        "checks": [
            {"title": "Thread dump review", "description": "Look for deadlocked or blocked threads."},
            {"title": "GC and heap check", "description": "Confirm whether memory pressure or OOM is causing the issue."},
            {"title": "Dependency health", "description": "Verify connection to SAP, DB, and middleware services."}
        ]
    },
    "bobj": {
        "conditions": ["report", "universe", "schedule", "authentication", "refresh", "performance"],
        "severity": "medium",
        "root_cause": "BOBJ issue likely caused by report design, refresh configuration, scheduling context, or backend connectivity problems.",
        "recommendations": [
            "Review the report or universe definition for broken dependencies.",
            "Check the scheduling server, job status, and instance logs.",
            "Validate authentication and SSO configuration for the user or service account.",
            "Review database and query execution time for report refreshes."
        ],
        "checks": [
            {"title": "Report schedule validation", "description": "Verify job status and execution logs in the CMS and scheduling server."},
            {"title": "Universe connectivity", "description": "Validate universe connection and data foundation metadata."},
            {"title": "Security check", "description": "Confirm access rights to report, folder, and data source."}
        ]
    },
    "bods": {
        "conditions": ["job", "data flow", "mapping", "transformation", "batch", "load", "schedule"],
        "severity": "high",
        "root_cause": "BODS job failure likely caused by an invalid data flow transform, source fetch issue, or scheduler mismatch.",
        "recommendations": [
            "Inspect the failed data flow for mapping and transform errors.",
            "Review source and target metadata, connections, and row counts.",
            "Check the job log for row rejection, transformation, or loader failures.",
            "Validate scheduling dependencies and runtime parameters."
        ],
        "checks": [
            {"title": "Data flow review", "description": "Examine transform rules, validation steps, and error rows."},
            {"title": "Source-target validation", "description": "Verify schema, filters, and target constraints."},
            {"title": "Runtime scheduling", "description": "Check process chain, dependency order, and job timing."}
        ]
    },
    "hana": {
        "conditions": ["sql", "memory", "slow query", "lock", "timeout", "performance", "deadlock"],
        "severity": "high",
        "root_cause": "HANA database issue likely involves suboptimal SQL execution plans, memory pressure, lock contention, or resource saturation.",
        "recommendations": [
            "Review expensive SQL statements and plan visualizations.",
            "Check memory usage, thread allocation, and table statistics.",
            "Inspect lock tables and blocking sessions.",
            "Validate schema statistics and query parameterization." 
        ],
        "checks": [
            {"title": "SQL performance check", "description": "Use SQL plan cache and expensive statement trace."},
            {"title": "Memory health review", "description": "Confirm memory pressure and allocation bottlenecks."},
            {"title": "Lock analysis", "description": "Inspect active locks and blocking sessions for contention."}
        ]
    },
    "ase": {
        "conditions": ["lock", "deadlock", "tempdb", "log segment", "timeout", "blocking", "io"],
        "severity": "high",
        "root_cause": "ASE database issue likely caused by lock contention, transaction log growth, or I/O bottlenecks in high-volume operations.",
        "recommendations": [
            "Check sp_who, sp_lock, and blocking sessions for lock chains.",
            "Review log usage and transaction log dumps.",
            "Inspect tempdb and device fragmentation issues.",
            "Evaluate I/O wait and query plan efficiency."
        ],
        "checks": [
            {"title": "Blocking session analysis", "description": "Review active blocking chains and lock owners."},
            {"title": "Log usage check", "description": "Validate transaction log growth and segment usage."},
            {"title": "Tempdb and I/O check", "description": "Review tempdb pressure and physical I/O bottlenecks."}
        ]
    }
}


@app.get("/api/health")
def health():
    return {"status": "ok", "service": "sap-troubleshooting-suite"}


@app.get("/api/components")
def list_components():
    return {"components": AVAILABLE_COMPONENTS}


@app.post("/api/diagnose", response_model=DiagnosticResponse)
def diagnose(request: DiagnoseRequest):
    component = request.component.lower().strip()
    issue_text = request.issue.lower()

    if component not in AVAILABLE_COMPONENTS:
        raise ValueError(f"Unsupported component: {component}")

    rule = RULES[component]
    matched = [keyword for keyword in rule["conditions"] if keyword in issue_text]

    if not matched:
        severity = "medium"
        root_cause = "No direct rule match detected. Review logs, traces, and application context for the failing component."
        recommendations = [
            "Collect exact logs and stack traces for the failing step.",
            "Confirm whether the problem is environmental, configuration, or application-specific.",
            "Check recent changes in code, jobs, users, or connectivity paths."
        ]
        checks = [
            {"title": "Initial investigation", "description": "Capture workload context and exact error message."},
            {"title": "Change review", "description": "Check recent modifications or deployments affecting the component."}
        ]
    else:
        severity = rule["severity"]
        root_cause = rule["root_cause"]
        recommendations = rule["recommendations"]
        checks = [DiagnosticCheck(**item) for item in rule["checks"]]

    return DiagnosticResponse(
        component=component,
        issue=request.issue,
        severity=severity,
        root_cause=root_cause,
        recommendations=recommendations,
        checks=checks,
    )


@app.get("/")
def index():
    return {"message": "SAP Troubleshooting Suite API is running."}
