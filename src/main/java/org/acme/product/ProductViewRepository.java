package org.acme.product;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import org.acme.core.cqrsedaes.projection.ProjectionRepository;

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
}
