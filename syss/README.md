# SYSS COP Platform

A production-ready Common Operational Picture platform for military/security forces.

## Quick Start

```bash
# Dev prerequisites: Docker, Docker Compose, Node 18+, Java 21, Maven, pnpm
cd syss
./scripts/dev-up.sh
# Open http://localhost:8080 (API docs) and http://localhost:3000 (Frontend)
```

If Docker permissions error occurs:
```bash
sudo usermod -aG docker $USER
newgrp docker
# Or log out and back in
```

## Service Graph
- Frontend (Next.js)
- Backend services (Spring Boot 3.3): auth, ops, geo, message, report, realtime, replay, av-signal
- Postgres + PostGIS, MinIO, Redis Streams, Jitsi (A/V), Nginx reverse proxy

## Ports
- Frontend: 3000
- Nginx: 8080 (TLS off in dev)
- Auth: 8101, Ops: 8102, Geo: 8103, Message: 8104, Report: 8105, Realtime: 8106, Replay: 8107, AV Signal: 8108
- Postgres: 5432, MinIO: 9000/9001, Redis: 6379, Jitsi web: 8443 (internal)

## RBAC Matrix (summary)
- ADMIN: all
- COMMANDER: ops create/manage, A/V create, assign tasks
- ANALYST: intel layers, reports, A/V create
- FIELD_UNIT: update status, positions, messaging
- OBSERVER: read-only

## CI Steps (local)
```bash
pnpm -C frontend install && pnpm -C frontend build
mvn -q -f backend/pom.xml -DskipTests package
docker compose build
```

## Troubleshooting
- Leaflet SSR: Only import in client components; dynamic imports with `ssr:false`.
- React peer conflicts: React 18 pinned; prefer compatible UI deps.
- Spring Security: Boot 3.3 aligned dependencies; WebSocket security configured.
- IndexedDB: Keys validated; migrations handled; errors wrapped.
- Docker perms: See command above; scripts warn if daemon unavailable.
- Next metadata: using `generateViewport()` per Next 15.