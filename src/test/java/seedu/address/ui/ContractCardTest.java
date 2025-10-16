package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import seedu.address.model.contract.Contract;
import seedu.address.testutil.contract.ContractBuilder;

/**
 * Tests for {@link ContractCard}.
 */
public class ContractCardTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_populatesLabels() {
        Contract contract = new ContractBuilder().build();
        ContractCard card = JavaFxTestUtil.callOnFxThread(() -> new ContractCard(contract, 3));

        assertEquals("3. ", getLabelText(card, "id"));
        assertEquals(contract.getAthlete().getName().fullName + " - " + contract.getSport().value,
                getLabelText(card, "athleteInfo"));
        assertEquals("Organization: " + contract.getOrganization().getName().fullOrganizationName,
                getLabelText(card, "organizationInfo"));

        String durationText = getLabelText(card, "duration");
        assertTrue(durationText.startsWith("Duration: "));
        assertTrue(durationText.contains("01/01/2024"));
        assertTrue(durationText.contains("31/12/2024"));

        assertEquals("Amount: $" + String.format("%,d", contract.getAmount().value),
                getLabelText(card, "amount"));
    }

    @Test
    public void constructor_differentContract_formatsFields() {
        Contract contract = new ContractBuilder()
                .withSport("Basketball")
                .withStartDate("15062024")
                .withEndDate("14062025")
                .withAmount(1000000)
                .build();
        ContractCard card = JavaFxTestUtil.callOnFxThread(() -> new ContractCard(contract, 7));

        assertEquals("7. ", getLabelText(card, "id"));
        assertTrue(getLabelText(card, "athleteInfo").contains("Basketball"));
        String durationText = getLabelText(card, "duration");
        assertTrue(durationText.contains("15/06/2024"));
        assertTrue(durationText.contains("14/06/2025"));
        assertEquals("Amount: $" + String.format("%,d", contract.getAmount().value),
                getLabelText(card, "amount"));
    }

    private String getLabelText(ContractCard card, String fieldName) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = ContractCard.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return ((Label) field.get(card)).getText();
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }
}
