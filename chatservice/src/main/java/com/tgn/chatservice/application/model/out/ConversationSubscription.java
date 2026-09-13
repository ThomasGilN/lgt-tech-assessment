package com.tgn.chatservice.application.model.out;

@FunctionalInterface
public interface ConversationSubscription {

    void unsubscribe();
}
