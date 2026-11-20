package waypoint.model;

import java.util.Objects;

/** ENTITY. Identity = id. Invariant: a spot cannot be assigned while occupied. */
public final class Spot {
    private final SpotId id;
    private VehicleId occupant; // the ONLY mutable field

    public Spot(SpotId id) {
        this.id = Objects.requireNonNull(id, "SpotId cannot be null");
    }

    public SpotId id() {
        return id;
    }

    public VehicleId occupant() {
        return occupant;
    }

    public boolean tryAssign(VehicleId v) {
        Objects.requireNonNull(v, "VehicleId cannot be null");
        if (this.occupant != null) {
            return false;
        }
        this.occupant = v;
        return true;
    }

    public void release(VehicleId v) {
        Objects.requireNonNull(v, "VehicleId cannot be null");
        if (this.occupant != null && this.occupant.equals(v)) {
            this.occupant = null;
        }
    }

    public boolean isFree() {
        return this.occupant == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spot other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}