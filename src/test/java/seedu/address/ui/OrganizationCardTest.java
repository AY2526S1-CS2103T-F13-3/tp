package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;

/**
 * Tests for {@link OrganizationCard}.
 */
public class OrganizationCardTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_populatesLabels() {
        Organization organization = new OrganizationBuilder().build();
        OrganizationCard card = JavaFxTestUtil.callOnFxThread(() -> new OrganizationCard(organization, 2));

        assertEquals("2. ", getLabelText(card, "id"));
        assertEquals("Nike", getLabelText(card, "name"));
        assertEquals("98765432", getLabelText(card, "phone"));
        assertEquals("john.doe@nike.com", getLabelText(card, "email"));
    }

    @Test
    public void constructor_differentOrganization_updatesFields() {
        Organization organization = new OrganizationBuilder()
                .withName("Test Sports Corp")
                .withPhone("87654321")
                .withEmail("contact@testsports.com")
                .build();
        OrganizationCard card = JavaFxTestUtil.callOnFxThread(() -> new OrganizationCard(organization, 4));

        assertEquals("4. ", getLabelText(card, "id"));
        assertEquals("Test Sports Corp", getLabelText(card, "name"));
        assertEquals("87654321", getLabelText(card, "phone"));
        assertEquals("contact@testsports.com", getLabelText(card, "email"));
    }

    private String getLabelText(OrganizationCard card, String fieldName) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = OrganizationCard.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return ((Label) field.get(card)).getText();
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }
}
