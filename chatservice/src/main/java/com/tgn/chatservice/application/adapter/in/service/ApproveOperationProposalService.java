package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.ApproveOperationProposalRequest;
import com.tgn.chatservice.application.port.in.usecase.ApproveOperationProposalUseCase;
import com.tgn.chatservice.application.port.out.repository.OperationApprovalRepository;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;
import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;

import java.time.Instant;

public class ApproveOperationProposalService implements ApproveOperationProposalUseCase {

    private final OperationApprovalRepository operationApprovalRepository;

    public ApproveOperationProposalService(OperationApprovalRepository operationApprovalRepository) {
        this.operationApprovalRepository = operationApprovalRepository;
    }

    @Override
    public OperationApprovalRequestDecision perform(ApproveOperationProposalRequest request) {
        return operationApprovalRepository.findById(request.operationId())
                .map( operationApprovalRequest -> {
                    if(Instant.now().isAfter(operationApprovalRequest.expiresAt())) {
                        return new OperationApprovalRequestDecision(
                                operationApprovalRequest.proposal(),
                                "Operation approval request expired",
                                false
                        );
                    }

                    return new OperationApprovalRequestDecision(
                            operationApprovalRequest.proposal(),
                            "User decision",
                            request.userDecision() == OperationApprovalUserDecision.APPROVED
                    );
                }).orElseThrow();
    }
}
