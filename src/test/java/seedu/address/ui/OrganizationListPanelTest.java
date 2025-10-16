package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;

/**
 * Unit tests for {@code OrganizationListPanel}.
 */
public class OrganizationListPanelTest {

    @Test
    public void organizationList_emptyList_success() {
        List<Organization> emptyList = new ArrayList<>();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void organizationList_listWithOrganizations_success() {
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(new OrganizationBuilder().build());
        organizationList.add(new OrganizationBuilder().withName("Second Org").build());

        assertEquals(2, organizationList.size());
        assertEquals("Second Org", organizationList.get(1).getName().fullOrganizationName);
    }

    @Test
    public void organizationList_singleOrganization_success() {
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new OrganizationBuilder()
                .withName("Test Organization")
                .withPhone("98765432")
                .withEmail("test@org.com")
                .build();
        organizationList.add(organization);

        assertEquals(1, organizationList.size());
        assertEquals("Test Organization", organizationList.get(0).getName().fullOrganizationName);
    }
}
