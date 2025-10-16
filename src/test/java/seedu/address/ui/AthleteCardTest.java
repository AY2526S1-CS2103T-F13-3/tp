package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import seedu.address.model.athlete.Athlete;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * Tests for {@link AthleteCard}.
 */
public class AthleteCardTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_populatesLabels() {
        Athlete athlete = new AthleteBuilder().build();
        AthleteCard card = JavaFxTestUtil.callOnFxThread(() -> new AthleteCard(athlete, 1));

        assertEquals("1. ", getLabelText(card, "id"));
        assertEquals("Amy Bee", getLabelText(card, "name"));
        assertEquals("Swimming", getLabelText(card, "sport"));
        assertEquals("Age: 20", getLabelText(card, "age"));
        assertEquals("85355255", getLabelText(card, "phone"));
        assertEquals("amy@gmail.com", getLabelText(card, "email"));
    }

    @Test
    public void constructor_differentAthlete_updatesAllFields() {
        Athlete athlete = new AthleteBuilder()
                .withName("John Doe")
                .withSport("Basketball")
                .withAge("25")
                .withPhone("98765432")
                .withEmail("john@example.com")
                .build();
        AthleteCard card = JavaFxTestUtil.callOnFxThread(() -> new AthleteCard(athlete, 5));

        assertEquals("5. ", getLabelText(card, "id"));
        assertEquals("John Doe", getLabelText(card, "name"));
        assertEquals("Basketball", getLabelText(card, "sport"));
        assertEquals("Age: 25", getLabelText(card, "age"));
        assertEquals("98765432", getLabelText(card, "phone"));
        assertEquals("john@example.com", getLabelText(card, "email"));
    }

    private String getLabelText(AthleteCard card, String fieldName) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = AthleteCard.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return ((Label) field.get(card)).getText();
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }
}
