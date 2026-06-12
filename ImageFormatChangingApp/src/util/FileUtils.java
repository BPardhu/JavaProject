package util;

import java.io.File;

public class FileUtils {

    public static File ensureExtension(File file, String ext) {
        if (!file.getName().toLowerCase().endsWith("." + ext)) {
            return new File(file.getAbsolutePath() + "." + ext);
        }
        return file;
    }
}