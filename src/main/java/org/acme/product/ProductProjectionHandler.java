package org.acme.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.acme.core.cqrsedaes.eda.EventHandler;

@ApplicationScoped
public class ProductProjectionHandler implements EventHandler<ProductRegisteredEvent> {
    private static final Logger log = LoggerFactory.getLogger(ProductProjectionHandler.class);
    @Inject
    ProductViewRepository repository;

    @Override
    public Uni<Void> handle(ProductRegisteredEvent event) {
        ProductView view = new ProductView();
        view.productId = event.getAggregateId();
        view.name = event.getData().getName();
        view.sku = event.getData().getSku();
        return repository.persist(view)
        .onItem().invoke(() -> {
            log.debug("Product projection saved: {}", view.productId);
        })
        .replaceWithVoid();
    }
}
