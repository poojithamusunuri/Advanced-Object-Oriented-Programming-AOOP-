package waypoint.allocation;

import waypoint.model.*;
import java.util.*;

/** Lowest distanceFromGate wins. */
public final class NearestFirstAllocator implements SpotAllocator {
    private final List<Spot> spots;

    public NearestFirstAllocator(List<Spot> spots) {
        Objects.requireNonNull(spots, "Spots list cannot be null");
        this.spots = List.copyOf(spots);
    }

    @Override
    public Spot allocate(Vehicle vehicle) throws NoSpotAvailable {
        Objects.requireNonNull(vehicle, "Vehicle cannot be null");

        Spot best = spots.stream()
                .filter(Spot::isFree)
                .filter(s -> SpotType.fits(vehicle.size(), s.type()))
                .min(Comparator.comparingInt(Spot::distanceFromGate)
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