package udpServer;

import udpServer.protocol.StatusPacket;
import utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FFManager implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final File root;
    private final List<File> files;
    private DatagramSocket socket;
    private UDPServer server;
    private List<UDPClient> clients;
    private boolean running = true;
    private InetAddress localAddr = null;

    public FFManager(int port, File root, List<File> files, List<InetAddress> addrs) {
        this.root = root;
        this.files = files;
        try{
            this.socket = new DatagramSocket(port);
            this.localAddr = NetUtils.getLocalAddress();
        } catch (SocketException e){
            logger.log(Level.SEVERE, "Failed to bind socket", e);
            return;
        }
        this.clients = new ArrayList<>();
        for(InetAddress addr : addrs){
            UDPClient client = new UDPClient(port, this.socket, addr);
            this.clients.add(client);
            this.logger.info("Connected to " + addr.getHostAddress());
        }
        this.server = new UDPServer(this.root, this.files, this.socket, this.clients);
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
        ZonedDateTime lastRun = null;
        while(this.running){
            ZonedDateTime now = ZonedDateTime.now();
            if(lastRun == null || now.isAfter(lastRun.plusSeconds(5)) && !this.server.isReceivingFiles()){
                lastRun = now;
                for(UDPClient client : this.clients){
                    try {
                        byte[] arr = NetUtils.objectToBytes(new StatusPacket(this.root, this.files));
                        client.sendBytes(arr);
                        this.logger.log(Level.INFO, "Status packet sent to " + client.getAddr().getHostName());
                    } catch (IOException e) {
                        this.logger.log(Level.WARNING, "Failed to convert status packet to bytes", e);
                    }
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
