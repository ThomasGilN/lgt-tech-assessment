package com.tgn.chatservice.application.adapter;

import com.tgn.chatservice.domain.model.IdGenerator;

import java.util.UUID;

public class RandomUUIDIdGenerator implements IdGenerator {

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
