package com.tgn.chatservice.application.port.out.repository;

import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;

import java.util.Optional;
import java.util.UUID;

public interface OperationApprovalRepository {

    OperationApprovalRequest save(OperationApprovalRequest operationApprovalRequest);

    Optional<OperationApprovalRequest> findById(UUID operationId);

}
