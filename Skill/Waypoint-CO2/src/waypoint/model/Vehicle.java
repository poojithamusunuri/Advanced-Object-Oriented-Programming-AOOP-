package waypoint.model;

import java.util.Objects;

public record Vehicle(VehicleId id, VehicleSize size) {
    public Vehicle {
        Objects.requireNonNull(id, "VehicleId cannot be null");
        Objects.requireNonNull(size, "VehicleSize cannot be null");
    }
}