package org.acme.core;

import java.util.UUID;

import jakarta.inject.Singleton;

@Singleton
public class UuidGenerator implements IdGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
