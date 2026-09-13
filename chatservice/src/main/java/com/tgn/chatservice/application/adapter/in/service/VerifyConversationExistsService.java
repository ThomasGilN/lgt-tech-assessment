package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.VerifyConversationExistsRequest;
import com.tgn.chatservice.application.port.in.usecase.VerifyConversationExistsUseCase;
import com.tgn.chatservice.application.port.out.repository.ConversationRepository;

import java.util.Objects;

public class VerifyConversationExistsService implements VerifyConversationExistsUseCase {

    private final ConversationRepository conversationRepository;

    public VerifyConversationExistsService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public boolean perform(VerifyConversationExistsRequest request) {
        if(Objects.isNull(request.conversationId())){
            return false;
        }
        return conversationRepository.findById(request.conversationId()).isPresent();
    }
}
