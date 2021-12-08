import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final DatagramSocket socket;
    private boolean running = true;
    private List<UDPClient> clients;

    public UDPServer(DatagramSocket socket, List<UDPClient> clients){
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(this.running){
            try {
                this.socket.receive(packet);
                String in = new String(packet.getData());
                this.logger.log(Level.INFO, "From " + packet.getAddress().getHostAddress() + ": " + in);
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Server stoped.");
                this.stop();
                break;
            }
        }
    }

    public void stop(){
        this.running = false;
    }
}
