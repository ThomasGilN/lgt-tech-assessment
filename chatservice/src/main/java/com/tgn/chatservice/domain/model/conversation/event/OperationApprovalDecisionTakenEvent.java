package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;

import java.time.Instant;
import java.util.UUID;

public record OperationApprovalDecisionTakenEvent(
        UUID id,
        OperationApprovalRequestDecision decision,
        Instant createdAt
) implements ConversationEvent {

    public OperationApprovalDecisionTakenEvent(UUID id, OperationApprovalRequestDecision decision) {
        this(id, decision, Instant.now());
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source(),
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        "Operation %s approval status is: %s, because: %s"
                                .formatted(
                                        decision.proposal().name(),
                                        decision.isApproved(),
                                        decision.reason()
                                ),
                        createdAt
                )
        );
    }
}
