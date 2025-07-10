package org.acme.product.register;

import org.acme.core.cqrsedaes.AggregateRepository;
import org.acme.product.ProductAggregate;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends AggregateRepository<ProductAggregate> {
    
    @Override
    public ProductAggregate createAggregate(String id) {
        return new ProductAggregate(id);
    }
}
