package org.acme.core.cqrsedaes.eda;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class MutinyEventBus implements EventBus {

    private final Map<Class<? extends Event<?>>, EventHandler<?>> handlerRegistry = new HashMap<>();

    @Inject
    Instance<EventHandler<?>> handlers;

    @PostConstruct
    void initializeRegistry() {
        handlers.forEach(this::registerHandler);
    }

    @SuppressWarnings("unchecked")
    private void registerHandler(EventHandler<?> handler) {
        Class<?> handlerClass = handler.getClass();

        // Check implemented interfaces
        Type[] genericInterfaces = handlerClass.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getRawType().equals(EventHandler.class)) {
                    Type eventType = paramType.getActualTypeArguments()[0];
                    if (eventType instanceof Class) {
                        handlerRegistry.put((Class<? extends Event<?>>) eventType, handler);
                        return;
                    }
                }
            }
        }

        // Check superclass if not found in direct interfaces
        Class<?> superclass = handlerClass.getSuperclass();
        while (superclass != null && superclass != Object.class) {
            Type[] superInterfaces = superclass.getGenericInterfaces();
            for (Type type : superInterfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) type;
                    if (paramType.getRawType().equals(EventHandler.class)) {
                        Type eventType = paramType.getActualTypeArguments()[0];
                        if (eventType instanceof Class) {
                            handlerRegistry.put((Class<? extends Event<?>>) eventType,
                                    handler);
                            return;
                        }
                    }
                }
            }
            superclass = superclass.getSuperclass();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Event<?>> Uni<Void> publish(E event) {
        EventHandler<E> handler = (EventHandler<E>) handlerRegistry
                .get(event.getClass());

        if (handler == null) {
            return Uni.createFrom().failure(
                    () -> new IllegalStateException("No handler found for event: " +
                            event.getClass().getSimpleName()));
        }

        return handler.handle(event);
    }

    @Override
    public <E extends Event<?>> Uni<Void> publish(List<E> events) {
        if (events == null || events.isEmpty()) {
            return Uni.createFrom().voidItem();
        }

        List<Uni<Void>> publishTasks = events.stream()
                .map(this::publish)
                .toList();

        return Uni.combine().all().unis(publishTasks)
                .discardItems();
    }

    public Map<Class<? extends Event<?>>, EventHandler<?>> getHandlerRegistry() {
        return handlerRegistry;
    }
}
