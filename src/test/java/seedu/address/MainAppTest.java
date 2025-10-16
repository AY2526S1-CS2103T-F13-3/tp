package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.logic.Logic;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.OrganizationList;
import seedu.address.model.organization.ReadOnlyOrganizationList;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.contract.ContractBuilder;
import seedu.address.ui.Ui;

/**
 * Tests for {@link MainApp}.
 */
public class MainAppTest {

    @TempDir
    Path tempDir;

    @BeforeAll
    static void setupToolkit() throws Exception {
        FxTestHelper.ensureToolkitInitialized();
    }

    @Test
    public void initConfig_missingFile_createsDefaultConfig() throws Exception {
        MainApp mainApp = new MainApp();
        Path configPath = tempDir.resolve("config.json");

        Config config = mainApp.initConfig(configPath);

        assertEquals(new Config(), config);
        assertTrue(Files.exists(configPath));
        Optional<Config> persisted = ConfigUtil.readConfig(configPath);
        assertTrue(persisted.isPresent());
        assertEquals(new Config(), persisted.get());
    }

    @Test
    public void initConfig_invalidFile_usesDefaultConfig() throws Exception {
        MainApp mainApp = new MainApp();
        Path configPath = tempDir.resolve("badConfig.json");
        Files.writeString(configPath, "{ invalid json", StandardCharsets.UTF_8);

        Config config = mainApp.initConfig(configPath);

        assertEquals(new Config(), config);
        Optional<Config> persisted = ConfigUtil.readConfig(configPath);
        assertTrue(persisted.isPresent());
    }

    @Test
    public void initPrefs_missingFile_returnsDefaultPrefs() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("prefs.json");
        UserPrefsStorage storage = new JsonUserPrefsStorage(prefsPath);

        UserPrefs prefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), prefs);
        assertTrue(Files.exists(prefsPath));
        Optional<UserPrefs> persisted = storage.readUserPrefs();
        assertTrue(persisted.isPresent());
        assertEquals(new UserPrefs(), persisted.get());
    }

    @Test
    public void initPrefs_invalidFile_returnsDefaultPrefs() throws Exception {
        MainApp mainApp = new MainApp();
        Path prefsPath = tempDir.resolve("brokenPrefs.json");
        Files.writeString(prefsPath, "{ invalid json", StandardCharsets.UTF_8);
        UserPrefsStorage storage = new JsonUserPrefsStorage(prefsPath);

        UserPrefs prefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), prefs);
        Optional<UserPrefs> persisted = storage.readUserPrefs();
        assertTrue(persisted.isPresent());
    }

    @Test
    public void initModelManager_readsFromStorageAndFallsBack() {
        MainApp mainApp = new MainApp();
        AthleteList athletes = new AthleteList();
        athletes.addAthlete(new AthleteBuilder().build());
        ContractList contracts = new ContractList();
        contracts.addContract(new ContractBuilder().build());
        OrganizationList organizations = new OrganizationList();
        organizations.addOrganization(new OrganizationBuilder().build());

        Storage dataStorage = new StorageStubBuilder()
                .withAthletes(athletes)
                .withContracts(contracts)
                .withOrganizations(organizations)
                .build();

        Model populatedModel = invokeInitModelManager(mainApp, dataStorage, new UserPrefs());

        assertFalse(populatedModel.getFilteredAthleteList().isEmpty());
        assertFalse(populatedModel.getFilteredContractList().isEmpty());
        assertFalse(populatedModel.getFilteredOrganizationList().isEmpty());

        Storage fallbackStorage = new StorageStubBuilder()
                .withAthleteError()
                .withContractError()
                .withOrganizationError()
                .build();

        Model fallbackModel = invokeInitModelManager(mainApp, fallbackStorage, new UserPrefs());

        assertTrue(fallbackModel.getFilteredAthleteList().isEmpty());
        assertTrue(fallbackModel.getFilteredContractList().isEmpty());
        assertTrue(fallbackModel.getFilteredOrganizationList().isEmpty());
    }

    @Test
    public void start_invokesUiStart() throws Exception {
        TestableMainApp mainApp = new TestableMainApp();
        UiStub uiStub = new UiStub();
        mainApp.setUi(uiStub);

        Stage stage = FxTestHelper.callOnFxThread(Stage::new);
        FxTestHelper.runOnFxThreadAndWait(() -> mainApp.start(stage));

        assertTrue(uiStub.wasStarted());
        FxTestHelper.runOnFxThreadAndWait(stage::close);
    }

    @Test
    public void stop_savesUserPrefsAndHandlesFailures() {
        TestableMainApp mainApp = new TestableMainApp();
        Model model = new ModelManager(new AddressBook(), new UserPrefs(),
                SampleDataUtil.getEmptyAthleteList(), SampleDataUtil.getEmptyContractList(),
                SampleDataUtil.getEmptyOrganizationList());
        mainApp.setModel(model);

        RecordingStorage recordingStorage = new RecordingStorage();
        mainApp.setStorage(recordingStorage);
        mainApp.stop();
        assertNotNull(recordingStorage.getSavedPrefs());

        mainApp.setStorage(new FailingSaveStorage());
        mainApp.stop(); // should not throw
    }

    @Test
    public void init_runsFullInitializationPipeline() throws Exception {
        Path configPath = tempDir.resolve("full-init-config.json");
        Path prefsPath = tempDir.resolve("full-init-prefs.json");
        Path addressPath = tempDir.resolve("full-init-address.json");
        Path athletePath = tempDir.resolve("full-init-athletes.json");
        Path contractPath = tempDir.resolve("full-init-contracts.json");
        Path organizationPath = tempDir.resolve("full-init-organizations.json");

        FullInitMainApp app = new FullInitMainApp(configPath, prefsPath,
                addressPath, athletePath, contractPath, organizationPath);

        setApplicationParameters(app, Collections.singletonMap("config", configPath.toString()));

        app.init();

        assertNotNull(app.getConfig());
        assertNotNull(app.getStorage());
        assertNotNull(app.getModel());
        assertNotNull(app.getLogic());
        assertNotNull(app.getUi());
    }

    private Model invokeInitModelManager(MainApp mainApp, Storage storage, ReadOnlyUserPrefs userPrefs) {
        try {
            Method method = MainApp.class.getDeclaredMethod("initModelManager", Storage.class, ReadOnlyUserPrefs.class);
            method.setAccessible(true);
            return (Model) method.invoke(mainApp, storage, userPrefs);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e.getCause());
        }
    }

    private void setApplicationParameters(Application app, Map<String, String> namedParameters) {
        try {
            Class<?> implClass = Class.forName("com.sun.javafx.application.ParametersImpl");
            Constructor<?> targetConstructor = null;
            for (Constructor<?> candidate : implClass.getDeclaredConstructors()) {
                Class<?>[] parameterTypes = candidate.getParameterTypes();
                boolean matches = true;
                for (Class<?> type : parameterTypes) {
                    if (!List.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    targetConstructor = candidate;
                    break;
                }
            }
            if (targetConstructor == null) {
                throw new AssertionError("Unable to locate ParametersImpl constructor");
            }
            Constructor<?> constructor = implClass.getDeclaredConstructor(Map.class, String[].class);
            constructor.setAccessible(true);
            Object parameters = constructor.newInstance(new HashMap<>(namedParameters), new String[0]);
            Method registerMethod = implClass.getDeclaredMethod("registerParameters",
                    Application.class, Application.Parameters.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(null, app, parameters);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Test helper for interacting with the JavaFX toolkit.
     */
    private static final class FxTestHelper {
        private static final AtomicBoolean INITIALISED = new AtomicBoolean(false);
        private static final long TIMEOUT_SECONDS = 5;

        private FxTestHelper() {}

        static void ensureToolkitInitialized() throws Exception {
            if (INITIALISED.get()) {
                return;
            }
            synchronized (INITIALISED) {
                if (INITIALISED.get()) {
                    return;
                }
                SwingUtilities.invokeAndWait(JFXPanel::new);
                CompletableFuture<Void> readyFuture = new CompletableFuture<>();
                Platform.runLater(() -> {
                    Platform.setImplicitExit(false);
                    readyFuture.complete(null);
                });
                waitFor(readyFuture);
                INITIALISED.set(true);
            }
        }

        static void runOnFxThreadAndWait(Runnable runnable) {
            if (Platform.isFxApplicationThread()) {
                runnable.run();
                return;
            }
            CompletableFuture<Void> future = new CompletableFuture<>();
            Platform.runLater(() -> {
                try {
                    runnable.run();
                    future.complete(null);
                } catch (Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
            waitFor(future);
        }

        static <T> T callOnFxThread(Callable<T> callable) throws Exception {
            if (Platform.isFxApplicationThread()) {
                return callable.call();
            }
            CompletableFuture<T> future = new CompletableFuture<>();
            Platform.runLater(() -> {
                try {
                    future.complete(callable.call());
                } catch (Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
            return waitFor(future);
        }

        private static <T> T waitFor(CompletableFuture<T> future) {
            try {
                return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for JavaFX task", e);
            } catch (ExecutionException e) {
                throw new IllegalStateException("JavaFX task failed", e.getCause());
            } catch (TimeoutException e) {
                throw new IllegalStateException("Timed out waiting for JavaFX task", e);
            }
        }
    }

    /**
     * Storage stub builder for configuring different read behaviours.
     */
    private static final class StorageStubBuilder {
        private Optional<ReadOnlyAthleteList> athletes = Optional.empty();
        private Optional<ReadOnlyContractList> contracts = Optional.empty();
        private Optional<ReadOnlyOrganizationList> organizations = Optional.empty();
        private boolean athleteError;
        private boolean contractError;
        private boolean organizationError;

        StorageStubBuilder withAthletes(ReadOnlyAthleteList list) {
            this.athletes = Optional.of(list);
            return this;
        }

        StorageStubBuilder withContracts(ReadOnlyContractList list) {
            this.contracts = Optional.of(list);
            return this;
        }

        StorageStubBuilder withOrganizations(ReadOnlyOrganizationList list) {
            this.organizations = Optional.of(list);
            return this;
        }

        StorageStubBuilder withAthleteError() {
            this.athleteError = true;
            return this;
        }

        StorageStubBuilder withContractError() {
            this.contractError = true;
            return this;
        }

        StorageStubBuilder withOrganizationError() {
            this.organizationError = true;
            return this;
        }

        Storage build() {
            return new StorageStub(athletes, contracts, organizations,
                    athleteError, contractError, organizationError);
        }
    }

    private static class StorageStub implements Storage {
        private final Optional<ReadOnlyAthleteList> athletes;
        private final Optional<ReadOnlyContractList> contracts;
        private final Optional<ReadOnlyOrganizationList> organizations;
        private final boolean athleteError;
        private final boolean contractError;
        private final boolean organizationError;

        StorageStub(Optional<ReadOnlyAthleteList> athletes, Optional<ReadOnlyContractList> contracts,
                    Optional<ReadOnlyOrganizationList> organizations,
                    boolean athleteError, boolean contractError, boolean organizationError) {
            this.athletes = athletes;
            this.contracts = contracts;
            this.organizations = organizations;
            this.athleteError = athleteError;
            this.contractError = contractError;
            this.organizationError = organizationError;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("data", "prefs.json");
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {}

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("data", "addressbook.json");
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
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) {}

        @Override
        public Path getAthleteListFilePath() {
            return Path.of("data", "athletes.json");
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList() throws DataLoadingException {
            if (athleteError) {
                throw new DataLoadingException(new IOException("athlete error"));
            }
            return athletes;
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList(Path filePath) throws DataLoadingException {
            return readAthleteList();
        }

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athletes) {}

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athletes, Path filePath) {}

        @Override
        public Path getContractListFilePath() {
            return Path.of("data", "contracts.json");
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList() throws DataLoadingException {
            if (contractError) {
                throw new DataLoadingException(new IOException("contract error"));
            }
            return contracts;
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList(Path filePath) throws DataLoadingException {
            return readContractList();
        }

        @Override
        public void saveContractList(ReadOnlyContractList contracts) {}

        @Override
        public void saveContractList(ReadOnlyContractList contracts, Path filePath) {}

        @Override
        public Path getOrganizationListFilePath() {
            return Path.of("data", "organizations.json");
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList() throws DataLoadingException {
            if (organizationError) {
                throw new DataLoadingException(new IOException("org error"));
            }
            return organizations;
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList(Path filePath)
                throws DataLoadingException {
            return readOrganizationList();
        }

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations) {}

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations, Path filePath) {}
    }

    private static class RecordingStorage extends StorageStub {
        private UserPrefs savedPrefs;

        RecordingStorage() {
            super(Optional.empty(), Optional.empty(), Optional.empty(),
                    false, false, false);
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            this.savedPrefs = new UserPrefs(userPrefs);
        }

        UserPrefs getSavedPrefs() {
            return savedPrefs;
        }
    }

    private static class FailingSaveStorage extends RecordingStorage {
        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            throw new IOException("boom");
        }
    }

    private static class FullInitMainApp extends MainApp {
        private final Path configPath;
        private final Path userPrefsPath;
        private final Path addressPath;
        private final Path athletePath;
        private final Path contractPath;
        private final Path organizationPath;

        FullInitMainApp(Path configPath, Path userPrefsPath,
                        Path addressPath, Path athletePath, Path contractPath, Path organizationPath) {
            this.configPath = configPath;
            this.userPrefsPath = userPrefsPath;
            this.addressPath = addressPath;
            this.athletePath = athletePath;
            this.contractPath = contractPath;
            this.organizationPath = organizationPath;
        }

        @Override
        protected Config initConfig(Path ignored) {
            Config result = super.initConfig(configPath);
            result.setUserPrefsFilePath(userPrefsPath);
            return result;
        }

        @Override
        protected UserPrefs initPrefs(UserPrefsStorage storage) {
            UserPrefs prefs = super.initPrefs(storage);
            prefs.setAddressBookFilePath(addressPath);
            prefs.setAthleteListFilePath(athletePath);
            prefs.setContractListFilePath(contractPath);
            prefs.setOrganizationListFilePath(organizationPath);
            try {
                storage.saveUserPrefs(prefs);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to persist user prefs", e);
            }
            return prefs;
        }

        Config getConfig() {
            return config;
        }

        Storage getStorage() {
            return storage;
        }

        Model getModel() {
            return model;
        }

        Logic getLogic() {
            return logic;
        }

        Ui getUi() {
            return ui;
        }
    }

    private static class TestableMainApp extends MainApp {
        void setUi(Ui ui) {
            this.ui = ui;
        }

        void setStorage(Storage storage) {
            this.storage = storage;
        }

        void setModel(Model model) {
            this.model = model;
        }
    }

    private static class UiStub implements Ui {
        private boolean started;

        @Override
        public void start(Stage primaryStage) {
            started = true;
        }

        boolean wasStarted() {
            return started;
        }
    }

}
