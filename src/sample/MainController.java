package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Created by Gon on 2017-05-06.
 */
public class MainController extends Controller {

    private Scene unZipUtil;
    private Scene markerUtil;


    public void handleUnzip() throws IOException {
        if (unZipUtil == null){
            FXMLLoader loader = new FXMLLoader(TAhelperApp.class.getResource("unzip.fxml"));
            Parent root = loader.load();
            ((Controller) (loader.getController())).setMainApp(this.mainApp, this.primaryStage, this.mainScene);
            unZipUtil = new Scene(root, 740, 550);
        }

        primaryStage.setScene(unZipUtil);
    }

    public void handleMarker() throws IOException {
        if (markerUtil == null){
            FXMLLoader loader = new FXMLLoader(TAhelperApp.class.getResource("marker.fxml"));
            Parent root = loader.load();
            ((Controller) (loader.getController())).setMainApp(this.mainApp, this.primaryStage, this.mainScene);
            markerUtil = new Scene(root, 1000, 850);
        }

        primaryStage.setScene(markerUtil);
    }
}
