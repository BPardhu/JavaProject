package service;

import data.ImageFileRepository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageConversionService {

    public static void convert(File input, File output, String format)
            throws Exception {

        BufferedImage image = ImageFileRepository.readImage(input);
        if (image == null) {
            throw new RuntimeException("Unsupported input image");
        }

        // Handle alpha channel for JPG / BMP
        if ((format.equalsIgnoreCase("jpg")
                || format.equalsIgnoreCase("jpeg")
                || format.equalsIgnoreCase("bmp"))
                && image.getColorModel().hasAlpha()) {

            BufferedImage rgbImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D g = rgbImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.drawImage(image, 0, 0, null);
            g.dispose();

            image = rgbImage;
        }

        boolean success =
                ImageFileRepository.writeImage(image, format, output);

        if (!success) {
            throw new RuntimeException(
                    "No ImageIO writer found for format: " + format
            );
        }
    }
}