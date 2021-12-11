package udpServer;

import udpServer.packets.FilePacket;
import utils.NetUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FFManager implements Runnable {
    private Logger logger = Logger.getLogger("FFSync");
    private DatagramSocket socket;
    private UDPServer server;
    private List<UDPClient> clients;
    private boolean running = true;

    public FFManager(List<InetAddress> addrs){
        try{
            this.socket = new DatagramSocket(8888);
        } catch (SocketException e){
            logger.log(Level.SEVERE, "Failed to bind socket", e);
            return;
        }
        this.clients = new ArrayList<>();
        for(InetAddress addr : addrs){
            UDPClient client = new UDPClient(this.socket, addr);
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
            try {
                byte[] arr = NetUtils.objectToBytes(new FilePacket("tass bem rei"));
                DatagramPacket packet = new DatagramPacket(
                        arr, arr.length,
                        clients.get(0).getAddr(), 8888
                );
                System.out.println(Arrays.toString(packet.getData()));
                this.clients.get(0).sendPacket(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.socket.close();
        scanner.close();
    }

    public void stop(){
        this.running = false;
    }
}
