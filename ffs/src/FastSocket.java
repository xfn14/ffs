import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class FastSocket implements Runnable{
    // JUST A TEST

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];

    public FastSocket(){
        try {
            this.socket = new DatagramSocket(80);
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Failed to create UDP socket on port 80");
        }
    }

    @Override
    public void run() {
        this.running = true;

        while(this.running){
            DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            packet = new DatagramPacket(this.buffer, this.buffer.length, packet.getAddress(), packet.getPort());
            String received = new String(packet.getData(), 0, packet.getLength());

            if(received.equals("end")){
                this.running = false;
                continue;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
