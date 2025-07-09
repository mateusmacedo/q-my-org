package org.acme.core.edaes;

import io.smallrye.mutiny.Uni;

public interface CommandHandler<C extends Command<R>, R> {
    Uni<R> handle(C command);
}
