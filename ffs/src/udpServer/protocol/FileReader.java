package udpServer.protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;

public class FileReader {
    private ZonedDateTime lastReceived;
    private Date lastModified;
    private final String path;
    private final int len;
    private final int packetSize;
    private boolean received[];
    private byte[] data;

    public FileReader(String path, int len, int packetSize, Date lastModified){
        this.lastReceived = ZonedDateTime.now();
        this.lastModified = lastModified;
        this.path = path;
        this.len = len;
        this.packetSize = packetSize;
        this.received = new boolean[this.len];
        this.data = new byte[this.packetSize * len];
    }

    public void addFilePacket(FilePacket packet){
        if(packet.getLen() != this.len || this.received[packet.getId()]
        || this.lastModified.getTime() != packet.getLastModified().getTime()) return;
        this.lastReceived = ZonedDateTime.now();
        this.received[packet.getId()] = true;
        byte[] packetData = packet.getData();
        int start = this.packetSize * packet.getId();
        System.arraycopy(packetData, 0, this.data, start, packetData.length);
    }

    public void writeFile(File root){
        File outputFile = new File(root.getPath() + "/" + this.path + ".txt");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(this.data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFile.setLastModified(this.lastModified.getTime());
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
