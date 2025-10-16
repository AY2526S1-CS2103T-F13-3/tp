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
import seedu.address.model.contract.Contract;
import seedu.address.testutil.contract.ContractBuilder;

/**
 * Tests for {@link ContractListPanel}.
 */
public class ContractListPanelTest {

    @BeforeAll
    static void setupToolkit() {
        JavaFxTestUtil.initFxToolkit();
    }

    @Test
    public void constructor_bindsObservableList() {
        ObservableList<Contract> contracts = FXCollections.observableArrayList(
                new ContractBuilder().build(),
                new ContractBuilder().withSport("Tennis").withAmount(750000).build());

        ContractListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new ContractListPanel(contracts));
        ListView<Contract> listView = getListView(panel);

        assertSame(contracts, listView.getItems());
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void contractListViewCell_updateItem_togglesGraphic() {
        ObservableList<Contract> contracts = FXCollections.observableArrayList(
                new ContractBuilder().build());
        ContractListPanel panel = JavaFxTestUtil.callOnFxThread(() -> new ContractListPanel(contracts));
        ListCell<Contract> cell = createCell(panel);

        invokeUpdateItem(cell, contracts.get(0), false);
        assertNotNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));

        invokeUpdateItem(cell, null, true);
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getGraphic));
        assertNull(JavaFxTestUtil.callOnFxThread(cell::getText));
    }

    private ListView<Contract> getListView(ContractListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            try {
                Field field = ContractListPanel.class.getDeclaredField("contractListView");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                ListView<Contract> listView = (ListView<Contract>) field.get(panel);
                return listView;
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    private ListCell<Contract> createCell(ContractListPanel panel) {
        return JavaFxTestUtil.callOnFxThread(() -> {
            ListView<Contract> listView = getListView(panel);
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
