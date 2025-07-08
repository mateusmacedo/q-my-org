package org.acme.core;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.acme.core.edaes.UuidGenerator;

@QuarkusTest
public class UuidGeneratorTest {
    @Test
    public void testGenerateId() {
        // Arrange
        UuidGenerator uuidGenerator = new UuidGenerator();

        // Act
        String generatedId = uuidGenerator.generateId();

        // Assert
        assertNotNull(generatedId);
        assertFalse(generatedId.isEmpty());
    }
}
