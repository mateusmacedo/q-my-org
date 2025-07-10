package org.acme.product;

import org.acme.core.cqrsedaes.AggregateRoot;
import org.acme.core.cqrsedaes.eda.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;

public class Product extends AggregateRoot {

    private static final Logger log = LoggerFactory.getLogger(Product.class);

    private String name;
    private String sku;

    public Product(String id) {
        super(id);
    }

    public Uni<ProductRegisteredEvent> registerProduct(RegisterProductCommand command) {
        ProductRegisteredEvent.Data eventData = new ProductRegisteredEvent.Data(command.getData().name,
                command.getData().sku);
        ProductRegisteredEvent event = new ProductRegisteredEvent(getId(), eventData, command.getHeaders(),
                getVersion() + 1);

        applyEvent(event);

        return Uni.createFrom().item(event);
    }

    @Override
    protected Uni<Void> handleEvent(Event<?> event) {
        switch (event.getEventType()) {
            case "ProductRegistered" -> {
                ProductRegisteredEvent productEvent = (ProductRegisteredEvent) event;
                this.name = productEvent.getData().getName();
                this.sku = productEvent.getData().getSku();
                log.debug("Handling ProductRegisteredEvent for product: {}", productEvent);
            }
            default -> {
                // Ignorar eventos desconhecidos
            }
        }
        return Uni.createFrom().voidItem();
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }
}
