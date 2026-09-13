package com.tgn.chatservice.infrastructure.adapter.out.repository;

import com.tgn.chatservice.application.port.out.repository.ConversationSubscriptionRepository;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class InMemoryConversationSubscriptionRepository implements ConversationSubscriptionRepository {

    private final Map<UUID, Consumer<ConversationUpdate>> subscriptions = new ConcurrentHashMap<>();

    @Override
    public void create(UUID conversationId, Consumer<ConversationUpdate> listener) {
        subscriptions.put(conversationId, listener);
    }

    @Override
    public void remove(UUID conversationId) {
        subscriptions.remove(conversationId);
    }

    @Override
    public Optional<Consumer<ConversationUpdate>> findById(UUID conversationId) {
        return Optional.ofNullable(subscriptions.get(conversationId));
    }
}
