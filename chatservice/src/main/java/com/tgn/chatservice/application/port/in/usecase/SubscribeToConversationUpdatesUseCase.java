package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.application.model.in.ConversationUpdateSubscriptionRequest;
import com.tgn.chatservice.application.model.out.ConversationSubscription;

public interface SubscribeToConversationUpdatesUseCase {

    ConversationSubscription perform(ConversationUpdateSubscriptionRequest request);
}
