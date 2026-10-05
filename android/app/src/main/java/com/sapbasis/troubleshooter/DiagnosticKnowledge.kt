package com.sapbasis.troubleshooter

data class TroubleshootItem(
    val id: String,
    val title: String,
    val summary: String,
    val sampleIssue: String,
    val keywords: List<String>
)

data class DiagnosticResult(
    val title: String,
    val summary: String,
    val actions: List<String>,
    val checks: List<String>
)

fun troubleshootingCatalog(): List<TroubleshootItem> = listOf(
    TroubleshootItem(
        id = "abap",
        title = "ABAP",
        summary = "Runtime errors, authorization issues, dumps, batch jobs, and RFC problems.",
        sampleIssue = "short dump in billing job",
        keywords = listOf("short dump", "dump", "authorization", "batch job", "rfc", "runtime error")
    ),
    TroubleshootItem(
        id = "java",
        title = "Java",
        summary = "JVM memory issues, thread deadlocks, connection failures, and startup instability.",
        sampleIssue = "out of memory in Java server",
        keywords = listOf("memory", "oom", "thread", "deadlock", "heap", "exception")
    ),
    TroubleshootItem(
        id = "bobj",
        title = "BOBJ",
        summary = "Report refresh failures, universe issues, scheduling, and access problems.",
        sampleIssue = "BOBJ report not refreshing",
        keywords = listOf("report", "universe", "schedule", "authentication", "refresh")
    ),
    TroubleshootItem(
        id = "bods",
        title = "BODS",
        summary = "ETL data flow failures, transformations, scheduler gaps, and target load issues.",
        sampleIssue = "BODS job failed at transformation step",
        keywords = listOf("job", "data flow", "load", "mapping", "transformation", "batch")
    ),
    TroubleshootItem(
        id = "sap-router",
        title = "SAP Router",
        summary = "Connectivity, routing, gateway, and communication path troubleshooting.",
        sampleIssue = "SAP Router connection error to backend",
        keywords = listOf("router", "connectivity", "gateway", "routing", "trace")
    ),
    TroubleshootItem(
        id = "cloud-connector",
        title = "Cloud Connector",
        summary = "BTP connectivity, on-premise reachability, and Cloud Connector health checks.",
        sampleIssue = "BTP connection failed through cloud connector",
        keywords = listOf("cloud connector", "btp", "cloud", "connectivity", "ssl")
    ),
    TroubleshootItem(
        id = "hana",
        title = "HANA DB",
        summary = "SQL slowness, memory issues, table locks, and system performance bottlenecks.",
        sampleIssue = "HANA SQL is extremely slow",
        keywords = listOf("hana", "sql", "slow query", "memory", "lock", "timeout")
    ),
    TroubleshootItem(
        id = "ase",
        title = "ASE DB",
        summary = "Blocking sessions, transaction log full, tempdb pressure, and I/O churn.",
        sampleIssue = "ASE lock wait and blocking session",
        keywords = listOf("ase", "lock", "deadlock", "tempdb", "log segment", "blocking")
    ),
    TroubleshootItem(
        id = "ssl",
        title = "SSL / Certificates",
        summary = "Certificate validation, expired certs, handshake failures, and trust store issues.",
        sampleIssue = "SSL handshake failed with SAP backend",
        keywords = listOf("ssl", "certificate", "cert", "handshake", "expired", "trust")
    )
)

fun diagnoseIssue(item: TroubleshootItem, issueText: String): DiagnosticResult {
    val lower = issueText.lowercase()
    val matched = item.keywords.filter { it in lower }

    val title = when {
        matched.isNotEmpty() -> "${item.title} diagnostic result"
        else -> "Initial ${item.title} review"
    }

    val summary = when {
        matched.isNotEmpty() -> "Matched symptoms: ${matched.joinToString(", ")}. Review the likely root cause and validate the suggested checks."
        else -> "No direct keyword match found. Proceed with foundational checks, recent changes review, and log validation."
    }

    val actions = when (item.id) {
        "abap" -> listOf(
            "Check ST22 short dump details",
            "Review SU53 authorization failures",
            "Validate SM12/SM13 locks and RFC destination"
        )
        "java" -> listOf(
            "Review JVM heap and GC logs",
            "Capture thread dump and analyze blocking threads",
            "Check DB connection pools and service config"
        )
        "bobj" -> listOf(
            "Validate report and universe connections",
            "Check scheduling server and job logs",
            "Review user security rights and refresh dependencies"
        )
        "bods" -> listOf(
            "Inspect failed data flow and mapping logic",
            "Review row rejects and metadata drift",
            "Verify scheduler dependency order"
        )
        "sap-router" -> listOf(
            "Check SAP Router trace and log files",
            "Validate route entries and network reachability",
            "Inspect firewall and port accessibility"
        )
        "cloud-connector" -> listOf(
            "Verify Cloud Connector status and mapping",
            "Check BTP destination and on-prem connectivity",
            "Review SSL trust and tunnel configuration"
        )
        "hana" -> listOf(
            "Inspect expensive SQL and plan cache",
            "Review memory usage and locks",
            "Validate table statistics and blocking sessions"
        )
        "ase" -> listOf(
            "Check sp_who and sp_lock blocking chains",
            "Review tempdb and transaction log exhaustion",
            "Assess I/O waits and query plans"
        )
        else -> listOf(
            "Inspect certificate chain and validity dates",
            "Validate hostname and trust store mappings",
            "Review handshake logs and backend trust configuration"
        )
    }

    val checks = when (item.id) {
        "abap" -> listOf(
            "Check ST22 exception class, program, and source",
            "Validate SU53 and user authorization context",
            "Review SM37, SM12, and SM13 logs"
        )
        "java" -> listOf(
            "Confirm heap pressure and GC activity",
            "Check thread dump and deadlock state",
            "Validate dependency health and server logs"
        )
        "bobj" -> listOf(
            "Review report server and designer metadata",
            "Check business layer and universe connection test",
            "Investigate authentication and directory mapping"
        )
        "bods" -> listOf(
            "Review transform steps and data quality rules",
            "Inspect target constraints and source row counts",
            "Validate object schedules and dependencies"
        )
        "sap-router" -> listOf(
            "Check route config, log files, and port status",
            "Verify if the backend is reachable from the router host",
            "Inspect connection timeout and firewall behavior"
        )
        "cloud-connector" -> listOf(
            "Check Cloud Connector system mapping and status",
            "Inspect bridge and backend destination health",
            "Review certificate and authentication trust"
        )
        "hana" -> listOf(
            "Review expensive statements and blocking sessions",
            "Check memory allocation and thread saturation",
            "Inspect plan cache quality and statistics"
        )
        "ase" -> listOf(
            "Inspect block owners and log space usage",
            "Review tempdb and I/O consumption",
            "Check SQL plan efficiency and wait events"
        )
        else -> listOf(
            "Check date validity, chain of trust, and hostname match",
            "Review TLS versions and certificate mapping",
            "Inspect trust store and backend certificate import"
        )
    }

    return DiagnosticResult(title, summary, actions, checks)
}
