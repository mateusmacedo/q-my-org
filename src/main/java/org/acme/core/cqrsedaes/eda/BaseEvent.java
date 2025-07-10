package org.acme.core.cqrsedaes.eda;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.acme.core.Header;

public abstract class BaseEvent<T> implements Event<T> {

    protected T data;
    protected List<Header> headers;

    public BaseEvent(T data, List<Header> headers) {
        this.data = data;
        this.headers = headers != null ? new ArrayList<>(headers) : new ArrayList<>();
    }

    public BaseEvent<T> withHeader(String key, String value) {
        this.headers.add(new Header(key, value));
        return this;
    }

    public BaseEvent<T> withEventId(String eventId) {
        return withHeader(EventHeaders.EVENT_ID, eventId);
    }

    public BaseEvent<T> withAggregateId(String aggregateId) {
        return withHeader(EventHeaders.AGGREGATE_ID, aggregateId);
    }

    public BaseEvent<T> withEventType(String eventType) {
        return withHeader(EventHeaders.EVENT_TYPE, eventType);
    }

    public BaseEvent<T> withTimestamp(Instant timestamp) {
        return withHeader(EventHeaders.TIMESTAMP, timestamp.toString());
    }

    public BaseEvent<T> withVersion(Long version) {
        return withHeader(EventHeaders.VERSION, version.toString());
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public List<Header> getHeaders() {
        return List.copyOf(headers);
    }
}
