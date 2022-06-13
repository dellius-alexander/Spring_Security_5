package com.example.Spring_Security_5.Utility.Generator;

import java.util.UUID;

public class InMemoryUniqueIdGenerator implements UniqueIdGenerator<UUID> {
    @Override
    public UUID getNextUniqueId() {
        return UUID.randomUUID();
    }
}
