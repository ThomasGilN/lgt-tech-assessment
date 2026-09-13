# Local Keycloak

This directory contains the reproducible Keycloak realm for the local Document Chat demo.
It is development-only and uses deliberately simple credentials.

## Prerequisite

Start Rancher Desktop with the `dockerd (moby)` container engine enabled and set an OpenAI API key
in a root-level `.env` file. Use `.env.example` as the template.

## Start

From the repository root in PowerShell, start all four services:

```powershell
Copy-Item .env.example .env
# Replace the placeholder in .env, then run:
& 'C:\Program Files\Rancher Desktop\resources\resources\win32\docker-cli-plugins\docker-compose.exe' up --build
```

Keycloak imports `import/document-chat-realm.json` while the container starts.

## Addresses and credentials

- Application: <http://localhost:8080>
- Keycloak: <http://localhost:8080/auth/>
- Realm discovery: <http://localhost:8080/auth/realms/document-chat/.well-known/openid-configuration>
- Administration console: <http://localhost:8080/auth/admin/>
- Bootstrap administrator: `admin` / `admin`
- Demo application user: `demo` / `demo`
- SPA client ID: `chat-ui`
- API audiences: `chat-api` and `mcp-api`

The `chat-ui` client uses Authorization Code Flow with PKCE S256. The integrated application exposes
only the UI proxy on `127.0.0.1:8080`; chat, RAG/MCP, and Keycloak have no direct host ports.

## Logs and shutdown

```powershell
& 'C:\Program Files\Rancher Desktop\resources\resources\win32\docker-cli-plugins\docker-compose.exe' logs -f keycloak
```

```powershell
& 'C:\Program Files\Rancher Desktop\resources\resources\win32\docker-cli-plugins\docker-compose.exe' down
```

Realm data lives in the container's development database. Recreating the container resets it to the
checked-in import, which keeps this learning environment deterministic.
