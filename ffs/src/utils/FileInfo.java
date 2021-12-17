package utils;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    private final String fileName;
    private final long size;
    private final Date lastModified;

    public FileInfo(String fileName, long size, Date lastModified){
        this.fileName = fileName;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return this.fileName;
    }

    public long getSize() {
        return this.size;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                ", lastModified=" + lastModified +
                '}';
    }
}
