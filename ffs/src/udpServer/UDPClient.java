package udpServer;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient {
    private final Logger logger = Logger.getLogger("FFSync");
    private final int port;
    private DatagramSocket socket;
    private InetAddress addr;

    public UDPClient(int port, DatagramSocket socket, InetAddress addr){
        this.port = port;
        this.socket = socket;
        this.addr = addr;
    }

    /**
     * Método que envia um array de bytes
     * @param arr array de bytes
     */
    public void sendBytes(byte[] arr){
        this.sendPacket(new DatagramPacket(
                arr, arr.length,
                this.addr, this.port
        ));
    }


    /**
     * Método que envia um packet
     * @param packet packet
     */
    public void sendPacket(DatagramPacket packet){
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Failed to send packet to " + this.addr.getHostAddress(), e);
        }
    }

    /**
     * Metodo que envia um packet no formato de mensagem
     * @param msg mensagem a ser enviada
     */
    public void sendPacket(String msg){
        byte[] data = msg.getBytes(StandardCharsets.US_ASCII);
        this.sendPacket(new DatagramPacket(
                data, data.length,
                this.addr, this.port
        ));
    }

    public InetAddress getAddr() {
        return this.addr;
    }
}
