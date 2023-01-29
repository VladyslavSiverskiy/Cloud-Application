package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.ValidationTools;
import tools.exceptions.UserWithSuchEmailExists;
import tools.workWithServer.RegisterRequest;

import javax.swing.text.LabelView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Class to create new user
 * */
public class RegistrationPageController implements Initializable {

    //variables to manipulate javaFx window
    private Stage stage;
    private Scene scene;
    private Parent root;


    //true if all fields are validated
    boolean readyToRegister = false;

    @FXML
    Button close_reg;

    @FXML
    TextField emailField;

    @FXML
    PasswordField createPasswordField;

    @FXML
    Label emailWarning;

    @FXML
    Label createPasswordWarning;
    @FXML
    PasswordField repeatPasswordField;

    @FXML
    Label repeatPasswordWarning;

    /**
     * Method to close window
     * */
    @FXML
    public void closeReg() {
        Stage stage = (Stage) close_reg.getScene().getWindow();
        stage.close();
    }

    /**
     * Return to login page
     * */
    public void backToSignInPage(ActionEvent actionEvent) {
        try {
            openLoginPage(actionEvent);
        } catch (IOException ex) {
            AlertController.getAlertController().showErrorAndStopApp(ex.getMessage());
        }
    }



    public void openLoginPage(ActionEvent actionEvent) throws IOException {
        //return to login
        root = FXMLLoader.load(getClass().getResource("/pages/loginPage.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/loginPage.css").toExternalForm());
        stage.setScene(scene);
        stage.getStyle();
        stage.show();
    }

    /**
     * Method which add handlers on email and password fields (to validate fields on input)
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkEmailField();
        checkPasswordField();
        checkRepeatPasswordField();
    }

    public void checkEmailField(){
        emailField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1){
                boolean isValid = ValidationTools.getValidationTools().validateEmailField(emailField.getText());
                if(!isValid){
                    emailField.setStyle("-fx-border-color: red");
                    emailWarning.setText("Enter your email in such format: username@domain");
                    readyToRegister = false;
                }else{
                    emailField.setStyle("-fx-border-color: none");
                    emailWarning.setText("");
                    readyToRegister = true;
                }
            }
        });
    }

    public void checkPasswordField(){
        createPasswordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                boolean isValid = ValidationTools.getValidationTools().validatePassword(createPasswordField.getText());
                if (!isValid) {
                    createPasswordField.setStyle("-fx-border-color: red");
                    createPasswordWarning.setText("Length should be from 8 to 20. At least one letter");
                    readyToRegister = false;
                } else {
                    createPasswordField.setStyle("-fx-border-color: none");
                    createPasswordWarning.setText("");
                    readyToRegister = true;
                }
            }
        });
    }

    public void checkRepeatPasswordField(){
        repeatPasswordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(!t1){
                boolean isValid = ValidationTools.getValidationTools().validatePassword(repeatPasswordField.getText()) && (repeatPasswordField.getText().equals(createPasswordField.getText()));
                if(!isValid){
                    repeatPasswordField.setStyle("-fx-border-color: red");
                    repeatPasswordWarning.setText("Passwords should be equal");
                    readyToRegister = false;
                }else{
                    repeatPasswordField.setStyle("-fx-border-color: none");
                    repeatPasswordWarning.setText("");
                    readyToRegister = true;
                }
            }
        });
    }

    /**
    * Method to register user in database
     * method run on button click
    * */
    public void registerUser(ActionEvent actionEvent) {
        if(emailField.getText().equals("") ||
           createPasswordField.getText().equals("") ||
           repeatPasswordField.equals("")){
            showWarnings("Fill this field");
        }else if(!readyToRegister){
            showWarnings("Check this field again");
        }else{
            //registration part
            try {
                /*
                *Use RegisterRequest and WorkWithServer classes to register user
                *if such user already exists, RegisterRequest.registerUser() will throw UserWithSuchEmailExists exception
                *if no such user in db, user will be registered
                * */
                RegisterRequest.registerUser(emailField.getText(), repeatPasswordField.getText());
                clearFields();
                AlertController.getAlertController().showInformationAlert("User was registered");
            }catch (UserWithSuchEmailExists | IOException e){
                Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public void showWarnings(String message){
        emailField.setStyle("-fx-border-color: red");
        emailWarning.setText(message);
        createPasswordField.setStyle("-fx-border-color: red");
        createPasswordWarning.setText(message);
        repeatPasswordField.setStyle("-fx-border-color: red");
        repeatPasswordWarning.setText(message);
    }

    public void clearFields(){
        emailField.setText("");
        repeatPasswordField.setText("");
        createPasswordField.setText("");
    }
}
