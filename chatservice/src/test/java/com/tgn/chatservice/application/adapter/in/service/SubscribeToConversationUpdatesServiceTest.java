package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.ConversationUpdateSubscriptionRequest;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationSubscriptionRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscribeToConversationUpdatesServiceTest {

    @Test
    void subscribeToConversationUpdatesServiceTest()
    {
        final var repository = new InMemoryConversationSubscriptionRepository();

        final var service = new SubscribeToConversationUpdatesService(repository);

        final var conversationId = UUID.randomUUID();
        service.perform(
                new ConversationUpdateSubscriptionRequest(conversationId, u -> {})
        );

        final var subscriptionSearch = repository.findById(conversationId);
        assertTrue(subscriptionSearch.isPresent());
    }

    @Test
    void unsubscribeToConversationUpdatesServiceTest()
    {
        final var repository = new InMemoryConversationSubscriptionRepository();

        final var service = new SubscribeToConversationUpdatesService(repository);

        final var conversationId = UUID.randomUUID();
        final var subscription = service.perform(
                new ConversationUpdateSubscriptionRequest(conversationId, u -> {})
        );

        var subscriptionSearch = repository.findById(conversationId);
        assertTrue(subscriptionSearch.isPresent());

        subscription.unsubscribe();

        subscriptionSearch = repository.findById(conversationId);
        assertTrue(subscriptionSearch.isEmpty());
    }
}
