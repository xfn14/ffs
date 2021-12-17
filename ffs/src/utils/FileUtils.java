package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
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

    public static byte[] fileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static Date getFileDate(File file) throws IOException {
        FileTime time = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
        return new Date(time.toMillis());
    }
}
