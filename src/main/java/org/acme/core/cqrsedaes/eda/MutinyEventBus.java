package org.acme.core.cqrsedaes.eda;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MutinyEventBus implements EventBus {

    @Inject
    Instance<EventHandler<?>> handlers;

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Event<?>> Uni<Void> publish(E event) {
        return Uni.createFrom().item(handlers)
                .onItem().transformToUni(h -> {
                    List<Uni<Void>> handlerUnis = h.stream()
                            .filter(eh -> eh.getClass()
                                    .getGenericInterfaces()[0]
                                    .getTypeName()
                                    .contains(event.getClass().getName()))
                            .map(eh -> ((EventHandler<E>) eh).handle(event))
                            .toList();

                    if (handlerUnis.isEmpty()) {
                        return Uni.createFrom().voidItem();
                    }

                    return Uni.combine().all().unis(handlerUnis)
                            .discardItems();
                });
    }

    @Override
    public <E extends Event<?>> Uni<Void> publish(List<E> events) {
        if (events.isEmpty()) {
            return Uni.createFrom().voidItem();
        }

        List<Uni<Void>> eventUnis = events.stream()
                .map(this::publish)
                .toList();

        return Uni.combine().all().unis(eventUnis)
                .discardItems();
    }
}
