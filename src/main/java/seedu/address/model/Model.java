package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.ReadOnlyOrganizationList;

/**
 * The unified API of the Model component.
 */
public interface Model {
    Predicate<Organization> PREDICATE_SHOW_ALL_ORGANIZATIONS = unused -> true;
    Predicate<Athlete> PREDICATE_SHOW_ALL_ATHLETES = unused -> true;
    Predicate<Contract> PREDICATE_SHOW_ALL_CONTRACTS = unused -> true;

    /** Returns the user preferences. */
    ReadOnlyUserPrefs getUserPrefs();
    /** Replaces user preferences data with {@code userPrefs}. */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);
    /** Returns the GUI settings. */
    GuiSettings getGuiSettings();
    /** Sets the GUI settings. */
    void setGuiSettings(GuiSettings guiSettings);
    /** Returns the address book file path. */
    Path getAddressBookFilePath();
    /** Sets the address book file path. */
    void setAddressBookFilePath(Path addressBookFilePath);

    /** Returns the AddressBook. */
    ReadOnlyAddressBook getAddressBook();
    /** Replaces address book data with {@code addressBook}. */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the OrganizationList. */
    ReadOnlyOrganizationList getOrganizationList();
    /** Returns true if an organization with the same identity exists. */
    boolean hasOrganization(Organization organization);
    /** Deletes the given organization. */
    void deleteOrganization(Organization target);
    /** Adds the given organization. */
    void addOrganization(Organization organization);
    /** Replaces the given organization with an edited one. */
    void setOrganization(Organization target, Organization editedOrganization);
    /** Returns an unmodifiable view of the filtered organization list. */
    ObservableList<Organization> getFilteredOrganizationList();
    /** Updates the filtered organization list using the given predicate. */
    void updateFilteredOrganizationList(Predicate<Organization> predicate);

    /** Returns the AthleteList. */
    ReadOnlyAthleteList getAthleteList();
    /** Returns true if an athlete with the same identity exists. */
    boolean hasAthlete(Athlete athlete);
    /** Deletes the given athlete. */
    void deleteAthlete(Athlete target);
    /** Adds the given athlete. */
    void addAthlete(Athlete athlete);
    /** Returns an unmodifiable view of the filtered athlete list. */
    ObservableList<Athlete> getFilteredAthleteList();
    /** Updates the filtered athlete list using the given predicate. */
    void updateFilteredAthleteList(Predicate<Athlete> predicate);

    /** Returns the ContractList. */
    ReadOnlyContractList getContractList();
    /** Returns true if a contract with the same identity exists. */
    boolean hasContract(Contract contract);
    /** Adds the given contract. */
    void addContract(Contract contract);
    /** Deletes the given contract. */
    void deleteContract(Contract target);
    /** Returns an unmodifiable view of the filtered contract list. */
    ObservableList<Contract> getFilteredContractList();
    /** Updates the filtered contract list using the given predicate. */
    void updateFilteredContractList(Predicate<Contract> predicate);

    boolean hasActiveFilters();
    void clearAllFilters();
}
