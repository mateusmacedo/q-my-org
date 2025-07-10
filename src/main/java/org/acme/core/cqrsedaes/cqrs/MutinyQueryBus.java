package org.acme.core.cqrsedaes.cqrs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class MutinyQueryBus implements QueryBus {

        private final Map<Class<? extends Query<?>>, QueryHandler<?, ?, ?>> handlerRegistry = new HashMap<>();

        @Inject
        Instance<QueryHandler<?, ?, ?>> handlers;

        @PostConstruct
        void initializeRegistry() {
                handlers.forEach(this::registerHandler);
        }

        @SuppressWarnings("unchecked")
        private void registerHandler(QueryHandler<?, ?, ?> handler) {
                Class<?> handlerClass = handler.getClass();

                // Check implemented interfaces
                Type[] genericInterfaces = handlerClass.getGenericInterfaces();
                for (Type type : genericInterfaces) {
                        if (type instanceof ParameterizedType) {
                                ParameterizedType paramType = (ParameterizedType) type;
                                if (paramType.getRawType().equals(QueryHandler.class)) {
                                        Type queryType = paramType.getActualTypeArguments()[0];
                                        if (queryType instanceof Class) {
                                                handlerRegistry.put((Class<? extends Query<?>>) queryType, handler);
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
                                        if (paramType.getRawType().equals(QueryHandler.class)) {
                                                Type queryType = paramType.getActualTypeArguments()[0];
                                                if (queryType instanceof Class) {
                                                        handlerRegistry.put((Class<? extends Query<?>>) queryType,
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
        public <T, R> Uni<R> execute(Query<T> query) {
                QueryHandler<Query<T>, T, R> handler = (QueryHandler<Query<T>, T, R>) handlerRegistry
                                .get(query.getClass());

                if (handler == null) {
                        return Uni.createFrom().failure(
                                        () -> new IllegalStateException("No handler found for query: " +
                                                        query.getClass().getSimpleName()));
                }

                return handler.handle(query);
        }

        public Map<Class<? extends Query<?>>, QueryHandler<?, ?, ?>> getHandlerRegistry() {
                return handlerRegistry;
        }
}
