package org.acme.core.cqrsedaes.es;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "event_store")
public class EventEntity extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String eventId;

    @Column(nullable = false)
    public String aggregateId;

    @Column(nullable = false)
    public String eventType;

    @Lob
    @Column(nullable = false)
    public String data; // JSON serializado

    @Lob
    @Column(nullable = false)
    public String headers; // JSON serializado

    @Column(nullable = false)
    public Instant timestamp;

    @Column(nullable = false)
    public Long version;
} 