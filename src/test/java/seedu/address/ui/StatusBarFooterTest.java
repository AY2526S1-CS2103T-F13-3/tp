package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Tests for {@link StatusBarFooter}.
 */
public class StatusBarFooterTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_validPath_displaysResolvedLocation() {
        Path relativePath = Paths.get("data", "addressbook.json");
        StatusBarFooter footer = JavaFxTestUtil.callOnFxThread(() -> new StatusBarFooter(relativePath));

        Label label = getSaveLocationLabel(footer);
        String expected = Paths.get(".").resolve(relativePath).toString();
        assertEquals(expected, JavaFxTestUtil.callOnFxThread(label::getText));
    }

    private Label getSaveLocationLabel(StatusBarFooter footer) {
        return JavaFxTestUtil.callOnFxThread(() ->
                (Label) ((GridPane) footer.getRoot()).getChildren().get(0));
    }
}

