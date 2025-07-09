package org.acme.core.cqrsedaes.cqrs;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MutinyCommandBus implements CommandBus {

        private final Map<Class<? extends Command<?>>, CommandHandler<?, ?>> handlerRegistry = new HashMap<>();

        @Inject
        Instance<CommandHandler<?, ?>> handlers;

        @PostConstruct
        void initializeRegistry() {
                handlers.forEach(this::registerHandler);
        }

        @SuppressWarnings("unchecked")
        private void registerHandler(CommandHandler<?, ?> handler) {
                Class<?> handlerClass = handler.getClass();
                
                // Check implemented interfaces
                Type[] genericInterfaces = handlerClass.getGenericInterfaces();
                for (Type type : genericInterfaces) {
                        if (type instanceof ParameterizedType) {
                                ParameterizedType paramType = (ParameterizedType) type;
                                if (paramType.getRawType().equals(CommandHandler.class)) {
                                        Type commandType = paramType.getActualTypeArguments()[0];
                                        if (commandType instanceof Class) {
                                                handlerRegistry.put((Class<? extends Command<?>>) commandType, handler);
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
                                        if (paramType.getRawType().equals(CommandHandler.class)) {
                                                Type commandType = paramType.getActualTypeArguments()[0];
                                                if (commandType instanceof Class) {
                                                        handlerRegistry.put((Class<? extends Command<?>>) commandType, handler);
                                                        return;
                                                }
                                        }
                                }
                        }
                        superclass = superclass.getSuperclass();
                }
        }

        @SuppressWarnings("unchecked")
        public <R> Uni<R> dispatch(Command<R> command) {
                CommandHandler<Command<R>, R> handler = (CommandHandler<Command<R>, R>) handlerRegistry
                                .get(command.getClass());

                if (handler == null) {
                        return Uni.createFrom().failure(
                                        () -> new IllegalStateException("No handler found for command: " +
                                                        command.getClass().getSimpleName()));
                }

                return handler.handle(command);
        }

        // For testing purposes
        public Map<Class<? extends Command<?>>, CommandHandler<?, ?>> getHandlerRegistry() {
                return handlerRegistry;
        }
}
