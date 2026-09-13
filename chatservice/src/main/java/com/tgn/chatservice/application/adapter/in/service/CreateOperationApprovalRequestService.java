package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.CreateOperationApprovalRequest;
import com.tgn.chatservice.application.port.in.usecase.CreateOperationApprovalRequestUseCase;
import com.tgn.chatservice.application.port.out.repository.OperationApprovalRepository;
import com.tgn.chatservice.domain.model.IdGenerator;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CreateOperationApprovalRequestService implements CreateOperationApprovalRequestUseCase {

    private final OperationApprovalRepository operationApprovalRepository;
    private final IdGenerator idGenerator;

    public CreateOperationApprovalRequestService(OperationApprovalRepository operationApprovalRepository, IdGenerator idGenerator) {
        this.operationApprovalRepository = operationApprovalRepository;
        this.idGenerator = idGenerator;
    }


    @Override
    public OperationApprovalRequest perform(CreateOperationApprovalRequest request) {
        final var approvalRequest = new OperationApprovalRequest(
                idGenerator.generateId(),
                request.proposal(),
                Instant.now().plus(1, ChronoUnit.MINUTES)
        );

        operationApprovalRepository.save(approvalRequest);

        return approvalRequest;
    }
}
