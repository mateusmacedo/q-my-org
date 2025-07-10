package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;

public interface CommandHandler<C extends Command<T>, T> {
    Uni<Void> handle(C command);
}
