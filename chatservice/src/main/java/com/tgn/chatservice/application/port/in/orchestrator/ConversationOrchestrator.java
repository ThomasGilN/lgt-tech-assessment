package com.tgn.chatservice.application.port.in.orchestrator;

import com.tgn.chatservice.application.model.out.ConversationSubscription;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.Operation;
import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface ConversationOrchestrator {

    List<Operation> getAvailableOperations();

    UUID startConversation(UUID possibleExistingConversationId);

    void pushUserMessage(UUID conversationId, String message);

    ConversationSubscription subscribeToConversationUpdates(
            UUID conversationId,
            Consumer<ConversationUpdate> listener
    );

    void approveOperationExecution(
            UUID conversationId,
            UUID operationId,
            OperationApprovalUserDecision userDecision
    );
}
