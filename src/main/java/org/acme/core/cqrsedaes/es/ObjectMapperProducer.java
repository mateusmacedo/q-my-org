package org.acme.core.cqrsedaes.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ObjectMapperProducer {
    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 