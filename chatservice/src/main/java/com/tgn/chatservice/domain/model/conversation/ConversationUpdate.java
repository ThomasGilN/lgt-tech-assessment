package com.tgn.chatservice.domain.model.conversation;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConversationUpdate(
        UUID id,
        UUID conversationId,
        ConversationEntrySource source,
        ConversationUpdateData data
) {
    public record ConversationUpdateData(
            String entryName,
            String text,
            Map<String, Object> parameters,
            Instant timestamp
    ) {
        public ConversationUpdateData(String entryName, String text, Instant timestamp) {
            this(entryName, text, Map.of(), timestamp);
        }
    }
}
