package org.acme.core;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class UuidGeneratorTest {
    @Test
    void shouldGenerateNonEmptyIdWhenGeneratingId() {
        // Arrange
        UuidGenerator uuidGenerator = new UuidGenerator();

        // Act
        String generatedId = uuidGenerator.generate();

        // Assert
        assertNotNull(generatedId);
        assertFalse(generatedId.isEmpty());
    }
}
