# SYSS COP Platform

A production-ready Common Operational Picture (COP) platform for military/security forces with real-time map, operations/tasking, messaging/reports, AAR replay, offline sync, and secure voice/video conferencing.

## Quick Start (Dev)

Prereqs: Docker, Docker Compose, Node 18+, pnpm, Java 21, Maven 3.9+

```bash
# One-time: if docker permission denied, add user to docker group then re-login
sudo usermod -aG docker "$USER" || true

# Start dev stack (datastores, Jitsi, backend, frontend)
cd syss
./scripts/dev-up.sh

# Stop and clean
./scripts/dev-down.sh
```

If Docker is unavailable, scripts will warn with actionable steps instead of failing abruptly.

## Services (dev ports)

- frontend (Next.js): http://localhost:3000
- backend auth-service: http://localhost:8081
- backend ops-service: http://localhost:8082
- backend geo-service: http://localhost:8083
- backend message-service: http://localhost:8084
- backend report-service: http://localhost:8085
- backend realtime-service (WS): http://localhost:8086
- backend replay-service: http://localhost:8087
- backend av-signal-service: http://localhost:8088
- Postgres + PostGIS: localhost:5432
- MinIO: http://localhost:9000 (console http://localhost:9001)
- Kafka: localhost:9092 (controller at 29092 internal)
- Jitsi Meet: http://localhost:8443 (TLS off in dev; proxied by container)

## RBAC Roles

- ADMIN, COMMANDER, ANALYST, FIELD_UNIT, OBSERVER

## Architecture

- Frontend: Next.js (App Router), TS, React 18, React Query, Zustand, Leaflet + MapLibre, shadcn/ui. Offline-first via IndexedDB (`idb`), mutation queue.
- Backend: Spring Boot 3.3.x microservices, OAuth2 Resource Server (JWT), WebSocket gateway, Flyway, OpenAPI.
- Data: PostgreSQL + PostGIS; MinIO for attachments/recordings; Kafka for events.
- Realtime: STOMP/SockJS or SSE; delta sync via `since=lastEventId`.
- A/V: Jitsi stack; signaling service provides auth-gated room links and attendance logs.

## CI (local)

```bash
# Typecheck, lint, test, docker build
(cd frontend && pnpm i && pnpm run -s typecheck && pnpm run -s build && pnpm run -s test)
(cd backend && mvn -q -DskipITs=false -DskipTests=false verify)
# Build images
docker compose build | cat
# Bring up and seed
./scripts/dev-up.sh && ./scripts/seed.sh
```

## Troubleshooting

- Docker permission denied: run `sudo usermod -aG docker $USER` and re-login.
- Leaflet SSR: map components are client-only with dynamic import `ssr:false` and guarded `window` access.
- React peer conflicts: pinned to React 18.x for all UI libs; install should pass without `--force`.
- Spring Security: dependencies aligned with Spring Boot 3.3.x; WebSocket security uses supported config.
- IndexedDB: keys validated and coerced to strings; transactions wrapped with error handling; schema migrations versioned.
- Next.js metadata warnings: `generateViewport()` used for viewport/themeColor per Next 15 docs.

## Docs

- See `docs/` for ADRs, AAR/Replay guides.
- OpenAPI UIs exposed at each service `/swagger-ui.html` in dev.