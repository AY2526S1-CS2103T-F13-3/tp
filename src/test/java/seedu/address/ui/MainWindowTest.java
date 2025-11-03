package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonAthleteListStorage;
import seedu.address.storage.JsonContractListStorage;
import seedu.address.storage.JsonOrganizationListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class MainWindowTest {

    @Test
    public void mainWindow_logicInitialization_success() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model, new StorageManager(
                new JsonAddressBookStorage(java.nio.file.Paths.get("test")),
                new JsonUserPrefsStorage(java.nio.file.Paths.get("test")),
                new JsonAthleteListStorage(java.nio.file.Paths.get("test")),
                new JsonContractListStorage(java.nio.file.Paths.get("test"),
                    java.nio.file.Paths.get("test"), java.nio.file.Paths.get("test")),
                new JsonOrganizationListStorage(java.nio.file.Paths.get("test"))));

        assertNotNull(logic);
        assertNotNull(logic.getFilteredAthleteList());
        assertNotNull(logic.getFilteredOrganizationList());
        assertNotNull(logic.getFilteredContractList());
    }
}
