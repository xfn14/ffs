package udpServer;

import udpServer.protocol.FilePacket;
import udpServer.protocol.StatusPacket;
import utils.NetUtils;

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
                byte[] arr = packet.getData();
                Object o = NetUtils.bytesToObject(arr);
                if(o instanceof StatusPacket){
                    StatusPacket statusPacket = (StatusPacket) o;
                    this.logger.log(Level.INFO, "Received status packet from " + statusPacket.getOrigin().getHostAddress());
                    this.logger.log(Level.INFO, "Files info: " + statusPacket.getFilesInfo());
                }else if(o instanceof FilePacket){
                    FilePacket filePacket = (FilePacket) o;
                    System.out.println(filePacket);
                }
//                String in = new String(packet.getData());
//                this.logger.log(Level.INFO, "From " + packet.getAddress().getHostAddress() + ": " + in);
            } catch (IOException | ClassNotFoundException e) {
                this.logger.log(Level.WARNING, "Error receiving packet.", e);
            }
        }
    }

    public void stop(){
        this.running = false;
    }
}