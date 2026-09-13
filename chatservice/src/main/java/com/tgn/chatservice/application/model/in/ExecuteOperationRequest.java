package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;
import com.tgn.chatservice.domain.model.operation.OperationProposal;

import java.util.List;

public record ExecuteOperationRequest(
        OperationProposal operationProposal,
        List<ConversationEntry> conversationHistory
) {}
