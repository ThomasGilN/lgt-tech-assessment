package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;

import java.util.UUID;

public record PushConversationEntryRequest(
        UUID conversationId,
        ConversationEntry entry
) { }
