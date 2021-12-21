package udpServer.protocol;

import java.io.*;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
    private final Logger logger = Logger.getLogger("FFSync");
    private ZonedDateTime lastReceived;
    private final Date lastModified;
    private final Date startTime;
    private final String path;
    private final int len;
    private final int packetSize;
    private final int lastByte;
    private int totalBytesUsed;
    private boolean[] received;
    private byte[] data;

    public FileReader(String path, Date startTime, int len, int packetSize, Date lastModified, int lastByte){
        this.lastReceived = ZonedDateTime.now();
        this.startTime = startTime;
        this.lastModified = lastModified;
        this.path = path;
        this.len = len;
        this.packetSize = packetSize;
        this.lastByte = lastByte;
        this.totalBytesUsed = 0;
        this.received = new boolean[this.len];
        this.data = new byte[(this.packetSize * len) - this.packetSize + this.lastByte];
    }

    public void addFilePacket(FilePacket packet){
        if(packet.getLen() != this.len || this.received[packet.getId()]
        || this.lastModified.getTime() != packet.getLastModified().getTime())return;
        this.lastReceived = ZonedDateTime.now();
        this.received[packet.getId()] = true;
        byte[] packetData = packet.getData();
        this.totalBytesUsed += packetData.length;
        int start = this.packetSize * packet.getId();
        int len = packet.getId()+1 == this.len ? this.lastByte : packetData.length;
        System.arraycopy(packetData, 0, this.data, start, len);
    }

    public void writeFile(File root){
        Date endTime = new Date();
        long time = (endTime.getTime() - startTime.getTime());
        float speed = ((float) (this.totalBytesUsed * 8)) / ((float) time/1000);
        String path = root.getPath() + "/" + this.path;
        File file = new File(path);
        try {
            Files.createDirectories(file.toPath().getParent());
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to create folder", e);
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(this.data);
            this.logger.info("File " + this.path + " has been written to " + root.getPath());
            this.logger.info("Transfer took " + time + "ms. " + speed + "bits/s");
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to write file " + path, e);
        }
        boolean fileMod = file.setLastModified(this.lastModified.getTime());
        if(!fileMod) this.logger.log(Level.SEVERE, "Failed to change file " + path + " last modified date.");
    }

    public String getPath() {
        return this.path;
    }

    public int getLen() {
        return this.len;
    }

    public boolean isComplete() {
        for (boolean received : this.received) {
            if(!received) return false;
        } return true;
    }

    public byte[] getData() {
        return this.data;
    }

    public ZonedDateTime getLastReceived() {
        return this.lastReceived;
    }

    public void setLastReceived(ZonedDateTime lastReceived) {
        this.lastReceived = lastReceived;
    }
}
