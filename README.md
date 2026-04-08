# Quantity Measurement App — Microservices Architecture

## Architecture Overview

```
Browser (React Frontend — port 3000)
              │
              ▼
  ┌─────────────────────┐
  │   API Gateway       │  ← port 8080  (single entry point)
  │   (Spring Boot)     │
  └──────────┬──────────┘
             │ routes to:
    ┌────────┼──────────────────────┐
    ▼        ▼                      ▼
┌───────┐ ┌──────────┐ ┌──────────────────────┐
│ Auth  │ │  Admin   │ │  Quantity + History  │
│ :8081 │ │  :8082   │ │  :8083     :8084     │
└───────┘ └──────────┘ └──────────────────────┘
```

## Services

| Service          | Port | Responsibility                            |
|------------------|------|-------------------------------------------|
| api-gateway      | 8080 | Route requests, validate JWT              |
| auth-service     | 8081 | Register, Login, JWT generation, Users    |
| admin-service    | 8082 | User management, Stats dashboard          |
| quantity-service | 8083 | Convert, Compare, Arithmetic operations   |
| history-service  | 8084 | Persist & retrieve measurement history    |
| frontend (React) | 3000 | UI (Vite + React)                         |

## Prerequisites

- Java 21+
- Maven 3.8+
- Node.js 18+ (for frontend)

## Quick Start

### Option 1 — Run each service manually (recommended for development)

Open **5 separate terminals** and run each:

```bash
# Terminal 1 — Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 2 — History Service
cd history-service
mvn spring-boot:run

# Terminal 3 — Quantity Service
cd quantity-service
mvn spring-boot:run

# Terminal 4 — Admin Service
cd admin-service
mvn spring-boot:run

# Terminal 5 — API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 6 — Frontend
cd frontend
npm install
npm run dev
```

### Option 2 — Use the startup script (Linux/Mac)

```bash
chmod +x start-all.sh stop-all.sh
./start-all.sh
# Then in a new terminal:
cd frontend && npm install && npm run dev
```

## API Endpoints (via Gateway at port 8080)

### Public (no auth required)
- `POST /auth/register` — Register a new user
- `POST /auth/login`    — Login, returns JWT token

### Protected (requires `Authorization: Bearer <token>` header)
- `POST /api/v1/quantities/compare/{userId}`  — Compare two quantities
- `POST /api/v1/quantities/convert/{userId}`  — Convert unit
- `POST /api/v1/quantities/add/{userId}`      — Add quantities
- `POST /api/v1/quantities/subtract/{userId}` — Subtract quantities
- `POST /api/v1/quantities/multiply/{userId}` — Multiply quantity
- `POST /api/v1/quantities/divide/{userId}`   — Divide quantities
- `GET  /api/v1/quantities/history`           — Get user history
- `GET  /api/v1/quantities/history/{op}`      — Get history by operation
- `GET  /api/v1/quantities/count/{op}`        — Count operations
- `DELETE /api/v1/quantities/history/{id}`    — Delete a record
- `DELETE /api/v1/quantities/history`         — Delete all user history

### Admin
- `GET    /admin/users`     — List all users
- `DELETE /admin/users/{id}`— Delete a user
- `GET    /admin/stats`     — Operation statistics
- `GET    /admin/history`   — All history records

## Frontend

The frontend is **100% unchanged** — it still points to `http://localhost:8080`
(the API gateway), so it works exactly as before.

The only addition is `src/api/admin.js` for the admin dashboard.

## Database

Each service uses its own H2 in-memory database:
- `auth-service`    → `authdb` (users table)
- `history-service` → `historydb` (quantity_measurements table)

Data resets on each restart. To use persistent storage, change
`spring.datasource.url` in each service's `application.properties`
to a MySQL/PostgreSQL URL.

## Notes

- All services share the same JWT secret (`quantimeasure-secret-key-2024-secure!!`)
- The API Gateway validates tokens locally without calling Auth Service (for performance)
- Services communicate via REST using Spring's `RestTemplate`
