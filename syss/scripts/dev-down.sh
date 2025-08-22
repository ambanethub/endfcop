#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
ROOT_DIR=$(cd "$SCRIPT_DIR/.." && pwd)

export COMPOSE_PROJECT_NAME=syss
cd "$ROOT_DIR"

echo "[dev-down] Stopping containers..."
docker compose down -v --remove-orphans || true

echo "[dev-down] Done."