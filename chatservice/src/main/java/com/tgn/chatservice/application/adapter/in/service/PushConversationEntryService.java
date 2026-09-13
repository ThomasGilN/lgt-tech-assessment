package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.PushConversationEntryRequest;
import com.tgn.chatservice.application.port.in.usecase.PushConversationEntryUseCase;
import com.tgn.chatservice.application.port.out.repository.ConversationRepository;
import com.tgn.chatservice.application.port.out.repository.ConversationSubscriptionRepository;

public class PushConversationEntryService implements PushConversationEntryUseCase {

    private final ConversationRepository conversationRepository;
    private final ConversationSubscriptionRepository conversationSubscriptionRepository;

    public PushConversationEntryService(ConversationRepository conversationRepository, ConversationSubscriptionRepository conversationSubscriptionRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationSubscriptionRepository = conversationSubscriptionRepository;
    }

    @Override
    public void perform(PushConversationEntryRequest request) {
        final var conversationSearch = conversationRepository.findById(request.conversationId());
        final var conversationSubscriberSearch = conversationSubscriptionRepository
                .findById(request.conversationId());

        if(conversationSearch.isPresent() && conversationSubscriberSearch.isPresent()){
            final var conversation = conversationSearch.get();
            final var conversationSubscriber = conversationSubscriberSearch.get();

            conversation.pushEntry(request.entry());

            conversationRepository.save(conversation);
            conversationSubscriber.accept(request.entry().toConversationUpdate(request.conversationId()));
        }
    }
}
