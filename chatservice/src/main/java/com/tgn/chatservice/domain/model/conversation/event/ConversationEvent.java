package com.tgn.chatservice.domain.model.conversation.event;

import com.tgn.chatservice.domain.model.conversation.ConversationEntry;
import com.tgn.chatservice.domain.model.conversation.ConversationEntrySource;

public interface ConversationEvent extends ConversationEntry {

    @Override
    default ConversationEntrySource source() {
        return ConversationEntrySource.EVENT;
    }
}
