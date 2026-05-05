package util;

import java.io.File;

public class FileUtils {

    public static String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? "" : name.substring(dotIndex + 1);
    }

    public static File ensureExtension(File file, String ext) {
        if (!file.getName().toLowerCase().endsWith("." + ext)) {
            return new File(file.getAbsolutePath() + "." + ext);
        }
        return file;
    }
}