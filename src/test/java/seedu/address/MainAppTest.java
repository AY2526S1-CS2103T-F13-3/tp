package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.ReadOnlyOrganizationList;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonAthleteListStorage;
import seedu.address.storage.JsonContractListStorage;
import seedu.address.storage.JsonOrganizationListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;

/**
 * Tests for {@link MainApp}. These tests focus on the non-GUI logic so that they
 * can run in a headless continuous integration environment.
 */
class MainAppTest {

    @TempDir
    Path tempDir;

    @Test
    void initConfig_existingConfigFile_readsConfig() throws Exception {
        MainApp mainApp = new MainApp();
        Path configPath = tempDir.resolve("customConfig.json");

        Config expectedConfig = new Config();
        expectedConfig.setLogLevel(Level.WARNING);
        expectedConfig.setUserPrefsFilePath(tempDir.resolve("customPrefs.json"));
        ConfigUtil.saveConfig(expectedConfig, configPath);

        Config actualConfig = mainApp.initConfig(configPath);

        assertEquals(expectedConfig, actualConfig);
        assertTrue(Files.exists(configPath));
    }

    @Test
    void initConfig_missingConfigFile_createsDefaultConfig() throws Exception {
        MainApp mainApp = new MainApp();
        Path configPath = tempDir.resolve("missingConfig.json");

        Config actualConfig = mainApp.initConfig(configPath);

        assertEquals(new Config(), actualConfig);
        assertTrue(Files.exists(configPath));
    }

    @Test
    void initConfig_corruptedConfigFile_fallsBackToDefault() throws Exception {
        MainApp mainApp = new MainApp();
        Path configPath = tempDir.resolve("corruptedConfig.json");
        Files.writeString(configPath, "{not valid json");

        Config actualConfig = mainApp.initConfig(configPath);

        assertEquals(new Config(), actualConfig);
        assertTrue(Files.exists(configPath));
    }

    @Test
    void initPrefs_existingPrefsFile_readsPrefs() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("userPrefs.json");
        UserPrefs expectedPrefs = new UserPrefs();
        expectedPrefs.setGuiSettings(new GuiSettings(800, 600, 20, 40));
        expectedPrefs.setAddressBookFilePath(tempDir.resolve("addressBook.json"));

        JsonUserPrefsStorage prefsStorage = new JsonUserPrefsStorage(prefsPath);
        prefsStorage.saveUserPrefs(expectedPrefs);

        UserPrefs actualPrefs = mainApp.initPrefs(prefsStorage);

        assertEquals(expectedPrefs, actualPrefs);
        assertTrue(Files.exists(prefsPath));
    }

    @Test
    void initPrefs_corruptedPrefsFile_returnsDefaultPrefs() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("userPrefs.json");
        Files.writeString(prefsPath, "{not valid json");

        UserPrefs actualPrefs = mainApp.initPrefs(new JsonUserPrefsStorage(prefsPath));

        assertEquals(new UserPrefs(), actualPrefs);
        assertTrue(Files.exists(prefsPath));
    }

    @Test
    void initPrefs_missingPrefsFile_createsDefaults() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("userPrefs.json");
        UserPrefsStorage storage = new JsonUserPrefsStorage(prefsPath);

        UserPrefs actualPrefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), actualPrefs);
        assertTrue(Files.exists(prefsPath));
    }

    @Test
    void start_invokesUiStart() {
        MainApp mainApp = new MainApp();
        UiStub uiStub = new UiStub();
        mainApp.ui = uiStub;

        mainApp.start(null);

        assertTrue(uiStub.startCalled);
        assertNull(uiStub.receivedStage);
    }

    @Test
    void stop_savesUserPrefs() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("prefs.json");

        Storage storage = new seedu.address.storage.StorageManager(
                new JsonAddressBookStorage(tempDir.resolve("addressBook.json")),
                new JsonUserPrefsStorage(prefsPath),
                new JsonAthleteListStorage(tempDir.resolve("athletes.json")),
                new JsonContractListStorage(tempDir.resolve("contracts.json"),
                    tempDir.resolve("athletes.json"), tempDir.resolve("organizations.json")),
                new JsonOrganizationListStorage(tempDir.resolve("organizations.json")));

        mainApp.storage = storage;
        mainApp.model = new ModelManager(
                new AddressBook(),
                new UserPrefs(),
                SampleDataUtil.getEmptyAthleteList(),
                SampleDataUtil.getEmptyContractList(),
                SampleDataUtil.getEmptyOrganizationList());

        assertFalse(Files.exists(prefsPath));
        mainApp.stop();
        assertTrue(Files.exists(prefsPath));
    }

    @Test
    void initModelManager_missingDataFiles_returnsEmptyCollections() throws Exception {
        MainApp mainApp = new MainApp();

        Storage storage = new seedu.address.storage.StorageManager(
                new JsonAddressBookStorage(tempDir.resolve("addressBook.json")),
                new JsonUserPrefsStorage(tempDir.resolve("prefs.json")),
                new JsonAthleteListStorage(tempDir.resolve("athletes.json")),
                new JsonContractListStorage(tempDir.resolve("contracts.json"),
                    tempDir.resolve("athletes.json"), tempDir.resolve("organizations.json")),
                new JsonOrganizationListStorage(tempDir.resolve("organizations.json")));

        UserPrefs userPrefs = new UserPrefs();

        Model model = invokeInitModelManager(mainApp, storage, userPrefs);

        assertTrue(model.getAthleteList().getAthleteList().isEmpty());
        assertTrue(model.getContractList().getContractList().isEmpty());
        assertTrue(model.getOrganizationList().getOrganizationList().isEmpty());
    }

    @Test
    void initModelManager_storageThrowsDataLoadingException_returnsEmptyCollections() throws Exception {
        MainApp mainApp = new MainApp();
        Path basePath = tempDir.resolve("storage");
        Files.createDirectories(basePath);

        Storage throwingStorage = new ThrowingStorageStub(basePath);
        UserPrefs userPrefs = new UserPrefs();

        Model model = invokeInitModelManager(mainApp, throwingStorage, userPrefs);

        assertTrue(model.getAthleteList().getAthleteList().isEmpty());
        assertTrue(model.getContractList().getContractList().isEmpty());
        assertTrue(model.getOrganizationList().getOrganizationList().isEmpty());
    }

    private Model invokeInitModelManager(MainApp mainApp, Storage storage, ReadOnlyUserPrefs userPrefs)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MainApp.class.getDeclaredMethod("initModelManager", Storage.class, ReadOnlyUserPrefs.class);
        method.setAccessible(true);
        return (Model) method.invoke(mainApp, storage, userPrefs);
    }

    private static class UiStub implements Ui {
        private boolean startCalled;
        private Stage receivedStage;

        @Override
        public void start(Stage primaryStage) {
            startCalled = true;
            receivedStage = primaryStage;
        }
    }

    /**
     * Storage stub that simulates {@link DataLoadingException}s for each data type.
     */
    private static class ThrowingStorageStub implements Storage {
        private final Path basePath;

        private ThrowingStorageStub(Path basePath) {
            this.basePath = basePath;
        }

        @Override
        public Path getUserPrefsFilePath() {
            return basePath.resolve("prefs.json");
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            throw new DataLoadingException(new IOException("user prefs error"));
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAddressBookFilePath() {
            return basePath.resolve("addressBook.json");
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAthleteListFilePath() {
            return basePath.resolve("athletes.json");
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList() throws DataLoadingException {
            throw new DataLoadingException(new IOException("athlete list error"));
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList(Path filePath) throws DataLoadingException {
            throw new DataLoadingException(new IOException("athlete list error"));
        }

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athleteList) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athleteList, Path filePath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getContractListFilePath() {
            return basePath.resolve("contracts.json");
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList() throws DataLoadingException {
            throw new DataLoadingException(new IOException("contract list error"));
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList(Path filePath) throws DataLoadingException {
            throw new DataLoadingException(new IOException("contract list error"));
        }

        @Override
        public void saveContractList(ReadOnlyContractList contracts) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveContractList(ReadOnlyContractList contracts, Path filePath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getOrganizationListFilePath() {
            return basePath.resolve("organizations.json");
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList() throws DataLoadingException {
            throw new DataLoadingException(new IOException("organization list error"));
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList(Path filePath) throws DataLoadingException {
            throw new DataLoadingException(new IOException("organization list error"));
        }

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations, Path filePath) {
            throw new UnsupportedOperationException();
        }
    }
}
