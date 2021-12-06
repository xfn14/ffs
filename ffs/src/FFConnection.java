import java.net.DatagramSocket;
import java.net.InetAddress;

public class FFConnection {
    private DatagramSocket socket;
    private InetAddress addr;
    private UDPServer server;
    private UDPClient client;

    public FFConnection(DatagramSocket socket, InetAddress addr){
        this.socket = socket;
        this.addr = addr;
        this.server = new UDPServer(this.socket, this.addr);
        this.client = new UDPClient(this.socket, this.addr);
    }

    public void connect() {
        Thread serverThread = new Thread(this.server);
        serverThread.start();
        Thread clientThread = new Thread(this.client);
        clientThread.start();
    }
}