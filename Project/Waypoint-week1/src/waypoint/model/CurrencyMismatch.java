package waypoint.model;

public final class CurrencyMismatch extends RuntimeException {
    public CurrencyMismatch(Currency expected, Currency actual) {
        super("Currency mismatch: cannot combine " + expected + " and " + actual);
    }
}