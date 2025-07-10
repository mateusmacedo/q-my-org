package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;

public interface QueryBus {
    <T, R> Uni<R> execute(Query<T> query);
}
