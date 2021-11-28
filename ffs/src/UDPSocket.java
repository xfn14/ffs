import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocket {
    private int port = 80;
    private InetAddress add;
    public DatagramSocket socket;

    public UDPSocket(InetAddress add) throws SocketException {
        this.add = add;
        this.socket = new DatagramSocket(this.port);
    }

    public void sendPacket(String msg) throws IOException {
        byte[] buffer = msg.getBytes();
        DatagramPacket p = new DatagramPacket(buffer, buffer.length, this.add, this.port);
        this.socket.send(p);
    }

    public void receivePacket() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(p);
        System.out.println(new String(p.getData()));
    }

    public void close(){
        this.socket.close();
    }
}
