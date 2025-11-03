package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
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
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.AthleteListStorage;
import seedu.address.storage.ContractListStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonAthleteListStorage;
import seedu.address.storage.JsonContractListStorage;
import seedu.address.storage.JsonOrganizationListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.OrganizationListStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    /**
     * Initializes the application, including configuration, storage, model, logic, and UI.
     */
    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(userPrefs.getAddressBookFilePath());
        AthleteListStorage athleteListStorage = new JsonAthleteListStorage(userPrefs.getAthleteListFilePath());
        ContractListStorage contractListStorage = new JsonContractListStorage(
                userPrefs.getContractListFilePath());
        OrganizationListStorage organizationListStorage = new JsonOrganizationListStorage(
                userPrefs.getOrganizationListFilePath());
        storage = new StorageManager(addressBookStorage, userPrefsStorage, athleteListStorage,
                contractListStorage, organizationListStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Initializes and returns a {@code ModelManager} with data from {@code storage} and {@code userPrefs}.
     * <p>
     * The method attempts to load data from all files:
     * - Athlete list
     * - Contract list
     * - Organization list
     * <p>
     * Special handling:
     * <ul>
     *   <li>If the contract list is non-empty but either the athlete list or organization list is empty,
     *       all three lists are reset to empty. This ensures contracts do not exist independently of
     *       their dependent entities.</li>
     *   <li>If any data files fail to load (e.g., due to corruption), all lists are reset to empty.</li>
     *   <li>If all files are present and valid, the data is loaded normally.</li>
     * </ul>
     *
     * @param storage the storage manager used to read the data files. Cannot be null.
     * @param userPrefs the user preferences to use for the model. Cannot be null.
     * @return a {@code ModelManager} initialized with the loaded or default data.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Initializing ModelManager...");
        ReadOnlyAddressBook initialData = new AddressBook();
        ReadOnlyAthleteList initialAthleteList;
        ReadOnlyContractList initialContractList;
        ReadOnlyOrganizationList initialOrganizationList;
        try {
            Optional<ReadOnlyAthleteList> athleteOpt = storage.readAthleteList();
            Optional<ReadOnlyContractList> contractOpt = storage.readContractList();
            Optional<ReadOnlyOrganizationList> orgOpt = storage.readOrganizationList();
            initialAthleteList = athleteOpt.orElse(SampleDataUtil.getEmptyAthleteList());
            initialContractList = contractOpt.orElse(SampleDataUtil.getEmptyContractList());
            initialOrganizationList = orgOpt.orElse(SampleDataUtil.getEmptyOrganizationList());
            // Reset all if contracts exist but either athlete or organization list is empty
            if (!initialContractList.getContractList().isEmpty()
                    && (initialAthleteList.getAthleteList().isEmpty()
                    || initialOrganizationList.getOrganizationList().isEmpty())) {
                logger.info("Contracts exist but athlete/org data missing. Resetting all lists.");
                initialAthleteList = SampleDataUtil.getEmptyAthleteList();
                initialContractList = SampleDataUtil.getEmptyContractList();
                initialOrganizationList = SampleDataUtil.getEmptyOrganizationList();
            }
        } catch (DataLoadingException e) {
            logger.warning("Failed to load data files. Resetting all lists.");
            initialAthleteList = SampleDataUtil.getEmptyAthleteList();
            initialContractList = SampleDataUtil.getEmptyContractList();
            initialOrganizationList = SampleDataUtil.getEmptyOrganizationList();
        }
        return new ModelManager(initialData, userPrefs, initialAthleteList, initialContractList,
                initialOrganizationList);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                    + " Using default config properties.");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    /**
     * Starts the JavaFX application UI.
     *
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
        logger.info("UI started successfully.");
    }

    /**
     * Stops the application, saving user preferences before exit.
     */
    @Override
    public void stop() {
        logger.info("============================ [ Stopping AddressBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
            logger.info("User preferences saved successfully.");
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
