package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.CreateOperationApprovalRequest;
import com.tgn.chatservice.domain.model.IdGenerator;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryOperationApprovalRepository;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateOperationApprovalRequestServiceTest {

    @Test
    void createOperationApprovalRequestServiceTest() {
        final var repo = new InMemoryOperationApprovalRepository();
        final var operationId = UUID.randomUUID();
        final var idGenerator = mock(IdGenerator.class);

        when(idGenerator.generateId()).thenReturn(operationId);

        final var service = new CreateOperationApprovalRequestService(repo, idGenerator);

        final var proposal = new OperationProposal(
                "name",
                "description",
                Map.of(),
                false
        );

        final var request = new CreateOperationApprovalRequest(proposal);

        service.perform(request);

        final var savedRequest = repo.findById(operationId);

        assertTrue(savedRequest.isPresent());
    }
}
