package sample;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MarkerController extends Controller {

    private UnzipUtility unzip;
    private String chooseDirectory;
    private ArrayList<Record> records;

    private Scene createTemplate;

    public ArrayList<Record> getRecords() {
        return records;
    }

    public TextField getFilename() {
        return filename;
    }

    public void setFilename(TextField filename) {
        this.filename = filename;
    }

    @FXML private TextField filename;
    @FXML private TextField templatename;
    @FXML private TableView tableView;
    @FXML private TableColumn index;

    public ImageView getBack() {
        return back;
    }

    public void setBack(ImageView back) {
        this.back = back;
    }

    @FXML private javafx.scene.image.ImageView back;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(TAhelperApp mainApp, Stage primaryStage, Scene scene) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.mainScene = scene;
        unzip = new UnzipUtility(this);
        records = new ArrayList<>();
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
            filename.setText(file.getAbsolutePath());
        }
    }
    public void handleTemplateButton(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Template file");
        if (chooseDirectory != null)
            chooser.setInitialDirectory(new File(chooseDirectory));
        // Set extentsion filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Zip files (*.zip)", "*.zip");
        chooser.getExtensionFilters().add(filter);

        // Open dialog
        File file = chooser.showOpenDialog(primaryStage);
        if (file!= null) {
            chooseDirectory = file.getParent();
            templatename.setText(file.getAbsolutePath());
        }
    }
    /**
     * Opens a FileChooser to let the user select an zip file to extract
     */
    public void handleStartButton(){
        if(!FilenameUtils.getExtension(filename.getText()).equals("zip")){
            reportError("Please choose a zip file!");
            return;
        }
        File file = new File(filename.getText());
        if(!file.exists()){
            reportError("File does not exist!");
            return;
        }
        records.clear();
        try {
            unzip.unzip(filename.getText(), filename.getText().replace(".zip", ""), 1);
        } catch (IOException e) {
            reportError("Error extracting zip file");
            return;
        }
    }

    public void addRecord(String name){
        records.add(new Record(name));
    }

    public void updateTable(){
        ObservableList<Record> data = FXCollections.observableArrayList(records);
        tableView.setItems(data);
        index.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Record>, ObservableValue<Record>>() {
            @Override public ObservableValue<Record> call(TableColumn.CellDataFeatures<Record, Record> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });

        index.setCellFactory(new Callback<TableColumn<Record, Record>, TableCell<Record, Record>>() {
            @Override public TableCell<Record, Record> call(TableColumn<Record, Record> param) {
                return new TableCell<Record, Record>() {
                    @Override protected void updateItem(Record item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex()+1 +"");
                            setStyle("-fx-alignment: center");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        index.setSortable(false);
    }

    public void handleCreateButton() throws IOException {
        if (createTemplate == null){
            FXMLLoader loader = new FXMLLoader(TAhelperApp.class.getResource("template.fxml"));
            Parent root = loader.load();
            ((Controller) (loader.getController())).setMainApp(this.mainApp, this.primaryStage, primaryStage.getScene());
            createTemplate = new Scene(root, 1000, 850);
        }

        primaryStage.setScene(createTemplate);
    }
}

