package org.acme.product;

import org.acme.core.cqrsedaes.eda.EventHandler;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRegisteredEventHandler implements EventHandler<ProductRegisteredEvent> {

    @Override
    public Uni<Void> handle(ProductRegisteredEvent event) {
        return Uni.createFrom().voidItem();
    }
}
