package udpServer.protocol;

import java.io.Serializable;
import java.net.InetAddress;

public class FTRPacket implements Serializable {
    private final InetAddress origin;
    private final InetAddress destination;
    private final int port;

    public FTRPacket(InetAddress origin, InetAddress destination, int port){
        this.origin = origin;
        this.destination = destination;
        this.port = port;
    }

    public InetAddress getOrigin() {
        return this.origin;
    }

    public InetAddress getDestination() {
        return this.destination;
    }

    public int getPort() {
        return this.port;
    }
}
