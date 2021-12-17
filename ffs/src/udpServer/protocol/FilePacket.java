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

    public FilePacket(String path, Date lastModified, int id, int len, byte[] data){
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

    @Override
    public String toString() {
        return "FilePacket{" +
                "path='" + this.path + '\'' +
                ", lastModified=" + this.lastModified +
                ", id=" + this.id +
                ", len=" + this.len +
                ", data=" + Arrays.toString(this.data) +
                '}';
    }
}
