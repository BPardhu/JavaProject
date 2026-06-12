package util;

import java.io.File;

public class ImageValidator {

    private static final String[] SUPPORTED_FORMATS =
            {"png", "jpg", "jpeg", "bmp", "webp"};

    public static boolean isValidImage(File file) {
        if (file == null || !file.exists()) return false;

        String name = file.getName().toLowerCase();
        for (String format : SUPPORTED_FORMATS) {
            if (name.endsWith("." + format)) {
                return true;
            }
        }
        return false;
    }
}