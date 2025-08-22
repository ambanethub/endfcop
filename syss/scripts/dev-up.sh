#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
ROOT_DIR=$(cd "$SCRIPT_DIR/.." && pwd)

chmod +x "$SCRIPT_DIR"/*.sh || true

echo "[dev-up] Checking environment..."
"$SCRIPT_DIR/check-env.sh"

if ! command -v docker >/dev/null 2>&1; then
  echo "[dev-up] Docker is not installed. Please install Docker Desktop/Engine." >&2
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "[dev-up] Docker daemon not available. If permission denied, run: sudo usermod -aG docker $USER && newgrp docker" >&2
  exit 1
fi

export COMPOSE_PROJECT_NAME=syss
cd "$ROOT_DIR"

echo "[dev-up] Building backend (skip tests)..."
if command -v mvn >/dev/null 2>&1; then
  mvn -q -f backend/pom.xml -DskipTests package || echo "[dev-up] Maven build failed; will rely on Docker build logs."
else
  echo "[dev-up] Maven not found; skipping local build."
fi

echo "[dev-up] Installing frontend deps..."
if command -v pnpm >/dev/null 2>&1; then
  pnpm -C frontend install || echo "[dev-up] pnpm install failed; will install during container build."
fi

echo "[dev-up] Starting Docker dependencies and apps..."
docker compose up -d --build

echo "[dev-up] Seeding demo data..."
"$SCRIPT_DIR/seed.sh" || echo "[dev-up] Seed failed or skipped."

echo "[dev-up] Done. Frontend: http://localhost:3000  API via Nginx: http://localhost:8080"