package udpServer.protocol;

import java.nio.file.attribute.FileTime;

public class FileInfo {
    private String fileName;
    private long size;
    private FileTime lastModified;

    public FileInfo(String fileName, long size, FileTime lastModified){
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

    public FileTime getLastModified() {
        return this.lastModified;
    }
}
