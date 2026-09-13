package com.tgn.chatservice.domain.model.operation;

public record OperationResult(
        OperationProposal proposal,
        String rawResult,
        String assistantResponse
) {
}
