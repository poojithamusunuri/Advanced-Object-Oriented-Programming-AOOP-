package waypoint.model;

import java.util.Objects;

/** ENTITY. Identity = id. Invariant: a spot cannot be assigned while occupied. */
public final class Spot {
    private final SpotId id;
    private final int level;
    private final int distanceFromGate;
    private final SpotType type;
    private VehicleId occupant;

    public Spot(SpotId id, int level, int distanceFromGate, SpotType type) {
        this.id = Objects.requireNonNull(id, "SpotId cannot be null");
        if (level < 0) throw new IllegalArgumentException("Level cannot be negative");
        if (distanceFromGate < 0) throw new IllegalArgumentException("Distance cannot be negative");
        this.level = level;
        this.distanceFromGate = distanceFromGate;
        this.type = Objects.requireNonNull(type, "SpotType cannot be null");
    }

    // Convenience overload for single-argument tests
    public Spot(SpotId id) {
        this(id, 1, 10, new SpotType.Large());
    }

    public SpotId id() { return id; }
    public int level() { return level; }
    public int distanceFromGate() { return distanceFromGate; }
    public SpotType type() { return type; }
    public VehicleId occupant() { return occupant; }

    public boolean tryAssign(VehicleId v) {
        Objects.requireNonNull(v, "VehicleId cannot be null");
        if (this.occupant != null) return false;
        this.occupant = v;
        return true;
    }

    public void release(VehicleId v) {
        Objects.requireNonNull(v, "VehicleId cannot be null");
        if (this.occupant != null && this.occupant.equals(v)) {
            this.occupant = null;
        }
    }

    public void forceRelease() {
        this.occupant = null;
    }

    public boolean isFree() {
        return this.occupant == null;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Spot other) && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Spot[%s, L%d, dist=%dm, %s, free=%s]", id.value(), level, distanceFromGate, type, isFree());
    }
}