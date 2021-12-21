package udpServer;

import udpServer.protocol.FilePacket;
import udpServer.protocol.FileReader;
import udpServer.protocol.StatusPacket;
import utils.FileInfo;
import utils.FileUtils;
import utils.NetUtils;
import utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UDPServer implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final int PACKET_MAX_SIZE = 2048;
    private final DatagramSocket socket;
    private final File root;
    private List<File> files;
    private final List<UDPClient> clients;
    private boolean running = true;
    private final Map<String, FileReader> fileReader = new HashMap<>();

    public UDPServer(File root, List<File> files, DatagramSocket socket, List<UDPClient> clients){
        this.root = root;
        this.files = files;
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        this.timeoutFileReader();
        byte[] buffer = new byte[this.PACKET_MAX_SIZE + 5000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        String hash = SecurityUtils.getStringSHA1("FF-Transfer Protocol");
        while(this.running){
            try {
                this.socket.receive(packet);
                byte[] arr = packet.getData();
                Object o = NetUtils.bytesToObject(arr);
                if(o instanceof StatusPacket){
                    if(!this.fileReader.isEmpty()) continue;
                    StatusPacket statusPacket = (StatusPacket) o;
                    if(!hash.equals(statusPacket.getHash())){
                        this.logger.severe("Received invalid hash in packet " + statusPacket);
                        continue;
                    }
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
                } else if(o instanceof FilePacket) {
                    FilePacket filePacket = (FilePacket) o;
                    if(!hash.equals(filePacket.getHash())){
                        this.logger.severe("Received invalid hash in packet " + filePacket);
                        continue;
                    }
                    long checksum = SecurityUtils.getCRC32Checksum(filePacket.getData());
                    if(checksum != filePacket.getChecksum()){
                        this.logger.warning("Invalid checksum in packet " + filePacket);
                        continue;
                    }
                    this.logger.info("Received file packet " + filePacket.getId() + "/" + filePacket.getLen() + " for " + filePacket.getPath());
                    FileReader fileReader;
                    if(this.fileReader.containsKey(filePacket.getPath())){
                        fileReader = this.fileReader.get(filePacket.getPath());
                        fileReader.addFilePacket(filePacket);
                        if(fileReader.isComplete()){
                            fileReader.writeFile(this.root);
                            this.files = FileUtils.getFiles(this.root);
                            this.fileReader.remove(filePacket.getPath());
                        }else this.fileReader.put(fileReader.getPath(), fileReader);
                    }else{
                        fileReader = new FileReader(
                                filePacket.getPath(),
                                filePacket.getStartTime(),
                                filePacket.getLen(),
                                this.PACKET_MAX_SIZE,
                                filePacket.getLastModified(),
                                filePacket.getLastByte()
                        );
                        fileReader.addFilePacket(filePacket);
                        if(fileReader.isComplete()){
                            fileReader.writeFile(this.root);
                            this.files = FileUtils.getFiles(this.root);
                        } else this.fileReader.put(fileReader.getPath(), fileReader);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                this.logger.log(Level.WARNING, "Error receiving packet.", e);
            }
        }
    }

    public void timeoutFileReader(){
        new Thread(() -> {
            while(this.running){
                if(!this.fileReader.isEmpty()){
                    ZonedDateTime now = ZonedDateTime.now();
                    List<String> toRemove = new ArrayList<>();
                    for(FileReader fileReader : this.fileReader.values()){
                        // Timeout at 5 minutes
                        if(now.isAfter(fileReader.getLastReceived().plusMinutes(5))){
                            this.logger.warning("File " + fileReader.getPath() + " is no longer on read list");
                            toRemove.add(fileReader.getPath());
                        }
                        // TODO At 2 minutes ask for missing files
                    }
                    toRemove.forEach(this.fileReader::remove);
                }
            }
        }).start();
    }

    public void sendFileToClient(File file, UDPClient client){
        new Thread(() -> {
            try {
                byte[] fileArr = FileUtils.fileToBytes(file);
                String path = file.getPath().substring(root.getPath().length() + 1);
                Date lastModified = FileUtils.getFileDate(file);
                Date startTime = new Date();
                int needed = (int) Math.ceil((double) fileArr.length / PACKET_MAX_SIZE);
                for (int i = 0; i < needed; i++) {
                    byte[] data = new byte[PACKET_MAX_SIZE];
                    int startPos = i * PACKET_MAX_SIZE;
                    System.arraycopy(fileArr, startPos, data, 0,
                            Math.min(fileArr.length - startPos, PACKET_MAX_SIZE));
                    FilePacket filePacket = new FilePacket("FF-Transfer Protocol", path, startTime, lastModified, i, needed, data, fileArr.length - (needed-1)*this.PACKET_MAX_SIZE);
                    byte[] filePacketArr = NetUtils.objectToBytes(filePacket);
                    client.sendBytes(filePacketArr);
                    logger.log(Level.INFO, "Sent " + filePacket.getId() + "/" + filePacket.getLen() + " of " + filePacket.getPath() + " to " + client.getAddr().getHostAddress());
                }
            } catch (IOException e) {
                logger.warning("Failed to convert " + file.getPath() + " to byte array.");
            }
        }).start();
    }

    public List<File> getFiles() {
        return this.files;
    }

    public void stop(){
        this.running = false;
    }

    public boolean isReceivingFiles() {
        return !this.fileReader.isEmpty();
    }
}
