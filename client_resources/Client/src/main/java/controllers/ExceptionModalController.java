package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ExceptionModalController {

    @FXML
    Label errorLabel;

    public void setMessage(String message){
        errorLabel.setText(message);
    }
}
