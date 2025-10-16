package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import seedu.address.model.athlete.Athlete;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * Tests for {@link AthleteListPanel}.
 */
public class AthleteListPanelTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_bindsObservableList() {
        ObservableList<Athlete> athletes = FXCollections.observableArrayList(
                new AthleteBuilder().withName("Jane Doe").build(),
                new AthleteBuilder().withName("John Smith").build());

        AthleteListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new AthleteListPanel(athletes));
        ListView<Athlete> listView = getListView(panel);

        assertSame(athletes, listView.getItems());
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void athleteListViewCell_updateItem_togglesGraphic() {
        ObservableList<Athlete> athletes = FXCollections.observableArrayList(
                new AthleteBuilder().withName("Jane Doe").build());
        AthleteListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new AthleteListPanel(athletes));
        ListCell<Athlete> cell = createCell(panel);

        invokeUpdateItem(cell, athletes.get(0), false);
        assertNotNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));

        invokeUpdateItem(cell, null, true);
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getText));
    }

    private ListView<Athlete> getListView(AthleteListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = AthleteListPanel.class.getDeclaredField("athleteListView");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                ListView<Athlete> listView = (ListView<Athlete>) field.get(panel);
                return listView;
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    private ListCell<Athlete> createCell(AthleteListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            ListView<Athlete> listView = getListView(panel);
            return listView.getCellFactory().call(listView);
        });
    }

    private <T> void invokeUpdateItem(ListCell<T> cell, T item, boolean empty) {
        JavaFxTestUtil.runOnFxThreadAndWait(() -> {
            try {
                java.lang.reflect.Method method = javafx.scene.control.Cell.class
                        .getDeclaredMethod("updateItem", Object.class, boolean.class);
                method.setAccessible(true);
                method.invoke(cell, item, empty);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }
}
