import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocket {
    private int port = 80;
    private InetAddress add;
    private DatagramSocket socket;

    public UDPSocket(InetAddress add) throws SocketException {
        this.add = add;
        this.socket = new DatagramSocket();
    }
}
