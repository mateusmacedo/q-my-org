package org.acme.core.edaes;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class MutinyCommandBus implements CommandBus {

    @Inject
    Instance<CommandHandler<?, ?>> handlers;

    @SuppressWarnings("unchecked")
    public <R> Uni<R> dispatch(Command<R> command) {
        return Uni.createFrom().item(handlers)
                .onItem().transformToUni(h -> h.stream()
                        .filter(ch -> ch.getClass()
                                .getGenericInterfaces()[0]
                                .getTypeName()
                                .contains(command.getClass().getName()))
                        .findFirst()
                        .map(ch -> ((CommandHandler<Command<R>, R>) ch).handle(command))
                        .orElse(Uni.createFrom().failure(
                                () -> new IllegalStateException("No handler"))));
    }
}
