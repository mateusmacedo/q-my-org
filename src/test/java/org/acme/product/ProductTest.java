package org.acme.product;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;

import org.acme.core.UuidGenerator;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProductTest {

    @Inject
    UuidGenerator idGenerator;

    @Test
    void shouldRegisterProduct() {
        // Given
        String productId = idGenerator.generate();
        Product product = new Product(productId);
        RegisterProductDto dto = new RegisterProductDto("TEST001", "Test Product");

        // When
        product.registerProduct(RegisterProductCommand.fromDto(dto));

        // Then
        assertEquals("Test Product", product.getName());
        assertEquals("TEST001", product.getSku());
        assertEquals(1, product.getUncommittedEvents().await().indefinitely().size());
        assertEquals(1L, product.getVersion());
    }

    @Test
    void shouldLoadFromHistory() {
        // Given
        String productId = idGenerator.generate();
        Product product = new Product(productId);

        ProductRegisteredEvent.Data eventData = new ProductRegisteredEvent.Data("Historical Product", "HIST001");
        ProductRegisteredEvent event = new ProductRegisteredEvent(productId, eventData, List.of(), 1L);

        // When
        product.loadFromHistory(Multi.createFrom().iterable(List.of(event))).await().indefinitely();

        // Then
        assertEquals("Historical Product", product.getName());
        assertEquals("HIST001", product.getSku());
        assertEquals(1L, product.getVersion());
        assertEquals(0, product.getUncommittedEvents().await().indefinitely().size()); // Eventos históricos não são uncommitted
    }
}
