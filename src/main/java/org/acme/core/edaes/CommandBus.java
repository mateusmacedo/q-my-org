package org.acme.core.edaes;

import io.smallrye.mutiny.Uni;

public interface CommandBus {
    <R> Uni<R> dispatch(Command<R> command);
}
