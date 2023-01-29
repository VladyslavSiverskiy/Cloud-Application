package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tools.ValidationTools;
import tools.exceptions.LoginException;
import tools.workWithServer.LoginRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



/**
 * Class to log in user and load main window of application
 *
 * */
public class LoginPageController implements Initializable {

    //variables which allow us make window movable
    private double xOffset = 0;
    private double yOffset = 0;

    //variables to manipulate javaFx elements
    private Stage stage;
    private Scene scene;
    private Parent root;

    //if all fields were verified make this variable true
    private boolean readyToLogin = false;

    //fxml elements
    @FXML
    Label emailWarning;
    @FXML
    Label passwordWarning;
    @FXML
    TextField emailField;
    @FXML
    Button close_login;
    @FXML
    PasswordField passwordField;
    @FXML
    Hyperlink forgotPassword; //TODO: implement this function

    /**Method to close login window
     * */
    @FXML
    public void closeLogin(){
        Stage stage = (Stage) close_login.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to open registration window
     * */
    @FXML
    public void goToRegistrationPage(ActionEvent actionEvent){
        try {
            openRegistrationPage(actionEvent);
        } catch (IOException e) {
            AlertController.getAlertController().showErrorAndStopApp("Unexpected error occurs...");
        }
    }


    /**
     * Method which add handlers on email and password fields (to validate fields on input)
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkEmailField();
        checkPassword();
    }

    private void checkEmailField(){
        emailField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1){
                //use validate method, validate field
                boolean isValid = ValidationTools.getValidationTools().validateEmailField(emailField.getText());

                //show or hide warnings
                if(!isValid){
                    emailField.setStyle("-fx-border-color: red");
                    emailWarning.setText("Enter your email in such format: username@domain");
                    readyToLogin = false;
                }else{
                    emailField.setStyle("-fx-border-color: none");
                    emailWarning.setText("");
                    readyToLogin = true;
                }
            }
        });
    }

    private void checkPassword(){
        passwordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(!t1){
                //use validate method, validate field
                boolean isValid = ValidationTools.getValidationTools().validatePassword(passwordField.getText());

                //show or hide warnings
                if(!isValid){
                    passwordField.setStyle("-fx-border-color: red");
                    passwordWarning.setText("Length should be from 8 to 20 or wrong format");
                    readyToLogin = false;
                }else{
                    passwordField.setStyle("-fx-border-color: none");
                    passwordWarning.setText("");
                    readyToLogin = true;
                }
            }
        });
    }


    /**
     * Method to login user, get his username and open main window
     * */
    public void loginUser(ActionEvent actionEvent){
        try {

            /*
            * Method send email and password to server using LoginRequest and WorkWithServer classes,
            * if there is no such user, LoginRequest.loginUser() will throw custom LoginException exception
            * if such user exists, LoginRequest.loginUser() will return username from server
            * */
            String userName = LoginRequest.loginUser(emailField.getText(), passwordField.getText());

            //load main window, pass username
            openMainPage(actionEvent, userName);

        }catch (IOException exception){
            AlertController.getAlertController().showError(exception.getMessage());
        }catch (LoginException exception) {
            //show warnings
            if(emailField.getText().equals("") || passwordField.getText().equals("")){
                showWarnings("Fill this field");
            }else if(!readyToLogin){
                showWarnings("Check this field again");
            }else{
                AlertController.getAlertController().showError(exception.getMessage());
            }
        }
    }
    public void showWarnings(String message){
        //to show warnings
        emailField.setStyle("-fx-border-color: red");
        emailWarning.setText(message);
        passwordField.setStyle("-fx-border-color: red");
        passwordWarning.setText(message);
    }


    public void openRegistrationPage(ActionEvent actionEvent) throws IOException {
        //open registration page
        root = FXMLLoader.load(getClass().getResource("/pages/registrationPage.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/loginPage.css").toExternalForm());
        stage.setScene(scene);
        stage.getStyle();
        stage.show();
    }

    /**
     * Method to open main window
     * */
    public void openMainPage(ActionEvent actionEvent,String userName) throws IOException {
        //set up a username on main page
        MainPageController.setUserPath(userName);

        //load main window
        root = FXMLLoader.load(getClass().getResource("/pages/MainPage.fxml"));
        makeWindowMovable();
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/mainPage.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        MainPageController.setStage(stage);
    }

    public void makeWindowMovable(){
        //to make window movable
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }
}
