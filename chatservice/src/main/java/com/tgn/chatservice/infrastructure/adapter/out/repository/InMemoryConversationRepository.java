package com.tgn.chatservice.infrastructure.adapter.out.repository;

import com.tgn.chatservice.application.port.out.repository.ConversationRepository;
import com.tgn.chatservice.domain.model.conversation.Conversation;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryConversationRepository implements ConversationRepository {

    private final Map<UUID,Conversation> conversations = new ConcurrentHashMap<>();

    @Override
    public Conversation save(Conversation conversation) {
        return conversations.put(conversation.conversationId(), conversation);
    }

    @Override
    public Optional<Conversation> findById(UUID conversationId) {
        return Optional.ofNullable(conversations.get(conversationId));
    }
}
