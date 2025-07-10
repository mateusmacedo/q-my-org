package org.acme.core.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class HeaderFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(HeaderFilter.class);

    @Inject
    HeaderContext headerContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.debug("Iniciando o HeaderFilter...");

        String correlationId = extractHeader(requestContext, HeaderConstants.CORRELATION_ID);
        if (correlationId == null || correlationId.trim().isEmpty()) {
            String generatedCorrelationId = headerContext.getCorrelationId();
            headerContext.setCorrelationId(generatedCorrelationId);
            requestContext.getHeaders().putSingle(HeaderConstants.CORRELATION_ID, generatedCorrelationId);
            log.debug("Gerado e Adicionando CorrelationId: {} ao HeaderContext", generatedCorrelationId);
        } else {
            headerContext.setCorrelationId(correlationId);
            requestContext.getHeaders().putSingle(HeaderConstants.CORRELATION_ID, correlationId);
            log.debug("Adicionando CorrelationId: {} ao HeaderContext", correlationId);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        log.debug("Iniciando o HeaderFilter para resposta...");

        if (headerContext.getCorrelationId() != null) {
            responseContext.getHeaders().putSingle(HeaderConstants.CORRELATION_ID, headerContext.getCorrelationId());
            log.debug("Adicionando CorrelationId na resposta: {}", headerContext.getCorrelationId());
        }

        log.debug("HeaderFilter para resposta finalizado.");
    }

    private String extractHeader(ContainerRequestContext requestContext, String headerName) {
        String header = requestContext.getHeaderString(headerName);
        return header != null ? header.trim() : null;
    }
}
