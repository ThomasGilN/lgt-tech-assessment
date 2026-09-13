package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.application.model.in.ApproveOperationProposalRequest;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;

public interface ApproveOperationProposalUseCase {

    OperationApprovalRequestDecision perform(ApproveOperationProposalRequest request);
}
