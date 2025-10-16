package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * Tests for {@link ResultDisplay}.
 */
public class ResultDisplayTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void setFeedbackToUser_updatesTextArea() {
        ResultDisplay resultDisplay = JavaFxTestUtil.callOnFxThread(ResultDisplay::new);
        TextArea textArea = getTextArea(resultDisplay);

        JavaFxTestUtil.runOnFxThreadAndWait(() -> resultDisplay.setFeedbackToUser("success"));
        assertEquals("success", JavaFxTestUtil.callOnFxThread(textArea::getText));

        JavaFxTestUtil.runOnFxThreadAndWait(() -> resultDisplay.setFeedbackToUser("second"));
        assertEquals("second", JavaFxTestUtil.callOnFxThread(textArea::getText));
    }

    @Test
    public void setFeedbackToUser_null_throwsNullPointerException() {
        ResultDisplay resultDisplay = JavaFxTestUtil.callOnFxThread(ResultDisplay::new);
        assertThrows(NullPointerException.class, () ->
                JavaFxTestUtil.runOnFxThreadAndWait(() -> resultDisplay.setFeedbackToUser(null)));
    }

    private TextArea getTextArea(ResultDisplay resultDisplay) {
        return JavaFxTestUtil.callOnFxThread(() ->
                (TextArea) ((StackPane) resultDisplay.getRoot()).getChildren().get(0));
    }
}
