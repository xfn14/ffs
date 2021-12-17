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
    private final DatagramSocket socket;
    private final File root;
    private final List<File> files;
    private boolean running = true;
    private final List<UDPClient> clients;

    public UDPServer(File root, List<File> files, DatagramSocket socket, List<UDPClient> clients){
        this.root = root;
        this.files = files;
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
                    System.out.println(filePacket);
                }
            } catch (IOException | ClassNotFoundException e) {
                this.logger.log(Level.WARNING, "Error receiving packet.", e);
            }
        }
    }

    public boolean sendFileToClient(File file, UDPClient client){
        this.logger.log(Level.INFO, "Need to send " + file.getPath() + " to " + client.getAddr().getHostAddress());
        return false;
    }

    public void stop(){
        this.running = false;
    }
}
