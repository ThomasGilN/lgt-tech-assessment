package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.PushConversationEntryRequest;
import com.tgn.chatservice.domain.model.conversation.Conversation;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.conversation.message.UserMessage;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationRepository;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationSubscriptionRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PushConversationEntryServiceTest {

    @Test
    void pushConversationEntryServiceTest() {

        final var conversationRepo = new InMemoryConversationRepository();
        final var subscriptionRepo = new InMemoryConversationSubscriptionRepository();

        final var service = new PushConversationEntryService(conversationRepo, subscriptionRepo);

        final var conversationId = UUID.randomUUID();

        final var conversation = new Conversation(conversationId);
        final var userMessage = new UserMessage(UUID.randomUUID(), "user message");

        final var listener = (Consumer<ConversationUpdate>) mock(Consumer.class);

        conversationRepo.save(conversation);
        subscriptionRepo.create(conversationId, listener);

        service.perform(new PushConversationEntryRequest(conversationId, userMessage));

        verify(listener).accept(any(ConversationUpdate.class));
    }

}
