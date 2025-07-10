package org.acme.core.cqrsedaes.eda;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.acme.core.Header;

public interface Event<T> extends Serializable {
    T getData();

    List<Header> getHeaders();

    default Optional<String> getHeader(String key) {
        return getHeaders().stream()
                .filter(header -> header.key().equals(key))
                .map(Header::value)
                .findFirst();
    }

    default String getEventId() {
        return getHeader(EventHeaders.EVENT_ID).orElse(null);
    }

    default String getAggregateId() {
        return getHeader(EventHeaders.AGGREGATE_ID).orElse(null);
    }

    default String getEventType() {
        return getHeader(EventHeaders.EVENT_TYPE).orElse(null);
    }

    default String getTimestamp() {
        return getHeader(EventHeaders.TIMESTAMP).orElse(null);
    }

    default Long getVersion() {
        return getHeader(EventHeaders.VERSION)
                .map(Long::parseLong)
                .orElse(null);
    }
}
