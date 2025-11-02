package seedu.address.model.athlete;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new seedu.address.model.athlete.Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new seedu.address.model.athlete.Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertFalse(seedu.address.model.athlete.Email.isValidEmail(null));

        // invalid emails (Commons Validator rejects these)
        String longEmail = "a".repeat(80) + "@example.com"; // too long overall (> 64+255 typical limits)
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("")); // empty string
        assertFalse(seedu.address.model.athlete.Email.isValidEmail(" ")); // spaces only
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("@example.com")); // missing local part
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjackexample.com")); // missing '@'
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjack@")); // missing domain name
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peter jack@example.com")); // spaces
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjack@.example.com")); // invalid domain start
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjack@example.com.")); // trailing dot
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjack@@example.com")); // double '@'
        assertFalse(seedu.address.model.athlete.Email.isValidEmail("peterjack@example")); // no TLD
        assertFalse(seedu.address.model.athlete.Email.isValidEmail(longEmail)); // exceeds max length

        // valid emails (Commons Validator accepts)
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("PeterJack_1190@example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("PeterJack.1190@example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("PeterJack+1190@example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("PeterJack-1190@example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("a@b.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("123@145.com")); // numeric domain accepted
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("a1+be.d@example1.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("peter_jack@very-long-domain-example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com"));
        assertTrue(seedu.address.model.athlete.Email.isValidEmail("e1234567@u.nus.edu"));
    }

    @Test
    public void equals() {
        seedu.address.model.athlete.Email email = new seedu.address.model.athlete.Email("valid@email.com");

        // same values -> returns true
        assertTrue(email.equals(new seedu.address.model.athlete.Email("valid@email.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email.com")));
    }
}