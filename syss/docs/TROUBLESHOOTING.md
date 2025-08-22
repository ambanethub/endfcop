# Troubleshooting

- Docker permission denied: `sudo usermod -aG docker $USER` then `newgrp docker` or re-login.
- Leaflet SSR crash: ensure map components are client-only and use `dynamic(..., { ssr:false })`.
- Next 15 metadata warnings: move viewport/themeColor to `generateViewport()`.
- Spring Security OAuth2 errors: confirm JWKS URL and Boot version alignment.
- IndexedDB key errors: validate keys are strings/numbers; wrap transactions; handle migrations.