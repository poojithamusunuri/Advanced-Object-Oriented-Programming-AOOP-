package waypoint.model;

public enum Currency {
    INR(2), USD(2), EUR(2), JPY(0);

    private final int minorUnits;

    Currency(int minorUnits) {
        this.minorUnits = minorUnits;
    }

    public int minorUnits() {
        return minorUnits;
    }
}