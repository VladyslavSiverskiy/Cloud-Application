package tools;

import controllers.AlertController;
import controllers.MainPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.FilesTableModel;
import tools.workWithServer.WorkWithServer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;


/**
 * Class, which get info about all user files from server and represent them
 * */
public class FilesTableTools {

    //list for javafx TableView
    public ObservableList<FilesTableModel> tableData = FXCollections.observableArrayList();


    public void fillTableData(String path){

        String result;
        //send req to server, get arraylist of files
        try {
            result = WorkWithServer.getFiles(path);
            fillTable(result);
        } catch (IOException e) {
            AlertController.getAlertController().showErrorAndStopApp(e.getMessage());
        }
    }

    //fill observable list from server data
    public void fillTable(String response){
        //parse data and show it
        String[] rows = response.equals("") ? new String[0] : response.split("\n");
        for (String row: rows){
            String[] cells = row.split(",");
            if(cells[0].equals("folder")){
                File dir = new File(cells[1]);
                dir.mkdirs();
                tableData.add(new FilesTableModel(dir,new Date(Long.valueOf(cells[3])), Long.valueOf(cells[2])));
            }else{
                tableData.add(new FilesTableModel(new File(cells[1]),new Date(Long.valueOf(cells[3])), Long.valueOf(cells[2])));
            }
        }
    }
}
