package org.acme.core.cqrsedaes;

import java.util.List;

import org.acme.core.cqrsedaes.eda.Event;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public abstract class AggregateRoot {

    private String id;
    private Long version;
    private List<Event<?>> uncommittedEvents;

    public AggregateRoot(String id) {
        this.id = id;
        this.version = 0L;
        this.uncommittedEvents = new java.util.ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Uni<List<Event<?>>> getUncommittedEvents() {
        return Uni.createFrom().item(List.copyOf(uncommittedEvents));
    }

    public Uni<Void> markEventsAsCommitted() {
        this.uncommittedEvents.clear();
        return Uni.createFrom().voidItem();
    }

    protected Uni<Void> applyEvent(Event<?> event) {
        this.uncommittedEvents.add(event);
        this.version++;
        return handleEvent(event);
    }

    protected Uni<Void> replayEvent(Event<?> event) {
        this.version = event.getVersion();
        return handleEvent(event);
    }

    protected abstract Uni<Void> handleEvent(Event<?> event);

    public Uni<Void> loadFromHistory(Multi<Event<?>> events) {
        return events.onItem().transformToUniAndConcatenate(this::replayEvent)
                .onItem().ignoreAsUni();
    }
}
