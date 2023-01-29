package com.vsivercloud.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageController {

    private static StorageController storageController;

    private StorageController() {
    }

    public static StorageController getStorageController() {
        if(storageController == null){
            storageController = new StorageController();
        }
        return storageController;
    }

    public void delete(DataInputStream dataInputStream) throws IOException {
        String path = dataInputStream.readUTF();
        File file = new File(StorageUtils.getPathToMainDir() + File.separator + path);
        file.delete();
    }

    public void addFolder(DataInputStream dataInputStream) throws IOException {
        String folderPath = dataInputStream.readUTF();
        new File(StorageUtils.getPathToMainDir() + File.separator + folderPath).mkdirs();
    }

    public void getFiles(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String userStoragePath = dataInputStream.readUTF();
        dataOutputStream.writeUTF(StorageUtils.returnFileList(userStoragePath));
    }

    public void uploadFile(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String fileName = dataInputStream.readUTF();
        String userName = dataInputStream.readUTF();
        boolean exists = StorageUtils.checkIfExists(fileName, userName);
        dataOutputStream.writeBoolean(exists);
        System.out.println("Exists");
        if(exists) return;
        System.out.println("Here");
        long size = dataInputStream.readLong();

        //check file size
        boolean isEnoughMemory = new MemoryUtils().checkIfEnoughMemory(size, userName);
        dataOutputStream.writeBoolean(isEnoughMemory); // true if memory available
        System.out.println(isEnoughMemory);
        if (!isEnoughMemory) return;

        //read and write
        FileOutputStream fileOutputStream = new FileOutputStream(StorageUtils.getPathToMainDir() + File.separator
                                                                 + userName + File.separator + fileName);

        //4. Пишем файл
        byte[] buffer = new byte[4 * 1024];
        int bytes = 0;
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }

        fileOutputStream.close();
        //success
        dataOutputStream.writeBoolean(true);
    }

    public void downloadFile(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String pathToFile = StorageUtils.getPathToMainDir() +
                            File.separator + dataInputStream.readUTF();
        System.out.println(pathToFile);

        dataOutputStream.writeLong(Files.size(Path.of(pathToFile)));
        dataInputStream.readBoolean();
        FileInputStream fileInputStream = new FileInputStream(pathToFile);
        byte[] chunk = new byte[4 * 1024];
        int bytesN = 0;
        while ((bytesN = fileInputStream.read(chunk)) != -1) {
            dataOutputStream.write(chunk, 0, bytesN);
            dataOutputStream.flush();
        }

        fileInputStream.close();
    }

    public void getMemory(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(new MemoryUtils().getBytesByAccount());
    }

    public void getUsedMemory(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        String userFolder = dataInputStream.readUTF();
        dataOutputStream.writeLong(StorageUtils.getFolderSize(new File(StorageUtils.getPathToMainDir() + File.separator + userFolder)));

    }
}
