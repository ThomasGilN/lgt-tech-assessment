package com.tgn.chatservice.infrastructure.adapter.out.repository;

import com.tgn.chatservice.application.port.out.repository.OperationApprovalRepository;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOperationApprovalRepository implements OperationApprovalRepository {

    private final Map<UUID, OperationApprovalRequest> operationApprovalRequests = new ConcurrentHashMap<>();

    @Override
    public OperationApprovalRequest save(OperationApprovalRequest operationApprovalRequest) {
        operationApprovalRequests.put(operationApprovalRequest.id(), operationApprovalRequest);
        return operationApprovalRequest;
    }

    @Override
    public Optional<OperationApprovalRequest> findById(UUID id) {
        return Optional.ofNullable(operationApprovalRequests.get(id));
    }
}
