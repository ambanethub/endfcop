#!/usr/bin/env bash
set -euo pipefail

check_bin() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "[check-env] Missing: $1" >&2
    return 1
  fi
}

check_bin docker || true
check_bin docker compose || true
check_bin bash || true

if command -v node >/dev/null 2>&1; then
  NODE_VER=$(node -v)
  echo "[check-env] Node $NODE_VER"
else
  echo "[check-env] Node not found; frontend will build in container." >&2
fi

if ! groups | grep -q docker; then
  echo "[check-env] You may not be in the docker group. If you see permission denied, run: sudo usermod -aG docker $USER && newgrp docker" >&2
fi