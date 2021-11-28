import java.io.IOException;
import java.util.List;

public class UDPServer implements Runnable {
    private List<UDPSocket> sockets;

    public UDPServer(List<UDPSocket> sockets){
        this.sockets = sockets;
    }

    @Override
    public void run() {
        for(UDPSocket socket : this.sockets){
            try {
                socket.receivePacket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
