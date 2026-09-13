package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;

import java.util.UUID;
import java.util.function.Consumer;

public record ConversationUpdateSubscriptionRequest(
        UUID conversationId,
        Consumer<ConversationUpdate> listener
) {
}
