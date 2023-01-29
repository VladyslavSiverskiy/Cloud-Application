package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.FilesTableModel;
import tools.FilesTableTools;
import tools.ValidationTools;
import tools.workWithServer.WorkWithServer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class, which represents main window of application.
 * */
public class MainPageController implements Initializable {

    /*static variable of FilesTableTools, contains observable list (getTableData())
    *to fill the TableView
    * */
    private static FilesTableTools tableData = new FilesTableTools();

    //full path to folder
    private static String userPath;

    private static Stage stage;

    @FXML
    private Label userNameLabel;
    @FXML
    private Button closeMain;
    @FXML
    private Label pathToFolder;
    @FXML
    private TableView filesTable;

    @FXML
    private TableColumn<FilesTableModel, String> size;
    @FXML
    private TableColumn<FilesTableModel, String> date;
    @FXML
    private TableColumn<FilesTableModel, String> files;
    @FXML
    private Label fileChoose;


    public static void setUserPath(String name){
        userPath = name;
    }

    public static void setStage(Stage stageName){
        stage = stageName;
    }

    public static String getUserPath(){
        return userPath;
    }

    public static ObservableList<FilesTableModel> getTableData() {
        return tableData.tableData;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(userPath.replaceAll("[\\\\/]",""));
        if(!String.valueOf(userPath.charAt(userPath.length()-1)).equals(File.separator)){
            userPath+=File.separator;
        }
        getTableData().clear();
        pathToFolder.setText(getUserPath());

        files.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        date.setCellValueFactory(new PropertyValueFactory<>("creatingDate"));
        size.setCellValueFactory(new PropertyValueFactory<>("sizeOfFile"));

        tableData.fillTableData(getUserPath());

        filesTable.setItems(getTableData());

        filesTable.setRowFactory(tv->{
            TableRow<FilesTableModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && row.getItem().isFolder() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    System.out.println(row.getItem().isFolder());
                    userPath += row.getItem().getFileName() + File.separator;
                    pathToFolder.setText(userPath);
                    refreshTable();
                }


                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    FilesTableModel clickedRow = row.getItem();
                    changeFileChoseLabel(clickedRow.getFileName().toString());
                }
            });
            return row;
        });
    }

    /**
     * Change name of file, which was selected to deleting or downloading
     * */
    private void changeFileChoseLabel(String filename){
        fileChoose.setText(filename);
    };

    /**
     * Update data in table
     * */
    public void refreshTable(){
        getTableData().clear();
        tableData.fillTableData(getUserPath());
        filesTable.setItems(getTableData());
    }

    /**
     * Close window
     * */
    @FXML
    private void closeApp() {
        Stage stage = (Stage)closeMain.getScene().getWindow();
        stage.close();
    }


    /**
     * Upload file to server
     * */
    public void choseFile(ActionEvent e){
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile == null){
                AlertController.getAlertController().showInformationAlert("You haven`t selected a file");
                return;
            }
            if(WorkWithServer.sendFile(selectedFile)){
                refreshTable();
                AlertController.getAlertController().showInformationAlert("Was uploaded!");
            }
        }catch (IOException ioException){
            AlertController.getAlertController().showError(ioException.getMessage());
        }catch (Exception exception){
            exception.printStackTrace();
            AlertController.getAlertController().showError(exception.getMessage());
        }
    }

    /**
     * Method, which allows us to move through folder tree
     * */
    public void moveBack(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile("[/\\\\]\\w+[/\\\\]$");
        Matcher matcher = pattern.matcher(pathToFolder.getText());

        while (matcher.find()){
            userPath = userPath.substring(0,matcher.start()+1);
            pathToFolder.setText(userPath);
            refreshTable();
        }
    }

    public void showModalFolderName(ActionEvent actionEvent) {
        try{
            openFolderNameModal(actionEvent);
        }catch (Exception e){
            AlertController.getAlertController().showWarningAlert(e.getMessage());
        }
    }

    public void openFolderNameModal(ActionEvent actionEvent) throws IOException {
        Stage modalStage = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("/pages/FolderNameModal.fxml"));
        modalStage.setTitle("Create new folder");
        modalStage.setResizable(false);
        modalStage.setScene(new Scene(root2));
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        modalStage.show();
    }

    public void deleteBtn() {
        WorkWithServer.remove(getUserPath() + fileChoose.getText());
        refreshTable();
    }


    /**
     * Download file from server
     * */
    public void downloadFile() {
        if(verifyChosenFile()){ //verify if user select file (not folder)

            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose the download location:");
            File defaultDirectory = new File("./");
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(stage);

            //download file
            if(selectedDirectory != null){
                try {
                    WorkWithServer.downloadFile(getUserPath() + fileChoose.getText(),selectedDirectory.getPath());
                    AlertController.getAlertController().showInformationAlert("Was downloaded!");
                } catch (IOException e) {
                    AlertController.getAlertController().showError(e.getMessage());
                }
            }
        }else{
            AlertController.getAlertController().showWarningAlert("Please, choose file that you want to download." + " You can't download folder");
        }
    }
    public boolean verifyChosenFile(){
        System.out.println(fileChoose.getText());
        return fileChoose.getText().length() > 0 && (fileChoose.getText().matches("^([\\w() $&+,:;=?@#|'<>.^*()%!-]+)(\\.[^\\\\]+)$"));
    }

    public void openStats(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pages/MemoryStats.fxml")));
            stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style/mainPage.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            AlertController.getAlertController().showErrorAndStopApp(e.getMessage());
        }
    }
}
