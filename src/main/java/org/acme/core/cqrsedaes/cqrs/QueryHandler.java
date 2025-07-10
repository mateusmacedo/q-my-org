package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;

public interface QueryHandler<C extends Query<T>, T, R> {
    Uni<R> handle(C query);
}
