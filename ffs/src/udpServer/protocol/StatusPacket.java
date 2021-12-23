package udpServer.protocol;

import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusPacket extends FTRPacket implements Serializable {
    private transient final Logger logger = Logger.getLogger("FFSync");
    private final List<FileInfo> filesInfo;
    private transient final File root;

    public StatusPacket(String prefix, File root, List<File> files){
        super(prefix);
        this.root = root;
        this.filesInfo = filesToInfo(files);
    }

    private List<FileInfo> filesToInfo(List<File> files) {
        List<FileInfo> filesInfo = new ArrayList<>();
        for(File file : files){
            String fileName = file.getPath().substring(root.getPath().length() + 1);
            Date date = null;
            try {
                date = FileUtils.getFileDate(file);
            } catch (IOException e) {
                this.logger.log(Level.WARNING, "Failed to get file attributes", e);
            } finally {
                FileInfo i = new FileInfo(fileName, file.length(), date);
                filesInfo.add(i);
            }
        }
        return filesInfo;
    }

    public List<FileInfo> getFilesInfo() {
        return this.filesInfo;
    }

    @Override
    public String toString() {
        return "StatusPacket{" +
                "filesInfo=" + filesInfo +
                ", root=" + root +
                '}';
    }
}
