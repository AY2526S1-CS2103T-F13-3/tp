package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationList;
import seedu.address.model.organization.ReadOnlyOrganizationList;

/**
 * Represents the in-memory model of all app data using a single AddressBook:
 * - Athletes, Contracts, Organizations
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    // ---- Single AddressBook domain ----
    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final AthleteList athleteList;
    private final ContractList contractList;
    private final OrganizationList organizationList;

    // filtered views
    private final FilteredList<Organization> filteredOrganizations; // placeholder until wired
    private final FilteredList<Contract> filteredContracts;
    private final FilteredList<Athlete> filteredAthletes;
    private Predicate<Organization> organizationPredicate = PREDICATE_SHOW_ALL_ORGANIZATIONS;
    private Predicate<Contract> contractPredicate = PREDICATE_SHOW_ALL_CONTRACTS;
    private Predicate<Athlete> athletePredicate = PREDICATE_SHOW_ALL_ATHLETES;

    // =====================================================================================
    // Constructors
    // =====================================================================================

    /**
     * Constructs a {@code ModelManager} with the given address book and user preferences.
     *
     * @param addressBook the address book data to initialize from
     * @param userPrefs   the user preferences to initialize from
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs,
                        ReadOnlyAthleteList athleteList, ReadOnlyContractList contractList,
                        ReadOnlyOrganizationList organizationList) {
        requireAllNonNull(addressBook, userPrefs, athleteList, contractList, organizationList);

        logger.fine("Initializing with address book: " + addressBook
            + " | user prefs: " + userPrefs);
        logger.fine("Initializing athlete list: " + athleteList);
        logger.fine("Initializing contract list: " + contractList);
        logger.fine("Initializing organization list: " + organizationList);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.athleteList = new AthleteList(athleteList);
        this.contractList = new ContractList(contractList);
        this.organizationList = new OrganizationList(organizationList);

        // Populate addressBook with the loaded athletes
        for (Athlete athlete : athleteList.getAthleteList()) {
            this.addressBook.addAthlete(athlete);
        }

        // Populate addressBook with the loaded contracts
        for (Contract contract : contractList.getContractList()) {
            this.addressBook.addContract(contract);
        }

        // Populate addressBook with the loaded organizations
        for (Organization organization : organizationList.getOrganizationList()) {
            this.addressBook.addOrganization(organization);
        }

        this.filteredContracts = new FilteredList<>(this.addressBook.getContractList());
        this.filteredOrganizations = new FilteredList<>(this.addressBook.getOrganizationList());
        this.filteredAthletes = new FilteredList<>(this.addressBook.getAthleteList());
        reapplyPredicate(filteredContracts, contractPredicate);
        reapplyPredicate(filteredOrganizations, organizationPredicate);
        reapplyPredicate(filteredAthletes, athletePredicate);
    }

    /** Constructs a {@code ModelManager} with empty data. */
    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new AthleteList(), new ContractList(), new OrganizationList());
    }

    // =====================================================================================
    // UserPrefs (AddressBook)
    // =====================================================================================

    /** Returns the user preferences. */
    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    /** Sets the user preferences. */
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    /** Returns the GUI settings from user preferences. */
    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    /** Sets the GUI settings in user preferences. */
    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    /** Returns the address book file path from user preferences. */
    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    /** Sets the address book file path in user preferences. */
    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // =====================================================================================
    // AddressBook (Contracts / Orgs / Athletes)
    // =====================================================================================

    /** Replaces the contents of the AddressBook with the given data. */
    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    /** Returns the AddressBook. */
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // ---- Contracts ----

    @Override
    public ReadOnlyContractList getContractList() {
        // Return a read-only view of the addressBook's contract list (where changes actually happen)
        return new ReadOnlyContractList() {
            @Override
            public ObservableList<Contract> getContractList() {
                return addressBook.getContractList();
            }
        };
    }

    /** Returns true if a contract with the same identity exists in the AddressBook. */
    @Override
    public boolean hasContract(Contract contract) {
        requireNonNull(contract);
        return addressBook.hasContract(contract);
    }

    /** Adds a contract to the AddressBook. Updates filtered view. */
    @Override
    public void addContract(Contract contract) {
        requireNonNull(contract);
        addressBook.addContract(contract);
        reapplyPredicate(filteredContracts, contractPredicate);
        reapplyPredicate(filteredAthletes, athletePredicate);
        reapplyPredicate(filteredOrganizations, organizationPredicate);
    }

    /** Deletes a contract from the AddressBook. */
    @Override
    public void deleteContract(Contract target) {
        requireNonNull(target);
        addressBook.removeContract(target);
        reapplyPredicate(filteredContracts, contractPredicate);
        reapplyPredicate(filteredAthletes, athletePredicate);
        reapplyPredicate(filteredOrganizations, organizationPredicate);
    }

    /** Returns the filtered contract list. */
    @Override
    public ObservableList<Contract> getFilteredContractList() {
        return filteredContracts;
    }

    /** Updates the filtered contract list using the given predicate. */
    @Override
    public void updateFilteredContractList(Predicate<Contract> predicate) {
        requireNonNull(predicate);
        contractPredicate = predicate;
        reapplyPredicate(filteredContracts, contractPredicate);
    }

    // ---- Organizations ----

    @Override
    public ReadOnlyOrganizationList getOrganizationList() {
        // Return a read-only view of the live organization list from addressBook
        return new ReadOnlyOrganizationList() {
            @Override
            public ObservableList<Organization> getOrganizationList() {
                return addressBook.getOrganizationList();
            }
        };
    }

    /** Returns true if an organization with the same identity exists in the AddressBook. */
    @Override
    public boolean hasOrganization(Organization organization) {
        requireNonNull(organization);
        return addressBook.hasOrganization(organization);
    }

    /** Deletes an organization from the AddressBook. */
    @Override
    public void deleteOrganization(Organization target) {
        requireNonNull(target);
        addressBook.removeOrganization(target);
    }

    /** Adds an organization to the AddressBook and updates filtered view. */
    @Override
    public void addOrganization(Organization organization) {
        requireNonNull(organization);
        addressBook.addOrganization(organization);
        reapplyPredicate(filteredOrganizations, organizationPredicate);
    }

    /** Replaces an existing organization with an edited organization. */
    @Override
    public void setOrganization(Organization target, Organization editedOrganization) {
        requireAllNonNull(target, editedOrganization);
        addressBook.setOrganization(target, editedOrganization);
    }

    /** Returns the filtered organization list. */
    @Override
    public ObservableList<Organization> getFilteredOrganizationList() {
        return filteredOrganizations;
    }

    /** Updates the filtered organization list using the given predicate. */
    @Override
    public void updateFilteredOrganizationList(Predicate<Organization> predicate) {
        requireNonNull(predicate);
        organizationPredicate = predicate;
        reapplyPredicate(filteredOrganizations, organizationPredicate);
    }

    // ---- Athletes ----

    @Override
    public ReadOnlyAthleteList getAthleteList() {
        // Return a read-only view of the addressBook's athlete list (where changes actually happen)
        return new ReadOnlyAthleteList() {
            @Override
            public ObservableList<Athlete> getAthleteList() {
                return addressBook.getAthleteList();
            }
        };
    }

    /** Returns true if an athlete with the same identity exists in the AddressBook. */
    @Override
    public boolean hasAthlete(Athlete athlete) {
        requireNonNull(athlete);
        return addressBook.hasAthlete(athlete);
    }

    /** Deletes an athlete from the AddressBook. */
    @Override
    public void deleteAthlete(Athlete target) {
        addressBook.removeAthlete(target);
    }

    /** Adds an athlete to the AddressBook and updates filtered view. */
    @Override
    public void addAthlete(Athlete athlete) {
        addressBook.addAthlete(athlete);
        reapplyPredicate(filteredAthletes, athletePredicate);
    }

    /** Returns the filtered athlete list. */
    @Override
    public ObservableList<Athlete> getFilteredAthleteList() {
        return filteredAthletes;
    }

    /** Updates the filtered athlete list using the given predicate. */
    @Override
    public void updateFilteredAthleteList(Predicate<Athlete> predicate) {
        requireNonNull(predicate);
        athletePredicate = predicate;
        reapplyPredicate(filteredAthletes, athletePredicate);
    }

    private <T> void reapplyPredicate(FilteredList<T> list, Predicate<T> predicate) {
        list.setPredicate(predicate == null ? null : predicate::test);
    }

    @Override
    public boolean hasActiveFilters() {
        return getFilteredAthleteList().size() < getAddressBook().getAthleteList().size()
                || getFilteredOrganizationList().size() < getAddressBook().getOrganizationList().size()
                || getFilteredContractList().size() < getContractList().getContractList().size();
    }

    @Override
    public void clearAllFilters() {
        updateFilteredAthleteList(PREDICATE_SHOW_ALL_ATHLETES);
        updateFilteredOrganizationList(PREDICATE_SHOW_ALL_ORGANIZATIONS);
        updateFilteredContractList(PREDICATE_SHOW_ALL_CONTRACTS);
    }

    // =====================================================================================
    // Equality
    // =====================================================================================

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredContracts.equals(otherModelManager.filteredContracts)
                && filteredAthletes.equals(otherModelManager.filteredAthletes);
    }
}
