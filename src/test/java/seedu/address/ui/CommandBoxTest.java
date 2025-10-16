package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Tests for {@link CommandBox}.
 */
public class CommandBoxTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void handleCommandEntered_successfulExecution_clearsTextAndLeavesDefaultStyle() {
        AtomicReference<String> executedCommand = new AtomicReference<>();
        CommandBox commandBox = JavaFxTestUtil.callOnFxThread(() -> new CommandBox(commandText -> {
            executedCommand.set(commandText);
            return new CommandResult("done");
        }));

        TextField commandTextField = getCommandTextField(commandBox);

        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.setText("list"));
        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.getOnAction().handle(new ActionEvent()));

        assertEquals("list", executedCommand.get());
        assertEquals("", JavaFxTestUtil.callOnFxThread(commandTextField::getText));
        assertFalse(JavaFxTestUtil.callOnFxThread(() ->
                commandTextField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS)));
    }

    @Test
    public void handleCommandEntered_exception_addsAndClearsErrorStyle() {
        CommandBox commandBox = JavaFxTestUtil.callOnFxThread(() -> new CommandBox(commandText -> {
            throw new CommandException("boom");
        }));
        TextField commandTextField = getCommandTextField(commandBox);

        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.setText("bad"));
        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.getOnAction().handle(new ActionEvent()));

        assertTrue(JavaFxTestUtil.callOnFxThread(() ->
                commandTextField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS)));
        assertEquals("bad", JavaFxTestUtil.callOnFxThread(commandTextField::getText));

        // Changing the text should reset the style back to default via the listener.
        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.setText("retry"));
        assertFalse(JavaFxTestUtil.callOnFxThread(() ->
                commandTextField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS)));
    }

    @Test
    public void handleCommandEntered_emptyInput_doesNotInvokeExecutor() {
        AtomicInteger invocationCount = new AtomicInteger();
        CommandBox commandBox = JavaFxTestUtil.callOnFxThread(() -> new CommandBox(commandText -> {
            invocationCount.incrementAndGet();
            return new CommandResult("should not be called");
        }));
        TextField commandTextField = getCommandTextField(commandBox);

        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.setText(""));
        JavaFxTestUtil.runOnFxThreadAndWait(() -> commandTextField.getOnAction().handle(new ActionEvent()));

        assertEquals(0, invocationCount.get());
        assertFalse(JavaFxTestUtil.callOnFxThread(() ->
                commandTextField.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS)));
    }

    private TextField getCommandTextField(CommandBox commandBox) {
        return JavaFxTestUtil.callOnFxThread(() ->
                (TextField) ((StackPane) commandBox.getRoot()).getChildren().get(0));
    }
}
