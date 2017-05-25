package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Created by Gon on 2017-05-24.
 */
public class EditController {
    /*For the dialog pane*/
    @FXML
    private TextArea markField;

    private Controller markerController;

    public Controller getMarkerController() {
        return markerController;
    }

    public void setMarkerController(Controller markerController) {
        this.markerController = markerController;
    }



    public TextArea getMarkField() {
        return markField;
    }

    public void setMarkField(TextArea markField) {
        this.markField = markField;
    }

    public void handleImportButton(ActionEvent actionEvent) {
        this.markField.setText(((MarkerController)markerController).getCommentFromFile());
    }

    public void handleSaveButton(ActionEvent actionEvent) {
        ((MarkerController)markerController).setCommentFromFile(markField.getText());
    }
}
