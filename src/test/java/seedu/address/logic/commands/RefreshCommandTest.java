package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
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

/**
 * Unit tests for {@link RefreshCommand}.
 */
public class RefreshCommandTest {

    @Test
    public void execute_refreshCommand_success() {
        ModelStubForRefresh modelStub = new ModelStubForRefresh();

        RefreshCommand command = new RefreshCommand();
        CommandResult result = command.execute(modelStub);

        // Verify feedback message
        assertEquals(RefreshCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        // Verify that all update methods were called
        assertTrue(modelStub.isAthleteListUpdated);
        assertTrue(modelStub.isOrganizationListUpdated);
        assertTrue(modelStub.isContractListUpdated);
    }

    @Test
    public void equals() {
        RefreshCommand refreshCommand1 = new RefreshCommand();
        RefreshCommand refreshCommand2 = new RefreshCommand();

        // same object -> true
        assertTrue(refreshCommand1.equals(refreshCommand1));

        // different instances but same class -> true
        assertTrue(refreshCommand1.equals(refreshCommand2));

        // null -> false
        assertFalse(refreshCommand1.equals(null));

        // different type -> false
        assertFalse(refreshCommand1.equals("refresh"));
    }

    /**
     * Minimal Model stub where all methods throw AssertionError.
     */
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

    /**
     * Minimal stub for RefreshCommand that tracks update method calls.
     */
    private static class ModelStubForRefresh extends ModelStub {
        private boolean isAthleteListUpdated = false;
        private boolean isOrganizationListUpdated = false;
        private boolean isContractListUpdated = false;

        @Override
        public void updateFilteredAthleteList(Predicate<Athlete> predicate) {
            isAthleteListUpdated = true;
        }

        @Override
        public void updateFilteredOrganizationList(Predicate<Organization> predicate) {
            isOrganizationListUpdated = true;
        }

        @Override
        public void updateFilteredContractList(Predicate<Contract> predicate) {
            isContractListUpdated = true;
        }

        @Override
        public ObservableList<Athlete> getFilteredAthleteList() {
            return javafx.collections.FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Organization> getFilteredOrganizationList() {
            return javafx.collections.FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Contract> getFilteredContractList() {
            return javafx.collections.FXCollections.observableArrayList();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public ReadOnlyAthleteList getAthleteList() {
            return new ReadOnlyAthleteList() {
                @Override
                public ObservableList<Athlete> getAthleteList() {
                    return javafx.collections.FXCollections.observableArrayList();
                }
            };
        }

        @Override
        public ReadOnlyOrganizationList getOrganizationList() {
            return new ReadOnlyOrganizationList() {
                @Override
                public ObservableList<Organization> getOrganizationList() {
                    return javafx.collections.FXCollections.observableArrayList();
                }
            };
        }

        @Override
        public ReadOnlyContractList getContractList() {
            return new ReadOnlyContractList() {
                @Override
                public ObservableList<Contract> getContractList() {
                    return javafx.collections.FXCollections.observableArrayList();
                }
            };
        }

    }
}
