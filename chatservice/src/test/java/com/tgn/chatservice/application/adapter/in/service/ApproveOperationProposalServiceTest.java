package com.tgn.chatservice.application.adapter.in.service;

import com.tgn.chatservice.application.model.in.ApproveOperationProposalRequest;
import com.tgn.chatservice.application.port.in.usecase.ApproveOperationProposalUseCase;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;
import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryOperationApprovalRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApproveOperationProposalServiceTest {

    @Test
    void testApproveOperationProposal() {
        final var repo = new InMemoryOperationApprovalRepository();
        final var service = new ApproveOperationProposalService(repo);

        final var conversationId = UUID.randomUUID();
        final var operationId = UUID.randomUUID();

        final var proposal = new OperationProposal(
                "test name",
                "test description",
                Map.of(),
                true
        );

        repo.save(
                new OperationApprovalRequest(
                operationId,
                proposal,
                Instant.now().plusSeconds(60)
        ));

        final var approval = service.perform(
                new ApproveOperationProposalRequest(
                    conversationId,
                    operationId,
                    OperationApprovalUserDecision.APPROVED
                )
        );

        assertTrue(approval.isApproved());
        assertEquals("User decision", approval.reason());
        assertTrue(repo.findById(operationId).isEmpty());
        assertThrows(java.util.NoSuchElementException.class, () ->
                service.perform(new ApproveOperationProposalRequest(
                        conversationId, operationId,
                        OperationApprovalUserDecision.APPROVED)));
    }

    @Test
    void testRejectOperationProposal() {
        final var repo = new InMemoryOperationApprovalRepository();
        final var service = new ApproveOperationProposalService(repo);

        final var conversationId = UUID.randomUUID();
        final var operationId = UUID.randomUUID();

        final var proposal = new OperationProposal(
                "test name",
                "test description",
                Map.of(),
                true
        );

        repo.save(
                new OperationApprovalRequest(
                        operationId,
                        proposal,
                        Instant.now().plusSeconds(60)
                ));

        final var approval = service.perform(
                new ApproveOperationProposalRequest(
                        conversationId,
                        operationId,
                        OperationApprovalUserDecision.REJECTED
                )
        );

        assertFalse(approval.isApproved());
        assertEquals("User decision", approval.reason());
        assertTrue(repo.findById(operationId).isEmpty());
        assertThrows(java.util.NoSuchElementException.class, () ->
                service.perform(new ApproveOperationProposalRequest(
                        conversationId, operationId,
                        OperationApprovalUserDecision.APPROVED)));
    }

    @Test
    void testApproveOperationProposalExpired() {
        final var repo = new InMemoryOperationApprovalRepository();
        final var service = new ApproveOperationProposalService(repo);

        final var conversationId = UUID.randomUUID();
        final var operationId = UUID.randomUUID();

        final var proposal = new OperationProposal(
                "test name",
                "test description",
                Map.of(),
                true
        );

        repo.save(
                new OperationApprovalRequest(
                        operationId,
                        proposal,
                        Instant.now().minusSeconds(61)
                ));

        final var approval = service.perform(
                new ApproveOperationProposalRequest(
                        conversationId,
                        operationId,
                        OperationApprovalUserDecision.REJECTED
                )
        );

        assertFalse(approval.isApproved());
        assertEquals("Operation approval request expired", approval.reason());
        assertTrue(repo.findById(operationId).isEmpty());
    }
}
