package tcpServer;

import utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private ServerSocket serverSocket;
    private boolean running = true;
    private final int port = 8889;
    private final File dir;

    public TCPServer(File dir){
        this.dir = dir;
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.logger.info("TCP Server running at: " +
                    Objects.requireNonNull(NetUtils.getLocalAddress()).getHostName() +
                    ":" + this.port);
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to bind TCP Socket", e);
            this.stop();
        }
    }

    @Override
    public void run() {
        while(this.running){
            try {
                Socket socket = this.serverSocket.accept();
                this.logger.info(socket.getInetAddress().getHostName() + " connected to TCP Socket");
                TCPConnection connection = new TCPConnection(socket);
                Thread connectionThread = new Thread(connection);
                connectionThread.start();
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to connect to new socket", e);
            }
        }
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            this.logger.warning("Failed to close server socket.");
        }
    }

    private void stop(){
        this.running = false;
    }
}
