#!/usr/bin/env bash
set -euo pipefail

require() {
	if ! command -v "$1" >/dev/null 2>&1; then
		echo "[WARN] Missing dependency: $1" >&2
		return 1
	fi
}

ok=true
require docker || ok=false
require docker compose || ok=false || true
require jq || ok=false || true
require java || ok=false || true
require mvn || ok=false || true
require node || ok=false || true
require pnpm || ok=false || true

if [ "$ok" != true ]; then
	echo "[INFO] Some dependencies are missing. You can still run parts, but for full dev you'll need them." >&2
	echo "- If Docker permission denied: sudo usermod -aG docker $USER && newgrp docker" >&2
fi

if ! docker info >/dev/null 2>&1; then
	echo "[WARN] Docker daemon not reachable. Start Docker Desktop/Service or ensure permissions." >&2
fi