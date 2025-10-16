package seedu.address.logic.commands.contract;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.Date8;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationName;
import seedu.address.model.organization.ReadOnlyOrganizationList;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * Tests for {@link AddContractCommand}.
 */
public class AddContractCommandTest {

    private static final Name ATHLETE_NAME = new Name("Lionel Messi");
    private static final Sport SPORT = new Sport("Football");
    private static final OrganizationName ORG_NAME = new OrganizationName("Inter Miami");
    private static final Date8 START = new Date8("01012024");
    private static final Date8 END = new Date8("01012025");
    private static final Amount AMT = new Amount(5_000_000);

    private Athlete validAthlete() {
        return new AthleteBuilder().withName(ATHLETE_NAME.fullName).build();
    }

    private Organization validOrganization() {
        return new OrganizationBuilder().withName(ORG_NAME.fullOrganizationName).build();
    }

    // -------------------------------------------------------------------------
    // Constructor null checks
    // -------------------------------------------------------------------------
    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(null, SPORT, ORG_NAME, START, END, AMT));
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(ATHLETE_NAME, null, ORG_NAME, START, END, AMT));
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(ATHLETE_NAME, SPORT, null, START, END, AMT));
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, null, END, AMT));
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, null, AMT));
        assertThrows(NullPointerException.class, () ->
            new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, null));
    }

    @Test
    public void execute_athleteMissing_throwsCommandException() {
        ModelStubAcceptingContractAdded modelStub = new ModelStubAcceptingContractAdded();
        modelStub.addOrganization(validOrganization());

        AddContractCommand cmd = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, AMT);
        assertThrows(CommandException.class, () -> cmd.execute(modelStub));
        assertTrue(modelStub.contractsAdded.isEmpty());
    }

    @Test
    public void execute_organizationMissing_throwsCommandException() {
        ModelStubAcceptingContractAdded modelStub = new ModelStubAcceptingContractAdded();
        modelStub.addAthlete(validAthlete());

        AddContractCommand cmd = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, AMT);
        assertThrows(CommandException.class, () -> cmd.execute(modelStub));
        assertTrue(modelStub.contractsAdded.isEmpty());
    }

    @Test
    public void execute_startAfterEnd_throwsCommandException() {
        ModelStubAcceptingContractAdded modelStub = new ModelStubAcceptingContractAdded();
        modelStub.addAthlete(validAthlete());
        modelStub.addOrganization(validOrganization());

        Date8 invalidStart = new Date8("02012025");
        Date8 invalidEnd = new Date8("01012025");
        AddContractCommand cmd = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME,
                invalidStart, invalidEnd, AMT);

        assertThrows(CommandException.class,
                String.format(AddContractCommand.MESSAGE_INVALID_DATE_RANGE, invalidStart, invalidEnd),
                () -> cmd.execute(modelStub));
        assertTrue(modelStub.contractsAdded.isEmpty());
    }

    // -------------------------------------------------------------------------
    // equals() and toString()
    // -------------------------------------------------------------------------
    @Test
    public void equals() {
        AddContractCommand a = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, AMT);
        AddContractCommand same = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, AMT);
        AddContractCommand diff =
                new AddContractCommand(new Name("Kylian Mbappe"), SPORT, ORG_NAME, START, END, AMT);

        assertTrue(a.equals(a));
        assertTrue(a.equals(same));
        assertFalse(a.equals(null));
        assertFalse(a.equals(1));
        assertFalse(a.equals(diff));
    }

    @Test
    public void toStringMethod() {
        AddContractCommand a = new AddContractCommand(ATHLETE_NAME, SPORT, ORG_NAME, START, END, AMT);
        String s = a.toString();

        for (String mustContain : Arrays.asList(
                "athleteName=" + ATHLETE_NAME,
                "sport=" + SPORT,
                "organizationName=" + ORG_NAME,
                "startDate=" + START,
                "endDate=" + END,
                "amount=" + AMT)) {
            assertTrue(s.contains(mustContain), "toString should contain: " + mustContain);
        }
    }

    // -------------------------------------------------------------------------
    // Minimal model stubs
    // -------------------------------------------------------------------------
    private class ModelStub implements Model {
        @Override
        public ReadOnlyOrganizationList getOrganizationList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public boolean hasOrganization(Organization organization) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void deleteOrganization(Organization target) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void addOrganization(Organization organization) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void setOrganization(Organization target, Organization editedOrganization) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ObservableList<Organization> getFilteredOrganizationList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void updateFilteredOrganizationList(Predicate<Organization> predicate) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ReadOnlyContractList getContractList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public boolean hasContract(Contract contract) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void addContract(Contract contract) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void deleteContract(Contract target) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ObservableList<Contract> getFilteredContractList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void updateFilteredContractList(Predicate<Contract> predicate) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void addAthlete(Athlete athlete) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ReadOnlyAthleteList getAthleteList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public boolean hasAthlete(Athlete athlete) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void deleteAthlete(Athlete target) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ObservableList<Athlete> getFilteredAthleteList() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void updateFilteredAthleteList(Predicate<Athlete> predicate) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("Should not be called");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("Should not be called");
        }
    }

    private class ModelStubAcceptingContractAdded extends ModelStub {
        private final AddressBook backing = new AddressBook();
        private final List<Contract> contractsAdded = new ArrayList<>();
        private boolean duplicateOnHasContract = false;

        @Override
        public boolean hasContract(Contract contract) {
            return duplicateOnHasContract;
        }

        @Override
        public void addContract(Contract contract) {
            requireNonNull(contract);
            contractsAdded.add(contract);
        }

        @Override
        public void addAthlete(Athlete athlete) {
            requireNonNull(athlete);
            backing.addAthlete(athlete);
        }

        @Override
        public void addOrganization(Organization organization) {
            requireNonNull(organization);
            backing.addOrganization(organization);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return backing;
        }

        public void setDuplicateOnHasContract(boolean duplicateOnHasContract) {
            this.duplicateOnHasContract = duplicateOnHasContract;
        }

        public boolean isDuplicateOnHasContract() {
            return duplicateOnHasContract;
        }
    }
}
