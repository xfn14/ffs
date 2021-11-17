import java.io.File;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        if(args.length < 2){
//            System.out.println("Invalid arguments");
//            return;
//        }
//
//        File path = new File(args[0]);
//        if(!path.exists()){
//            if(!path.mkdirs()){
//                System.out.println("Error creating folder");
//                return;
//            }
//        }
        List<InetAddress> adds = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            try {
                InetAddress add = InetAddress.getByName(args[i]);
                adds.add(add);
            } catch (UnknownHostException e) {
                System.out.println("Invalid address: " + args[i]);
//                e.printStackTrace();
            }
        }
        if (adds.size() == 0) return;

        List<UDPSocket> sockets = new ArrayList<>();
        for (InetAddress add : adds){
            try {
                UDPSocket socket = new UDPSocket(add);
                sockets.add(socket);
            } catch (SocketException e) {
                System.out.println("Failed to create socket: " + add.getHostAddress());
//                e.printStackTrace();
            }
        }
        if(sockets.size() == 0) return;
    }
}
