package sample;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by Gon on 2017-05-06.
 */
public abstract class Controller {
    protected TAhelperApp mainApp;
    protected Stage primaryStage;
    protected Scene mainScene;

    public TAhelperApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(TAhelperApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setMainApp(TAhelperApp mainApp, Stage primaryStage, Scene scene) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        mainScene = scene;
    }

    public void reportError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void backToMainScene(){
        primaryStage.setScene(mainScene);
    }
}
