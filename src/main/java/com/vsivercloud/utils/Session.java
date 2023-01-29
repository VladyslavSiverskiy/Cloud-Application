package com.vsivercloud.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

/*
*
* class that provide parallel executing of clients
*
* */
public class Session implements Runnable{

    private Socket clientSocket;

    Session(Socket socket){
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try(DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        ){
            while (true){
                // Server accept command from client
                String command = dataInputStream.readUTF();
                switch (command){
                    case "DELETE":
                        StorageController.getStorageController().delete(dataInputStream);
                        break;
                    case "ADD_FOLDER":
                        StorageController.getStorageController().addFolder(dataInputStream);
                        break;
                    case "LOGIN":
                        DBController.getDbController().login(dataInputStream, dataOutputStream);
                        break;
                    case "GET_FILES":
                        StorageController.getStorageController().getFiles(dataInputStream,dataOutputStream);
                        break;
                    case "UPLOAD":
                        StorageController.getStorageController().uploadFile(dataInputStream,dataOutputStream);
                        break;
                    case "DOWNLOAD":
                        StorageController.getStorageController().downloadFile(dataInputStream,dataOutputStream);
                        break;
                    case "CHECK_IF_EXIST":
                        DBController.getDbController().checkIfEmailExist(dataInputStream, dataOutputStream);
                        break;
                    case "REG_USER":
                        DBController.getDbController().register(dataInputStream,dataOutputStream);
                        break;
                    case "GET_USED_MEMORY":
                        StorageController.getStorageController().getUsedMemory(dataInputStream, dataOutputStream);
                        break;
                    case "GET_MEMORY_BY_USER":
                        StorageController.getStorageController().getMemory(dataOutputStream);
                        break;
                }
            }

        }catch (SocketException e){
            System.out.println("End of session");
        }catch (IOException | SQLException e){
            e.printStackTrace();
            System.out.println("End of session, problem with db...");
        }
    }
}
