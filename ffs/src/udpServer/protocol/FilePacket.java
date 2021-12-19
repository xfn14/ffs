package udpServer.protocol;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class FilePacket extends FTRPacket implements Serializable {
    private final String path;
    private final Date lastModified;
    private final int id;
    private final int len;
    private final byte[] data;
    private final int lastByte;

    public FilePacket(String path, Date lastModified, int id, int len, byte[] data, int lastByte){
        super();
        this.path = path;
        this.lastModified = lastModified;
        this.id = id;
        this.len = len;
        this.data = data;
        this.lastByte = lastByte;
    }

    public String getPath() {
        return this.path;
    }

    public Date getLastModified() {
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

    public int getLastByte() {
        return this.lastByte;
    }

    @Override
    public String toString() {
        return "FilePacket{" +
                "path='" + path + '\'' +
                ", lastModified=" + lastModified +
                ", id=" + id +
                ", len=" + len +
                ", data=" + Arrays.toString(data) +
                ", lastByte=" + lastByte +
                '}';
    }
}
