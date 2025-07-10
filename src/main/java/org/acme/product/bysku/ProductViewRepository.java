package org.acme.product.bysku;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import org.acme.core.cqrsedaes.projection.ProjectionRepository;
import org.acme.product.ProductView;

@ApplicationScoped
public class ProductViewRepository implements ProjectionRepository<ProductView>, PanacheRepository<ProductView> {

    @Override
    public Uni<ProductView> findById(String id) {
        return ProductView.find("productId", id).firstResult();
    }

    @Override
    public Uni<ProductView> save(ProductView projection) {
        return projection.persist();
    }

    public Uni<ProductView> findBySku(String sku) {
        return ProductView.find("sku", sku).firstResult();
    }
}
