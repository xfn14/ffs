package udpServer.protocol;

import utils.SecurityUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class FilePacket extends FTRPacket implements Serializable {
    private final String path;
    private final Date startTime;
    private final Date lastModified;
    private final long checksum;
    private final int id;
    private final int len;
    private final byte[] data;
    private final int lastByte;

    public FilePacket(String prefix, String path, Date startTime, Date lastModified, int id, int len, byte[] data, int lastByte){
        super(prefix);
        this.path = path;
        this.startTime = startTime;
        this.lastModified = lastModified;
        this.id = id;
        this.len = len;
        this.data = data;
        this.lastByte = lastByte;
        this.checksum = SecurityUtils.getCRC32Checksum(data);
    }

    public String getPath() {
        return this.path;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public long getChecksum() {
        return this.checksum;
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
                "path='" + this.path + '\'' +
                ", startTime=" + this.startTime +
                ", lastModified=" + this.lastModified +
                ", id=" + this.id +
                ", len=" + this.len +
                ", data=" + Arrays.toString(this.data) +
                ", lastByte=" + this.lastByte +
                '}';
    }
}
