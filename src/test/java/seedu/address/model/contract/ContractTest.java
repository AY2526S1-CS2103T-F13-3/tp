package seedu.address.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.Sport;
import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.contract.ContractBuilder;

class ContractTest {

    @Test
    void isSameContract_identityRules() {
        Contract base = new ContractBuilder()
                .withAthlete(new AthleteBuilder().withName("Alice").build())
                .withSport("Football")
                .withOrganization(new OrganizationBuilder().withName("Inter Miami").build())
                .withStartDate("01012024")
                .withEndDate("01012025")
                .withAmount(5000000L)
                .build();

        // Same reference
        assertTrue(base.isSameContract(base));

        // Exact copy -> same
        Contract copy = new ContractBuilder(base).build();
        assertTrue(base.isSameContract(copy));

        // Change athlete -> not same
        Athlete differentAthlete = new AthleteBuilder().withName("Bob").build();
        assertFalse(base.isSameContract(new ContractBuilder(base).withAthlete(differentAthlete).build()));

        // Change sport -> not same
        assertFalse(base.isSameContract(new ContractBuilder(base).withSport("Basketball").build()));

        // Change org -> not same
        Organization differentOrg = new OrganizationBuilder().withName("FC Barcelona").build();
        assertFalse(base.isSameContract(new ContractBuilder(base).withOrganization(differentOrg).build()));

        // Change start date -> not same
        assertFalse(base.isSameContract(new ContractBuilder(base).withStartDate("02012024").build()));

        // Change end date -> not same
        assertFalse(base.isSameContract(new ContractBuilder(base).withEndDate("02012025").build()));

        // Change amount -> not same
        assertFalse(base.isSameContract(new ContractBuilder(base).withAmount(6000000L).build()));
    }

    @Test
    void equals_strongEqualityAllFields() {
        Contract a = new ContractBuilder().build();
        Contract b = new ContractBuilder(a).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(null, a);

        // differ one field -> not equal
        Contract diffAmount = new ContractBuilder(a).withAmount(42L).build();
        assertNotEquals(a, diffAmount);
    }

    @Test
    void getters_ok() {
        Sport s = new Sport("Swimming");
        Contract c = new ContractBuilder()
                .withSport("Swimming")
                .withStartDate("01012024")
                .withEndDate("31122024")
                .withAmount(123L)
                .build();

        assertEquals("01012024", c.getStartDate().value);
        assertEquals("31122024", c.getEndDate().value);
        assertEquals(123, c.getAmount().value);
        assertEquals(s, c.getSport());
        assertNotNull(c.getAthlete());
        assertNotNull(c.getOrganization());
    }

    @Test
    void toString_containsKeyFields() {
        Contract c = new ContractBuilder().build();
        String s = c.toString();
        assertTrue(s.contains("athlete"));
        assertTrue(s.contains("sport"));
        assertTrue(s.contains("organization"));
        assertTrue(s.contains("startDate"));
        assertTrue(s.contains("endDate"));
        assertTrue(s.contains("amount"));
    }
}
