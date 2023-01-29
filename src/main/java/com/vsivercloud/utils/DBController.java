package com.vsivercloud.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DBController {

    private static DBController dbController;

    private DBController() {
    }

    public static DBController getDbController() {
        if (dbController == null){
            dbController = new DBController();
        }
        return dbController;
    }

    public void login(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, SQLException {
        String email = dataInputStream.readUTF();
        String password = dataInputStream.readUTF();
        String folder = DBTools.getUser(email,password);
        dataOutputStream.writeUTF(folder); //write username or FALSE
    }

    public void register(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, SQLException {
        String emailIn = dataInputStream.readUTF();
        String passwordIn = dataInputStream.readUTF();
        boolean wasRegistered = DBTools.registerUser(emailIn, passwordIn);
        String username = DBTools.getUser(emailIn, passwordIn);
        if (wasRegistered == true){
            new File(StorageUtils.getPathToMainDir() + File.separator + username).mkdirs();
        }
        dataOutputStream.writeBoolean(wasRegistered);
    }

    public void checkIfEmailExist(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, SQLException {
        String emailToCheck = dataInputStream.readUTF();
        dataOutputStream.writeBoolean(DBTools.isExist(emailToCheck));
    }
}
