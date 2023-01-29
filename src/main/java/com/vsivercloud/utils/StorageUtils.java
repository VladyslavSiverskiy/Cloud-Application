package com.vsivercloud.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class StorageUtils {

    private static final File PATH_TO_MAIN_DIR = new File(Paths.get("").toAbsolutePath()
                                                          + File.separator + "src" + File.separator + "Storage");

    public static String getPathToMainDir() {
        return PATH_TO_MAIN_DIR.getPath();
    }

    public static String returnFileList(String directory){
        File rootDirectory = new File(PATH_TO_MAIN_DIR + File.separator + directory);
        File[] filesInFolder = rootDirectory.listFiles();
        StringBuilder stringBuilder = new StringBuilder(); // type, filename, size, date,
        for (File file: filesInFolder) {

            if(file.isDirectory()){
                stringBuilder.append("folder,");
                stringBuilder.append(file.getName() + ",");
                try{
                    stringBuilder.append(StorageUtils.getFolderSize(file) + ",");
                }catch (IOException e){
                    stringBuilder.append("0");
                }

            }else {
                stringBuilder.append("file,");
                stringBuilder.append(file.getName() + ",");

                try {
                    stringBuilder.append(Files.size(Path.of(file.getPath())) +",");
                } catch (IOException e) {
                    stringBuilder.append("0");
                }
            }

            try {
                FileTime fileTime = (FileTime) Files.getAttribute(Path.of(file.getPath()), "creationTime");
                stringBuilder.append(fileTime.toMillis());
            } catch (IOException e) {
                stringBuilder.append(new Date(0));
            }

            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static long getFolderSize(File folder) throws IOException {
        long size = 0;
        File[] filesInFolder = folder.listFiles();
        for(File file: filesInFolder){
            if(file.isDirectory()){
                size += getFolderSize(file);
            }else{
                size += Files.size(Path.of(file.getPath()));
            }
        }

        return size;
    }


    public static boolean checkIfExists(String fileName, String pathToDirectory){
        if(new File(PATH_TO_MAIN_DIR + File.separator
                + pathToDirectory + File.separator + fileName).exists()) return true;
        return false;
    }
}
