package com.vsivercloud.utils;

import java.io.File;
import java.io.IOException;

public class MemoryUtils {

    private static final Integer MAX_USERS_COUNT = 10;

    private static final Long AVAILABLE_MEMORY = 1048576 * 1000L; //(bytes in 1 mb * mb count) 1 GB of memory

    private static final Long MEMORY_BY_ACCOUNT = AVAILABLE_MEMORY / MAX_USERS_COUNT;

    public Long getBytesByAccount(){
        return MEMORY_BY_ACCOUNT;
    }


    public Long getMBytesByAccount(){
        return MEMORY_BY_ACCOUNT/1000000;
    }

    public Long getBytesAvailable(){
        return AVAILABLE_MEMORY;
    }


    public Long getMBytesAvailable(){
        return AVAILABLE_MEMORY/1000000;
    }

    public boolean checkIfEnoughMemory(long sizeOfUserFile, String pathToFolder) throws IOException {
        long sizeOfExistingFiles = StorageUtils.getFolderSize(new File(StorageUtils.getPathToMainDir() + File.separator + pathToFolder));
        System.out.println(sizeOfExistingFiles);
        System.out.println(sizeOfUserFile);
        System.out.println(getBytesByAccount());
        System.out.println(pathToFolder);
        if(sizeOfExistingFiles + sizeOfUserFile > getBytesByAccount()) return false;
        return true;
    }

}
