package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.*;
import com.tgn.chatservice.application.model.out.ConversationSubscription;
import com.tgn.chatservice.application.port.in.orchestrator.ConversationOrchestrator;
import com.tgn.chatservice.application.port.in.usecase.*;
import com.tgn.chatservice.application.port.out.llm.OperationOrchestrator;
import com.tgn.chatservice.domain.model.ConversationEntryFactory;
import com.tgn.chatservice.domain.model.conversation.ConversationEntry;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ConversationOrchestratorService implements ConversationOrchestrator {

    private final OperationOrchestrator operationOrchestrator;

    private final StartConversationUseCase startConversationUseCase;
    private final PushConversationEntryUseCase pushConversationEntryUseCase;
    private final SubscribeToConversationUpdatesUseCase subscribeToConversationUpdatesUseCase;
    private final ApproveOperationProposalUseCase approveOperationProposalUseCase;
    private final GetConversationHistoryUseCase getConversationHistoryUseCase;
    private final CreateOperationApprovalRequestUseCase createOperationApprovalRequestUseCase;
    private final VerifyConversationExistsUseCase verifyConversationExistsUseCase;

    private final ConversationEntryFactory conversationEntryFactory;

    public ConversationOrchestratorService(OperationOrchestrator operationOrchestrator, StartConversationUseCase startConversationUseCase, PushConversationEntryUseCase pushConversationEntryUseCase, SubscribeToConversationUpdatesUseCase subscribeToConversationUpdatesUseCase, ApproveOperationProposalUseCase approveOperationProposalUseCase, GetConversationHistoryUseCase getConversationHistoryUseCase, CreateOperationApprovalRequestUseCase createOperationApprovalRequestUseCase, VerifyConversationExistsUseCase verifyConversationExistsUseCase, ConversationEntryFactory conversationEntryFactory) {
        this.operationOrchestrator = operationOrchestrator;
        this.startConversationUseCase = startConversationUseCase;
        this.pushConversationEntryUseCase = pushConversationEntryUseCase;
        this.subscribeToConversationUpdatesUseCase = subscribeToConversationUpdatesUseCase;
        this.approveOperationProposalUseCase = approveOperationProposalUseCase;
        this.getConversationHistoryUseCase = getConversationHistoryUseCase;
        this.createOperationApprovalRequestUseCase = createOperationApprovalRequestUseCase;
        this.verifyConversationExistsUseCase = verifyConversationExistsUseCase;
        this.conversationEntryFactory = conversationEntryFactory;
    }

    @Override
    public List<Operation> getAvailableOperations() {
        return operationOrchestrator.getAvailableOperations();
    }

    @Override
    public UUID startConversation(UUID possibleExistingConversationId) {
        final var conversationExists = verifyConversationExistsUseCase.perform(
                new VerifyConversationExistsRequest(possibleExistingConversationId)
        );

        if (conversationExists) {
            return possibleExistingConversationId;
        }

        return startConversationUseCase.perform();
    }

    @Override
    public void pushUserMessage(UUID conversationId, String message) {
        recordUserMessage(conversationId, message);

        final var operationProposalResponse = askForOperationProposal(conversationId);

        if(operationProposalResponse.proposal().isEmpty()){
            recordAssistantMessage(conversationId, operationProposalResponse.reason());
            return;
        }

        final var proposedOperation = operationProposalResponse.proposal().get();
        recordOperationProposedEvent(conversationId, proposedOperation);

        if(proposedOperation.requiresApproval()){
            recordOperationApprovalRequestedEvent(conversationId, proposedOperation);
            return;
        }

        executeApprovedOperation(conversationId, proposedOperation);
    }

    @Override
    public ConversationSubscription subscribeToConversationUpdates(UUID conversationId, Consumer<ConversationUpdate> listener) {
        final var subscriptionRequest = new ConversationUpdateSubscriptionRequest(conversationId, listener);
        final var history = getConversationHistoryUseCase.perform(conversationId);
        history.forEach(historyEntry -> listener.accept(historyEntry.toConversationUpdate(conversationId)));
        return subscribeToConversationUpdatesUseCase.perform(subscriptionRequest);
    }

    @Override
    public void approveOperationExecution(UUID conversationId, UUID operationId, OperationApprovalUserDecision userDecision) {
        final var request = new ApproveOperationProposalRequest(conversationId, operationId, userDecision);
        final var decision = approveOperationProposalUseCase.perform(request);
        recordOperationApprovalDecisionTakenEvent(conversationId, decision);

        if(decision.isApproved()){
            executeApprovedOperation(conversationId, decision.proposal());
        }
    }

    private void recordOperationApprovalDecisionTakenEvent(UUID conversationId, OperationApprovalRequestDecision decision) {
        final var operationApprovalTakenEvent = conversationEntryFactory.createOperationApprovalTakenEvent(decision);
        pushConversationEntry(conversationId, operationApprovalTakenEvent);
    }

    private void recordUserMessage(UUID conversationId, String message) {
        final var userMessage = conversationEntryFactory.createUserMessage(message);
        pushConversationEntry(conversationId, userMessage);
    }

    private OperationProposalResponse askForOperationProposal(UUID conversationId) {
        final var conversationHistory = getConversationHistoryUseCase.perform(conversationId);
        final var operationProposalRequest = new RequestOperationProposalRequest(conversationHistory);
        return operationOrchestrator.requestOperationProposal(operationProposalRequest);
    }

    private void recordAssistantMessage(UUID conversationId, String message) {
        final var assistantMessage = conversationEntryFactory.createAssistantMessage(message);
        pushConversationEntry(conversationId, assistantMessage);
    }

    private void recordOperationProposedEvent(UUID conversationId, OperationProposal proposedOperation) {
        final var operationProposalEvent = conversationEntryFactory.createOperationProposedEvent(proposedOperation);
        pushConversationEntry(conversationId, operationProposalEvent);
    }

    private void recordOperationApprovalRequestedEvent(UUID conversationId, OperationProposal proposal) {
        final var createApprovalRequest = new CreateOperationApprovalRequest(proposal);
        final var approvalRequest = createOperationApprovalRequestUseCase.perform(createApprovalRequest);
        final var operationApprovalRequestedEvent = conversationEntryFactory
                .createOperationApprovalRequestedEvent(approvalRequest);
        pushConversationEntry(conversationId, operationApprovalRequestedEvent);
    }

    private void executeApprovedOperation(UUID conversationId, OperationProposal proposal) {
        try {
            recordOperationStartedEvent(conversationId, proposal);
            final var result = executeOperation(conversationId, proposal);
            recordOperationCompletedEvent(conversationId, proposal, result);
            recordAssistantMessage(conversationId, result.assistantResponse());

        } catch (final Exception e) {
            recordOperationFailedEvent(conversationId, proposal, e.getMessage());
            recordAssistantMessage(conversationId, "An error occurred while trying to execute the operation");
        }
    }

    private void recordOperationStartedEvent(UUID conversationId, OperationProposal proposal) {
        final var operationStartedEvent = conversationEntryFactory.createOperationStartedEvent(proposal);
        pushConversationEntry(conversationId, operationStartedEvent);
    }

    private void recordOperationCompletedEvent(UUID conversationId, OperationProposal proposal, OperationResult result) {
        final var operationCompletedEvent = conversationEntryFactory.createOperationCompletedEvent(proposal, result);
        pushConversationEntry(conversationId, operationCompletedEvent);
    }

    private void recordOperationFailedEvent(UUID conversationId, OperationProposal proposal, String errorMessage) {
        final var operationFailedEvent = conversationEntryFactory.createOperationFailedEvent(proposal, errorMessage);
        pushConversationEntry(conversationId, operationFailedEvent);
    }

    private OperationResult executeOperation(UUID conversationId, OperationProposal proposal) {
        final var conversationHistory = getConversationHistoryUseCase.perform(conversationId);
        final var request = new ExecuteOperationRequest(
                proposal,
                conversationHistory
        );
        return operationOrchestrator.executeOperation(request);
    }

    private void pushConversationEntry(UUID conversationId, ConversationEntry conversationEntry) {
        final var request = new PushConversationEntryRequest(conversationId, conversationEntry);
        pushConversationEntryUseCase.perform(request);
    }
}
