import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static Logger logger = Logger.getLogger("FFSync");

    public static void main(String[] args) {
        loadLogger();

        if(args.length < 2){
            logger.severe("Invalid arguments");
            return;
        }

        // Load folder from 1st argument
        File dir = new File(args[0]);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                logger.severe("Error creating folder.");
                return;
            }
        }else if(dir.isFile()){
            logger.severe( "Provided path is for a file.");
            return;
        }
        List<File> files = FileUtils.getFiles(dir);
        logger.info("Loaded: " + files);

        // Get the different ipv4
        List<InetAddress> addrs = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            try {
                InetAddress addr = InetAddress.getByName(args[i]);
                addrs.add(addr);
            } catch (UnknownHostException e) {
                logger.log(Level.SEVERE, "Invalid address " + args[i], e);
            }
        }
        if (addrs.size() == 0) return;

        // Create socket
        DatagramSocket socket;
        try{
            socket = new DatagramSocket(8888);
        } catch (SocketException e){
            logger.log(Level.SEVERE, "Failed to bind socket", e);
            return;
        }

        List<FFConnection> connections = new ArrayList<>();
        for(InetAddress addr : addrs){
            FFConnection connection = new FFConnection(socket, addr);
            connection.connect();
            connections.add(connection);
            logger.info("Connected to " + addr.getHostAddress());
        }
    }

    private static void loadLogger(){
        try {
            FileHandler fileHandler = new FileHandler("log.txt");
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            logger.warning("Failed to open log file. Logs won't be registered.");
        }
    }
}
