import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

        // Create socket
        DatagramSocket socket;
        try{
            socket = new DatagramSocket(8888);
        } catch (SocketException e){
            logger.log(Level.SEVERE, "Failed to bind socket", e);
            return;
        }

        // Get the different ipv4


        List<InetAddress> adds = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            try {
                InetAddress add = InetAddress.getByName(args[i]);
                adds.add(add);
            } catch (UnknownHostException e) {
                logger.log(Level.SEVERE, "Invalid address " + args[i], e);
            }
        }
        if (adds.size() == 0) return;
//
//        List<UDPSocket> sockets = new ArrayList<>();
//        for (InetAddress add : adds){
//            try {
//                UDPSocket socket = new UDPSocket(add);
//                sockets.add(socket);
//                System.out.println("Socket created: " + socket.socket.toString());
//            } catch (SocketException e) {
//                System.out.println("Failed to create socket: " + add.getHostAddress());
////                e.printStackTrace();
//            }
//        }
//        if(sockets.size() == 0) return;
//
//        UDPServer udpServer = new UDPServer(sockets);
//        udpServer.run();
//
//        Scanner scanner = new Scanner(System.in);
//        String msg = scanner.nextLine();
//
//        for(UDPSocket socket : sockets){
//            try {
//                socket.sendPacket(msg);
//            } catch (IOException e) {
//                System.out.println("Failed to send packet");
////                e.printStackTrace();
//            }
//        }
//
//        for(UDPSocket socket : sockets){
//            socket.close();
//        }
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
