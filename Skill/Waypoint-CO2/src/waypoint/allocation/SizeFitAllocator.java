package waypoint.allocation;

import waypoint.model.*;
import java.util.*;

/** Smallest spot that fits. A compact car may take a large bay ONLY when no compact bay remains. */
public final class SizeFitAllocator implements SpotAllocator {
    private final List<Spot> spots;

    public SizeFitAllocator(List<Spot> spots) {
        Objects.requireNonNull(spots, "Spots list cannot be null");
        this.spots = List.copyOf(spots);
    }

    private static int sizeRank(SpotType type) {
        return switch (type) {
            case SpotType.Compact() -> 1;
            case SpotType.Ev(int kw) -> 2;
            case SpotType.Large()   -> 3;
        };
    }

    @Override
    public Spot allocate(Vehicle vehicle) throws NoSpotAvailable {
        Objects.requireNonNull(vehicle, "Vehicle cannot be null");

        Spot best = spots.stream()
                .filter(Spot::isFree)
                .filter(s -> SpotType.fits(vehicle.size(), s.type()))
                .min(Comparator.comparingInt((Spot s) -> sizeRank(s.type()))
                        .thenComparingInt(Spot::distanceFromGate)
                        .thenComparing(s -> s.id().value()))
                .orElseThrow(() -> new NoSpotAvailable(vehicle));

        if (!best.tryAssign(vehicle.id())) {
            throw new NoSpotAvailable(vehicle);
        }
        return best;
    }

    @Override
    public void release(Spot spot) {
        Objects.requireNonNull(spot, "Spot cannot be null");
        spot.forceRelease();
    }
}