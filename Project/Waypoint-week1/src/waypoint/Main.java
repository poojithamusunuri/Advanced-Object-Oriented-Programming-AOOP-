package waypoint;

import waypoint.model.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("     WAYPOINT SMART PARKING SYSTEM - WEEK 1 WORKFLOW      ");
        System.out.println("==========================================================");

        // 1. Validate & normalize vehicle registration (Value Object)
        Registration plate = new Registration("  ts09ab1234 ");
        VehicleId vehicle = new VehicleId(plate.value());
        System.out.println("1. Vehicle Registered   : " + plate.value());

        // 2. Create Parking Spot & assign vehicle (Entity)
        SpotId spotId = new SpotId("B1-04");
        Spot spot = new Spot(spotId);
        boolean assigned = spot.tryAssign(vehicle);
        System.out.println("2. Spot " + spot.id().value() + " Assigned  : " + assigned + " (Free? " + spot.isFree() + ")");

        // Prevent double-assignment
        boolean secondAttempt = spot.tryAssign(new VehicleId("MH12XY9999"));
        System.out.println("   Duplicate Claim Blocked: " + !secondAttempt);

        // 3. Open Parking Ticket at 10:00 UTC (Entity)
        Instant entryTime = Instant.parse("2026-11-20T10:00:00Z");
        Ticket ticket = Ticket.open(new TicketId("TCK-1001"), spotId, entryTime);
        System.out.println("3. Ticket Opened        : " + ticket.id().value() + " at " + ticket.entry());

        // 4. Define Hourly Fare Policy (Rs. 50 per started hour) & close ticket after 2h 30m
        FarePolicy hourlyPolicy = (entry, exit, s) -> {
            long minutes = Duration.between(entry, exit).toMinutes();
            long billedHours = Math.max(1, (minutes + 59) / 60);
            return Money.rupees(50).times((int) billedHours);
        };

        Instant exitTime = Instant.parse("2026-11-20T12:30:00Z");
        Money totalFare = ticket.close(exitTime, hourlyPolicy);
        spot.release(vehicle);
        System.out.println("4. Ticket Closed        : Exit at " + ticket.exit() + " | Total Fare = " + totalFare);
        System.out.println("   Spot " + spot.id().value() + " Released  : Free? " + spot.isFree());

        // 5. Split bill across 3 riders using exact Money.allocate()
        List<Money> splitShares = totalFare.allocate(3);
        System.out.println("5. Exact 3-Way Split    : " + splitShares);
    }
}