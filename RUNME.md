# Run and use Document Chat

See [README.md](README.md) for the architecture. Run the following commands from the repository root, the directory containing `compose.yaml`. The setup examples use PowerShell.

## Prerequisites

- A running Docker-compatible engine with Linux containers and Docker Compose. With Rancher Desktop, select the `dockerd (moby)` container engine.
- An OpenAI API key that can make model requests.
- Internet access to download images and build dependencies, and to call OpenAI.
- Local ports `8080`, `8081`, and `8082` available.

The container builds supply Java, Node.js, and the build tools; you do not need to install these separately for the Compose setup.

Check that Docker and Compose are available:

```powershell
docker version
docker compose version
```

## Start the application

1. Create the environment file if it does not already exist:

   ```powershell
   if (-not (Test-Path -LiteralPath .env)) {
       Copy-Item -LiteralPath .env.example -Destination .env
   }
   ```

2. Open `.env` in your editor and replace the placeholder with your API key:

   ```dotenv
   OPENAI_API_KEY=replace-with-your-openai-api-key
   ```

   The root `.env` is ignored by Git. Compose passes this key to `chatservice`.

3. Build and start all four services in the background:

   ```powershell
   docker compose up --build -d
   ```

4. Check the service status and startup logs:

   ```powershell
   docker compose ps
   docker compose logs --tail=100 -f
   ```

   The first build downloads dependencies and can take several minutes. Compose starts dependencies in order but does not define readiness health checks; wait for both Spring services and Keycloak to finish starting before opening the app. Press `Ctrl+C` to stop following logs; the containers keep running.

5. Open [http://localhost:8080/](http://localhost:8080/) and sign in with username **`demo`** and password **`demo`**. A conversation starts automatically after login.
   A 502 Bad Gateway error page usually means that the application services are not ready yet, try again after a minute.

Use `localhost` in the browser URL to match the configured authentication redirects and token issuer.

### Addresses and credentials

| Purpose | Address | Credentials or access |
| --- | --- | --- |
| Application | [localhost:8080](http://localhost:8080/) | `demo` / `demo` |
| Keycloak administration | [localhost:8080/auth/admin/](http://localhost:8080/auth/admin/) | `admin` / `admin` |
| Chat API base | [localhost:8081/api/conversations](http://localhost:8081/api/conversations) | Bearer token with `chat-api` audience |
| Knowledge-base MCP endpoint | [localhost:8082/mcp](http://localhost:8082/mcp) | MCP client and bearer token with `mcp-api` audience |

The API and MCP addresses are service endpoints, not additional application pages. The browser UI handles authentication and sends its chat requests through `http://localhost:8080/api/`.

## Use the app

### Search and read articles

1. Check that the **Operations** sidebar lists search, get, and create article operations. This list shows available capabilities; request them by typing into the chat.
2. Enter this message and click **Send**:

   > Search the knowledge base for remote access documentation.

3. Wait for the operation events and assistant response. The proposed operation is highlighted in the sidebar.
4. To retrieve the full article, send a follow-up message:

   > Read the full Remote Access and VPN Guide and summarize its requirements.

Search returns article previews. The app performs at most one tool operation per message, so use a follow-up to retrieve an article found in the previous response. The model selects tools from your messages, so wording of responses may vary.

Other topics in the supplied data include password reset, MFA recovery, phishing, onboarding, and file restoration. Search matches words in article descriptions; simple, relevant keywords work best.

### Create an article

1. Send a request with a title and the complete content, for example:

   > Create an article titled "Demo Troubleshooting Notes" with this content: "For this demo, record the error message and the steps that caused it before asking for help."

2. Read the proposed operation and its parameters in the conversation, then click **Approve** to save it or **Deny** to cancel it.
3. Submit your decision within **one minute** of the approval request. The server checks expiry when you click a decision button. A denied or expired operation is not executed, and the assistant responds so you can continue the conversation.
4. After a successful approval, the article is written under `itknowledgebase/data/articles/` and registered in `itknowledgebase/data/knowledge-base.json`. The author comes from the signed-in user.

Use a different title when repeating the example: article filenames are derived from titles, and creation fails if the file already exists. Decision buttons can remain visible in older messages; submit each approval request only once. To retry after denial or expiry, explicitly ask the assistant to create the article again.

The message field is disabled while the app awaits an assistant response. If an approval request is pending, use **Approve** or **Deny** to continue.

### Continue or start a conversation

- Refreshing the current `/conversation/<id>` URL reloads its history while `chatservice` is still running.
- Open [localhost:8080/conversation](http://localhost:8080/conversation) without an ID to start a new conversation.
- Keep a conversation open in one tab; the current server stores one live subscriber per conversation.
- Use **Sign out** in the sidebar to end the login session.

## Logs, restart, and shutdown

Follow one service's logs, replacing `chatservice` with `ui`, `itknowledgebase`, or `keycloak` as needed:

```powershell
docker compose logs --tail=100 -f chatservice
```

Restart one service:

```powershell
docker compose restart chatservice
```

After changing source code or `.env`, rebuild and apply the configuration changes:

```powershell
docker compose up --build -d
```

Stop the containers while keeping them available for a later start:

```powershell
docker compose stop
```

Start those existing containers again later:

```powershell
docker compose start
```

To stop and remove the suite's containers and network instead:

```powershell
docker compose down
```

Chat history and pending approvals are lost whenever `chatservice` stops. Articles remain in the checkout after either shutdown command. Keycloak changes survive a stop/start of the same container, but are lost when its container is removed and recreated; the next startup imports the checked-in realm again.

## Troubleshooting

| Symptom | What to check |
| --- | --- |
| Docker cannot connect to its engine | Start your container engine and confirm that `docker version` shows a server. |
| Compose reports `Set OPENAI_API_KEY in .env` | Check that `.env` exists at the repository root and contains a nonempty `OPENAI_API_KEY`. Replace the example placeholder before starting. |
| A published port is already allocated | Stop the conflicting local service. Keep the documented ports so the authentication URLs remain consistent. |
| The app reports that authentication could not initialize, or shows a gateway error during startup | Check `docker compose ps` and the `ui`/`keycloak` logs, wait for startup to finish, then reload `http://localhost:8080/`. |
| Login succeeds but the operations list stays empty | Check `chatservice` and `itknowledgebase` logs for MCP connection or token-validation errors, then reload once both services are ready. |
| Sending a message fails or no assistant response arrives | Check `chatservice` logs for API-key, quota, model-access, or network errors. Correct the configuration and run `docker compose up --build -d` if needed, then reload the conversation. |
| Search finds nothing | Try words present in article descriptions, such as `remote access` or `password reset`. Search does not scan full article contents. |
| An approval expires | Click a decision button to let the server process the expiry, then ask the assistant to retry. Old approval requests cannot be reused. |
| Creating an article fails because a file exists | Choose a new title; the creation tool does not overwrite existing article files. |
| Chat history disappears after a restart | This is expected: conversations are held in memory. Reopening the old URL creates a new conversation if the old ID no longer exists. |

If Rancher Desktop is installed on Windows but `docker compose` is unavailable on your PATH, its bundled Compose executable may be available at the following location. Substitute it for `docker compose` in the commands above:

```powershell
& 'C:\Program Files\Rancher Desktop\resources\resources\win32\docker-cli-plugins\docker-compose.exe' version
```
