package org.acme.core.cqrsedaes.eda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.acme.core.Header;
import org.acme.core.UuidGenerator;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class EventHeadersTest {

    @Inject
    UuidGenerator idGenerator;

    @Test
    void shouldWorkWithMinimalHeaders() {
        // Given - Evento com apenas dados
        String data = "Test Data";
        TestEvent event = new TestEvent(data, List.of());

        // When - Adicionar headers dinamicamente
        event.withHeader("custom-field", "custom-value")
                .withEventId(idGenerator.generate())
                .withAggregateId("test-aggregate")
                .withEventType("TestEvent")
                .withTimestamp(Instant.now())
                .withVersion(1L);

        // Then - Verificar headers padrão
        assertNotNull(event.getEventId());
        assertEquals("test-aggregate", event.getAggregateId());
        assertEquals("TestEvent", event.getEventType());
        assertNotNull(event.getTimestamp());
        assertEquals(1L, event.getVersion());

        // Verificar header customizado
        Optional<String> customField = event.getHeader("custom-field");
        assertTrue(customField.isPresent());
        assertEquals("custom-value", customField.get());
    }

    @Test
    void shouldHandleEmptyHeaders() {
        // Given
        TestEvent event = new TestEvent("Test Data", List.of());

        // When/Then - Headers opcionais devem retornar null
        assertNull(event.getEventId());
        assertNull(event.getAggregateId());
        assertNull(event.getEventType());
        assertNull(event.getTimestamp());
        assertNull(event.getVersion());

        // Header inexistente deve retornar Optional.empty()
        assertFalse(event.getHeader("non-existent").isPresent());
    }

    @Test
    void shouldAllowFlexibleHeaderStructure() {
        // Given
        TestEvent event = new TestEvent("Test Data", List.of());

        // When - Adicionar headers diversos
        event.withHeader("correlation-id", "corr-123")
                .withHeader("source-system", "api-gateway")
                .withHeader("tenant-id", "tenant-456")
                .withHeader("trace-id", "trace-789")
                .withAggregateId("aggregate-001")
                .withEventType("FlexibleEvent")
                .withVersion(5L);

        // Then - Todos os headers devem estar presentes
        assertEquals("corr-123", event.getHeader("correlation-id").get());
        assertEquals("api-gateway", event.getHeader("source-system").get());
        assertEquals("tenant-456", event.getHeader("tenant-id").get());
        assertEquals("trace-789", event.getHeader("trace-id").get());
        assertEquals("aggregate-001", event.getAggregateId());
        assertEquals("FlexibleEvent", event.getEventType());
        assertEquals(5L, event.getVersion());
    }

    private static class TestEvent extends BaseEvent<String> {
        public TestEvent(String data, List<Header> headers) {
            super(data, headers);
        }
    }
}
