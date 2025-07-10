package org.acme.core.cqrsedaes.eda;

public final class EventHeaders {
    public static final String EVENT_ID = "event-id";
    public static final String AGGREGATE_ID = "aggregate-id";
    public static final String EVENT_TYPE = "event-type";
    public static final String TIMESTAMP = "timestamp";
    public static final String VERSION = "version";
    
    private EventHeaders() {
        // Utility class
    }
}
