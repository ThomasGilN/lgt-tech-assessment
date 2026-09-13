package com.tgn.chatservice.application.port.out.repository;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface ConversationSubscriptionRepository {

    void create(UUID conversationId, Consumer<ConversationUpdate> listener);

    void remove(UUID conversationId);

    Optional<Consumer<ConversationUpdate>> findById(UUID conversationId);
}
