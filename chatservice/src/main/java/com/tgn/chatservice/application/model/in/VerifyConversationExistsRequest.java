package com.tgn.chatservice.application.model.in;

import java.util.UUID;

public record VerifyConversationExistsRequest(
        UUID conversationId
) {
}
