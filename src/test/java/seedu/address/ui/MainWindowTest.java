package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
 * Tests for {@link MainWindow}.
 */
public class MainWindowTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void fillInnerParts_populatesPlaceholders() {
        LogicStub logic = new LogicStub();
        MainWindow mainWindow = createMainWindow(logic);

        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        assertEquals(1, getStackPane(mainWindow, "commandBoxPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "athleteListPanelPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "organizationListPanelPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "contractListPanelPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "resultDisplayPlaceholder").getChildren().size());
        assertEquals(1, getStackPane(mainWindow, "statusbarPlaceholder").getChildren().size());

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    @Test
    public void handleHelp_togglesShowAndFocus() {
        LogicStub logic = new LogicStub();
        MainWindow mainWindow = createMainWindow(logic);
        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        HelpWindowStub helpWindowStub = JavaFxTestUtil.callOnFxThread(HelpWindowStub::new);
        JavaFxTestUtil.runOnFxThreadAndWait(() -> setField(mainWindow, "helpWindow", helpWindowStub));

        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::handleHelp);
        assertTrue(helpWindowStub.wasShowCalled());
        assertFalse(helpWindowStub.wasFocusCalled());

        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::handleHelp);
        assertTrue(helpWindowStub.wasFocusCalled());

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    @Test
    public void executeCommand_success_updatesDisplay() throws Exception {
        LogicStub logic = new LogicStub();
        logic.setNextResult(new CommandResult("done"));
        MainWindow mainWindow = createMainWindow(logic);
        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        CommandResult result = invokeExecuteCommand(mainWindow, "list");

        assertEquals("done", result.getFeedbackToUser());
        assertEquals("list", logic.getLastExecutedCommand());
        assertEquals("done", getResultDisplayText(mainWindow));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    @Test
    public void executeCommand_showHelpAndExit_invokesHandlers() throws Exception {
        LogicStub logic = new LogicStub();
        logic.setNextResult(new CommandResult("bye", true, true));
        MainWindow mainWindow = createMainWindow(logic);
        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        HelpWindowStub helpWindowStub = JavaFxTestUtil.callOnFxThread(HelpWindowStub::new);
        JavaFxTestUtil.runOnFxThreadAndWait(() -> setField(mainWindow, "helpWindow", helpWindowStub));

        invokeExecuteCommand(mainWindow, "exit");

        assertTrue(helpWindowStub.wasShowCalled());
        assertTrue(helpWindowStub.wasHideCalled());
        assertTrue(logic.isGuiSettingsSet());
        assertFalse(JavaFxTestUtil.callOnFxThread(() -> mainWindow.getPrimaryStage().isShowing()));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    @Test
    public void accelerators_triggerHelpAndTabSwitch() {
        LogicStub logic = new LogicStub();
        MainWindow mainWindow = createMainWindow(logic);
        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        HelpWindowStub helpWindowStub = JavaFxTestUtil.callOnFxThread(HelpWindowStub::new);
        JavaFxTestUtil.runOnFxThreadAndWait(() -> setField(mainWindow, "helpWindow", helpWindowStub));

        TextField commandField = JavaFxTestUtil.callOnFxThread(() -> {
            StackPane placeholder = getStackPane(mainWindow, "commandBoxPlaceholder");
            return (TextField) ((StackPane) placeholder.getChildren().get(0)).getChildren().get(0);
        });
        JavaFxTestUtil.runOnFxThreadAndWait(commandField::requestFocus);
        JavaFxTestUtil.runOnFxThreadAndWait(() -> {
            KeyEvent f1 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.F1,
                    false, false, false, false);
            commandField.fireEvent(f1);
        });
        assertTrue(helpWindowStub.wasShowCalled());

        TabPane tabPane = JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = MainWindow.class.getDeclaredField("tabPane");
                field.setAccessible(true);
                return (TabPane) field.get(mainWindow);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });

        JavaFxTestUtil.runOnFxThreadAndWait(() -> {
            KeyEvent shortcutTwo = new KeyEvent(KeyEvent.KEY_PRESSED, "2", "2", KeyCode.DIGIT2,
                    false, true, false, false);
            Event.fireEvent(mainWindow.getPrimaryStage(), shortcutTwo);
        });
        assertEquals(1, JavaFxTestUtil.callOnFxThread(() ->
                tabPane.getSelectionModel().getSelectedIndex()));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> {
            KeyEvent shortcutThree = new KeyEvent(KeyEvent.KEY_PRESSED, "3", "3", KeyCode.DIGIT3,
                    false, true, false, false);
            Event.fireEvent(mainWindow.getPrimaryStage(), shortcutThree);
        });
        assertEquals(2, JavaFxTestUtil.callOnFxThread(() ->
                tabPane.getSelectionModel().getSelectedIndex()));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    @Test
    public void executeCommand_exception_showsErrorMessage() {
        LogicStub logic = new LogicStub();
        logic.setCommandException(new CommandException("boom"));
        MainWindow mainWindow = createMainWindow(logic);
        JavaFxTestUtil.runOnFxThreadAndWait(mainWindow::fillInnerParts);

        assertThrows(CommandException.class, () -> invokeExecuteCommand(mainWindow, "bad"));
        assertEquals("boom", getResultDisplayText(mainWindow));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> mainWindow.getPrimaryStage().close());
    }

    private MainWindow createMainWindow(LogicStub logic) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            Stage stage = new Stage();
            MainWindow mainWindow = new MainWindow(stage, logic);
            mainWindow.show();
            return mainWindow;
        });
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

    private String getResultDisplayText(MainWindow mainWindow) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = MainWindow.class.getDeclaredField("resultDisplay");
                field.setAccessible(true);
                ResultDisplay resultDisplay = (ResultDisplay) field.get(mainWindow);
                TextArea textArea = (TextArea) ((StackPane) resultDisplay.getRoot()).getChildren().get(0);
                return textArea.getText();
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    private CommandResult invokeExecuteCommand(MainWindow mainWindow, String commandText)
            throws CommandException, ParseException {
        Method method;
        try {
            method = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
            method.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }

        try {
            return JavaFxTestUtil.callOnFxThread(() -> {
                try {
                    return (CommandResult) method.invoke(mainWindow, commandText);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    }
                    if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                    throw new RuntimeException(cause);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw new AssertionError(e);
                }
            });
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CommandException) {
                throw (CommandException) cause;
            }
            if (cause instanceof ParseException) {
                throw (ParseException) cause;
            }
            throw e;
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static class HelpWindowStub extends HelpWindow {
        private boolean showing;
        private boolean showCalled;
        private boolean focusCalled;
        private boolean hideCalled;

        HelpWindowStub() {
            super(new Stage());
        }

        @Override
        public void show() {
            showCalled = true;
            showing = true;
        }

        @Override
        public boolean isShowing() {
            return showing;
        }

        @Override
        public void focus() {
            focusCalled = true;
        }

        @Override
        public void hide() {
            hideCalled = true;
            showing = false;
        }

        boolean wasShowCalled() {
            return showCalled;
        }

        boolean wasFocusCalled() {
            return focusCalled;
        }

        boolean wasHideCalled() {
            return hideCalled;
        }
    }

    /**
     * Minimal {@link Logic} stub that tracks interactions with the UI.
     */
    private static class LogicStub implements Logic {
        private final ObservableList<Athlete> athletes = FXCollections.observableArrayList(
                new AthleteBuilder().build());
        private final ObservableList<Organization> organizations = FXCollections.observableArrayList(
                new OrganizationBuilder().build());
        private final ObservableList<Contract> contracts = FXCollections.observableArrayList(
                new ContractBuilder().build());
        private final Path filePath = Path.of("data", "addressbook.json");
        private GuiSettings guiSettings = new GuiSettings(800, 600, 0, 0);
        private CommandResult nextResult = new CommandResult("ok");
        private CommandException commandException;

        private String lastExecutedCommand;
        private boolean guiSettingsSet;

        void setNextResult(CommandResult result) {
            this.nextResult = result;
            this.commandException = null;
        }

        void setCommandException(CommandException exception) {
            this.commandException = exception;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            lastExecutedCommand = commandText;
            if (commandException != null) {
                throw commandException;
            }
            return nextResult;
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
            guiSettingsSet = true;
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

        String getLastExecutedCommand() {
            return lastExecutedCommand;
        }

        boolean isGuiSettingsSet() {
            return guiSettingsSet;
        }
    }
}
