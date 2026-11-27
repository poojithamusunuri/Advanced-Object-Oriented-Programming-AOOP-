package waypoint.model;

import java.util.Objects;

/** Sealed hierarchy representing bay types; exhaustive switch with no default branch. */
public sealed interface SpotType permits SpotType.Compact, SpotType.Large, SpotType.Ev {
    record Compact() implements SpotType {}
    record Large()   implements SpotType {}
    record Ev(int kw) implements SpotType {
        public Ev {
            if (kw <= 0) throw new IllegalArgumentException("Charger kW must be positive: " + kw);
        }
    }

    static boolean fits(VehicleSize size, SpotType spot) {
        Objects.requireNonNull(size, "VehicleSize cannot be null");
        Objects.requireNonNull(spot, "SpotType cannot be null");

        return switch (spot) {
            case Compact() -> size == VehicleSize.MOTORCYCLE || size == VehicleSize.COMPACT;
            case Large()   -> size == VehicleSize.MOTORCYCLE || size == VehicleSize.COMPACT || size == VehicleSize.LARGE;
            case Ev(int kw) -> size == VehicleSize.MOTORCYCLE || size == VehicleSize.COMPACT || size == VehicleSize.EV;
        };
    }
}