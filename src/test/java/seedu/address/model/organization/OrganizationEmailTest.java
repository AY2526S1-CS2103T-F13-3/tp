package seedu.address.model.organization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code OrganizationEmail}.
 */
public class OrganizationEmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OrganizationEmail(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new OrganizationEmail(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertFalse(OrganizationEmail.isValidEmail(null));

        // invalid emails (according to Commons Validator)
        String longEmail = "a".repeat(80) + "@example.com";
        assertFalse(OrganizationEmail.isValidEmail("")); // empty
        assertFalse(OrganizationEmail.isValidEmail(" ")); // spaces
        assertFalse(OrganizationEmail.isValidEmail("@example.com")); // missing local part
        assertFalse(OrganizationEmail.isValidEmail("noatsymbol.com")); // missing '@'
        assertFalse(OrganizationEmail.isValidEmail("john@.com")); // domain starts with dot
        assertFalse(OrganizationEmail.isValidEmail("john@@example.com")); // double '@'
        assertFalse(OrganizationEmail.isValidEmail("john@example")); // missing TLD
        assertFalse(OrganizationEmail.isValidEmail(longEmail)); // too long

        // valid emails (Commons Validator accepts)
        assertTrue(OrganizationEmail.isValidEmail("contact@nike.com"));
        assertTrue(OrganizationEmail.isValidEmail("info@procter-and-gamble.com"));
        assertTrue(OrganizationEmail.isValidEmail("sponsorship@brand.co"));
        assertTrue(OrganizationEmail.isValidEmail("john.doe@sub.example.org"));
        assertTrue(OrganizationEmail.isValidEmail("support_team@enterprise.global"));
        assertTrue(OrganizationEmail.isValidEmail("sales@company123.com"));
        assertTrue(OrganizationEmail.isValidEmail("marketing.dept@super-brand.io"));
    }

    @Test
    public void equals() {
        OrganizationEmail email = new OrganizationEmail("info@brand.com");

        // same values -> returns true
        assertTrue(email.equals(new OrganizationEmail("info@brand.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new OrganizationEmail("different@brand.com")));
    }
}
