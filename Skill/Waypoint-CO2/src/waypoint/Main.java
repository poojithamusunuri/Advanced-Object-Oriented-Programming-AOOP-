package waypoint;

import waypoint.allocation.*;
import waypoint.model.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   WAYPOINT SMART PARKING - WEEK 2 ALLOCATION STRATEGIES  ");
        System.out.println("==========================================================");

        List<Spot> lot = List.of(
                new Spot(new SpotId("L1-LARGE"), 1, 5, new SpotType.Large()),
                new Spot(new SpotId("L1-COMP"),  1, 20, new SpotType.Compact()),
                new Spot(new SpotId("L2-EV"),    2, 10, new SpotType.Ev(22))
        );

        Vehicle compactCar = new Vehicle(new VehicleId("TS09AB1234"), VehicleSize.COMPACT);
        Vehicle evCar      = new Vehicle(new VehicleId("KA01EV9999"), VehicleSize.EV);

        // SizeFitAllocator prefers L1-COMP over closer L1-LARGE for a compact car
        SpotAllocator allocator = new SizeFitAllocator(lot);
        Spot s1 = allocator.allocate(compactCar);
        Spot s2 = allocator.allocate(evCar);

        System.out.println("Compact Car allocated to : " + s1);
        System.out.println("EV Car allocated to      : " + s2);

        allocator.release(s1);
        allocator.release(s1); // Idempotent second release
        System.out.println("After idempotent release : " + s1);
    }
}