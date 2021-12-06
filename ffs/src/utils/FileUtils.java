package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<File> getFiles(File dir){
        if(dir == null || !dir.exists() || dir.isFile()) return new ArrayList<>();
        List<File> files = new ArrayList<>();
        File[] dirFiles = dir.listFiles();

        if(dirFiles == null || dirFiles.length == 0) return files;
        for(File file : dirFiles) {
            if (file.isDirectory()) {
                List<File> sub = getFiles(file);
                files.addAll(sub);
            } else files.add(file);
        }
        return files;
    }
}
