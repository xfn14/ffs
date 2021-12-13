package udpServer.protocol;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusPacket extends FTRPacket implements Serializable {
    private final Logger logger = Logger.getLogger("FFSync");
    private final List<FileInfo> filesInfo;
    private transient final File root;

    public StatusPacket(InetAddress origin, InetAddress destination, int port, File root, List<File> files){
        super(origin, destination, port);
        this.root = root;
        this.filesInfo = filesToInfo(files);
    }

    private List<FileInfo> filesToInfo(List<File> files) {
        List<FileInfo> filesInfo = new ArrayList<>();
        for(File file : files){
            String fileName = file.getPath().substring(root.getPath().length() + 1);
            FileTime date = null;
            try {
                date = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Failed to get file attributes", e);
            }
            FileInfo i = new FileInfo(fileName, file.length(), date);
            filesInfo.add(i);
        }
        return filesInfo;
    }

    public List<FileInfo> getFilesInfo() {
        return this.filesInfo;
    }
}
