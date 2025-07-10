package org.acme.core.cqrsedaes.eda;

import io.smallrye.mutiny.Uni;

public interface EventHandler<E extends Event<?>> {
    Uni<Void> handle(E event);
}
