package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.FilesTableModel;
import tools.FilesTableTools;
import tools.ValidationTools;
import tools.workWithServer.WorkWithServer;

import java.net.URL;
import java.util.ResourceBundle;

public class FolderNameController implements Initializable {

    @FXML
    Label warningLabel;

    @FXML
    TextField folderName;

    private boolean readyToCreate = false;
    public void createFolder(ActionEvent actionEvent){
        if(readyToCreate){
            WorkWithServer.addFolder(MainPageController.getUserPath(), folderName.getText());
            Stage stage = (Stage)folderName.getScene().getWindow();
            stage.close();
            try {
                new MainPageController().refreshTable();
            }catch (Exception e){

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        folderName.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1){
                boolean isValid = ValidationTools.getValidationTools().validateFolderName(folderName.getText());
//                System.out.println(isValid);
                if(!isValid){
                    folderName.setStyle("-fx-border-color: red");
                }else{
                    if(checkIfExist(folderName.getText())){
                        warningLabel.setText("such folder already exists");
                        folderName.setStyle("-fx-border-color: red");
                    }else{
                        warningLabel.setText("");
                        folderName.setStyle("-fx-border-color: none");
                        readyToCreate = true;
                    }
                    folderName.setStyle("-fx-border-color: none");
                    readyToCreate = true;
                }
            }
        });
    }

    public static boolean checkIfExist(String folderName){
        for(FilesTableModel model: MainPageController.getTableData()){
            if(model.isFolder() && (folderName.equals(model.getFileName().getName()))){
                return true;
            }
        }
        return false;
    }
}
