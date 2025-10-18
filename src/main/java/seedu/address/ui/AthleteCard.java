package seedu.address.ui;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;

/**
 * An UI component that displays information of a {@code Athlete}.
 */
public class AthleteCard extends UiPart<Region> {

    private static final String FXML = "AthleteListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Athlete athlete;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label sport;
    @FXML
    private Label age;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label totalContracts;
    @FXML
    private Label organizationList;

    /**
     * Creates a {@code AthleteCard} with the given {@code Athlete} and index to display.
     */
    public AthleteCard(Athlete athlete, int displayedIndex, ObservableList<Contract> allContracts) {
        super(FXML);
        this.athlete = athlete;
        id.setText(displayedIndex + ". ");
        name.setText(athlete.getName().fullName);
        sport.setText(athlete.getSport().value);
        age.setText("Age: " + athlete.getAge().value);
        phone.setText(athlete.getPhone().value);
        email.setText(athlete.getEmail().value);
        int totalAmount = athlete.getTotalContractAmount(allContracts);
        String formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(totalAmount);
        totalContracts.setText("Total Contract Value: $" + formattedAmount);
        ObservableList<Organization> organizations = athlete.getOrganizations(allContracts);
        if (organizations.isEmpty()) {
            organizationList.setText("Organizations: None");
        } else {
            String organizationNames = organizations.stream()
                    .map(a -> a.getName().fullOrganizationName)
                    .collect(Collectors.joining(", "));
            organizationList.setText("Organizations: " + organizationNames);
        }
    }
}
