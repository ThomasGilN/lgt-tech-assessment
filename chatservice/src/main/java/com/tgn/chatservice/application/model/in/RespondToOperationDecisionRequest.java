package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;

import java.util.List;

public record RespondToOperationDecisionRequest(
        List<ConversationEntry> conversationHistory
) {

    public RespondToOperationDecisionRequest {
        conversationHistory = List.copyOf(conversationHistory);
    }
}
