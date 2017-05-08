package sample;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;

import java.io.*;

/**
 * Created by Gon on 2017-05-07.
 */
public class TemplateControler extends Controller {

    private Scene createSection;
    private SectionControler sectionControler;

    @FXML
    private TableColumn buttons;
    @FXML
    private TableColumn content;
    @FXML
    private TableColumn classname;
    @FXML
    private TableColumn method;

    @FXML
    private TableView sectionView;

    @FXML
    private TextField nameField;
    @FXML
    private TextField classField;
    @FXML
    private TextField methodField;

    private Template template;

    private ObservableList<Section> tableList;


    public void setMainApp(TAhelperApp mainApp, Stage primaryStage, Scene scene) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        mainScene = scene;

        tableList = FXCollections.observableArrayList(Section.extractor());

        template = new Template();
        sectionView.setEditable(true);
        sectionView.setItems(tableList);

        sectionView.setRowFactory(tv -> {
            TableRow<Section> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Section rowData = row.getItem();
                    try {
                        updateSection(rowData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        sectionView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    Section section = (Section) (sectionView.getSelectionModel().getSelectedItem());
                    try {
                        updateSection(section);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    handleAddEvent();
                }
            }
        });

        classField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    handleAddEvent();
                }
            }
        });
        methodField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    handleAddEvent();
                }
            }
        });

        content.setCellFactory(TextFieldTableCell.forTableColumn());
        content.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Section, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Section, String> t) {
                        ((Section) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );
        classname.setCellFactory(TextFieldTableCell.forTableColumn());
        classname.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Section, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Section, String> t) {
                        ((Section) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setClassName(t.getNewValue());
                    }
                }
        );
        method.setCellFactory(TextFieldTableCell.forTableColumn());
        method.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Section, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Section, String> t) {
                        ((Section) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMethodName(t.getNewValue());
                    }
                }
        );

        tableList.addListener(new ListChangeListener<Section>() {
            @Override
            public void onChanged(Change<? extends Section> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            sectionView.refresh();
                        }
                    }
                }
            }
        });
    }

    private void updateSection(Section rowData) throws IOException {
        FXMLLoader loader = new FXMLLoader(TAhelperApp.class.getResource("section.fxml"));
        if (createSection == null) {
            Parent root = loader.load();
            this.sectionControler = (SectionControler) (loader.getController());
            ((SectionControler) (loader.getController())).setMainApp(this.mainApp, this.primaryStage, primaryStage.getScene());
            createSection = new Scene(root, 1000, 850);
        }
        sectionControler.initScene(rowData);
        primaryStage.setScene(createSection);
    }


    public void handleAddEvent() {
        if (!nameField.getText().trim().equals("")) {
            Section section = new Section(nameField.getText(), classField.getText(), methodField.getText(), this);
            tableList.add(section);
            template.addSection(section);

            nameField.clear();
            classField.clear();
            methodField.clear();

            nameField.requestFocus();

            buttons.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

            Callback<TableColumn<Section, String>, TableCell<Section, String>> cellFactory = //
                    new Callback<TableColumn<Section, String>, TableCell<Section, String>>() {
                        @Override
                        public TableCell call(final TableColumn<Section, String> param) {
                            final TableCell<Section, String> cell = new TableCell<Section, String>() {

                                final Button btn = new Button("X");

                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                        setText(null);
                                    } else {
                                        this.setStyle("-fx-alignment: center");
                                        btn.setOnAction((ActionEvent event) ->
                                        {
                                            Section section = (Section) (getTableView().getItems().get(getIndex()));
                                            tableList.remove(section);
                                            template.removeSection(section);
                                        });
                                        setGraphic(btn);
                                        setText(null);
                                    }
                                }
                            };
                            return cell;
                        }
                    };

            buttons.setCellFactory(cellFactory);
        }
    }

    public void handleSaveEvent() {
        //Prepare JSON object
        template.countMark();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Template File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Template file", "*.temp"));
        File f = chooser.showSaveDialog(primaryStage);

        if (f ==null){
            return;
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(template);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Template temp = null;
        try {
            temp = (Template) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(temp);

    }
}