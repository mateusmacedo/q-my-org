package org.acme.core.cqrsedaes;

import org.acme.core.IdGenerator;
import org.acme.core.cqrsedaes.es.EventStore;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

public abstract class AggregateRepository<T extends AggregateRoot> {

    @Inject
    protected EventStore eventStore;

    @Inject
    protected IdGenerator<String> idGenerator;

    public Uni<T> getById(String id) {
        return eventStore.getEvents(id)
                .collect().asList()
                .onItem().transformToUni(events -> {
                    if (events.isEmpty()) {
                        return Uni.createFrom().failure(new IllegalStateException("Aggregate not found: " + id));
                    }
                    T aggregate = createAggregate(id);
                    return aggregate.loadFromHistory(eventStore.getEvents(id))
                            .onItem().transform(v -> aggregate);
                });
    }

    public Uni<Void> save(T aggregate) {
        return aggregate.getUncommittedEvents()
                .onItem().transformToUni(events -> {
                    if (events.isEmpty()) {
                        return Uni.createFrom().voidItem();
                    }
                    return eventStore.saveEvents(aggregate.getId(), events, aggregate.getVersion());
                });
    }

    public Uni<Boolean> exists(String id) {
        return eventStore.existsAggregate(id);
    }

    protected abstract T createAggregate(String id);
}
