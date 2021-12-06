import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient implements Runnable {
    private Logger logger = Logger.getLogger("FFSync");
    private DatagramSocket socket;
    private InetAddress addr;
    private boolean running = true;

    public UDPClient(DatagramSocket socket, InetAddress addr){
        this.socket = socket;
        this.addr = addr;
    }

    @Override
    public void run() {
        int i = 0;
        Scanner scanner = new Scanner(System.in);
        while(this.running){
            if(i++ == 10) this.running = false;
            String in = scanner.nextLine();
            byte[] data = in.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, this.addr, 8888);
            try {
                this.socket.send(packet);
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Failed to send packet to " + this.addr.getHostAddress(), e);
            }
        }
        scanner.close();
    }
}
