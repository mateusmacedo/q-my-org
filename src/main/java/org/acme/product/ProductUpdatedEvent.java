package org.acme.product;

import java.time.Instant;
import java.util.List;

import org.acme.core.Header;
import org.acme.core.cqrsedaes.eda.BaseEvent;

public class ProductUpdatedEvent extends BaseEvent<ProductUpdatedEvent.Data> {

    public ProductUpdatedEvent(String aggregateId, Data data, List<Header> headers, Long version) {
        super(data, headers);
        withAggregateId(aggregateId)
                .withEventType("ProductUpdated")
                .withTimestamp(Instant.now())
                .withVersion(version);
    }

    // Exemplo de header customizado
    public ProductUpdatedEvent withUser(String userId) {
        return (ProductUpdatedEvent) withHeader("user-id", userId);
    }

    public ProductUpdatedEvent withReason(String reason) {
        return (ProductUpdatedEvent) withHeader("update-reason", reason);
    }

    public static class Data {
        private final String oldName;
        private final String newName;
        private final String oldSku;
        private final String newSku;

        public Data(String oldName, String newName, String oldSku, String newSku) {
            this.oldName = oldName;
            this.newName = newName;
            this.oldSku = oldSku;
            this.newSku = newSku;
        }

        public String getOldName() {
            return oldName;
        }

        public String getNewName() {
            return newName;
        }

        public String getOldSku() {
            return oldSku;
        }

        public String getNewSku() {
            return newSku;
        }
    }
}
