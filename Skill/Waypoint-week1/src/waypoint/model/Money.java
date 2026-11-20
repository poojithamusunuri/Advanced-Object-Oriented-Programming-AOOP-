package waypoint.model;

import java.util.*;

/** VALUE OBJECT. Exact money. */
public final class Money implements Comparable<Money> {
    private final long minor;
    private final Currency currency;

    private Money(long minor, Currency currency) {
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.minor = minor;
    }

    public static Money ofMinor(long minor, Currency currency) {
        return new Money(minor, currency);
    }

    public static Money rupees(long rupees) {
        return new Money(Math.multiplyExact(rupees, 100L), Currency.INR);
    }

    public long minor() {
        return minor;
    }

    public Currency currency() {
        return currency;
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.addExact(this.minor, other.minor), this.currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.subtractExact(this.minor, other.minor), this.currency);
    }

    public Money times(int factor) {
        return new Money(Math.multiplyExact(this.minor, (long) factor), this.currency);
    }

    public boolean isNegative() {
        return this.minor < 0;
    }

    /** Allocates this amount across n equal shares without losing a single paisa/cent. */
    public List<Money> allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of shares must be positive: " + n);
        }
        long baseShare = this.minor / n;
        long remainder = Math.abs(this.minor % n);
        long sign = this.minor < 0 ? -1L : 1L;

        List<Money> shares = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            long extra = (i < remainder) ? sign : 0L;
            shares.add(new Money(baseShare + extra, this.currency));
        }
        return Collections.unmodifiableList(shares);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "Other Money cannot be null");
        if (this.currency != other.currency) {
            throw new CurrencyMismatch(this.currency, other.currency);
        }
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return Long.compare(this.minor, other.minor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money other)) return false;
        return this.minor == other.minor && this.currency == other.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minor, currency);
    }

    @Override
    public String toString() {
        if (currency.minorUnits() == 0) {
            return currency + " " + minor;
        }
        long divisor = (long) Math.pow(10, currency.minorUnits());
        long whole = minor / divisor;
        long frac = Math.abs(minor % divisor);
        return String.format("%s %d.%0" + currency.minorUnits() + "d", currency, whole, frac);
    }
}