package com.tgn.chatservice.application.port.out.repository;

import com.tgn.chatservice.domain.model.conversation.Conversation;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository {

    Conversation save(Conversation conversation);

    Optional<Conversation> findById(UUID conversationId);
}
