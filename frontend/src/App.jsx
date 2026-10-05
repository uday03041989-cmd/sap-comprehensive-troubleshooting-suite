import { useEffect, useState } from 'react'

const initialForm = {
  component: 'abap',
  issue: 'short dump during billing job',
  environment: 'production',
}

const componentLabels = {
  abap: 'SAP ABAP',
  java: 'Java',
  bobj: 'BOBJ',
  bods: 'BODS',
  hana: 'HANA',
  ase: 'ASE',
}

export default function App() {
  const [components, setComponents] = useState([])
  const [form, setForm] = useState(initialForm)
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    async function loadComponents() {
      try {
        const response = await fetch('http://localhost:8000/api/components')
        const data = await response.json()
        setComponents(data.components)
      } catch (error) {
        console.error('Failed to load components:', error)
      }
    }

    loadComponents()
  }, [])

  const handleSubmit = async (event) => {
    event.preventDefault()
    setLoading(true)

    try {
      const response = await fetch('http://localhost:8000/api/diagnose', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(form),
      })

      const data = await response.json()
      setResult(data)
    } catch (error) {
      setResult({
        component: form.component,
        issue: form.issue,
        severity: 'error',
        root_cause: 'Unable to reach the backend API. Verify the backend service is running.',
        recommendations: ['Check browser network settings and backend service status.'],
        checks: [{ title: 'API connectivity', description: 'Confirm the FastAPI service is running on port 8000.' }],
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Enterprise support</p>
          <h1>SAP Troubleshooting Suite</h1>
        </div>
      </header>

      <main className="layout">
        <section className="panel form-panel">
          <h2>New diagnostic case</h2>
          <form onSubmit={handleSubmit}>
            <label>
              Component
              <select
                value={form.component}
                onChange={(e) => setForm({ ...form, component: e.target.value })}
              >
                {components.map((component) => (
                  <option key={component} value={component}>
                    {componentLabels[component] || component}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Issue description
              <textarea
                rows="5"
                value={form.issue}
                onChange={(e) => setForm({ ...form, issue: e.target.value })}
              />
            </label>

            <label>
              Environment
              <select
                value={form.environment}
                onChange={(e) => setForm({ ...form, environment: e.target.value })}
              >
                <option value="development">Development</option>
                <option value="qa">QA</option>
                <option value="production">Production</option>
              </select>
            </label>

            <button type="submit" disabled={loading}>
              {loading ? 'Analyzing...' : 'Run diagnosis'}
            </button>
          </form>
        </section>

        <section className="panel result-panel">
          <h2>Recommended actions</h2>
          {result ? (
            <>
              <div className="meta-row">
                <span className={`badge badge-${result.severity}`}>{result.severity}</span>
                <span>{componentLabels[result.component] || result.component}</span>
              </div>

              <p className="section-label">Root cause</p>
              <p>{result.root_cause}</p>

              <p className="section-label">Recommendations</p>
              <ul>
                {result.recommendations.map((recommendation) => (
                  <li key={recommendation}>{recommendation}</li>
                ))}
              </ul>

              <p className="section-label">Checks</p>
              <ul>
                {result.checks.map((check) => (
                  <li key={check.title}>
                    <strong>{check.title}:</strong> {check.description}
                  </li>
                ))}
              </ul>
            </>
          ) : (
            <p>Select a component and issue to generate a troubleshooting recommendation.</p>
          )}
        </section>
      </main>
    </div>
  )
}
