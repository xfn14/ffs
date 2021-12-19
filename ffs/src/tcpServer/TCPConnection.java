package tcpServer;

import utils.FileUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TCPConnection implements Runnable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final Socket socket;
    private PrintWriter printWriter;
    private final List<InetAddress> connections;
    private final File dir;

    public TCPConnection(Socket socket, File dir, List<InetAddress> connections){
        this.socket = socket;
        this.dir = dir;
        this.connections = connections;
        try {
            this.printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Failed to get client stream", e);
        }
    }

    @Override
    public void run() {
        try {
            this.printWriter.write("HTTP/1.1 200 OK\r\n");
            this.printWriter.write("Content-type: text/html; charset=UTF-8\r\n");
            this.printWriter.write("\r\n");
            File file = new File("../resources/index.html");
            InputStream in = new FileInputStream(file);
            String files = FileUtils.getFiles(dir).stream().map(File::getPath).collect(Collectors.toList()).toString();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String s = br.lines()
                    .collect(Collectors.joining("\n"))
                    .replace("%files%", files)
                    .replace("%connections%", connections.toString());
            this.printWriter.write(s);
            this.printWriter.flush();
            br.close(); isr.close();
            this.printWriter.close();
            this.socket.close();
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Error reading data input stream", e);
        }
    }
}
