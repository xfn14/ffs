import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) throws SocketException {
        Logger logger = new Logger();
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
        InetAddress inetA_aux = null;
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for(NetworkInterface netint : Collections.list(nets)) {
            if (!netint.isLoopback() || !netint.isUp()) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for(InetAddress inetA : Collections.list(inetAddresses))
                    if (inetA instanceof Inet4Address)
                        inetA_aux = inetA;
            }
        }

        final InetAddress inetAddress = inetA_aux;
        System.out.println(inetAddress);
    }
}
