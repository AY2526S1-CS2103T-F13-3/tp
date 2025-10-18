package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;

/**
 * Panel containing the list of organizations.
 */
public class OrganizationListPanel extends UiPart<Region> {
    private static final String FXML = "OrganizationListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(OrganizationListPanel.class);

    @FXML
    private ListView<Organization> organizationListView;

    private final ObservableList<Contract> allContracts;

    /**
     * Creates a {@code OrganizationListPanel} with the given {@code ObservableList}.
     */
    public OrganizationListPanel(ObservableList<Organization> organizationList, ObservableList<Contract> allContracts) {
        super(FXML);
        this.allContracts = allContracts;
        organizationListView.setItems(organizationList);
        organizationListView.setCellFactory(listView -> new OrganizationListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Organization} using a {@code OrganizationCard}.
     */
    class OrganizationListViewCell extends ListCell<Organization> {
        @Override
        protected void updateItem(Organization organization, boolean empty) {
            super.updateItem(organization, empty);

            if (empty || organization == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OrganizationCard(organization, getIndex() + 1, allContracts).getRoot());
            }
        }
    }

}
