package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.adapter.RandomUUIDIdGenerator;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartConversationServiceTest {

    @Test
    void startConversationServiceTest() {
        final var repository = new InMemoryConversationRepository();
        final var idGenerator = new RandomUUIDIdGenerator();

        final var service = new StartConversationService(repository, idGenerator);
        final var conversationId = service.perform();

        final var conversationSearch = repository.findById(conversationId);
        assertTrue(conversationSearch.isPresent());

        final var conversation = conversationSearch.get();
        assertEquals(conversationId, conversation.conversationId());
        assertEquals(0, conversation.history().size());
    }
}
