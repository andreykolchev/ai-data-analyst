AI Data Analyst
================

An LLM‑powered Spring Boot service that turns natural‑language questions into validated SQL, executes them against PostgreSQL, and returns both the raw results and an AI‑generated analysis. A minimal web UI is included.

Features
- Text → SQL using LLMs (Gemini via `google-genai`)
- SQL syntax validation (`jsqlparser`)
- Safe execution against PostgreSQL (Flyway migration included)
- Result explanation/summary by LLM
- Simple web UI (`/`) and REST API (`/api/query`)

Architecture (high level)
- `SchemaService` extracts database schema to guide SQL generation
- `SqlGeneratorService` creates SQL from the user question and schema
- `SqlValidatorService` checks SQL syntax
- `QueryExecutorService` runs SQL and returns rows
- `ResultAnalyzerService` explains the results in natural language

See `_doc/architecture` for diagrams and examples.

Tech stack
- Java 23, Spring Boot 3.5.x
- Maven
- PostgreSQL 16 (Docker Compose provided)
- LLM client: `com.google.genai:google-genai`
- Optional: LangChain4j

Prerequisites
- Java 23+
- Maven 3.9+
- Docker (for the local PostgreSQL instance)
- A Gemini API key

Quickstart
1) Start PostgreSQL
```
docker compose up -d
```
This starts a Postgres 16 container with:
- DB: `analytics`
- User/Password: `analyst/analyst`
- Port: `5432`

Database schema and seed data are managed via Flyway at startup from `src/main/resources/db/migration/V1__init.sql`.

2) Provide your Gemini API key
Preferred (do not commit secrets):
```
export GEMINI_API_KEY="<your-api-key>"
```
Spring property mapping:
- The application reads `gemini.api.key` (see `src/main/resources/application.yml`).
- Spring Boot will map `GEMINI_API_KEY` env var to `gemini.api.key`.

Note: The sample `application.yml` in this repo contains a placeholder key for local testing. Replace it or override via environment variable before running.

3) Run the app
```
mvn spring-boot:run
# or build a jar
mvn -q -DskipTests package && java -jar target/ai-data-analyst-0.0.1-SNAPSHOT.jar
```

Using the API
- Endpoint: `POST /api/query`
- Request body:
```
{
  "question": "Total revenue by month in 2024"
}
```
- Response body:
```
{
  "sql": "SELECT ...",
  "data": [ { "col": "value" }, ... ],
  "analysis": "Natural-language explanation of results"
}
```

Try it quickly with curl:
```
curl -sS -X POST http://localhost:8080/api/query \
  -H 'Content-Type: application/json' \
  -d '{"question":"Top 5 customers by total spend"}' | jq .
```

Web UI
- Open `http://localhost:8080/` for a minimal UI in `src/main/resources/static/index.html`.

Project layout (selected)
- `src/main/java/com/aidataanalyst/controller/QueryController.java` — REST endpoint
- `src/main/java/com/aidataanalyst/service/*` — core services (schema, SQL generation/validation, execution, analysis)
- `src/main/resources/db/migration/V1__init.sql` — Flyway migration
- `docker-compose.yml` — local Postgres
- `_doc/test.http` — example HTTP request (usable with the IntelliJ HTTP Client)

Configuration
`src/main/resources/application.yml` contains defaults:
- Server: `localhost:8080`
- Datasource: `jdbc:postgresql://localhost:5432/analytics`, user/password `analyst/analyst`
- Gemini API key: `gemini.api.key` (override via `GEMINI_API_KEY` env var)

Environment variables
- `GEMINI_API_KEY` — your Gemini key (recommended approach)

Development tips
- Change the schema/data in Flyway migrations and restart.
- Inspect or tweak prompts in services responsible for SQL generation/analysis.
- Use `_doc/test.http` for quick iterative queries.

Testing
```
mvn test
```

Troubleshooting
- Cannot connect to DB: ensure `docker compose ps` shows Postgres healthy and port 5432 is free.
- SQL errors: check logs for `SqlValidatorService` messages and examine generated SQL in the API response.
- 401/LLM issues: ensure `GEMINI_API_KEY` is set and valid.

Security note
- Never commit real API keys. Use environment variables or a secrets manager.

License
This project is provided as-is. Add your license of choice here.
