package models;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;


/**
 * Model that represent data structure in observable list and TableView
 * */
public class FilesTableModel {
    private File file;
    private Date creatingDate;
    private long sizeOfFile;

    public File getFileName() {
        return file;
    }

    public Date getCreatingDate() {
        return creatingDate;
    }

    public long getSizeOfFile() {
        return sizeOfFile;
    }

    public boolean isFolder(){
        return this.file.isDirectory();
    }

    public FilesTableModel(File fileName, Date creatingDate, long sizeOfFile) {
        this.file = fileName;
        this.creatingDate = creatingDate;
        this.sizeOfFile = sizeOfFile;
    }
}
