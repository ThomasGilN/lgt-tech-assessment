package com.tgn.chatservice.domain.model.operation;

import java.time.Instant;
import java.util.UUID;

public record OperationApprovalRequest(
        UUID id,
        OperationProposal proposal,
        Instant expiresAt
) {

}
