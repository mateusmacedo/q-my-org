package org.acme.product.bysku;

import org.acme.core.cqrsedaes.cqrs.QueryHandler;
import org.acme.product.ProductView;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductViewBySkuQueryHandler implements QueryHandler<ProductViewBySkuQuery, ProductBySkuDto, ProductView> {
    @Inject
    ProductViewRepository repository;

    @Override
    public Uni<ProductView> handle(ProductViewBySkuQuery query) {
        final var sku = query.getData().sku;
        return repository.findBySku(sku)
                .onItem()
                .ifNull()
                .failWith(() -> new ProductNotFoundException("Product with SKU " + sku + " not found"));
    }
}
