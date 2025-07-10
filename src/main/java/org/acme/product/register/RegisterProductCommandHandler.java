package org.acme.product.register;

import org.acme.core.IdGenerator;
import org.acme.core.cqrsedaes.cqrs.CommandHandler;
import org.acme.core.cqrsedaes.eda.EventBus;
import org.acme.product.ProductAggregate;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RegisterProductCommandHandler implements CommandHandler<RegisterProductCommand, RegisterProductDto> {

    @Inject
    ProductRepository productRepository;

    @Inject
    EventBus eventBus;

    @Inject
    IdGenerator<String> idGenerator;

    @Override
    public Uni<Void> handle(RegisterProductCommand command) {
        String productId = idGenerator.generate();
        ProductAggregate product = productRepository.createAggregate(productId);

        product.registerProduct(command);

        return productRepository.save(product)
                .onItem()
                .call(() -> publishEvents(product))
                .onItem()
                .call(() -> product.markEventsAsCommitted())
                .replaceWithVoid();
    }

    private Uni<Void> publishEvents(ProductAggregate product) {
        return product.getUncommittedEvents()
                .onItem()
                .transformToUni(events -> eventBus.publish(events));
    }
}
