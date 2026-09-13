package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.application.model.in.VerifyConversationExistsRequest;

public interface VerifyConversationExistsUseCase {

    boolean perform(VerifyConversationExistsRequest request);
}
