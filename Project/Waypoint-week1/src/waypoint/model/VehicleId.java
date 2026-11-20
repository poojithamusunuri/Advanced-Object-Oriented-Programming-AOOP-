// File: VehicleId.java
package waypoint.model;

import java.util.Objects;

public record VehicleId(String value) {
    public VehicleId {
        Objects.requireNonNull(value, "VehicleId cannot be null");
        value = value.trim();
        if (value.isEmpty()) throw new IllegalArgumentException("VehicleId cannot be blank");
    }
}