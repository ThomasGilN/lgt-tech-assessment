package com.tgn.chatservice.domain.model.operation;

import java.util.Optional;

public record OperationProposalResponse(
        Optional<OperationProposal> proposal,
        String reason
) {
}
