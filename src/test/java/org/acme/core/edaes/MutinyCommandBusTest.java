package org.acme.core.edaes;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.enterprise.inject.Instance;

@ExtendWith(MockitoExtension.class)
class MutinyCommandBusTest {

    @Mock
    private Instance<CommandHandler<?, ?>> handlers;

    @InjectMocks
    private MutinyCommandBus commandBus;

    private TestCommand testCommand;
    private TestCommandHandler testCommandHandler;

    @BeforeEach
    void setUp() {
        testCommand = new TestCommand("test-data");
        testCommandHandler = new TestCommandHandler();
    }

    @Test
    void shouldSuccessfullyDispatchCommandWhenHandlerExists() {
        // Arrange
        String expectedResult = "test-result";
        testCommandHandler.setResult(expectedResult);

        Stream<CommandHandler<?, ?>> handlerStream = Stream.of(testCommandHandler);
        when(handlers.stream()).thenReturn(handlerStream);

        // Act
        Uni<String> result = commandBus.dispatch(testCommand);

        // Assert
        UniAssertSubscriber<String> subscriber = result.subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitItem().assertItem(expectedResult);

        verify(handlers, times(1)).stream();
    }

    @Test
    void shouldFailWithIllegalStateExceptionWhenNoHandlerExists() {
        // Arrange
        Stream<CommandHandler<?, ?>> emptyHandlerStream = Stream.empty();
        when(handlers.stream()).thenReturn(emptyHandlerStream);

        // Act
        Uni<String> result = commandBus.dispatch(testCommand);

        // Assert
        UniAssertSubscriber<String> subscriber = result.subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure().assertFailedWith(IllegalStateException.class, "No handler");

        verify(handlers, times(1)).stream();
    }

    @Test
    void shouldFailWithIllegalStateExceptionWhenNoMatchingHandlerExists() {
        // Arrange
        AnotherTestCommandHandler wrongHandler = new AnotherTestCommandHandler();
        Stream<CommandHandler<?, ?>> handlerStream = Stream.of(wrongHandler);
        when(handlers.stream()).thenReturn(handlerStream);

        // Act
        Uni<String> result = commandBus.dispatch(testCommand);

        // Assert
        UniAssertSubscriber<String> subscriber = result.subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure().assertFailedWith(IllegalStateException.class, "No handler");

        verify(handlers, times(1)).stream();
    }

    @Test
    void shouldSelectCorrectHandlerWhenMultipleHandlersExist() {
        // Arrange
        String expectedResult = "correct-result";
        testCommandHandler.setResult(expectedResult);

        AnotherTestCommandHandler wrongHandler = new AnotherTestCommandHandler();
        Stream<CommandHandler<?, ?>> handlerStream = Stream.of(wrongHandler, testCommandHandler);
        when(handlers.stream()).thenReturn(handlerStream);

        // Act
        Uni<String> result = commandBus.dispatch(testCommand);

        // Assert
        UniAssertSubscriber<String> subscriber = result.subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitItem().assertItem(expectedResult);

        verify(handlers, times(1)).stream();
    }

    @Test
    void shouldPropagateHandlerExceptionWhenHandlerFails() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Handler failed");
        testCommandHandler.setException(expectedException);

        Stream<CommandHandler<?, ?>> handlerStream = Stream.of(testCommandHandler);
        when(handlers.stream()).thenReturn(handlerStream);

        // Act
        Uni<String> result = commandBus.dispatch(testCommand);

        // Assert
        UniAssertSubscriber<String> subscriber = result.subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.awaitFailure().assertFailedWith(RuntimeException.class, "Handler failed");

        verify(handlers, times(1)).stream();
    }

    // Test command implementation
    private static class TestCommand implements Command<String> {
        private final String data;

        public TestCommand(String data) {
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }

        @Override
        public List<Header> getHeaders() {
            return List.of();
        }
    }

    // Test command handler implementation
    private static class TestCommandHandler implements CommandHandler<TestCommand, String> {
        private String result = "default-result";
        private RuntimeException exception;

        public void setResult(String result) {
            this.result = result;
        }

        public void setException(RuntimeException exception) {
            this.exception = exception;
        }

        @Override
        public Uni<String> handle(TestCommand command) {
            if (exception != null) {
                return Uni.createFrom().failure(exception);
            }
            return Uni.createFrom().item(result);
        }
    }

    // Another test command for testing handler selection
    private static class AnotherTestCommand implements Command<String> {
        @Override
        public String getData() {
            return "another-data";
        }

        @Override
        public List<Header> getHeaders() {
            return List.of();
        }
    }

    // Another test command handler for testing handler selection
    private static class AnotherTestCommandHandler implements CommandHandler<AnotherTestCommand, String> {
        @Override
        public Uni<String> handle(AnotherTestCommand command) {
            return Uni.createFrom().item("another-result");
        }
    }
}
