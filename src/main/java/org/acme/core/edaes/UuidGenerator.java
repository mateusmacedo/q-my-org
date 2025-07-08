package org.acme.core.edaes;

import java.util.UUID;

public class UuidGenerator implements IdGenerator<String> {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
