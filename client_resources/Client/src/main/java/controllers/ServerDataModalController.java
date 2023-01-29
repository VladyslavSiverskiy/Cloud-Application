package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.workWithServer.WorkWithServer;

import java.io.IOException;


/**
 * Class to get ip and port number and connect to server
 *
 *
 * */
public class ServerDataModalController{

    @FXML
    TextField serverIP;

    @FXML
    TextField serverPort;

    /**
     * Connect button handler
     */
    @FXML
    public void connectToServer(){
        Stage stage = (Stage)serverPort.getScene().getWindow();
        try {
            //connect to server and close current window
            WorkWithServer.connect(serverIP.getText(),Integer.parseInt(serverPort.getText()));
            stage.close();

            //if connection OK, open login page window
            loadLoginPage(new Stage());
            //watch LoginPageController to see next actions
        }catch (Exception e){
            AlertController.getAlertController().showErrorAndStopApp("Can`t connect to server");
        }
    }

    public void loadLoginPage(Stage stage) throws IOException {
        //create login page window
        Parent pane = FXMLLoader.load(getClass().getResource("/pages/loginPage.fxml"));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/style/loginPage.css").toExternalForm());
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Cloud storage");
        Image image = new Image("/images/icon.png");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }
}
