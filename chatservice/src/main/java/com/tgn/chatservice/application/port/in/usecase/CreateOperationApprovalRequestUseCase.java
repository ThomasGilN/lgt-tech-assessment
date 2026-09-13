package com.tgn.chatservice.application.port.in.usecase;

import com.tgn.chatservice.application.model.in.CreateOperationApprovalRequest;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequest;

public interface CreateOperationApprovalRequestUseCase {

    OperationApprovalRequest perform(CreateOperationApprovalRequest request);
}
