package seedu.address.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Date8}.
 */
class Date8Test {

    @Test
    void constructor_invalid_throwsIllegalArgumentException() {
        assertThrows(NullPointerException.class, () -> new Date8(null));
        assertThrows(IllegalArgumentException.class, () -> new Date8(""));
        assertThrows(IllegalArgumentException.class, () -> new Date8("010120")); // too short
        assertThrows(IllegalArgumentException.class, () -> new Date8("01abc2025")); // non-digit letters
        assertThrows(IllegalArgumentException.class, () -> new Date8("31022025")); // invalid date
        assertThrows(IllegalArgumentException.class, () -> new Date8("29022025")); // non-leap year Feb 29
    }

    @Test
    void isValidDate8() {
        assertFalse(Date8.isValidDate8(null));
        assertFalse(Date8.isValidDate8(""));
        assertFalse(Date8.isValidDate8("010120"));
        assertFalse(Date8.isValidDate8("31abc2024")); // non-digit letters
        assertFalse(Date8.isValidDate8("31022025"));
        assertTrue(Date8.isValidDate8("01012025"));
        assertTrue(Date8.isValidDate8("29022024")); // leap day
        assertTrue(Date8.isValidDate8("01 01 2025")); // spaced format
        assertTrue(Date8.isValidDate8("01-01-2025")); // hyphenated format
        assertTrue(Date8.isValidDate8("01/01/2025")); // slash format
        assertTrue(Date8.isValidDate8(" 01 01 2025 ")); // spaced with extra whitespace
    }

    @Test
    void toLocalDate_ok() {
        Date8 d = new Date8("01012025");
        assertEquals(LocalDate.of(2025, 1, 1), d.toLocalDate());
    }

    @Test
    void equalsAndHashCode() {
        Date8 d1 = new Date8("01012025");
        Date8 d2 = new Date8("01012025");
        Date8 d3 = new Date8("02012025");

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotEquals(d1, d3);
        assertNotEquals(null, d1);
    }

    @Test
    void toString_ok() {
        assertEquals("31122024", new Date8("31122024").toString());
    }

    @Test
    void constructor_normalizedFormats_success() {
        // Test various normalized formats that should be accepted
        Date8 d1 = new Date8("01 01 2025");
        Date8 d2 = new Date8("01-01-2025");
        Date8 d3 = new Date8("01/01/2025");
        Date8 d4 = new Date8(" 01 01 2025 ");
        Date8 expected = new Date8("01012025");

        assertEquals(expected, d1);
        assertEquals(expected, d2);
        assertEquals(expected, d3);
        assertEquals(expected, d4);

        // Verify all normalize to the same internal format
        assertEquals("01012025", d1.toString());
        assertEquals("01012025", d2.toString());
        assertEquals("01012025", d3.toString());
        assertEquals("01012025", d4.toString());
    }
}
