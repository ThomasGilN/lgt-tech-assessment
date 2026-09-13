package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.application.model.in.PushConversationEntryRequest;

public interface PushConversationEntryUseCase {

    void perform(PushConversationEntryRequest request);
}
