import java.io.File;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws SocketException {
        Logger logger = Logger.getLogger("Test");
        System.out.println(20/10);
//        final File dir = new File("tester/src/");
//        if(!dir.exists()){
//            System.out.println("error");
////            if(!dir.mkdirs()){
////                logger.severe("Error creating folder.");
////                return;
////            }
//        }else if(dir.isFile()){
//            logger.severe( "Provided path is for a file.");
//            return;
//        }
//        List<File> files = FileUtils.getFiles(dir);
//        logger.info("Loaded: " + files);
//
//        for(File file : files){
//            System.out.println(file.getPath());
//        }
//        InetAddress addrDest = null;
//        DatagramSocket socket = new DatagramSocket(8888);
//        try {
//            addrDest = InetAddress.getByName(args[0]);
//            logger.log(Level.INFO, "Connected to "  + addrDest.getHostAddress() + "/" + addrDest.getHostName());
//        } catch (UnknownHostException e) {
//            System.out.println("Invalid destiantion address: " + args[0]);
//            e.printStackTrace();
//        }
//
//        Thread server = new Thread(new Server(logger, socket));
//        server.start();
//        Thread client = new Thread(new Client(logger, socket, addrDest));
//        client.start();
//
//        socket.disconnect();
//        socket.close();








//        Thread server = new Thread(new Server());
//        Thread client = null;
//        try {
//            client = new Thread(new Client(args[0]));
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        server.start();
//        client.start();
//        InetAddress inetA_aux = null;
//        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//        for(NetworkInterface netint : Collections.list(nets)) {
//            if (!netint.isLoopback() || !netint.isUp()) {
//                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
//                for(InetAddress inetA : Collections.list(inetAddresses))
//                    if (inetA instanceof Inet4Address)
//                        inetA_aux = inetA;
//            }
//        }
//
//        final InetAddress inetAddress = inetA_aux;
//        System.out.println(inetAddress);
    }
}
