package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationProposal;

import java.time.Instant;
import java.util.UUID;

public record OperationStartedEvent(
        UUID id,
        OperationProposal proposal,
        Instant createdAt
) implements ConversationEvent {

    public OperationStartedEvent(UUID id, OperationProposal proposal) {
        this(id, proposal, Instant.now());
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source(),
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        "Starting execution of operation %s".formatted(proposal.name()),
                        createdAt
                )
        );
    }
}
