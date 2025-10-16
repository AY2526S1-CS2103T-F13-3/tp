package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;

/**
 * Unit tests for {@code OrganizationCard}.
 * Verifies that {@code Organization} data is correctly built and accessible.
 */
public class OrganizationCardTest {

    @Test
    public void organizationCard_validOrganization_fieldsCorrect() {
        Organization organization = new OrganizationBuilder().build();
        assertNotNull(organization);
        assertEquals("Nike", organization.getName().fullOrganizationName);
        assertEquals("98765432", organization.getPhone().value);
        assertEquals("john.doe@nike.com", organization.getEmail().value);
    }

    @Test
    public void organizationCard_customData_fieldsCorrect() {
        Organization organization = new OrganizationBuilder()
                .withName("Test Sports Corp")
                .withPhone("87654321")
                .withEmail("contact@testsports.com")
                .build();

        assertNotNull(organization);
        assertEquals("Test Sports Corp", organization.getName().fullOrganizationName);
        assertEquals("87654321", organization.getPhone().value);
        assertEquals("contact@testsports.com", organization.getEmail().value);
    }
}
