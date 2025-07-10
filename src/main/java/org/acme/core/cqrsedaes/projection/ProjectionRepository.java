package org.acme.core.cqrsedaes.projection;

import io.smallrye.mutiny.Uni;

public interface ProjectionRepository<P extends Projection> {
    Uni<P> findById(String id);
    Uni<P> save(P projection);
}
