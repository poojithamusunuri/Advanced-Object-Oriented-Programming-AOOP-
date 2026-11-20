package waypoint.model;

public final class TicketAlreadyClosed extends RuntimeException {
    public TicketAlreadyClosed(TicketId id) {
        super("Ticket is already closed: " + id.value());
    }
}