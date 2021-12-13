package udpServer.protocol;

import java.net.InetAddress;

public class FilePacket extends FTRPacket {
    private int id;
    private byte[] data;

    public FilePacket(InetAddress origin, InetAddress destination, int port, int id, byte[] data){
        super(origin, destination, port);
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return this.id;
    }

    public byte[] getData() {
        return this.data;
    }
}
