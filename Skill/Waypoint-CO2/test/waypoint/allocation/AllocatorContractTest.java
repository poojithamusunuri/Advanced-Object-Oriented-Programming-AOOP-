package waypoint.allocation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import waypoint.model.*;

import java.util.List;
import java.util.stream.Stream;

/** ONE suite. Every implementation must pass it, unchanged. */
class AllocatorContractTest {

    static List<Spot> threeFreeSpots() {
        return List.of(
                new Spot(new SpotId("S1"), 1, 10, new SpotType.Compact()),
                new Spot(new SpotId("S2"), 2, 15, new SpotType.Large()),
                new Spot(new SpotId("S3"), 1, 25, new SpotType.Ev(11))
        );
    }

    static Vehicle car() {
        return new Vehicle(new VehicleId("CAR-" + System.nanoTime()), VehicleSize.COMPACT);
    }

    static Vehicle motorcycle() {
        return new Vehicle(new VehicleId("BIKE-" + System.nanoTime()), VehicleSize.MOTORCYCLE);
    }

    static void drainAllSpots(SpotAllocator a) {
        while (true) {
            try {
                a.allocate(car());
            } catch (NoSpotAvailable e) {
                return;
            }
        }
    }

    static Stream<SpotAllocator> implementations() {
        return Stream.of(
                new NearestFirstAllocator(threeFreeSpots()),
                new LevelBalancingAllocator(threeFreeSpots()),
                new SizeFitAllocator(threeFreeSpots())
        );
    }

    @ParameterizedTest @MethodSource("implementations")
    void neverReturnsNull(SpotAllocator a) {
        assertNotNull(a.allocate(car()));
    }

    @ParameterizedTest @MethodSource("implementations")
    void allocatedSpotIsNoLongerFree(SpotAllocator a) {
        assertFalse(a.allocate(car()).isFree());
    }

    @ParameterizedTest @MethodSource("implementations")
    void acceptsEveryVehicleSize(SpotAllocator a) {
        assertDoesNotThrow(() -> a.allocate(motorcycle()));
    }

    @ParameterizedTest @MethodSource("implementations")
    void throwsWhenNothingFits(SpotAllocator a) {
        drainAllSpots(a);
        assertThrows(NoSpotAvailable.class, () -> a.allocate(car()));
    }

    @ParameterizedTest @MethodSource("implementations")
    void releaseIsIdempotent(SpotAllocator a) {
        Spot s = a.allocate(car());
        a.release(s);
        assertDoesNotThrow(() -> a.release(s));
        assertTrue(s.isFree());
    }

    @ParameterizedTest @MethodSource("implementations")
    void neverAllocatesTheSameSpotTwice(SpotAllocator a) {
        Spot first = a.allocate(car());
        for (int i = 0; i < 2; i++) {
            try {
                assertNotEquals(first, a.allocate(car()));
            } catch (NoSpotAvailable expected) {
                return;
            }
        }
    }
}