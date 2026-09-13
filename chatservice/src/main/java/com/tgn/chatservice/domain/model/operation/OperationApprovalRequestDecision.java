package com.tgn.chatservice.domain.model.operation;

public record OperationApprovalRequestDecision(
        OperationProposal proposal,
        String reason,
        boolean isApproved
) {

}
