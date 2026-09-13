package com.tgn.chatservice.application.model.in;

import com.tgn.chatservice.domain.model.operation.OperationProposal;

public record CreateOperationApprovalRequest(
        OperationProposal proposal
)
{ }
