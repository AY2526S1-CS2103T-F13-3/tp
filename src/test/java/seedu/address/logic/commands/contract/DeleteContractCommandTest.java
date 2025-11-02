package seedu.address.logic.commands.contract;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
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
 * Tests for {@link DeleteContractCommand}.
 */
public class DeleteContractCommandTest {

    private static final Name ATHLETE_NAME = new Name("Lionel Messi");
    private static final OrganizationName ORG_NAME = new OrganizationName("Inter Miami");
    private static final Sport SPORT = new Sport("Football");
    private static final Date8 START = new Date8("01012024");
    private static final Date8 END = new Date8("01012025");
    private static final Amount AMOUNT = new Amount(5_000_000);

    private Athlete validAthlete() {
        return new AthleteBuilder().withName(ATHLETE_NAME.fullName).build();
    }

    private Organization validOrganization() {
        return new OrganizationBuilder().withName(ORG_NAME.fullOrganizationName).build();
    }

    @Test
    public void execute_notFound_throwsCommandException() {
        ModelStubWithContracts modelStub = new ModelStubWithContracts();
        // make names resolvable but no matching contract in filtered list
        modelStub.addAthlete(validAthlete());
        modelStub.addOrganization(validOrganization());

        DeleteContractCommand cmd = new DeleteContractCommand(ATHLETE_NAME, ORG_NAME, START, END, SPORT, AMOUNT);
    }

    // -------------------------------------------------------------------------
    // equals / toString
    // -------------------------------------------------------------------------
    @Test
    public void equals() {
        DeleteContractCommand a = new DeleteContractCommand(ATHLETE_NAME, ORG_NAME, START, END, SPORT, AMOUNT);
        DeleteContractCommand same = new DeleteContractCommand(ATHLETE_NAME, ORG_NAME, START, END, SPORT, AMOUNT);
        DeleteContractCommand diff =
                new DeleteContractCommand(new Name("Kylian Mbappe"), ORG_NAME, START, END, SPORT, AMOUNT);

        assertTrue(a.equals(a));
        assertTrue(a.equals(same));
        assertFalse(a.equals(null));
        assertFalse(a.equals(1));
        assertFalse(a.equals(diff));
    }

    @Test
    public void toStringMethod() {
        DeleteContractCommand a = new DeleteContractCommand(ATHLETE_NAME, ORG_NAME, START, END, SPORT, AMOUNT);
        String s = a.toString();
        for (String mustContain : Arrays.asList(
                "athleteName=" + ATHLETE_NAME,
                "organizationName=" + ORG_NAME,
                "startDate=" + START,
                "endDate=" + END)) {
            assertTrue(s.contains(mustContain), "toString should contain: " + mustContain);
        }
    }

    // -------------------------------------------------------------------------
    // minimal Model stubs
    // -------------------------------------------------------------------------
    private static class ModelStub implements Model {
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

        @Override
        public boolean hasActiveFilters() {
            throw new AssertionError("Should not be called");
        }

        @Override
        public void clearAllFilters() {
            throw new AssertionError("Should not be called");
        }
    }

    private static class ModelStubWithContracts extends ModelStub {
        private final AddressBook backing = new AddressBook();
        private final List<Contract> filteredContracts = new ArrayList<>();
        private final List<Contract> deleted = new ArrayList<>();

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
        public void addContract(Contract contract) {
            requireNonNull(contract);
            filteredContracts.add(contract);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return backing;
        }

        @Override
        public ObservableList<Contract> getFilteredContractList() {
            return javafx.collections.FXCollections.observableList(filteredContracts);
        }

        @Override
        public void deleteContract(Contract target) {
            deleted.add(target);
            filteredContracts.remove(target);
        }

        public List<Contract> getDeleted() {
            return deleted;
        }
    }
}
