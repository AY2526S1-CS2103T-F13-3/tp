package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalOrganizations.getTypicalOrganizations;
import static seedu.address.testutil.athlete.TypicalAthletes.getTypicalAthletes;
import static seedu.address.testutil.contract.TypicalContracts.getTypicalContracts;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.UserPrefs;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.OrganizationList;
import seedu.address.model.organization.ReadOnlyOrganizationList;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonAthleteListStorage athleteListStorage = new JsonAthleteListStorage(getTempFilePath("athletes"));
        JsonContractListStorage contractListStorage = new JsonContractListStorage(getTempFilePath("contracts"),
            getTempFilePath("athletes"), getTempFilePath("organizations"));
        JsonOrganizationListStorage organizationListStorage =
                new JsonOrganizationListStorage(getTempFilePath("organizations"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage,
                athleteListStorage, contractListStorage, organizationListStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void athleteListReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAthleteListStorage} class.
         * More extensive testing of athlete list saving/reading is done in {@link JsonAthleteListStorageTest} class.
         */
        AthleteList original = new AthleteList();
        original.setAthletes(getTypicalAthletes());
        storageManager.saveAthleteList(original);
        ReadOnlyAthleteList retrieved = storageManager.readAthleteList().get();
        assertEquals(original.getAthleteList(), retrieved.getAthleteList());
    }

    @Test
    public void getAthleteListFilePath() {
        assertNotNull(storageManager.getAthleteListFilePath());
    }
    
    @Test
    public void getContractListFilePath() {
        assertNotNull(storageManager.getContractListFilePath());
    }

    @Test
    public void organizationListReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonOrganizationListStorage} class.
         * More extensive testing of organization list saving/reading is done in
         * {@link JsonOrganizationListStorageTest} class.
         */
        OrganizationList original = new OrganizationList();
        original.setOrganizations(getTypicalOrganizations());
        storageManager.saveOrganizationList(original);
        ReadOnlyOrganizationList retrieved = storageManager.readOrganizationList().get();
        assertEquals(original.getOrganizationList(), retrieved.getOrganizationList());
    }

    @Test
    public void getOrganizationListFilePath() {
        assertNotNull(storageManager.getOrganizationListFilePath());
    }

}
