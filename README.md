# LGT tech assessment

## Local Docker Compose suite

Copy `.env.example` to `.env` and set `OPENAI_API_KEY`, then run `docker compose up --build`.

- UI: http://localhost:8080/
- Keycloak: http://localhost:8080/auth/ (admin: `admin` / `admin`)
- Chat service: http://localhost:8081/api/conversations
- IT knowledge-base MCP: http://localhost:8082/mcp

The demo user is `demo` / `demo`. The UI sends its Keycloak access token to the chat service, which relays it to the knowledge-base service for MCP requests.
