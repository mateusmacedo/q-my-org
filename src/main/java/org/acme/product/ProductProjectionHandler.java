package org.acme.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.mutiny.Uni;
import org.acme.core.cqrsedaes.eda.EventHandler;

@ApplicationScoped
public class ProductProjectionHandler implements EventHandler<ProductRegisteredEvent> {

    @Inject
    ProductViewRepository repository;

    @Override
    public Uni<Void> handle(ProductRegisteredEvent event) {
        ProductView view = new ProductView();
        view.productId = event.getAggregateId();
        view.name = event.getData().getName();
        view.sku = event.getData().getSku();
        return repository.persist(view);
    }
}
