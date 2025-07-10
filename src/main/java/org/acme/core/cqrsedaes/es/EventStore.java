package org.acme.core.cqrsedaes.es;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.List;

import org.acme.core.cqrsedaes.eda.Event;

public interface EventStore {
    
    Uni<Void> saveEvents(String aggregateId, List<Event<?>> events, Long expectedVersion);
    
    Multi<Event<?>> getEvents(String aggregateId);
    
    Multi<Event<?>> getEvents(String aggregateId, Long fromVersion);
    
    Multi<Event<?>> getEvents(String aggregateId, Long fromVersion, Long toVersion);
    
    Uni<Boolean> existsAggregate(String aggregateId);
}
