package data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageFileRepository {

    public static BufferedImage readImage(File file) throws Exception {
        return ImageIO.read(file);
    }

    public static boolean writeImage(
            BufferedImage image, String format, File outputFile)
            throws Exception {

        return ImageIO.write(image, format, outputFile);
    }
}