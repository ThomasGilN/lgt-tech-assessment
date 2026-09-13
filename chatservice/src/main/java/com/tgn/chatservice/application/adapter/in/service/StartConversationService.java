package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.port.in.usecase.StartConversationUseCase;
import com.tgn.chatservice.application.port.out.repository.ConversationRepository;
import com.tgn.chatservice.domain.model.IdGenerator;
import com.tgn.chatservice.domain.model.conversation.Conversation;

import java.util.UUID;

public class StartConversationService implements StartConversationUseCase {

    private final ConversationRepository conversationRepository;
    private final IdGenerator idGenerator;

    public StartConversationService(ConversationRepository conversationRepository, IdGenerator idGenerator) {
        this.conversationRepository = conversationRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public UUID perform() {
        final var conversation = new Conversation(idGenerator.generateId());
        conversationRepository.save(conversation);
        return conversation.conversationId();
    }
}
