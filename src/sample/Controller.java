package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class Controller {
    private TAhelperApp mainApp;
    private Stage primaryStage;
    private UnzipUtility unzip;
    private String chooseDirectory;

    public TAhelperApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(TAhelperApp mainApp) {
        this.mainApp = mainApp;
    }

    public TextField getFilename() {
        return filename;
    }

    public void setFilename(TextField filename) {
        this.filename = filename;
    }

    public TextArea getLoadingField() {
        return loadingField;
    }

    public void setLoadingField(TextArea loadingField) {
        this.loadingField = loadingField;
    }

    public ToggleGroup getOptions() {
        return options;
    }

    public void setOptions(ToggleGroup options) {
        this.options = options;
    }

    @FXML private TextField filename;
    @FXML private TextArea loadingField;
    @FXML private ToggleGroup options;
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(TAhelperApp mainApp, Stage primaryStage) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        unzip = new UnzipUtility(this);
    }

    /**
     * Opens a FileChooser to let the user select an zip file to extract
     */
    public void handleChooseButton(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Zip file");
        if (chooseDirectory != null)
            chooser.setInitialDirectory(new File(chooseDirectory));
        // Set extentsion filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        chooser.getExtensionFilters().add(filter);

        // Open dialog
        File file = chooser.showOpenDialog(primaryStage);
        if (file!= null) {
            chooseDirectory = file.getParent();
        }
        filename.setText(file.getAbsolutePath());
    }
    /**
     * Opens a FileChooser to let the user select an zip file to extract
     */
    public void handleStartButton(){
        int option;
        if (options.getSelectedToggle() != null) {
            option = Integer.parseInt(options.getSelectedToggle().getUserData().toString());
        } else {
            reportError("Please choose your option!");
            return;
        }
        if(!FilenameUtils.getExtension(filename.getText()).equals("zip")){
            reportError("Please choose a zip file!");
            return;
        }
        File file = new File(filename.getText());
        if(!file.exists()){
            reportError("File does not exist!");
            return;
        }
        getLoadingField().setText("");
        try {
            unzip.unzip(filename.getText(), filename.getText().replace(".zip", ""), option);
        } catch (IOException e) {
            reportError("Error extracting zip file");
            return;
        }
    }

    public void reportError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error !");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
