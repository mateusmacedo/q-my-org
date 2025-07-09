package org.acme.core.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.acme.core.edaes.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeaderServiceTest {

    @Mock
    private HeaderContext headerContext;

    @InjectMocks
    private HeaderService headerService;

    private static final String CORRELATION_ID_VALUE = "test-correlation-id";

    @BeforeEach
    void setUp() {
        when(headerContext.getCorrelationId()).thenReturn(CORRELATION_ID_VALUE);
    }

    @Test
    void shouldReturnListWithCorrelationIdHeaderWhenGeneratingDefaultHeaders() {
        // Act
        List<Header> headers = headerService.generateDefaultHeaders();

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnListWithBothHeadersWhenGeneratingDefaultHeadersWithValidCustomValue() {
        // Arrange
        String customKey = "Custom-Header";
        String customValue = "custom-value";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(2, headers.size());
        
        // Check correlation ID header
        Header correlationHeader = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, correlationHeader.key());
        assertEquals(CORRELATION_ID_VALUE, correlationHeader.value());
        
        // Check custom header
        Header customHeader = headers.get(1);
        assertEquals(customKey, customHeader.key());
        assertEquals(customValue, customHeader.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithNullKey() {
        // Arrange
        String customValue = "custom-value";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(null, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithEmptyKey() {
        // Arrange
        String customValue = "custom-value";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue("", customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithNullValue() {
        // Arrange
        String customKey = "Custom-Header";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, null);

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithEmptyValue() {
        // Arrange
        String customKey = "Custom-Header";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, "");

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithNullKeyAndValue() {
        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(null, null);

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnOnlyDefaultHeadersWhenGeneratingDefaultHeadersWithEmptyKeyAndValue() {
        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue("", "");

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        
        Header header = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, header.key());
        assertEquals(CORRELATION_ID_VALUE, header.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnBothHeadersWhenGeneratingDefaultHeadersWithWhitespaceKey() {
        // Arrange
        String customKey = "   ";
        String customValue = "custom-value";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(2, headers.size());
        
        // Check correlation ID header
        Header correlationHeader = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, correlationHeader.key());
        assertEquals(CORRELATION_ID_VALUE, correlationHeader.value());
        
        // Check custom header (whitespace key is allowed by the current implementation)
        Header customHeader = headers.get(1);
        assertEquals(customKey, customHeader.key());
        assertEquals(customValue, customHeader.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnBothHeadersWhenGeneratingDefaultHeadersWithWhitespaceValue() {
        // Arrange
        String customKey = "Custom-Header";
        String customValue = "   ";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(2, headers.size());
        
        // Check correlation ID header
        Header correlationHeader = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, correlationHeader.key());
        assertEquals(CORRELATION_ID_VALUE, correlationHeader.value());
        
        // Check custom header (whitespace value is allowed by the current implementation)
        Header customHeader = headers.get(1);
        assertEquals(customKey, customHeader.key());
        assertEquals(customValue, customHeader.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnBothHeadersWhenGeneratingDefaultHeadersWithValidKeyAndWhitespaceValue() {
        // Arrange
        String customKey = "Custom-Header";
        String customValue = "   ";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(2, headers.size());
        
        // Check correlation ID header
        Header correlationHeader = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, correlationHeader.key());
        assertEquals(CORRELATION_ID_VALUE, correlationHeader.value());
        
        // Check custom header (whitespace value is allowed by the current implementation)
        Header customHeader = headers.get(1);
        assertEquals(customKey, customHeader.key());
        assertEquals(customValue, customHeader.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }

    @Test
    void shouldReturnBothHeadersWhenGeneratingDefaultHeadersWithWhitespaceKeyAndValidValue() {
        // Arrange
        String customKey = "   ";
        String customValue = "custom-value";

        // Act
        List<Header> headers = headerService.generateDefaultHeadersWithCustomValue(customKey, customValue);

        // Assert
        assertNotNull(headers);
        assertEquals(2, headers.size());
        
        // Check correlation ID header
        Header correlationHeader = headers.get(0);
        assertEquals(HeaderConstants.CORRELATION_ID, correlationHeader.key());
        assertEquals(CORRELATION_ID_VALUE, correlationHeader.value());
        
        // Check custom header (whitespace key is allowed by the current implementation)
        Header customHeader = headers.get(1);
        assertEquals(customKey, customHeader.key());
        assertEquals(customValue, customHeader.value());
        
        verify(headerContext, times(1)).getCorrelationId();
    }
}
