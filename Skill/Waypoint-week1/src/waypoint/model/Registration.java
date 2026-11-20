package waypoint.model;

import java.util.Objects;
import java.util.regex.Pattern;

/** VALUE OBJECT. Validates and normalises Indian vehicle registration plates. */
public record Registration(String value) {
    private static final Pattern VALID_PLATE = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$");

    public Registration {
        Objects.requireNonNull(value, "Registration cannot be null");
        value = value.trim().toUpperCase();
        if (!VALID_PLATE.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid registration format (expected e.g. MH12AB1234): " + value);
        }
    }
}