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
import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;

/**
 * Tests for {@link OrganizationListPanel}.
 */
public class OrganizationListPanelTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_bindsObservableList() {
        ObservableList<Organization> organizations = FXCollections.observableArrayList(
                new OrganizationBuilder().build(),
                new OrganizationBuilder().withName("Second Org").build());

        OrganizationListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new OrganizationListPanel(organizations));
        ListView<Organization> listView = getListView(panel);

        assertSame(organizations, listView.getItems());
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void organizationListViewCell_updateItem_togglesGraphic() {
        ObservableList<Organization> organizations = FXCollections.observableArrayList(
                new OrganizationBuilder().build());
        OrganizationListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new OrganizationListPanel(organizations));
        ListCell<Organization> cell = createCell(panel);

        invokeUpdateItem(cell, organizations.get(0), false);
        assertNotNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));

        invokeUpdateItem(cell, null, true);
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getText));
    }

    private ListView<Organization> getListView(OrganizationListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = OrganizationListPanel.class.getDeclaredField("organizationListView");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                ListView<Organization> listView = (ListView<Organization>) field.get(panel);
                return listView;
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    private ListCell<Organization> createCell(OrganizationListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            ListView<Organization> listView = getListView(panel);
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
