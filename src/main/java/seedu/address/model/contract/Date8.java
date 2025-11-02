package seedu.address.model.contract;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

/** DDMMYYYY date with strict validation (e.g., 01012025). */
public class Date8 {
    public static final String MESSAGE_CONSTRAINTS =
            "Error: Date must be DDMMYYYY and a real calendar date (e.g., 01012025). "
            + "Spaces, hyphens, and forward slashes are accepted and will be automatically removed.";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("ddMMuuuu").withResolverStyle(ResolverStyle.STRICT);

    public final String value;

    /**
     * Constructs a {@code Date8} with the given date string.
     * Ensures the date is in DDMMYYYY format and represents a valid calendar date.
     * Accepts dates with spaces, hyphens, or forward slashes which are automatically removed.
     *
     * @param value The date string to validate and store.
     * @throws IllegalArgumentException if the value is not a valid DDMMYYYY date.
     */
    public Date8(String value) {
        requireNonNull(value);
        String normalized = normalizeDate(value.trim());
        if (normalized.length() != 8 || !normalized.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        try {
            LocalDate.parse(normalized, FMT); // throws if invalid
        } catch (Exception e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = normalized;
    }

    /**
     * Returns true if the given string is a valid DDMMYYYY date.
     * Accepts dates with spaces, hyphens, or forward slashes which are automatically removed.
     *
     * @param test The string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidDate8(String test) {
        if (test == null) {
            return false;
        }
        String normalized = normalizeDate(test.trim());
        if (normalized.length() != 8 || !normalized.chars().allMatch(Character::isDigit)) {
            return false;
        }
        try {
            LocalDate.parse(normalized, FMT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Normalizes a date string by removing spaces, hyphens, and forward slashes.
     * This allows the class to accept user-friendly formats like "01 01 2025",
     * "01-01-2025", and "01/01/2025" while maintaining strict DDMMYYYY storage.
     *
     * @param dateString The date string to normalize.
     * @return The normalized date string with separators removed.
     */
    private static String normalizeDate(String dateString) {
        return dateString.replaceAll("[\\s\\-/]", "");
    }

    /**
     * Converts this {@code Date8} to a {@link LocalDate}.
     *
     * @return A LocalDate representing the same date as this Date8.
     */
    public LocalDate toLocalDate() {
        return LocalDate.parse(value, FMT);
    }

    @Override public String toString() {
        return value;
    }
    @Override public boolean equals(Object o) {
        return o instanceof Date8 && value.equals(((Date8) o).value);
    }
    @Override public int hashCode() {
        return Objects.hash(value);
    }
}
