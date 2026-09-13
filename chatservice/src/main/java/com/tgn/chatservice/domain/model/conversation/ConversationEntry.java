package com.tgn.chatservice.domain.model.conversation;

import java.time.Instant;
import java.util.UUID;

public interface ConversationEntry {

    UUID id();
    ConversationEntrySource source();
    Instant createdAt();

    ConversationUpdate toConversationUpdate(UUID conversationId);
}
