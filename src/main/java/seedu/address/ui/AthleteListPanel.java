package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.contract.Contract;

/**
 * Panel containing the list of athletes.
 */
public class AthleteListPanel extends UiPart<Region> {
    private static final String FXML = "AthleteListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AthleteListPanel.class);

    @FXML
    private ListView<Athlete> athleteListView;
    private final ObservableList<Contract> allContracts;

    /**
     * Creates a {@code AthleteListPanel} with the given {@code ObservableList}.
     */
    public AthleteListPanel(ObservableList<Athlete> athleteList, ObservableList<Contract> contractList) {
        super(FXML);
        this.allContracts = contractList;
        athleteListView.setItems(athleteList);
        athleteListView.setCellFactory(listView -> new AthleteListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Athlete} using a {@code AthleteCard}.
     */
    class AthleteListViewCell extends ListCell<Athlete> {
        @Override
        protected void updateItem(Athlete athlete, boolean empty) {
            super.updateItem(athlete, empty);

            if (empty || athlete == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new AthleteCard(athlete, getIndex() + 1, allContracts).getRoot());
            }
        }
    }

}
