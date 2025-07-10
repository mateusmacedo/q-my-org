package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;

public interface CommandBus {
    <R> Uni<Void> dispatch(Command<R> command);
}
