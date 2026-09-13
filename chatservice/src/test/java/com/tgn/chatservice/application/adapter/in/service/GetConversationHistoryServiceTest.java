package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.domain.model.conversation.Conversation;
import com.tgn.chatservice.domain.model.conversation.message.UserMessage;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetConversationHistoryServiceTest {

    @Test
    void getConversationHistory() {
        final var repo = new InMemoryConversationRepository();

        final var conversationId = UUID.randomUUID();
        final var conversation = new Conversation(conversationId);

        final var messageId = UUID.randomUUID();
        final var userMessage = new UserMessage(messageId, "hi");

        conversation.pushEntry(userMessage);
        repo.save(conversation);

        final var service = new GetConversationHistoryService(repo);

        final var history = service.perform(conversationId);

        assertEquals(1, history.size());
        assertEquals(messageId, history.getFirst().id());
    }

    @Test
    void getConversationHistoryEmptyWhenNotFound() {
        final var repo = new InMemoryConversationRepository();

        final var conversationId = UUID.randomUUID();
        final var conversation = new Conversation(conversationId);

        final var messageId = UUID.randomUUID();
        final var userMessage = new UserMessage(messageId, "hi");

        conversation.pushEntry(userMessage);
        repo.save(conversation);

        final var service = new GetConversationHistoryService(repo);

        final var history = service.perform(UUID.randomUUID());

        assertEquals(0, history.size());
    }
}
