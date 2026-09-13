package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.ConversationUpdateSubscriptionRequest;
import com.tgn.chatservice.application.model.out.ConversationSubscription;
import com.tgn.chatservice.application.port.in.usecase.SubscribeToConversationUpdatesUseCase;
import com.tgn.chatservice.application.port.out.repository.ConversationSubscriptionRepository;

public class SubscribeToConversationUpdatesService implements SubscribeToConversationUpdatesUseCase {

    private final ConversationSubscriptionRepository conversationSubscriptionRepository;

    public SubscribeToConversationUpdatesService(ConversationSubscriptionRepository conversationSubscriptionRepository) {
        this.conversationSubscriptionRepository = conversationSubscriptionRepository;
    }

    @Override
    public ConversationSubscription perform(ConversationUpdateSubscriptionRequest request) {
        conversationSubscriptionRepository.create(request.conversationId(), request.listener());
        return () -> conversationSubscriptionRepository.remove(request.conversationId());
    }
}
