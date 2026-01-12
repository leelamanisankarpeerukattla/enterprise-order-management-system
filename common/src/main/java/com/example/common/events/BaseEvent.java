package com.example.common.events;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent {
    private String eventId = UUID.randomUUID().toString();
    private Instant occurredAt = Instant.now();
    private EventType type;
    private String correlationId;

    protected BaseEvent() {}
    protected BaseEvent(EventType type, String correlationId) {
        this.type = type;
        this.correlationId = correlationId;
    }

    public String getEventId() { return eventId; }
    public Instant getOccurredAt() { return occurredAt; }
    public EventType getType() { return type; }
    public String getCorrelationId() { return correlationId; }

    public void setEventId(String eventId) { this.eventId = eventId; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public void setType(EventType type) { this.type = type; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
