package udpServer.packets;

import utils.Tuple;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusPacket extends FFPacket implements Serializable {
    private List<Tuple<String,Long>> filesInfo = new ArrayList<>();
    private transient final File root;

    public StatusPacket(File root, List<File> files){
        this.root = root;
        filesToInfo(files);
    }

    private List<Tuple<String,Long>> filesToInfo(List<File> files){
        List<Tuple<String,Long>> filesInfo = new ArrayList<>();
        for(File file : files){
            String fileName = file.getPath().substring(root.getPath().length() + 1);
            Tuple<String,Long> i = new Tuple<>(fileName, file.length());
            filesInfo.add(i);
        }
        System.out.println(filesInfo);
        return filesInfo;
    }

    public List<Tuple<String, Long>> getFilesInfo() {
        return filesInfo;
    }
}
