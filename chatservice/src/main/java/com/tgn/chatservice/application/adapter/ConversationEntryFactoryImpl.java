package com.tgn.chatservice.application.adapter;

import com.tgn.chatservice.domain.model.ConversationEntryFactory;
import com.tgn.chatservice.domain.model.IdGenerator;
import com.tgn.chatservice.domain.model.conversation.event.*;
import com.tgn.chatservice.domain.model.conversation.message.AssistantMessage;
import com.tgn.chatservice.domain.model.conversation.message.UserMessage;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.domain.model.operation.OperationResult;

public class ConversationEntryFactoryImpl implements ConversationEntryFactory {

    private final IdGenerator idGenerator;

    public ConversationEntryFactoryImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public UserMessage createUserMessage(String message) {
        return new UserMessage(idGenerator.generateId(), message);
    }

    @Override
    public AssistantMessage createAssistantMessage(String message) {
        return new AssistantMessage(idGenerator.generateId(), message);
    }

    @Override
    public OperationProposedEvent createOperationProposedEvent(OperationProposal proposedOperation) {
        return new OperationProposedEvent(idGenerator.generateId(), proposedOperation);
    }

    @Override
    public OperationApprovalRequestedEvent createOperationApprovalRequestedEvent(OperationApprovalRequest approvalRequest) {
        return new OperationApprovalRequestedEvent(idGenerator.generateId(), approvalRequest);
    }

    @Override
    public OperationStartedEvent createOperationStartedEvent(OperationProposal proposal) {
        return new  OperationStartedEvent(idGenerator.generateId(), proposal);
    }

    @Override
    public OperationFailedEvent createOperationFailedEvent(OperationProposal proposal, String errorMessage) {
        return new OperationFailedEvent(idGenerator.generateId(), proposal, errorMessage);
    }

    @Override
    public OperationCompletedEvent createOperationCompletedEvent(OperationProposal proposal, OperationResult result) {
        return new OperationCompletedEvent(idGenerator.generateId(), proposal, result);
    }

    @Override
    public OperationApprovalDecisionTakenEvent createOperationApprovalTakenEvent(OperationApprovalRequestDecision decision) {
        return new  OperationApprovalDecisionTakenEvent(idGenerator.generateId(), decision);
    }
}
