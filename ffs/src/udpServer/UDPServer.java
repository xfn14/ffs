package udpServer;

import udpServer.protocol.FilePacket;
import udpServer.protocol.StatusPacket;
import utils.FileInfo;
import utils.FileUtils;
import utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UDPServer implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final int PACKET_MAX_SIZE = 200;
    private final DatagramSocket socket;
    private final File root;
    private final List<File> files;
    private final List<UDPClient> clients;
    private boolean running = true;
    private boolean working = false;

    public UDPServer(File root, List<File> files, DatagramSocket socket, List<UDPClient> clients){
        this.root = root;
        this.files = files;
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[this.PACKET_MAX_SIZE + 4000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(this.running){
            try {
                this.socket.receive(packet);
                byte[] arr = packet.getData();
                this.working = true;
                Object o = NetUtils.bytesToObject(arr);
                if(o instanceof StatusPacket){
                    StatusPacket statusPacket = (StatusPacket) o;
                    UDPClient client = this.clients.stream().filter(i -> i.getAddr().getHostAddress().equals(packet.getAddress().getHostAddress())).findFirst().orElse(null);
                    if(client == null){
                        this.logger.log(Level.SEVERE, "Status packet received from invalid client");
                        continue;
                    }

                    List<FileInfo> filesInfo = statusPacket.getFilesInfo();
                    List<String> fileNames = filesInfo.stream().map(FileInfo::getFileName).collect(Collectors.toList());
                    this.logger.log(Level.INFO, "Received status packet from " + packet.getAddress().getHostAddress());
                    this.logger.log(Level.INFO, "Files: " + fileNames);
                    for(File file : this.files){
                        String path = file.getPath().substring(root.getPath().length() + 1); // '/' -> +1
                        FileInfo fileInfo = filesInfo.stream().filter(i -> i.getFileName().equals(path)).findFirst().orElse(null);
                        if(fileInfo == null){
                            this.sendFileToClient(file, client);
                        }else{
                            if(fileInfo.getLastModified() != null){
                                try{
                                    Date fileDate = FileUtils.getFileDate(file);
                                    if(fileInfo.getLastModified().compareTo(fileDate) < 0){
                                        this.sendFileToClient(file, client);
                                        continue;
                                    }
                                }catch (IOException ignored){
                                    this.logger.log(Level.WARNING, "Failed to access " + file.getPath() + " date");
                                }
                            }
                            if(fileInfo.getSize() < file.length()){
                                this.sendFileToClient(file, client);
                            }
                        }
                    }
                }else if(o instanceof FilePacket){
                    FilePacket filePacket = (FilePacket) o;
                    this.logger.log(Level.INFO, filePacket.toString());
                }
            } catch (IOException | ClassNotFoundException e) {
                this.logger.log(Level.WARNING, "Error receiving packet.", e);
            }
            this.working = false;
        }
    }

    public void sendFileToClient(File file, UDPClient client){
        this.working = true;
        try {
            byte[] fileArr = FileUtils.fileToBytes(file);
            String path = file.getPath().substring(root.getPath().length() + 1);
            Date lastModified = FileUtils.getFileDate(file);
            int needed = (int) Math.ceil((double) fileArr.length / this.PACKET_MAX_SIZE);
            for (int i = 0; i < needed; i++) {
                byte[] data = new byte[this.PACKET_MAX_SIZE];
                int startPos = i * this.PACKET_MAX_SIZE;
                System.arraycopy(fileArr, startPos, data, 0,
                        Math.min(fileArr.length - startPos, this.PACKET_MAX_SIZE));
                FilePacket filePacket = new FilePacket(path, lastModified, i, needed, data);
                byte[] filePacketArr = NetUtils.objectToBytes(filePacket);
                client.sendBytes(filePacketArr);
                this.logger.log(Level.INFO, "Sent " + filePacket + " to " + client.getAddr().getHostAddress());
            }
        } catch (IOException e) {
            this.logger.warning("Failed to convert " + file.getPath() + " to byte array.");
        } finally {
            this.working = false;
        }
    }

    public void stop(){
        this.running = false;
        this.working = false;
    }

    public boolean isWorking() {
        return this.working;
    }
}
