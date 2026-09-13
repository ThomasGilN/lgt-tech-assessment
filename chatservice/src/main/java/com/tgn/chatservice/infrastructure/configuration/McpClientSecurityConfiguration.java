package com.tgn.chatservice.infrastructure.configuration;

import com.tgn.chatservice.infrastructure.security.McpBearerTokenRelay;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.common.McpTransportContext;
import org.springframework.ai.mcp.customizer.McpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class McpClientSecurityConfiguration {

    @Bean
    McpClientCustomizer<McpClient.SyncSpec> mcpClientCustomizer() {
        return (name, client) -> client.transportContextProvider(McpBearerTokenRelay::currentContext);
    }

    @Bean
    WebClient.Builder mcpWebClientBuilder() {
        return WebClient.builder().filter((request, next) -> Mono.deferContextual(context -> {
            McpTransportContext mcpContext = context.getOrDefault(
                    McpTransportContext.KEY,
                    McpTransportContext.EMPTY
            );
            var token = mcpContext.get(McpBearerTokenRelay.ACCESS_TOKEN);

            if (!(token instanceof String accessToken)) {
                return next.exchange(request);
            }

            var authenticatedRequest = ClientRequest.from(request)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .build();
            return next.exchange(authenticatedRequest);
        }));
    }
}
