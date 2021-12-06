import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer implements Runnable {
    private Logger logger = Logger.getLogger("FFSync");
    private DatagramSocket socket;
    private InetAddress addr;
    private boolean running = true;

    public UDPServer(DatagramSocket socket, InetAddress addr){
        this.socket = socket;
        this.addr = addr;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[2024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(this.running){
            try {
                this.socket.receive(packet);
                String in = new String(packet.getData());
                this.logger.log(Level.INFO, "From " + packet.getAddress().getHostAddress() + ": " + in);
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "UDP Server failure, socket might be closed (" + this.addr.getHostAddress() + ").", e);
            }
        }
        socket.close();
    }
}
