package waypoint.model;

public final class NoSpotAvailable extends RuntimeException {
    public NoSpotAvailable(Vehicle vehicle) {
        super("No compatible free spot available for vehicle: " + vehicle.id().value() + " (" + vehicle.size() + ")");
    }
}