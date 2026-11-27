package waypoint.allocation;

import waypoint.model.*;

/**
 * Behavioral contract for parking spot allocation strategies.
 *
 * <p><b>allocate(Vehicle vehicle):</b>
 * <ul>
 *   <li><b>PRE  :</b> {@code vehicle != null}; every {@code VehicleSize} is accepted as valid input.</li>
 *   <li><b>POST :</b> Returns a non-null {@code Spot} {@code s} such that {@code !s.isFree()},
 *                     {@code vehicle.id().equals(s.occupant())}, and {@code SpotType.fits(vehicle.size(), s.type())} is true.
 *                     If no free compatible spot exists, throws {@link NoSpotAvailable} without mutating any spot.</li>
 *   <li><b>INV  :</b> No spot is ever double-assigned; total occupied spots + total free spots == total managed spots.</li>
 * </ul>
 *
 * <p><b>release(Spot spot):</b>
 * <ul>
 *   <li><b>PRE  :</b> {@code spot != null} and belongs to this allocator's managed pool.</li>
 *   <li><b>POST :</b> {@code spot.isFree() == true} and {@code spot.occupant() == null}.</li>
 *   <li><b>INV  :</b> Releasing one spot never alters the occupancy state of any other spot.</li>
 * </ul>
 *
 * <p><b>Additional Specification Clauses (Section 2.5):</b>
 * <ul>
 *   <li><b>Timing       :</b> Synchronous, non-blocking in-memory selection; completes in O(N) time over managed spots without network/DB waits.</li>
 *   <li><b>Side effects :</b> Mutates only the selected {@code Spot}'s occupant state; does not mutate the {@code Vehicle} or external logs.</li>
 *   <li><b>Idempotency  :</b> {@code release(spot)} is strictly idempotent—calling it multiple times on an already-free spot succeeds with no error.
 *                             {@code allocate(vehicle)} is NOT idempotent (each successful call claims a distinct free spot).</li>
 *   <li><b>Threading    :</b> Implementations are single-threaded unless externally synchronized; concurrent callers must serialize access.</li>
 *   <li><b>Ordering     :</b> Deterministic tie-breaking (by {@code spot.id().value()}) when two candidate spots have equal priority score.</li>
 * </ul>
 */
public interface SpotAllocator {
    Spot allocate(Vehicle vehicle) throws NoSpotAvailable;
    void release(Spot spot);
}