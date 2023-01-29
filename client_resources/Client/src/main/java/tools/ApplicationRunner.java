package tools;

import controllers.AlertController;
import controllers.ServerDataModalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.workWithServer.WorkWithServer;

import java.io.IOException;


/**
 *  Class that run JavaFx application
 *
 * * */
public class ApplicationRunner extends Application {
    @Override
    public void start(Stage stage){
        try {
            //open page with server ip and port input
            loadServerDataPage(stage);
            //if success run methods in ServerDataModalController
        }catch (Exception e){
            AlertController.getAlertController().showErrorAndStopApp("Can`t connect to server");
        }

    }

    public static void run(String[] args) {
        //launch javaFx application
        launch(args);
        //method start() will be executed next
    }

    public void loadServerDataPage(Stage stage) throws IOException {
        //create window
        Parent pane = FXMLLoader.load(getClass().getResource("/pages/ServerDataModal.fxml"));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/style/loginPage.css").toExternalForm());
        stage.setResizable(false);
        stage.setTitle("Cloud storage");
        Image image = new Image("/images/icon.png");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }
}
