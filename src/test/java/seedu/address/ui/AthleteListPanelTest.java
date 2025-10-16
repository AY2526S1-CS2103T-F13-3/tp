package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.athlete.Athlete;
import seedu.address.testutil.athlete.AthleteBuilder;

public class AthleteListPanelTest {

    @Test
    public void athleteList_emptyList_success() {
        List<Athlete> emptyList = new ArrayList<>();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void athleteList_listWithAthletes_success() {
        List<Athlete> athleteList = new ArrayList<>();
        athleteList.add(new AthleteBuilder().build());
        athleteList.add(new AthleteBuilder().withName("Jane Doe").withSport("Tennis").build());

        assertEquals(2, athleteList.size());
        assertEquals("Amy Bee", athleteList.get(0).getName().fullName);
        assertEquals("Jane Doe", athleteList.get(1).getName().fullName);
    }

    @Test
    public void athleteList_singleAthlete_success() {
        List<Athlete> athleteList = new ArrayList<>();
        Athlete athlete = new AthleteBuilder()
                .withName("Test Athlete")
                .withSport("Running")
                .withAge("22")
                .withPhone("12345678")
                .withEmail("test@example.com")
                .build();
        athleteList.add(athlete);

        assertEquals(1, athleteList.size());
        assertEquals("Test Athlete", athleteList.get(0).getName().fullName);
    }
}
