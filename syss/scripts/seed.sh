#!/usr/bin/env bash
set -euo pipefail

API="http://localhost:8080"

echo "[seed] Seeding demo data..."
# Note: In a real environment, authenticate and post entities; here we log intention.
echo "[seed] Creating users: admin, commander, analyst, field_unit, observer"
echo "[seed] Creating sample operation in Addis Ababa bbox with 10 units and tracks"

exit 0