package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by Gon on 2017-05-07.
 */
public class SectionControler extends Controller{

    private Section section;

    @FXML
    private TableColumn buttons;
    @FXML private TableColumn content;
    @FXML private TableColumn classname;
    @FXML private TableColumn method;
    @FXML private TableView sectionView;
    @FXML private TableColumn mark;
    @FXML private TextField nameField;
    @FXML private TextField classField;
    @FXML private TextField methodField;
    @FXML private TextField markField;


    private ObservableList<Requirement> tableList;


    public void initScene(Section section) {
        this.section = section;
        tableList = FXCollections.observableArrayList(section.getRequirements());
        sectionView.setItems(tableList);
    }

    public void setMainApp(TAhelperApp mainApp, Stage primaryStage, Scene scene) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        mainScene = scene;


        nameField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    handleAddEvent();
                }
            }
        });

        classField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    handleAddEvent();
                }
            }
        });
        methodField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    handleAddEvent();
                }
            }
        });

        markField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    handleAddEvent();
                }
            }
        });

        content.setCellFactory(TextFieldTableCell.forTableColumn());
        content.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Requirement, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Requirement, String> t) {
                        ((Requirement) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setContent(t.getNewValue());
                    }
                }
        );
        classname.setCellFactory(TextFieldTableCell.forTableColumn());
        classname.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Requirement, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Requirement, String> t) {
                        ((Requirement) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setClassName(t.getNewValue());
                    }
                }
        );
        method.setCellFactory(TextFieldTableCell.forTableColumn());
        method.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Requirement, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Requirement, String> t) {
                        ((Requirement) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMethodName(t.getNewValue());
                    }
                }
        );

    }

    public void updateSection(Section section) {

    }


    public void handleAddEvent() {
        if(!nameField.getText().trim().equals("") && NumberUtils.isNumber(markField.getText()) ){
            Requirement requirement = new Requirement(Float.parseFloat(markField.getText()), nameField.getText(), classField.getText(), methodField.getText());
            tableList.add(requirement);
            section.addRequirement(requirement);
            sectionView.setEditable(true);

            nameField.clear();
            classField.clear();
            methodField.clear();

            nameField.requestFocus();


            buttons.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

            Callback<TableColumn<Requirement, String>, TableCell<Requirement, String>> cellFactory = //
                    new Callback<TableColumn<Requirement, String>, TableCell<Requirement, String>>()
                    {
                        @Override
                        public TableCell call(final TableColumn<Requirement, String> param )

                        {
                            final TableCell<Requirement, String> cell = new TableCell<Requirement, String>()
                            {

                                final Button btn = new Button( "X" );

                                @Override
                                public void updateItem( String item, boolean empty )
                                {
                                    super.updateItem( item, empty );
                                    if ( empty )
                                    {
                                        setGraphic( null );
                                        setText( null );
                                    }
                                    else
                                    {
                                        this.setStyle("-fx-alignment: center");
                                        btn.setOnAction( ( ActionEvent event ) ->
                                        {
                                            Requirement requirement = (Requirement) (getTableView().getItems().get(getIndex()));
                                            tableList.remove(requirement);
//                                            tableList.remove(getTableView().getItems().get(getIndex()));
                                            section.removeRequirement(requirement);
                                        } );
                                        setGraphic( btn );
                                        setText( null );
                                    }
                                }
                            };
                            return cell;
                        }
                    };

            buttons.setCellFactory(cellFactory);
        }
    }

    public void handleSaveEvent(ActionEvent actionEvent) {
    }
}
