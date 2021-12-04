import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
    private boolean run = true;
    @Override
    public void run() {
        DatagramSocket socket;
        try{
            socket = new DatagramSocket(3004);
        }catch (SocketException e){
            e.printStackTrace();
            return;
        }

        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        String temp;
        while(this.run){
            try{
                socket.receive(packet);
                temp = new String(packet.getData());
                if(temp.equals("quit")) run = false;
                System.out.println("Received: " + temp);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }
}
