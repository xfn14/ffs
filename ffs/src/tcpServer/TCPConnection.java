package tcpServer;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TCPConnection implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private Socket socket;
    private PrintWriter printWriter;
    private boolean running = true;

    public TCPConnection(Socket socket){
        this.socket = socket;
        try {
            this.printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Failed to get client stream", e);
        }
    }

    @Override
    public void run() {
        while(running){
            try {
                this.printWriter.println("HTTP/1.1 200 OK\r");
                this.printWriter.println("Content-type: text/html; charset=UTF-8");
                this.printWriter.println("\r");
                File file = new File("ffs/resources/index.html");
                InputStream in = new FileInputStream(file);
                String s = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                this.printWriter.write(s);
                this.printWriter.flush();
                this.printWriter.close();
                socket.close();
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Error reading data input stream", e);
            }
        }
    }

    private void stop(){
        this.running = false;
    }
}
