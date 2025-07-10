package org.acme.product;

import org.acme.core.cqrsedaes.AggregateRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends AggregateRepository<Product> {
    
    @Override
    protected Product createAggregate(String id) {
        return new Product(id);
    }
}
