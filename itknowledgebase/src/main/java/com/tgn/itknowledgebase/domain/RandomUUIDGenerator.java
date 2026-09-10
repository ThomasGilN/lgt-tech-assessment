package com.tgn.itknowledgebase.domain;

import java.util.UUID;

public class RandomUUIDGenerator implements IdGenerator {
    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
