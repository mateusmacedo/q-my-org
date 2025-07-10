package org.acme.core.cqrsedaes.eda;

import io.smallrye.mutiny.Uni;
import java.util.List;

public interface EventBus {
    <E extends Event<?>> Uni<Void> publish(E event);
    <E extends Event<?>> Uni<Void> publish(List<E> events);
}
