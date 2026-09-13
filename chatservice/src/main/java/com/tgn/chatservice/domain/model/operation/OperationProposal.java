package com.tgn.chatservice.domain.model.operation;

import java.util.Map;

public record OperationProposal(
        String name,
        String description,
        Map<String, Object> arguments,
        boolean requiresApproval
) {

}
