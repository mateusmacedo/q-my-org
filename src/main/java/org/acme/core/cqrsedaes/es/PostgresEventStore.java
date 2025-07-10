package org.acme.core.cqrsedaes.es;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.core.Header;
import org.acme.core.IdGenerator;
import org.acme.core.cqrsedaes.eda.BaseEvent;
import org.acme.core.cqrsedaes.eda.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostgresEventStore implements EventStore {

    @Inject
    IdGenerator<String> idGenerator;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<Void> saveEvents(String aggregateId, List<Event<?>> events, Long expectedVersion) {
        return Panache.withTransaction(() -> getEvents(aggregateId)
                .collect().asList()
                .onItem().transformToUni(currentEvents -> {
                    // Controle de concorrência otimista
                    if (expectedVersion != null && !currentEvents.isEmpty()) {
                        Long currentVersion = currentEvents.get(currentEvents.size() - 1).getVersion();
                        if (!currentVersion.equals(expectedVersion)) {
                            return Uni.createFrom()
                                    .failure(new IllegalStateException("Concurrent modification detected"));
                        }
                    }
                    // Serializar e persistir eventos
                    List<Uni<?>> persistOps = events.stream().map(event -> {
                        if (event instanceof BaseEvent<?> baseEvent) {
                            baseEvent.withEventId(idGenerator.generate());
                        }
                        EventEntity entity = new EventEntity();
                        entity.eventId = event.getEventId();
                        entity.aggregateId = event.getAggregateId();
                        entity.eventType = event.getEventType();
                        String timestampStr = event.getTimestamp();
                        entity.timestamp = timestampStr != null ? Instant.parse(timestampStr) : Instant.now();
                        entity.version = event.getVersion();
                        try {
                            String dataJson = objectMapper.writeValueAsString(event.getData());
                            String headersJson = objectMapper.writeValueAsString(event.getHeaders());
                            entity.data = dataJson;
                            entity.headers = headersJson;
                        } catch (JsonProcessingException e) {
                            return Uni.createFrom().failure(e);
                        }
                        return entity.persist();
                    }).collect(Collectors.toList());
                    return Uni.combine().all().unis(persistOps).discardItems();
                }));
    }

    @Override
    public Multi<Event<?>> getEvents(String aggregateId) {
        return Multi.createFrom().uni(
                EventEntity.<EventEntity>find("aggregateId", aggregateId).list()).onItem()
                .transformToMultiAndMerge(list -> Multi.createFrom().iterable(list))
                .onItem().transform(this::toEvent);
    }

    @Override
    public Multi<Event<?>> getEvents(String aggregateId, Long fromVersion) {
        return getEvents(aggregateId)
                .filter(event -> event.getVersion() >= fromVersion);
    }

    @Override
    public Multi<Event<?>> getEvents(String aggregateId, Long fromVersion, Long toVersion) {
        return getEvents(aggregateId)
                .filter(event -> event.getVersion() >= fromVersion && event.getVersion() <= toVersion);
    }

    @Override
    public Uni<Boolean> existsAggregate(String aggregateId) {
        return EventEntity.count("aggregateId", aggregateId)
                .onItem().transform(count -> count > 0);
    }

    private Event<?> toEvent(EventEntity entity) {
        try {
            if (entity.data == null) {
                throw new RuntimeException("Entity data is null for eventId: " + entity.eventId);
            }
            if (entity.headers == null) {
                throw new RuntimeException("Entity headers is null for eventId: " + entity.eventId);
            }

            Class<?> dataClass = Object.class; // Substitua por lógica de mapeamento se necessário
            Object data = objectMapper.readValue(entity.data, dataClass);
            List<Header> headers = objectMapper.readValue(entity.headers, new TypeReference<List<Header>>() {
            });
            BaseEvent<?> event = new BaseEvent<>(data, headers) {
            };
            event.withEventId(entity.eventId)
                    .withAggregateId(entity.aggregateId)
                    .withEventType(entity.eventType)
                    .withTimestamp(entity.timestamp)
                    .withVersion(entity.version);
            return event;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desserializar evento", e);
        }
    }
}