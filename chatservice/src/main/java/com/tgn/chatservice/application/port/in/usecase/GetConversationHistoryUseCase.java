package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;

import java.util.List;
import java.util.UUID;

public interface GetConversationHistoryUseCase {

    List<ConversationEntry> perform(UUID conversationId);
}
