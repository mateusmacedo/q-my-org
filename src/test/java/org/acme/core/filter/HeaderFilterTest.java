package org.acme.core.filter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;

@ExtendWith(MockitoExtension.class)
class HeaderFilterTest {

    @Mock
    private HeaderContext headerContext;

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private ContainerResponseContext responseContext;

    @InjectMocks
    private HeaderFilter headerFilter;

    private MultivaluedMap<String, String> requestHeaders;
    private MultivaluedMap<String, Object> responseHeaders;

    @BeforeEach
    void setUp() {
        requestHeaders = Mockito.mock(MultivaluedMap.class);
        responseHeaders = Mockito.mock(MultivaluedMap.class);
    }

    @Test
    void shouldGenerateCorrelationIdWhenNotPresent() throws IOException {
        // Given
        String generatedCorrelationId = "dummy-correlation-id";
        when(headerContext.getCorrelationId()).thenReturn(generatedCorrelationId);
        when(requestContext.getHeaderString(HeaderConstants.CORRELATION_ID)).thenReturn(null);
        when(requestContext.getHeaders()).thenReturn(requestHeaders);

        // When
        headerFilter.filter(requestContext);

        // Then
        verify(headerContext, times(1)).setCorrelationId(generatedCorrelationId);
        verify(requestContext.getHeaders()).putSingle(HeaderConstants.CORRELATION_ID, generatedCorrelationId);
    }

    @Test
    void shouldUseExistingCorrelationIdWhenPresent() throws IOException {
        // Given
        String existingCorrelationId = "existing-correlation-id";
        when(requestContext.getHeaderString(HeaderConstants.CORRELATION_ID)).thenReturn(existingCorrelationId);
        when(requestContext.getHeaders()).thenReturn(requestHeaders);

        // When
        headerFilter.filter(requestContext);

        // Then
        verify(headerContext).setCorrelationId(existingCorrelationId);
        verify(requestContext.getHeaders()).putSingle(HeaderConstants.CORRELATION_ID, existingCorrelationId);
    }

    @Test
    void shouldAddHeadersToResponse() throws IOException {
        // Given
        String correlationId = "correlation-id";
        when(headerContext.getCorrelationId()).thenReturn(correlationId);
        when(responseContext.getHeaders()).thenReturn(responseHeaders);

        // When
        headerFilter.filter(requestContext, responseContext);

        // Then
        verify(responseContext.getHeaders()).putSingle(HeaderConstants.CORRELATION_ID, correlationId);
    }

}
