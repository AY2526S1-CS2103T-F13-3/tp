package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;

/**
 * Tests for {@link HelpWindow}.
 */
public class HelpWindowTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_setsHelpMessage() {
        HelpWindow helpWindow = JavaFxTestUtil.callOnFxThread(HelpWindow::new);
        assertEquals(HelpWindow.HELP_MESSAGE, getHelpMessage(helpWindow));
        JavaFxTestUtil.runOnFxThreadAndWait(() -> helpWindow.hide());
    }

    @Test
    public void showFocusHide_lifecycleMaintainsState() {
        HelpWindow helpWindow = JavaFxTestUtil.callOnFxThread(HelpWindow::new);

        JavaFxTestUtil.runOnFxThreadAndWait(helpWindow::show);
        assertTrue(JavaFxTestUtil.callOnFxThread(helpWindow::isShowing));

        JavaFxTestUtil.runOnFxThreadAndWait(helpWindow::focus);

        JavaFxTestUtil.runOnFxThreadAndWait(helpWindow::hide);
        assertFalse(JavaFxTestUtil.callOnFxThread(helpWindow::isShowing));
    }

    @Test
    public void copyButton_copiesUserGuideUrlToClipboard() {
        HelpWindow helpWindow = JavaFxTestUtil.callOnFxThread(HelpWindow::new);
        JavaFxTestUtil.runOnFxThreadAndWait(() -> getCopyButton(helpWindow).fire());

        String clipboardText = JavaFxTestUtil.callOnFxThread(() -> Clipboard.getSystemClipboard().getString());
        assertEquals(HelpWindow.USERGUIDE_URL, clipboardText);
        JavaFxTestUtil.runOnFxThreadAndWait(helpWindow::hide);
    }

    private String getHelpMessage(HelpWindow helpWindow) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = HelpWindow.class.getDeclaredField("helpMessage");
                field.setAccessible(true);
                return ((Label) field.get(helpWindow)).getText();
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    private Button getCopyButton(HelpWindow helpWindow) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = HelpWindow.class.getDeclaredField("copyButton");
                field.setAccessible(true);
                return (Button) field.get(helpWindow);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }
}
