import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable{
    private InetAddress addr;
    private boolean run = true;

    public Client(String ip) throws UnknownHostException {
        this.addr = InetAddress.getByName(ip);
    }

    @Override
    public void run() {
        DatagramSocket socket;
        try{
            socket = new DatagramSocket(3004);
            socket.setBroadcast(true);
        }catch (SocketException e){
            e.printStackTrace();
            return;
        }

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
                    3004
            );
            try {
                socket.send(packet);
                System.out.println(new String(packet.getData()));
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
