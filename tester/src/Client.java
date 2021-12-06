import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable{
    private Logger logger;
    private DatagramSocket socket;
    private InetAddress addr;
    private boolean run = true;

    public Client(Logger logger, DatagramSocket socket, InetAddress addrDest) {
        this.logger = logger;
        this.socket = socket;
        this.addr = addrDest;
    }

    @Override
    public void run() {
        int i = 0;
        while(this.run){
            if(i == 10) this.run = false;
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            byte[] data = input.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    addr,
                    8888
            );
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }


//        int i = 10;
//        while(i > 0){
//            try{
//                System.out.println(new String(packet.getData()));
//                socket.send(packet);
////                Thread.sleep(250);
//                i--;
//                System.out.println(i);
//            }catch (IOException | InterruptedException e){
//                e.printStackTrace();
//            }
//        }
    }
}
