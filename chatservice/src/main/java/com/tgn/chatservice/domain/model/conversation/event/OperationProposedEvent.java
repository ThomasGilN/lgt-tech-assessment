package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.OperationProposal;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record OperationProposedEvent(
        UUID id,
        OperationProposal proposal,
        Instant createdAt
) implements ConversationEvent {

    public OperationProposedEvent(UUID id, OperationProposal proposal){
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
                        "Its proposed to use operation %s with parameters %s"
                                .formatted(proposal.name(), proposal.arguments().toString()),
                        Map.of(
                                "operationName", proposal.name()
                        ),
                        createdAt
                )
        );
    }
}
