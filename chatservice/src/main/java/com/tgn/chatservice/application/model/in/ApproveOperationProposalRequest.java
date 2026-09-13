package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;

import java.util.UUID;

public record ApproveOperationProposalRequest(
        UUID conversationId,
        UUID operationId,
        OperationApprovalUserDecision userDecision
) {
}
