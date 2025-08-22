# SYSS — Common Operational Picture (COP)

A production-ready Common Operational Picture platform for military/security forces with real-time map, operations/tasking, messaging/reports, AAR replay, offline sync, and secure voice/video conferencing.

## Contents
- Quick Start (Dev)
- Features & Architecture
- Monorepo Layout
- URLs & Ports
- RBAC Roles
- APIs & OpenAPI
- Tests & CI
- Deployment (Docker Compose, Kubernetes)
- Offline & Sync
- A/V Conferencing
- Troubleshooting

## Quick Start (Dev)

Prereqs: Docker, Docker Compose, Node 18+, Java 21, Maven, pnpm

```bash
cd syss
./scripts/dev-up.sh
# Frontend: http://localhost:3000
# API via Nginx: http://localhost:8080
```

Default users (seeded):
- admin/password (ADMIN)
- commander/password (COMMANDER)
- analyst/password (ANALYST)
- field/password (FIELD_UNIT)
- observer/password (OBSERVER)

If you see a Docker permission error:
```bash
sudo usermod -aG docker $USER
newgrp docker
```

Stop stack:
```bash
./scripts/dev-down.sh
```

## Features & Architecture

- Auth & RBAC (JWT + optional 2FA TOTP). Roles: ADMIN, COMMANDER, ANALYST, FIELD_UNIT, OBSERVER
- Interactive Map (Leaflet/OSM tiles): friendly units, threats, tasks, overlays; drawing tools
- Operations & Tasking: create ops, assign units, update status, audit
- Messaging & Reports: per-operation channels, attachments, SITREP/CONREP
- Replay: timeline playback of events, export
- Offline-first: IndexedDB caches, mutation queue, conflict policy
- Realtime: WebSocket/STOMP topics for positions, tasks, messages
- A/V Conferencing: Jitsi-based rooms, signaling endpoints, recordings (stub)

Backend (Spring Boot 3.3.x microservices):
- auth-service: JWT issuer (RS256), 2FA, user seed, JWKs endpoint
- ops-service: operations, tasks, units, audit
- geo-service: Geo endpoints (positions/overlays)
- message-service: channels, messages, MinIO attachments
- report-service: SITREP/CONREP
- realtime-service: STOMP/SockJS gateway
- replay-service: event timeline
- av-signal-service: signaling API for Jitsi

Data & Infra:
- PostgreSQL + PostGIS; MinIO; Redis (realtime)
- Nginx reverse proxy for dev; Kubernetes manifests for prod

## Monorepo Layout

```
syss/
  docker-compose.yml
  k8s/
  infra/
    nginx/
  frontend/
  backend/
    shared/
    auth-service/
    ops-service/
    geo-service/
    message-service/
    report-service/
    realtime-service/
    replay-service/
    av-signal-service/
  scripts/
    dev-up.sh
    dev-down.sh
    seed.sh
    check-env.sh
  docs/
    ADRs/
```

## URLs & Ports

- Frontend: http://localhost:3000
- Nginx (dev gateway): http://localhost:8080
  - /auth/ → auth-service
  - /ops/ → ops-service
  - /geo/ → geo-service
  - /messages/ → message-service
  - /reports/ → report-service
  - /realtime/ → realtime-service (WebSockets upgrade)
  - /replay/ → replay-service
  - /av/ → av-signal-service
- Datastores: Postgres 5432, MinIO 9000/9001, Redis 6379

## RBAC Roles

- ADMIN: full access
- COMMANDER: manage operations/tasks, A/V rooms
- ANALYST: overlays, reports, A/V rooms
- FIELD_UNIT: update positions/task status, messaging
- OBSERVER: read-only

## APIs & OpenAPI

Each service exposes OpenAPI in dev at `/swagger-ui.html` and `/v3/api-docs`.

Auth highlights:
- POST `/auth/login` → `{ accessToken, refreshToken }`
- GET `/.well-known/jwks.json` → RS256 public keys

Realtime topics (STOMP):
- `/topic/ops/{id}/positions`
- `/topic/ops/{id}/tasks`
- `/topic/ops/{id}/messages`

Frontend points to gateway via `NEXT_PUBLIC_API_BASE` (set to `http://localhost:8080`).

## Tests & CI

Frontend:
```bash
pnpm -C frontend install
pnpm -C frontend typecheck
pnpm -C frontend test
# Cypress (requires cypress installed):
# npx cypress run  (or use your preferred runner)
```

Backend:
```bash
mvn -q -f backend/pom.xml test
```

CI (local quick run):
```bash
pnpm -C frontend build
mvn -q -f backend/pom.xml -DskipTests package
docker compose build
```

## Deployment

### Docker Compose (dev)

```bash
cd syss
./scripts/dev-up.sh
```

### Kubernetes (prod-like)

Prereqs: a cluster + NGINX Ingress. Build/push images with your registry tags, then apply manifests:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/minio.yaml
kubectl apply -f k8s/apps.yaml
kubectl apply -f k8s/ingress.yaml
```

Ingress host: `syss.local`. Map in `/etc/hosts` to your ingress controller IP if needed.

JWT/JWKS: services use `${AUTH_JWKS_URL}` env (defaults to `http://auth-service:8080/.well-known/jwks.json`).

## Offline & Sync

- IndexedDB caches: operations, tasks, positions, messages, mutation queue
- Key validation on IDB writes; migrations supported
- Reconnect requests deltas via `since=lastEventId`; monotonically increasing eventId

## A/V Conferencing

- Jitsi selected as SFU
- `av-signal-service` endpoints:
  - POST `/av/rooms` (create)
  - POST `/av/rooms/{id}/join` → ICE servers + token (stubbed for dev)
  - POST `/av/rooms/{id}/recordings/start|stop`

## Troubleshooting

- Docker permission denied: `sudo usermod -aG docker $USER && newgrp docker`
- Leaflet SSR: map is client-only via `dynamic(..., { ssr:false })`; never access `window` on server
- React peer conflicts: React 18 pinned; install should pass without `--force`
- Spring Security: Boot 3.3 aligned; JWKS served by auth (`/.well-known/jwks.json`); resource servers use `jwk-set-uri`
- IndexedDB: keys validated; transactions wrapped; migrations versioned
- Next 15 metadata warnings: `generateViewport()` used in `app/viewport.ts`

## Acceptance Checklist

- Start stack with one command; login as Admin; create operation; map renders tiles
- Spawn demo users; positions and tasks update via WebSocket topics
- Draw overlay, save to backend, appears to other clients
- Create tasks; assign; status updates propagate; audit recorded
- Send messages + upload attachment; other client receives instantly
- Go offline; change task status + add message; reconnect → changes sync
- Start A/V room; two browsers join; audio/video + screenshare function (dev stub token)
- Replay renders timeline and metrics
- All tests pass; lint/typecheck clean; no SSR crash; no peer dep conflicts