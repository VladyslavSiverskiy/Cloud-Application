package tools.workWithServer;

import controllers.MainPageController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkWithServer {

    private static Socket clientSocket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void connect(String serverIP, int portNumber) throws Exception{
        try{
            clientSocket = new Socket(serverIP, portNumber);
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        }catch (Exception e){
            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();
        }
    }

    /*
    * write email and password string to output stream
    * return user folder name or FALSE
    * */
    public static String loginUser(String email, String password) throws IOException {
        //will sent next string: "email : password"
        dataOutputStream.writeUTF("LOGIN");
        dataOutputStream.writeUTF(email);
        dataOutputStream.writeUTF(password);
        String answer = dataInputStream.readUTF();
        return answer;
    }

    public static String getFiles(String path) throws IOException {
            dataOutputStream.writeUTF("GET_FILES");
            dataOutputStream.writeUTF(path);
            String res = dataInputStream.readUTF();
            return res;
    }

    /**
    * Method, which add selected file to remote storage.
    * Accept selected file from client
    *
    * Return true if file was added, false if something was wrong
    * */
    public static boolean sendFile(File file) throws IOException {
        dataOutputStream.writeUTF("UPLOAD");
        //send file name, check if such file exists
        dataOutputStream.writeUTF(file.getName());
        dataOutputStream.writeUTF(MainPageController.getUserPath());
        boolean exists = dataInputStream.readBoolean();
        if(exists) throw new IOException("File with such name already exists");

        dataOutputStream.writeLong(Files.size(Path.of(file.getPath())));
        boolean memoryAvailable = dataInputStream.readBoolean();
        if(!memoryAvailable) throw new IOException("No available memory");

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] chunk = new byte[4*1024];
        int bytes = 0;
        while((bytes = fileInputStream.read(chunk)) != -1) {
            dataOutputStream.write(chunk,0,bytes);
            dataOutputStream.flush();
        }

        fileInputStream.close();

        return dataInputStream.readBoolean();
    }

    public static void addFolder(String currentFolder, String newFolderName){
        try {
            dataOutputStream.writeUTF("ADD_FOLDER");
            dataOutputStream.writeUTF(currentFolder + newFolderName);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong...", ButtonType.CLOSE);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.CLOSE){
                System.exit(0);
            }
        }
    }

    public static void remove(String pathToFile){
        try {
            dataOutputStream.writeUTF("DELETE");
            dataOutputStream.writeUTF(pathToFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param pathToFile - name of file on server
     * @param folderToSaveTheFile  - location of folder on user computer
     * */
    public static void downloadFile(String pathToFile, String folderToSaveTheFile) throws IOException {
        try{
            System.out.println(pathToFile);
            System.out.println(folderToSaveTheFile);
            dataOutputStream.writeUTF("DOWNLOAD");
            dataOutputStream.writeUTF(pathToFile);

            long size = dataInputStream.readLong();
            String fileName = pathToFile.substring(pathToFile.lastIndexOf(File.separator));

            if(folderToSaveTheFile.charAt(folderToSaveTheFile.length()-1) == '\\'){
                folderToSaveTheFile = folderToSaveTheFile.substring(0,folderToSaveTheFile.length()-1);
            }

            String pathToWrite = folderToSaveTheFile + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(pathToWrite);
            //if program able to write file on the disk
            dataOutputStream.writeBoolean(true);

            //write file
            byte[] buffer = new byte[4*1024];
            int bytes = 0;
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer,0,bytes);
                size -= bytes;
            }

            fileOutputStream.close();

        }catch (IOException e){
            //terminate method on server
            dataOutputStream.writeBoolean(false);
            throw new IOException(e.getMessage());
        }

    }


    /**
     * Check if user with such email exists
     * */
    public static boolean checkIfUserExists(String email) throws IOException {
        dataOutputStream.writeUTF("CHECK_IF_EXIST");
        dataOutputStream.writeUTF(email);
        return dataInputStream.readBoolean(); // return true if exist
    }


    /**
     * Method to register user
     * @param email - email from registration page
     * @param password - password from registration page
     * @return true if user was register, false if wasn't
     * */
    public static boolean registerUser(String email, String password) throws IOException {
        dataOutputStream.writeUTF("REG_USER");
        dataOutputStream.writeUTF(email);
        dataOutputStream.writeUTF(password);
        return dataInputStream.readBoolean();
    }

    public static Long getUsedMemory(String userName) throws IOException {
        dataOutputStream.writeUTF("GET_USED_MEMORY");
        dataOutputStream.writeUTF(userName);
        return dataInputStream.readLong();
    }

    public static long getMemoryByUser() throws IOException {
        dataOutputStream.writeUTF("GET_MEMORY_BY_USER");
        return dataInputStream.readLong();
    }
}
