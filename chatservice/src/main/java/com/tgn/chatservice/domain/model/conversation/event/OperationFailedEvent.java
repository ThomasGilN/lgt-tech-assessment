package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationProposal;

import java.time.Instant;
import java.util.UUID;

public record OperationFailedEvent(
        UUID id,
        OperationProposal proposal,
        String errorMessage,
        Instant createdAt
) implements ConversationEvent {

    public OperationFailedEvent(UUID id, OperationProposal proposal, String errorMessage) {
        this(id,  proposal, errorMessage, Instant.now());
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source(),
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        "Operation %s failed with reason: %s."
                                .formatted(proposal.name(), errorMessage.toLowerCase()),
                        createdAt
                )
        );
    }
}
