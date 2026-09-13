package com.tgn.chatservice.infrastructure.security;

import io.modelcontextprotocol.common.McpTransportContext;

import java.util.Map;
import java.util.function.Supplier;

public final class McpBearerTokenRelay {

    public static final String ACCESS_TOKEN = "access-token";

    private static final ThreadLocal<String> CURRENT_TOKEN = new ThreadLocal<>();

    private McpBearerTokenRelay() {
    }

    public static <T> T withToken(String token, Supplier<T> action) {
        CURRENT_TOKEN.set(token);
        try {
            return action.get();
        } finally {
            CURRENT_TOKEN.remove();
        }
    }

    public static McpTransportContext currentContext() {
        var token = CURRENT_TOKEN.get();
        return token == null
                ? McpTransportContext.EMPTY
                : McpTransportContext.create(Map.of(ACCESS_TOKEN, token));
    }
}
