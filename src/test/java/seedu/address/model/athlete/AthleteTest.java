package seedu.address.model.athlete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.athlete.TypicalAthletes.ALICE;
import static seedu.address.testutil.athlete.TypicalAthletes.BENSON;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.contract.Contract;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.contract.ContractBuilder;

public class AthleteTest {

    @Test
    public void isSameAthlete() {
        // same object -> returns true
        assertTrue(ALICE.isSameAthlete(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameAthlete(null));

        // same name and sport, all other attributes different -> returns true
        Athlete editedAlice = new AthleteBuilder(ALICE).withAge("18").withPhone("99999999").withEmail("bob@example.com")
                .build();
        assertTrue(ALICE.isSameAthlete(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new AthleteBuilder(ALICE).withName("Bob").build();
        assertFalse(ALICE.isSameAthlete(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Athlete editedBenson = new AthleteBuilder(BENSON).withName("benson").build();
        assertFalse(BENSON.isSameAthlete(editedBenson));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Athlete aliceCopy = new AthleteBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BENSON));

        // different name -> returns false
        Athlete editedAlice = new AthleteBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new AthleteBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new AthleteBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));


    }

    @Test
    public void toStringMethod() {
        String expected = Athlete.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", sport=" + ALICE.getSport()
                + ", age=" + ALICE.getAge()
                + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void getTotalContractAmount_withContracts_returnsCorrectTotal() {
        Athlete athlete = new AthleteBuilder().build();

        Contract contract1 = new ContractBuilder()
                .withAthlete(athlete)
                .withAmount(1000000L)
                .build();
        Contract contract2 = new ContractBuilder()
                .withAthlete(athlete)
                .withAmount(2000000L)
                .build();

        ObservableList<Contract> contracts = FXCollections.observableArrayList(contract1, contract2);

        long totalAmount = athlete.getTotalContractAmount(contracts);
        assertEquals(3000000L, totalAmount);
    }
}
