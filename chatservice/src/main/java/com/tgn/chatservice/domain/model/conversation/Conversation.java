package com.tgn.chatservice.domain.model.conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Conversation(
        UUID conversationId,
        List<ConversationEntry> history
) {
    public Conversation(UUID conversationId) {
        this(conversationId, new ArrayList<>());
    }

    @Override
    public List<ConversationEntry> history() {
        return List.copyOf(history);
    }

    public void pushEntry(ConversationEntry entry) {
        history.add(entry);
    }
}
