package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tools.workWithServer.WorkWithServer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemoryController implements Initializable {

    @FXML
    Label userName;

    @FXML
    Label kbytesUsed;

    @FXML
    ProgressBar memoryBar;

    @FXML
    Label maxSpace;

    @FXML
    public Button closeMain;


    @FXML
    public void closeApp(ActionEvent actionEvent) {
        Stage stage = (Stage)closeMain.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openMain(MouseEvent mouseEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/pages/MainPage.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/mainPage.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(MainPageController.getUserPath().substring(0,10));
        try {

            //size of user folder in bytes
            try {
                long userMemory = WorkWithServer.getUsedMemory(MainPageController.getUserPath().substring(0,10));
                long bytesByUser = WorkWithServer.getMemoryByUser();
                maxSpace.setText(bytesByUser/1000000 + "Mb");
                kbytesUsed.setText(userMemory/1000 + " Kb");
                setProgressBar((double) userMemory/ (double) bytesByUser);
            }catch (ArithmeticException e){
                setProgressBar(0);
            }

        } catch (IOException e) {
            AlertController.getAlertController().showWarningAlert(e.getMessage());
        }
    }

    public void setProgressBar(double koef){
        memoryBar.setProgress(koef);
        if(koef > 0.5 && koef <= 0.8) memoryBar.setStyle("-fx-accent: yellow;");
        if(koef > 0.81) memoryBar.setStyle("-fx-accent: red;");
    }
}
