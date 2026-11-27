package waypoint.allocation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import waypoint.model.*;

/** Proves the contract suite actually tests the contract. */
class CanaryTest {

    @Test
    void theSuiteRejectsADeliberatelyBrokenImplementation() {
        SpotAllocator broken = new BrokenAllocator();
        Vehicle car = new Vehicle(new VehicleId("MH12AB1234"), VehicleSize.COMPACT);
        Spot dummySpot = new Spot(new SpotId("S1"));

        // 1. Rejects null-returning allocate()
        assertThrows(AssertionError.class,
                () -> assertNotNull(broken.allocate(car)),
                "the suite must fail on a null-returning allocator");

        // 2. Rejects non-idempotent release()
        assertThrows(AssertionError.class,
                () -> assertDoesNotThrow(() -> broken.release(dummySpot)),
                "the suite must fail on a non-idempotent release()");
    }
}