package waypoint.allocation;

import waypoint.model.*;

/**
 * THE CANARY. Deliberately violates the contract.
 * CanaryTest asserts that the shared suite REJECTS this class.
 */
public final class BrokenAllocator implements SpotAllocator {
    @Override
    public Spot allocate(Vehicle v) {
        return null; // weakened postcondition
    }

    @Override
    public void release(Spot s) {
        throw new IllegalStateException("Broken release: not idempotent"); // violates idempotency
    }
}