package com.tgn.chatservice.domain.model.conversation.message;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;
import com.tgn.chatservice.domain.model.conversation.ConversationEntrySource;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;

import java.time.Instant;
import java.util.UUID;

public record UserMessage(
        UUID id,
        ConversationEntrySource source,
        Instant createdAt,
        String message
) implements ConversationEntry {

    public UserMessage(UUID id, String message) {
        this(id, ConversationEntrySource.USER, Instant.now(), message);
    }

    @Override
    public ConversationUpdate toConversationUpdate(UUID conversationId) {
        return new ConversationUpdate(
                id,
                conversationId,
                source,
                new ConversationUpdate.ConversationUpdateData(
                        getClass().getSimpleName(),
                        message,
                        createdAt
                )
        );
    }
}
