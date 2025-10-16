package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.contract.ContractBuilder;

/**
 * Tests for {@link UiManager}.
 */
public class UiManagerTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void start_initialisesMainWindowAndShowsStage() {
        LogicStub logic = new LogicStub();
        UiManager uiManager = new UiManager(logic);
        Stage stage = JavaFxTestUtil.callOnFxThread(Stage::new);

        JavaFxTestUtil.runOnFxThreadAndWait(() -> uiManager.start(stage));

        MainWindow mainWindow = getMainWindow(uiManager);
        assertNotNull(mainWindow);
        assertSame(stage, mainWindow.getPrimaryStage());
        assertFalse(stage.getIcons().isEmpty());
        assertEquals(1, getStackPane(mainWindow, "commandBoxPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "statusbarPlaceholder").getChildren().size());
        assertTrue(JavaFxTestUtil.callOnFxThread(stage::isShowing));

        JavaFxTestUtil.runOnFxThreadAndWait(stage::hide);
    }

    private MainWindow getMainWindow(UiManager uiManager) {
        try {
            Field field = UiManager.class.getDeclaredField("mainWindow");
            field.setAccessible(true);
            return (MainWindow) field.get(uiManager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private StackPane getStackPane(MainWindow mainWindow, String fieldName) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = MainWindow.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (StackPane) field.get(mainWindow);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    /**
     * Minimal {@link Logic} stub for exercising {@link UiManager}.
     */
    private static class LogicStub implements Logic {
        private final ObservableList<Athlete> athletes = FXCollections.observableArrayList(
                new AthleteBuilder().build());
        private final ObservableList<Organization> organizations =
                FXCollections.observableArrayList(new OrganizationBuilder().build());
        private final ObservableList<Contract> contracts =
                FXCollections.observableArrayList(new ContractBuilder().build());
        private final Path filePath = Path.of("data", "addressbook.json");
        private GuiSettings guiSettings = new GuiSettings(800, 600, 0, 0);

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return new CommandResult("ok");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public Path getAddressBookFilePath() {
            return filePath;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return guiSettings;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            this.guiSettings = guiSettings;
        }

        @Override
        public ObservableList<Contract> getFilteredContractList() {
            return contracts;
        }

        @Override
        public ObservableList<Athlete> getFilteredAthleteList() {
            return athletes;
        }

        @Override
        public ObservableList<Organization> getFilteredOrganizationList() {
            return organizations;
        }
    }
}
