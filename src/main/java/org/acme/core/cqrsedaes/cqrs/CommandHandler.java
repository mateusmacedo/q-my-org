package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;

public interface CommandHandler<C extends Command<R>, R> {
    Uni<R> handle(C command);
}
