import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;

public class Server implements Runnable {
    private Logger logger;
    private boolean run = true;
    private DatagramSocket socket;

    public Server(Logger logger, DatagramSocket socket){
        this.logger = logger;
        this.socket = socket;
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        String temp;
        while(this.run){
            try{
                socket.receive(packet);
                temp = new String(packet.getData());
                if(temp.equals("quit")) run = false;
                this.logger.log(Level.INFO, "From " + packet.getAddress().getHostAddress() + ": " + temp);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
        socket.close();
    }
}
