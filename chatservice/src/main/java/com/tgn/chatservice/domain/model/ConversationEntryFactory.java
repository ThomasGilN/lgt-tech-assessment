package com.tgn.chatservice.domain.model;

import com.tgn.chatservice.domain.model.conversation.event.*;
import com.tgn.chatservice.domain.model.conversation.message.AssistantMessage;
import com.tgn.chatservice.domain.model.conversation.message.UserMessage;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.domain.model.operation.OperationResult;

public interface ConversationEntryFactory {

    UserMessage createUserMessage(String message);
    AssistantMessage createAssistantMessage(String message);

    OperationProposedEvent createOperationProposedEvent(OperationProposal proposedOperation);
    OperationApprovalRequestedEvent createOperationApprovalRequestedEvent(OperationApprovalRequest approvalRequest);
    OperationStartedEvent createOperationStartedEvent(OperationProposal proposal);
    OperationFailedEvent createOperationFailedEvent(OperationProposal proposal, String errorMessage);
    OperationCompletedEvent createOperationCompletedEvent(OperationProposal proposal, OperationResult result);
    OperationApprovalDecisionTakenEvent createOperationApprovalTakenEvent(OperationApprovalRequestDecision decision);
}
