package org.acme.core.cqrsedaes.es;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.acme.core.Header;
import org.acme.core.UuidGenerator;
import org.acme.core.cqrsedaes.eda.BaseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;

@QuarkusTest
class PostgresEventStoreTest {

    @Inject
    PostgresEventStore eventStore;

    @Inject
    UuidGenerator idGenerator;

    String aggregateId;

    @BeforeEach
    void setup() {
        aggregateId = idGenerator.generate();
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldSaveAndRetrieveEvents(UniAsserter asserter) {
        TestEvent event1 = new TestEvent(aggregateId, Map.of("key1", "TestData1"), List.of(), 1L);
        TestEvent event2 = new TestEvent(aggregateId, Map.of("key2", "TestData2"), List.of(), 2L);
        asserter.execute(() -> eventStore.saveEvents(aggregateId, List.of(event1, event2), null))
                .assertThat(() -> eventStore.getEvents(aggregateId).collect().asList(),
                        events -> {
                            assertEquals(2, events.size());
                            assertEquals(Map.of("key1", "TestData1"), events.get(0).getData());
                            assertEquals(Map.of("key2", "TestData2"), events.get(1).getData());
                        });
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldCheckAggregateExists(UniAsserter asserter) {
        TestEvent event = new TestEvent(aggregateId, Map.of("key1", "value1"), List.of(), 1L);
        asserter.execute(() -> eventStore.saveEvents(aggregateId, List.of(event), null))
                .assertThat(() -> eventStore.existsAggregate(aggregateId),
                        exists -> assertTrue(exists))
                .assertThat(() -> eventStore.existsAggregate("non-existent"),
                        exists -> assertFalse(exists));
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldRetrieveEventsByVersion(UniAsserter asserter) {
        asserter.execute(() -> eventStore.saveEvents(aggregateId, List.of(
                new TestEvent(aggregateId, Map.of("key1", "v1"), List.of(), 1L),
                new TestEvent(aggregateId, Map.of("key1", "v2"), List.of(), 2L),
                new TestEvent(aggregateId, Map.of("key1", "v3"), List.of(), 3L)), null))
                .assertThat(() -> eventStore.getEvents(aggregateId, 2L).collect().asList(),
                        events -> {
                            assertEquals(2, events.size());
                            assertEquals(Map.of("key1", "v2"), events.get(0).getData());
                            assertEquals(Map.of("key1", "v3"), events.get(1).getData());
                        });
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldRetrieveEventsByVersionRange(UniAsserter asserter) {

        asserter.execute(() -> eventStore.saveEvents(aggregateId, List.of(
                new TestEvent(aggregateId, Map.of("key1", "v1"), List.of(), 1L),
                new TestEvent(aggregateId, Map.of("key1", "v2"), List.of(), 2L),
                new TestEvent(aggregateId, Map.of("key1", "v3"), List.of(), 3L)), null))
                .assertThat(() -> eventStore.getEvents(aggregateId, 2L, 3L).collect().asList(),
                        events -> {
                            assertEquals(2, events.size());
                            assertEquals(Map.of("key1", "v2"), events.get(0).getData());
                            assertEquals(Map.of("key1", "v3"), events.get(1).getData());
                        });
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldThrowOnConcurrentModification(UniAsserter asserter) {
        asserter.execute(() -> eventStore.saveEvents(aggregateId,
                List.of(new TestEvent(aggregateId, Map.of("key1", "v1"), List.of(), 1L)), null))
                .assertFailedWith(
                        () -> eventStore.saveEvents(aggregateId,
                                List.of(new TestEvent(aggregateId, Map.of("key1", "v2"), List.of(), 2L)), 0L),
                        ex -> {
                            assertTrue(ex instanceof IllegalStateException);
                            assertTrue(ex.getMessage().contains("Concurrent modification"));
                        });
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldHandleEmptyEvents(UniAsserter asserter) {

        asserter.assertThat(() -> eventStore.getEvents("non-existent").collect().asList(),
                events -> assertTrue(events.isEmpty()));
    }

    @Test
    @RunOnVertxContext
    @TestReactiveTransaction
    void shouldSerializeAndDeserializeHeaders(UniAsserter asserter) {

        Header header = new Header("custom-key", "custom-value");
        TestEvent event = new TestEvent(aggregateId, Map.of("key1", "value1"), List.of(header), 1L);

        asserter.execute(() -> eventStore.saveEvents(aggregateId, List.of(event), null))
                .assertThat(() -> eventStore.getEvents(aggregateId).collect().asList(),
                        events -> {
                            assertEquals(1, events.size());
                            assertEquals("custom-value", events.get(0).getHeaders().get(0).value());
                        });
    }

    private static class TestEvent extends BaseEvent<Map<String, String>> {
        public TestEvent(String aggregateId, Map<String, String> data, List<Header> headers, Long version) {
            super(data, headers);
            withAggregateId(aggregateId)
                    .withEventType("TestEvent")
                    .withTimestamp(Instant.now())
                    .withVersion(version);
        }
    }
}