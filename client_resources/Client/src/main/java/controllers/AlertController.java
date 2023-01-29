package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController {

    private static AlertController alertController;

    private AlertController() {
    }

    public static AlertController getAlertController() {
        if(alertController == null){
            alertController = new AlertController();
        }
        return alertController;
    }

    public void showInformationAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    public void showWarningAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    public void showErrorAndStopApp(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR,  message, ButtonType.CLOSE);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CLOSE){
            System.exit(0);
        }
    }
}

