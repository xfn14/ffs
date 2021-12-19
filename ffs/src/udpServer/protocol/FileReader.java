package udpServer.protocol;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
    private final Logger logger = Logger.getLogger("FFSync");
    private ZonedDateTime lastReceived;
    private Date lastModified;
    private final String path;
    private final int len;
    private final int packetSize;
    private final int lastByte;
    private boolean received[];
    private byte[] data;

    public FileReader(String path, int len, int packetSize, Date lastModified, int lastByte){
        this.lastReceived = ZonedDateTime.now();
        this.lastModified = lastModified;
        this.path = path;
        this.len = len;
        this.packetSize = packetSize;
        this.lastByte = lastByte;
        this.received = new boolean[this.len];
        this.data = new byte[(this.packetSize * len) - this.packetSize + this.lastByte];
    }

    public void addFilePacket(FilePacket packet){
        if(packet.getLen() != this.len || this.received[packet.getId()]
        || this.lastModified.getTime() != packet.getLastModified().getTime()) return;
        this.lastReceived = ZonedDateTime.now();
        this.received[packet.getId()] = true;
        byte[] packetData = packet.getData();
        int start = this.packetSize * packet.getId();
        int len = packet.getId()+1 == this.len ? this.lastByte : packetData.length;
        System.arraycopy(packetData, 0, this.data, start, len);
    }

    public void writeFile(File root){
        String path = root.getPath() + "/" + this.path;
        File file = new File(path);
//        if(file.exists() && file.isFile()){
//            boolean rmFile = file.delete();
//            if(!rmFile) return false;
//        }
//        try {
//            boolean mkFile = file.createNewFile();
//            if(!mkFile) return false;
//        } catch (IOException e) {
//            this.logger.severe("Failed to create file " + this.path);
//            return false;
//        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(this.data);
        } catch (IOException e) {
            this.logger.severe("Failed to write file " + path);
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
