package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.model.athlete.Athlete;
import seedu.address.testutil.athlete.AthleteBuilder;

public class AthleteCardTest {

    @Test
    public void athleteCard_validAthlete_fieldsCorrect() {
        Athlete athlete = new AthleteBuilder().build();
        assertNotNull(athlete);
        assertEquals("Amy Bee", athlete.getName().fullName);
        assertEquals("Swimming", athlete.getSport().value);
        assertEquals("20", athlete.getAge().value);
        assertEquals("85355255", athlete.getPhone().value);
        assertEquals("amy@gmail.com", athlete.getEmail().value);
    }

    @Test
    public void athleteCard_customData_fieldsCorrect() {
        Athlete athlete = new AthleteBuilder()
                .withName("John Doe")
                .withSport("Basketball")
                .withAge("25")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .build();

        assertNotNull(athlete);
        assertEquals("John Doe", athlete.getName().fullName);
        assertEquals("Basketball", athlete.getSport().value);
        assertEquals("25", athlete.getAge().value);
        assertEquals("98765432", athlete.getPhone().value);
        assertEquals("john@example.com", athlete.getEmail().value);
    }

}
