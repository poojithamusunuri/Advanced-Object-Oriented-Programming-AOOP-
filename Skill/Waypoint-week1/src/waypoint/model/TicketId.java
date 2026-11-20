// File: TicketId.java
package waypoint.model;

import java.util.Objects;

public record TicketId(String value) {
    public TicketId {
        Objects.requireNonNull(value, "TicketId cannot be null");
        value = value.trim();
        if (value.isEmpty()) throw new IllegalArgumentException("TicketId cannot be blank");
    }
}