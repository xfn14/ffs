package htmlServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer implements Runnable {
    private Logger logger = Logger.getLogger("FFSync");
    private ServerSocket serverSocket;
    private boolean running = true;

    public TCPServer(){
        try {
            this.serverSocket = new ServerSocket(8889);
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
                TCPConnection connection = new TCPConnection(socket);
                Thread connectionThread = new Thread(connection);
                connectionThread.start();
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to connect to new socket", e);
            }
        }
    }

    private void stop(){
        this.running = false;
    }
}
