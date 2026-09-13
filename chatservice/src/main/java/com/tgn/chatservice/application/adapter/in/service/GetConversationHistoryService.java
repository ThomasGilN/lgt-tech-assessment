package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.port.in.usecase.GetConversationHistoryUseCase;
import com.tgn.chatservice.application.port.out.repository.ConversationRepository;
import com.tgn.chatservice.domain.model.conversation.Conversation;
import com.tgn.chatservice.domain.model.conversation.ConversationEntry;

import java.util.List;
import java.util.UUID;

public class GetConversationHistoryService implements GetConversationHistoryUseCase {

    private final ConversationRepository conversationRepository;

    public GetConversationHistoryService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public List<ConversationEntry> perform(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .map(Conversation::history)
                .orElse(List.of());
    }
}
