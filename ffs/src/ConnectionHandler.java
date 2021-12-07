import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {
    private Logger logger = Logger.getLogger("FFSync");
    private DatagramSocket socket;
    private UDPServer server;
    private List<UDPClient> clients;
    private boolean running = true;

    public ConnectionHandler(DatagramSocket socket, List<InetAddress> addrs){
        this.socket = socket;
        this.server = new UDPServer(this.socket);
        Thread serverThread = new Thread(this.server);
        serverThread.start();
        this.clients = new ArrayList<>();
        for(InetAddress addr : addrs){
            UDPClient client = new UDPClient(this.socket, addr);
            this.clients.add(client);
            this.logger.info("Connected to " + addr.getHostAddress());
        }
    }

    public void broadcastMessage(String msg){
        for(UDPClient client : clients)
            client.sendPacket(msg);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(running){
            String temp = scanner.nextLine();
            this.broadcastMessage(temp);
        }
        socket.close();
        scanner.close();
    }

    public void stop(){
        this.running = false;
    }
}
