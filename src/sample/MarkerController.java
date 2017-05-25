package sample;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class MarkerController extends Controller {

    private String commentFromFile;
    private float maxMark;

    public String getCommentFromFile() {
        return commentFromFile;
    }

    public void setCommentFromFile(String commentFromFile) {
        this.commentFromFile = commentFromFile;
    }

    public float getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(float maxMark) {
        this.maxMark = maxMark;
    }



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

    @FXML
    private TextField filename;
    @FXML
    private TextField templatename;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn index;
    @FXML
    private TableColumn mark;
    @FXML
    private TableColumn edit;
    @FXML
    private Button saveButton;

    public ImageView getBack() {
        return back;
    }

    public void setBack(ImageView back) {
        this.back = back;
    }

    @FXML
    private javafx.scene.image.ImageView back;

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
        commentFromFile ="";
        maxMark = 100;
        tableView.setEditable(true);


    }

    /**
     * Opens a FileChooser to let the user select an zip file to extract
     */
    public void handleChooseButton() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open CSV file");
        if (chooseDirectory != null)
            chooser.setInitialDirectory(new File(chooseDirectory));
        // Set extentsion filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.zip");
        chooser.getExtensionFilters().add(filter);

        // Open dialog
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null) {
            chooseDirectory = file.getParent();
            filename.setText(file.getAbsolutePath());
        }
    }

    public void handleTemplateButton() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Scheme file");
        if (chooseDirectory != null)
            chooser.setInitialDirectory(new File(chooseDirectory));
        // Set extentsion filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(filter);

        // Open dialog
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null) {
            chooseDirectory = file.getParent();
            templatename.setText(file.getAbsolutePath());
        }
    }

    /**
     * Opens a FileChooser to let the user select an zip file to extract
     */
    public void handleStartButton() {
        if (!FilenameUtils.getExtension(filename.getText()).equals("zip")) {
            reportError("Please choose a valid zip file!");
            return;
        }
        if (!templatename.getText().isEmpty() && !FilenameUtils.getExtension(templatename.getText()).equals("txt")) {
            reportError("Please choose a txt template file!");
            return;
        }
        File file = new File(filename.getText());
        File template = new File(templatename.getText());
        if (!file.exists()) {
            reportError("zip file does not exist!");
            return;
        }
        if (template.exists()) {
            BufferedReader in = null;
            commentFromFile = "";
            try {
                in = new BufferedReader(new FileReader(template));
            } catch (FileNotFoundException e) {
                reportError("File not found");
            }
            Scanner scanner = new Scanner(in);
            String mark = scanner.nextLine();
            if (NumberUtils.isCreatable(mark)){
                maxMark = Float.parseFloat(mark);
            } else {
                commentFromFile += mark;
            }
            while(scanner.hasNextLine()){
                commentFromFile += scanner.nextLine()+ "\n";
            }


            scanner.close();

        }
        records.clear();

        try {
            unzip.unzip(filename.getText(), filename.getText().replace(".zip", ""), 1);
        } catch (IOException e) {
            reportError("Error extracting zip file");
            return;
        }


    }

    public void addRecord(String name, String id) {
        records.add(new Record(name, id));
    }

    public void updateTable() {
        ObservableList<Record> data = FXCollections.observableArrayList(records);
        tableView.setItems(data);

        tableView.setRowFactory(tv -> {
            TableRow<Record> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Record clickedRow = row.getItem();
                    updateRecord(clickedRow);
                }
            });
            return row;
        });

        index.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Record>, ObservableValue<Record>>() {
            @Override
            public ObservableValue<Record> call(TableColumn.CellDataFeatures<Record, Record> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue());
            }
        });

        edit.setCellFactory(TextFieldTableCell.forTableColumn());
        edit.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Record, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Record, String> t) {
                        if (!t.getNewValue().equals("") && NumberUtils.isCreatable(t.getNewValue())) {
                            float mark = Float.parseFloat(t.getNewValue());
                            if (mark> maxMark){
                                mark = maxMark;
                            }
                            ((Record) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setMark(mark);
                        }
                        t.getTableView().refresh();
                        tableView.requestFocus();
                        tableView.getFocusModel().focus(t.getTablePosition().getRow() + 1);
                        tableView.getSelectionModel().select(t.getTablePosition().getRow() + 1, edit);
                    }
                }
        );

        index.setCellFactory(new Callback<TableColumn<Record, Record>, TableCell<Record, Record>>() {
            @Override
            public TableCell<Record, Record> call(TableColumn<Record, Record> param) {
                return new TableCell<Record, Record>() {
                    @Override
                    protected void updateItem(Record item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                            setStyle("-fx-alignment: center");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        index.setSortable(false);
        saveButton.setDisable(false);

    }

    private void updateRecord(Record record) {
        FXMLLoader loader = new FXMLLoader(getMainApp().getClass().getResource("edit-record.fxml"));
        Dialog<String> dialog = null;
        try {
            dialog = loader.load();
            dialog.initOwner(primaryStage);
            EditController controller = loader.getController();
            controller.setMarkerController(this);
            controller.getMarkField().setText(record.getComment());
            dialog.setResultConverter(
                    new Callback<ButtonType, String>() {
                        public String call(ButtonType b) {
                            if (!b.getButtonData().isCancelButton()) {
                                return controller.getMarkField().getText().trim();
                            }
                            return record.getComment();
                        }
                    });
            Optional<String> result= dialog.showAndWait();



            if (result.isPresent()){
                record.setComment(result.get());
            }

        } catch (IOException e) {
            reportError("Cannot find FXML file");
        }


    }

    public void handleCreateButton() throws IOException {
        if (createTemplate == null) {
            FXMLLoader loader = new FXMLLoader(TAhelperApp.class.getResource("template.fxml"));
            Parent root = loader.load();
            ((Controller) (loader.getController())).setMainApp(this.mainApp, this.primaryStage, primaryStage.getScene());
            createTemplate = new Scene(root, 1000, 850);
        }

        primaryStage.setScene(createTemplate);
    }

    public void handleSaveButton() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save to CSV File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV file, *.csv", "*.csv"));
        File f = chooser.showSaveDialog(primaryStage);

        if (f == null) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.println("Identifier,Full name,Email address,Status,Grade,Maximum Grade,Grade can be changed,Last modified (submission),Last modified (grade),Feedback comments");
            for (Record re : records) {
                out.println(re);
            }
            out.close();
        } catch (IOException e) {
            reportError("Cannot open file: " + f.getAbsolutePath());
            return;
        }
    }
}

