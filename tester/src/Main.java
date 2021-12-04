import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    private boolean run = true;

    public static void main(String[] args) throws UnknownHostException {
//        InetAddress local = InetAddress.getByName("localhost");
//        System.out.println(local.toString());
        Thread server = new Thread(new Server());
        Thread client = new Thread(new Client(args[0]));
        server.start();
        client.start();
    }
}
