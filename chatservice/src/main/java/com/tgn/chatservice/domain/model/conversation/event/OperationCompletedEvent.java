package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.domain.model.operation.OperationResult;

import java.time.Instant;
import java.util.UUID;

public record OperationCompletedEvent(
        UUID id,
        OperationProposal proposal,
        OperationResult result,
        Instant createdAt
) implements ConversationEvent {

    public OperationCompletedEvent(UUID id, OperationProposal proposal, OperationResult result) {
        this(id, proposal, result, Instant.now());
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source(),
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        "Operation %s completed".formatted(proposal.name()),
                        createdAt
                )
        );
    }
}
