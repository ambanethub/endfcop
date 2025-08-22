#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

./scripts/check-env.sh || true

if docker info >/dev/null 2>&1; then
	echo "[DEV] Starting core dependencies (postgres, minio, kafka, jitsi)"
	docker compose up -d postgres minio kafka prosody jicofo jvb web | cat
else
	echo "[WARN] Skipping Docker services (daemon unavailable). Start them manually when ready." >&2
fi

# Backend services (start only those present and if mvn exists)
if command -v mvn >/dev/null 2>&1 && [ -f backend/pom.xml ]; then
	echo "[DEV] Building backend"
	( cd backend && mvn -q -DskipTests install )
	if [ -d backend/auth-service ]; then
		echo "[DEV] Starting auth-service"
		( cd backend/auth-service && mvn -q spring-boot:run ) &
	fi
else
	echo "[INFO] Skipping local backend build (mvn not found). Use Docker images or install Maven." >&2
fi

# Frontend
if command -v pnpm >/dev/null 2>&1 && [ -f frontend/package.json ]; then
	echo "[DEV] Installing frontend deps"
	( cd frontend && pnpm install --no-frozen-lockfile )
	echo "[DEV] Starting frontend"
	( cd frontend && pnpm dev ) &
else
	echo "[INFO] Skipping frontend start (pnpm not found or package.json missing)." >&2
fi

wait || true