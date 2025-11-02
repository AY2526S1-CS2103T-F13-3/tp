package seedu.address.logic.commands.athlete;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.athlete.TypicalAthletes.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.AthleteMessages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.ReadOnlyOrganizationList;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * Tests for {@link AddAthleteCommand}.
 */
public class AddAthleteCommandTest {

    @Test
    public void constructor_nullAthlete_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddAthleteCommand(null));
    }

    @Test
    public void execute_athleteAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingAthleteAdded modelStub = new ModelStubAcceptingAthleteAdded();
        Athlete validAthlete = new AthleteBuilder().build();

        CommandResult commandResult = new AddAthleteCommand(validAthlete).execute(modelStub);

        assertEquals(
                String.format(AddAthleteCommand.MESSAGE_SUCCESS, AthleteMessages.format(validAthlete)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validAthlete), modelStub.athleteAdded);
    }

    @Test
    public void execute_duplicateAthlete_throwsCommandException() {
        Athlete validAthlete = new AthleteBuilder().build();
        AddAthleteCommand addAthleteCommand = new AddAthleteCommand(validAthlete);
        ModelStub modelStub = new ModelStubWithAthlete(validAthlete);

        assertThrows(
                CommandException.class,
                AddAthleteCommand.MESSAGE_DUPLICATE_ATHLETE, () ->
                addAthleteCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Athlete alice = new AthleteBuilder().withName("Alice").build();
        Athlete bob = new AthleteBuilder().withName("Bob").build();
        AddAthleteCommand addAliceCommand = new AddAthleteCommand(alice);
        AddAthleteCommand addBobCommand = new AddAthleteCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddAthleteCommand addAliceCommandCopy = new AddAthleteCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different athlete -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddAthleteCommand addAthleteCommand = new AddAthleteCommand(ALICE);
        String expected = AddAthleteCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addAthleteCommand.toString());
    }

    /**
     * Default model stub where all methods throw by default.
     */
    private class ModelStub implements Model {

        // ===== Organization =====
        @Override
        public ReadOnlyOrganizationList getOrganizationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOrganization(Organization organization) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteOrganization(Organization target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOrganization(Organization organization) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setOrganization(Organization target, Organization editedOrganization) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Organization> getFilteredOrganizationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOrganizationList(Predicate<Organization> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        // ===== Contracts =====
        @Override
        public ReadOnlyContractList getContractList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasContract(Contract contract) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addContract(Contract contract) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteContract(Contract target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Contract> getFilteredContractList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredContractList(Predicate<Contract> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        // ===== File/prefs/gui =====
        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        // ===== Athletes =====
        @Override
        public ReadOnlyAthleteList getAthleteList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAthlete(Athlete athlete) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasAthlete(Athlete athlete) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAthlete(Athlete target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Athlete> getFilteredAthleteList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAthleteList(Predicate<Athlete> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        // ===== AddressBook =====
        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasActiveFilters() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearAllFilters() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * Model stub containing a single athlete.
     */
    private class ModelStubWithAthlete extends ModelStub {
        private final Athlete athlete;

        ModelStubWithAthlete(Athlete athlete) {
            requireNonNull(athlete);
            this.athlete = athlete;
        }

        @Override
        public boolean hasAthlete(Athlete athlete) {
            requireNonNull(athlete);
            return this.athlete.isSameAthlete(athlete);
        }
    }

    /**
     * Model stub that always accepts any athlete added.
     */
    private class ModelStubAcceptingAthleteAdded extends ModelStub {
        final ArrayList<Athlete> athleteAdded = new ArrayList<>();

        @Override
        public boolean hasAthlete(Athlete athlete) {
            requireNonNull(athlete);
            return athleteAdded.stream().anyMatch(athlete::isSameAthlete);
        }

        @Override
        public void addAthlete(Athlete athlete) {
            requireNonNull(athlete);
            athleteAdded.add(athlete);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public boolean hasActiveFilters() {
            return false; // No filters active in test
        }
    }
}
