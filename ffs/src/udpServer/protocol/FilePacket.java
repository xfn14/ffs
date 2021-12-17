package udpServer.protocol;

import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;

public class FilePacket extends FTRPacket implements Serializable {
    private final String path;
    private final FileTime lastModified;
    private final int id;
    private final int len;
    private final byte[] data;

    public FilePacket(String path, FileTime lastModified, int id, int len, byte[] data){
        super();
        this.path = path;
        this.lastModified = lastModified;
        this.id = id;
        this.len = len;
        this.data = data;
    }

    public String getPath() {
        return this.path;
    }

    public FileTime getLastModified() {
        return lastModified;
    }

    public int getId() {
        return this.id;
    }

    public int getLen() {
        return this.len;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "FilePacket{" +
                "path='" + path + '\'' +
                ", lastModified=" + lastModified +
                ", id=" + id +
                ", len=" + len +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
