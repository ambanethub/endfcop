#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

if docker info >/dev/null 2>&1; then
	echo "[DEV] Stopping Docker services"
	docker compose down -v --remove-orphans | cat
else
	echo "[WARN] Docker daemon not reachable; nothing to stop." >&2
fi