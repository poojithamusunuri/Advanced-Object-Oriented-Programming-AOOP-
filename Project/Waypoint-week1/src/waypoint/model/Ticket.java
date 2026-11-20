package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/**
 * ENTITY. Invariants:
 * I1 entry is always present
 * I2 exit is null, or not before entry
 * I3 fare is null, or not negative
 * I4 fare exists only if exit exists
 * I5 a closed ticket never reopens
 */
public final class Ticket {
    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;
    private Instant exit;   // mutable (non-final #1)
    private Money fare;     // mutable (non-final #2)

    private Ticket(TicketId id, SpotId spotId, Instant entry) {
        this.id = Objects.requireNonNull(id, "TicketId cannot be null");
        this.spotId = Objects.requireNonNull(spotId, "SpotId cannot be null");
        this.entry = Objects.requireNonNull(entry, "Entry timestamp cannot be null");
    }

    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    public boolean isOpen() {
        return this.exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {
        Objects.requireNonNull(at, "Exit timestamp cannot be null");
        Objects.requireNonNull(policy, "FarePolicy cannot be null");

        if (!isOpen()) {
            throw new TicketAlreadyClosed(this.id);
        }
        if (at.isBefore(this.entry)) {
            throw new IllegalArgumentException("Exit time (" + at + ") cannot be before entry time (" + this.entry + ")");
        }

        Money calculatedFare = Objects.requireNonNull(policy.fareFor(this.entry, at, this.spotId), "Fare cannot be null");
        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("Calculated fare cannot be negative: " + calculatedFare);
        }

        // Set exit and fare together to preserve I4 atomically
        this.exit = at;
        this.fare = calculatedFare;
        return this.fare;
    }

    public TicketId id() { return id; }
    public SpotId spotId() { return spotId; }
    public Instant entry() { return entry; }
    public Instant exit() { return exit; }
    public Money fare() { return fare; }
}