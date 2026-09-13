package com.tgn.chatservice.domain.model.operation;

import java.util.List;

public record Operation(
        String name,
        String description,
        List<String> argumentNames,
        boolean requiresApproval
) {
}
