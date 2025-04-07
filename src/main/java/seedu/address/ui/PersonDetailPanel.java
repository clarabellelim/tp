package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.person.Person;

/**
 * Panel that manages and displays the PersonDetail component.
 */
public class PersonDetailPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailPanel.fxml";

    @FXML
    private StackPane personDetailPlaceholder;
    private PersonDetail personDetail;

    /**
     * Constructs a {@code PersonDetailPanel} UI component.
     * Initializes a {@code PersonDetail} component and adds it to the designated placeholder.
     */
    public PersonDetailPanel() {
        super(FXML);
        personDetail = new PersonDetail();
        personDetailPlaceholder.getChildren().add(personDetail.getRoot());
    }

    /**
     * Updates the displayed PersonDetail with the given person.
     */
    public void showPersonDetail(Person person) {
        personDetail.setPerson(person);
    }

    public PersonDetail getPersonDetail() {
        return personDetail;
    }
}
