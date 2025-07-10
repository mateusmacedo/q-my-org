package org.acme.product;

import org.acme.core.IdGenerator;
import org.acme.core.cqrsedaes.cqrs.CommandHandler;
import org.acme.core.cqrsedaes.eda.EventBus;

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
        Product product = new Product(productId);

        product.registerProduct(command);

        return productRepository.save(product)
                .onItem()
                .call(() -> publishEvents(product))
                .onItem()
                .call(() -> product.markEventsAsCommitted())
                .replaceWithVoid();
    }

    private Uni<Void> publishEvents(Product product) {
        return product.getUncommittedEvents()
                .onItem()
                .transformToUni(events -> eventBus.publish(events));
    }
}
