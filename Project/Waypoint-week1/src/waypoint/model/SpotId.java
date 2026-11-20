// File: SpotId.java
package waypoint.model;

import java.util.Objects;

public record SpotId(String value) {
    public SpotId {
        Objects.requireNonNull(value, "SpotId cannot be null");
        value = value.trim();
        if (value.isEmpty()) throw new IllegalArgumentException("SpotId cannot be blank");
    }
}