package udpServer;

import udpServer.protocol.StatusPacket;
import utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FFManager implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final File root;
    private final List<File> files;
    private final int port = 8888;
    private DatagramSocket socket;
    private UDPServer server;
    private List<UDPClient> clients;
    private boolean running = true;
    private InetAddress localAddr = null;

    public FFManager(File root, List<File> files, List<InetAddress> addrs) {
        this.root = root;
        this.files = files;
        try{
            this.socket = new DatagramSocket(this.port);
            this.localAddr = NetUtils.getLocalAddress();
        } catch (SocketException e){
            logger.log(Level.SEVERE, "Failed to bind socket", e);
            return;
        }
        this.clients = new ArrayList<>();
        for(InetAddress addr : addrs){
            UDPClient client = new UDPClient(this.port, this.socket, addr);
            this.clients.add(client);
            this.logger.info("Connected to " + addr.getHostAddress());
        }
        this.server = new UDPServer(this.socket, this.clients);
    }

    public void broadcastMessage(String msg){
        for(UDPClient client : clients)
            client.sendPacket(msg);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Thread serverThread = new Thread(this.server);
        serverThread.start();
        while(this.running){
            String in = scanner.nextLine();
            if(in.equalsIgnoreCase("quit")){
                this.server.stop();
                this.stop();
                continue;
            }
            for(UDPClient client : this.clients){
                try {
                    byte[] arr = NetUtils.objectToBytes(
                            new StatusPacket(
                                    localAddr,
                                    client.getAddr(),
                                    this.port,
                                    this.root,
                                    this.files
                            )
                    );
                    client.sendPacket(new DatagramPacket(
                            arr, arr.length,
                            client.getAddr(), this.port
                    ));
                    this.logger.log(Level.INFO, "Status packet sent to " + client.getAddr().getHostName());
                } catch (IOException e) {
                    this.logger.log(Level.WARNING, "Failed to convert status packet to bytes");
                }
            }
        }
        this.socket.close();
        scanner.close();
    }

    public void stop(){
        this.running = false;
    }
}
