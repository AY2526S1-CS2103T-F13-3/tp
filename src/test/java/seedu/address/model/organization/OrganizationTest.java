package seedu.address.model.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.contract.Contract;
import seedu.address.testutil.contract.ContractBuilder;

/**
 * Unit tests for {@link Organization}.
 */
public class OrganizationTest {

    private final OrganizationName nikeName = new OrganizationName("Nike");
    private final OrganizationPhone phone = new OrganizationPhone("98765432");
    private final OrganizationEmail email = new OrganizationEmail("john@nike.com");

    private final Organization nike = new Organization(nikeName, phone, email);

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Organization(null, phone, email));
        assertThrows(NullPointerException.class, () -> new Organization(nikeName, null, email));
        assertThrows(NullPointerException.class, () -> new Organization(nikeName, phone, null));
    }

    @Test
    public void isSameOrganization() {
        // same object -> true
        assertTrue(nike.isSameOrganization(nike));

        // null -> false
        assertFalse(nike.isSameOrganization(null));

        // same name -> true
        Organization sameNike = new Organization(
                new OrganizationName("Nike"),
                new OrganizationPhone("87654321"),
                new OrganizationEmail("other@nike.com"));
        assertTrue(nike.isSameOrganization(sameNike));

        // different name -> false
        Organization differentName = new Organization(
                new OrganizationName("Adidas"),
                phone,
                email);
        assertFalse(nike.isSameOrganization(differentName));
    }

    @Test
    public void equals() {
        // same values -> true
        Organization nikeCopy = new Organization(nikeName, phone, email);
        assertTrue(nike.equals(nikeCopy));

        // same object -> true
        assertTrue(nike.equals(nike));

        // null -> false
        assertFalse(nike.equals(null));

        // different type -> false
        assertFalse(nike.equals(5));

        // different name -> false
        Organization diffName = new Organization(new OrganizationName("Adidas"), phone, email);
        assertFalse(nike.equals(diffName));

        // different phone -> false
        Organization diffPhone = new Organization(nikeName, new OrganizationPhone("87654321"), email);
        assertFalse(nike.equals(diffPhone));

        // different email -> false
        Organization diffEmail = new Organization(nikeName, phone, new OrganizationEmail("new@nike.com"));
        assertFalse(nike.equals(diffEmail));
    }

    @Test
    public void toStringMethod() {
        String expected = Organization.class.getCanonicalName()
                + "{name=" + nike.getName()
                + ", phone=" + nike.getPhone()
                + ", email=" + nike.getEmail()
                + "}";
        assertEquals(expected, nike.toString());
    }

    @Test
    public void getTotalContractAmount_withContracts_returnsCorrectTotal() {
        Contract contract1 = new ContractBuilder()
                .withOrganization(nike)
                .withAmount(3000000L)
                .build();
        Contract contract2 = new ContractBuilder()
                .withOrganization(nike)
                .withAmount(7000000L)
                .build();

        ObservableList<Contract> contracts = FXCollections.observableArrayList(contract1, contract2);

        long totalAmount = nike.getTotalContractAmount(contracts);
        assertEquals(10000000L, totalAmount);
    }
}
