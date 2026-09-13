# LGT tech assessment — Document Chat

A local demo application for chatting with an internal IT knowledge base. Users can search for articles, read their contents, and create new articles after approving the proposed operation.

For setup commands, credentials, and a walkthrough, see [RUNME.md](RUNME.md).

## Architecture

Docker Compose runs four services. OpenAI is an external dependency.

| Component | Responsibility | Main technologies |
| --- | --- | --- |
| `webui/` | Login, conversation view, available operations, and Approve/Deny buttons. Nginx serves the built UI and proxies API and authentication requests. | Vue 3, TypeScript, Pinia, Vue Router, Vite, Nginx |
| `chatservice/` | Stores conversations, asks the model to select an operation, handles approvals, calls knowledge-base tools, and publishes conversation updates. | Java 21, Spring Boot, WebFlux, Spring AI, OpenAI, MCP client |
| `itknowledgebase/` | Exposes article search, retrieval, and creation as Model Context Protocol (MCP) tools. Reads and writes the local knowledge-base files. | Java 21, Spring Boot, Spring MVC, Spring AI MCP server |
| `keycloak/` | Supplies the imported `document-chat` realm, demo user, and OAuth clients. Issues access tokens for the UI and services. | Keycloak, OpenID Connect |

## How a conversation works

1. The browser signs in through Keycloak using the `chat-ui` client and Authorization Code Flow with PKCE. It sends the resulting bearer token with API requests.
2. The UI creates or resumes a conversation, loads the available operations, and subscribes to Server-Sent Events (SSE). The subscription first receives the existing conversation history.
3. A user message reaches the chat service through Nginx. The chat service supplies recent conversation context and available tool definitions to OpenAI.
4. The model can answer directly or propose one operation. Article searches and reads execute automatically. Article creation waits for an **Approve** or **Deny** decision; approval requests expire after one minute, checked when a decision is submitted.
5. The chat service calls the selected MCP tool, forwarding the user's access token. The chat API validates the `chat-api` audience; the knowledge-base service validates `mcp-api`. New articles use the authenticated username as their author.
6. The model turns the tool result into an assistant response. Messages and operation events reach the UI over SSE. A denied or expired request produces a follow-up response without executing the operation.

## Knowledge-base operations

| Tool | Behavior | Requires approval in the chat app |
| --- | --- | --- |
| `search_articles` | Matches query words against article descriptions and returns up to 10 article previews. | No |
| `get_article` | Retrieves the full text of an article by its ID. | No |
| `create_article` | Creates a Markdown article and updates its JSON metadata. | Yes |

Search uses simple word matching against descriptions. The current implementation has no embeddings or vector database. Each user message can trigger at most one tool, so reading a full article after a search can require a follow-up message. The model context includes the latest 10 relevant conversation entries.

## Code organization and storage

Both Java services separate domain models, application use cases and ports, and infrastructure adapters. This keeps conversation/article behavior separate from REST, MCP, authentication, model calls, and storage. Spring configuration wires these pieces together.

| Location | Contents |
| --- | --- |
| `webui/src/` | Views and components, Pinia stores, authentication, and API/SSE client code. |
| `chatservice/src/main/` | Chat application code and service configuration. |
| `itknowledgebase/src/main/` | Knowledge-base application code and service configuration. |
| `itknowledgebase/data/` | `knowledge-base.json` metadata and Markdown/text files under `articles/`. |
| `keycloak/import/` | Realm, clients, and demo-user configuration imported at startup. |
| `compose.yaml` | Service builds, networking, environment variables, and data mount. |

Conversations, pending approvals, and subscriptions live in chat-service memory and disappear when that service restarts. Article files and metadata are bind-mounted from `itknowledgebase/data/`, so successful article creation changes files in the checkout and survives container recreation. Metadata is loaded into memory at knowledge-base startup; restart that service after manually editing the metadata file.

Keycloak uses its container's development database without a persistent volume. Removing and recreating the container resets its state to the imported realm. The supplied credentials and configuration are intended for this local demo.

The browser uses [localhost:8080](http://localhost:8080/). Compose also publishes the authenticated chat API on localhost port `8081` and the MCP endpoint on localhost port `8082`; Keycloak is reached through the UI's `/auth/` proxy.
