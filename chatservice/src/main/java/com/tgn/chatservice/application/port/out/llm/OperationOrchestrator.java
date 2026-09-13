package com.tgn.chatservice.application.port.out.llm;

import com.tgn.chatservice.application.model.in.ExecuteOperationRequest;
import com.tgn.chatservice.application.model.in.RequestOperationProposalRequest;
import com.tgn.chatservice.domain.model.operation.Operation;
import com.tgn.chatservice.domain.model.operation.OperationProposalResponse;
import com.tgn.chatservice.domain.model.operation.OperationResult;

import java.util.List;

public interface OperationOrchestrator {

    List<Operation> getAvailableOperations();

    OperationProposalResponse requestOperationProposal(RequestOperationProposalRequest request);

    OperationResult executeOperation(ExecuteOperationRequest request);
}
