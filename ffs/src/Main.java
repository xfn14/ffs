import htmlServer.TCPServer;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

public class Main {
    public static Logger logger = Logger.getLogger("FFSync");

    public static void main(String[] args) {
        loadLoggerSettings();

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

        TCPServer tcpServer = new TCPServer();
        Thread tcpThread = new Thread(tcpServer);
        tcpThread.start();

        FFManager ffManager = new FFManager(socket, addrs);
        Thread ffManagerThread = new Thread(ffManager);
        ffManagerThread.start();
    }

    private static void loadLoggerSettings(){
        try {
            SimpleFormatter formatter = new SimpleFormatter(){
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(new Date(record.getMillis())).append(" ");
                    sb.append('[').append(record.getLoggerName()).append("] - ");
                    sb.append(record.getLevel().getLocalizedName()).append(" - ");
                    sb.append(record.getMessage()).append('\n');
                    if(record.getThrown() != null)
                        sb.append(record.getThrown()).append('\n');
                    return sb.toString();
                }
            };
            FileHandler fileHandler = new FileHandler("log.txt");
            fileHandler.setFormatter(formatter);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.warning("Failed to open log file. Logs won't be registered.");
        }
    }
}
