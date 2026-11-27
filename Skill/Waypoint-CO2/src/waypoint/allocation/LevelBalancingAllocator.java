package waypoint.allocation;

import waypoint.model.*;
import java.util.*;
import java.util.stream.Collectors;

/** Prefers the least-occupied level, then nearest within it. */
public final class LevelBalancingAllocator implements SpotAllocator {
    private final List<Spot> spots;

    public LevelBalancingAllocator(List<Spot> spots) {
        Objects.requireNonNull(spots, "Spots list cannot be null");
        this.spots = List.copyOf(spots);
    }

    @Override
    public Spot allocate(Vehicle vehicle) throws NoSpotAvailable {
        Objects.requireNonNull(vehicle, "Vehicle cannot be null");

        Map<Integer, Long> occupiedPerLevel = spots.stream()
                .collect(Collectors.groupingBy(
                        Spot::level,
                        Collectors.filtering(s -> !s.isFree(), Collectors.counting())
                ));

        Spot best = spots.stream()
                .filter(Spot::isFree)
                .filter(s -> SpotType.fits(vehicle.size(), s.type()))
                .min(Comparator.comparingLong((Spot s) -> occupiedPerLevel.getOrDefault(s.level(), 0L))
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