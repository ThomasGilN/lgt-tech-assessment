package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record OperationApprovalRequestedEvent(
        UUID id,
        OperationApprovalRequest approvalRequest,
        Instant createdAt
) implements ConversationEvent {

    public OperationApprovalRequestedEvent(UUID id, OperationApprovalRequest approvalRequest) {
        this(id, approvalRequest, Instant.now());
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source(),
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        "Operation %s requires approval.".formatted(approvalRequest.proposal().name()),
                        Map.of("operationId", approvalRequest.id()),
                        createdAt
                )
        );
    }
}
