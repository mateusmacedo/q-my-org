package org.acme.core.filter;

import org.acme.core.IdGenerator;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class HeaderContext {

    @Inject
    IdGenerator<String> idGenerator;

    private String correlationId;

    public String getCorrelationId() {
        if (correlationId == null) {
            correlationId = idGenerator.generate();
        }
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        if (correlationId != null && !correlationId.trim().isEmpty()) {
            this.correlationId = correlationId;
        } else {
            this.correlationId = idGenerator.generate();
        }
    }
}
