# SAP Troubleshooting Suite

A complete A-to-Z troubleshooting solution for:
- SAP ABAP
- Java
- Business Objects (BOBJ)
- Business Data Services (BODS)
- SAP HANA database
- SAP ASE database

This project is designed as a practical troubleshooting dashboard and knowledge engine for support teams, consultants, and SAP engineers working across enterprise integration and reporting stacks.

## Solution Highlights

- Component-based diagnosis for ABAP, Java, BOBJ, BODS, HANA, and ASE
- Rule-driven troubleshooting workflow for common production issues
- Severity scoring and recommended actions
- Searchable incident patterns and remediation knowledge base
- Web dashboard and backend API
- Docker-ready deployment with PostgreSQL

## Tech Stack

- Backend: FastAPI (Python)
- Frontend: React + Vite
- Database: PostgreSQL
- Containerization: Docker / Docker Compose

## Project Structure

- `backend/` – FastAPI service for diagnostics and rules engine
- `frontend/` – React dashboard UI
- `docker-compose.yml` – local orchestration for services
- `README.md` – project overview and usage

## Quick Start

### 1) Clone and run with Docker

```bash
cd sap-comprehensive-troubleshooting-suite
docker compose up --build
```

Then open:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8000/docs
- PostgreSQL: localhost:5432

### 2) Run backend locally

```bash
cd backend
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### 3) Run frontend locally

```bash
cd frontend
npm install
npm run dev
```

## Supported Diagnostic Areas

### SAP ABAP
- Short dumps
- RFC failures
- Authorization issues
- Performance bottlenecks
- Batch job problems

### Java
- Memory leaks
- Thread deadlocks
- Connectivity failures
- Service restart issues
- Stack traces

### BOBJ
- Report execution failures
- Universe refresh issues
- Scheduling failures
- Authentication problems
- Performance tuning

### BODS
- Job failures
- Data flow errors
- Source-target mapping issues
- Data quality issues
- Batch and scheduler issues

### SAP HANA
- SQL performance issues
- Memory pressure
- Table lock contention
- Query plan issues
- Connection exhaustion

### SAP ASE
- Lock waits
- Deadlocks
- Log segment issues
- Tempdb pressure
- I/O bottlenecks

## Example API

Send a diagnosis request:

```bash
curl -X POST "http://localhost:8000/api/diagnose" \
  -H "Content-Type: application/json" \
  -d '{
    "component": "abap",
    "issue": "short dump during billing job",
    "environment": "production"
  }'
```

## Roadmap

- Add more advanced rule sets and templates
- Integrate with SAP logs and job monitors
- Add database query validation for HANA and ASE
- Add user authentication and role-based access
- Add exportable reports and incident summaries
- Add knowledge base search and tagging

## License

This project is available for internal enterprise support and prototype use.
