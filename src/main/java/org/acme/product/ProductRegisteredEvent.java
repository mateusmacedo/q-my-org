package org.acme.product;

import java.time.Instant;
import java.util.List;

import org.acme.core.Header;
import org.acme.core.cqrsedaes.eda.BaseEvent;

public class ProductRegisteredEvent extends BaseEvent<ProductRegisteredEvent.Data> {

    public ProductRegisteredEvent(String aggregateId, Data data, List<Header> headers, Long version) {
        super(data, headers);
        withAggregateId(aggregateId)
                .withEventType("ProductRegistered")
                .withTimestamp(Instant.now())
                .withVersion(version);
    }

    public static class Data {
        private final String name;
        private final String sku;

        public Data(String name, String sku) {
            this.name = name;
            this.sku = sku;
        }

        public String getName() {
            return name;
        }

        public String getSku() {
            return sku;
        }
    }
}
