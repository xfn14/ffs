import tcpServer.TCPServer;
import udpServer.FFManager;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
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
        String path = args[0].charAt(args[0].length()-1) == '/' ? args[0] : args[0] + "/";
        final File dir = new File(path);
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
        } if (addrs.size() == 0) return;

        FFManager ffManager = new FFManager(8888, dir, files, addrs);
        Thread ffManagerThread = new Thread(ffManager);
        ffManagerThread.start();

        TCPServer tcpServer = new TCPServer(dir, addrs);
        Thread tcpThread = new Thread(tcpServer);
        tcpThread.start();
    }

    /**
     * Método que carrega as definições do logger e guarda num ficheiro
     */
    private static void loadLoggerSettings(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            SimpleFormatter formatter = new SimpleFormatter(){
                @Override
                public String format(LogRecord record) {
                    StringBuilder sb = new StringBuilder();
                    sb.append('[').append(record.getLoggerName()).append("] - ");
                    sb.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
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
            logger.warning("Failed to open log file. Logs won't be saved.");
        }
    }
}
