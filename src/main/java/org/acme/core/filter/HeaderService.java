package org.acme.core.filter;

import java.util.ArrayList;
import java.util.List;

import org.acme.core.Header;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static org.acme.core.filter.HeaderConstants.CORRELATION_ID;

@ApplicationScoped
public class HeaderService {

    @Inject
    HeaderContext headerContext;

    public List<Header> generateDefaultHeaders() {
        List<Header> headers = new ArrayList<>();

        headers.add(new Header(CORRELATION_ID, headerContext.getCorrelationId()));

        return headers;
    }

    public List<Header> generateDefaultHeadersWithCustomValue(String key, String value) {
        List<Header> headers = this.generateDefaultHeaders();

        if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
            headers.add(new Header(key, value));
        }

        return headers;
    }
}
