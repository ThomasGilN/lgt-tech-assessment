package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;

import java.util.List;

public record RequestOperationProposalRequest(
        List<ConversationEntry> conversationHistory
){}
