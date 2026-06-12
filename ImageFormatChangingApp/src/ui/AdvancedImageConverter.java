package ui;

import service.ImageConversionService;
import util.FileUtils;
import util.ImageValidator;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class AdvancedImageConverter extends JFrame {

    private File inputFile;
    private JLabel imageLabel;
    private JComboBox<String> formatBox;

    public AdvancedImageConverter() {
        setTitle("Advanced Image Converter");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel();

        JButton openBtn = new JButton("Open Image");
        JButton convertBtn = new JButton("Convert & Save");

        formatBox = new JComboBox<>(new String[]{
                "png", "jpg", "jpeg", "bmp", "webp"
        });

        openBtn.addActionListener(this::openImage);
        convertBtn.addActionListener(this::convertImage);

        topPanel.add(openBtn);
        topPanel.add(new JLabel("Output Format:"));
        topPanel.add(formatBox);
        topPanel.add(convertBtn);

        imageLabel = new JLabel("No image selected", SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);
    }

    private void openImage(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Images", "png", "jpg", "jpeg", "bmp", "webp"
        ));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputFile = chooser.getSelectedFile();
            displayImage(inputFile);
        }
    }

    private void displayImage(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                throw new RuntimeException("Unsupported image format");
            }

            Image scaled = img.getScaledInstance(
                    600, 400, Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load image:\n" + ex.getMessage());
        }
    }

    private void convertImage(ActionEvent e) {
        if (!ImageValidator.isValidImage(inputFile)) {
            JOptionPane.showMessageDialog(this, "Invalid image file.");
            return;
        }

        String format = formatBox.getSelectedItem().toString();

        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setSelectedFile(new File("converted_image." + format));

        if (saveChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputFile = FileUtils.ensureExtension(
                    saveChooser.getSelectedFile(), format
            );

            try {
                ImageConversionService.convert(inputFile, outputFile, format);
                JOptionPane.showMessageDialog(this,
                        "Image saved successfully:\n" + outputFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Conversion failed:\n" + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        // 🔴 REQUIRED: register ImageIO plugins BEFORE use
        IIORegistry.getDefaultInstance()
                .registerApplicationClasspathSpis();

        // Verify WebP writer availability
        System.out.println("Available writers:");
        System.out.println(Arrays.toString(ImageIO.getWriterFormatNames()));

        SwingUtilities.invokeLater(() ->
                new AdvancedImageConverter().setVisible(true)
        );
    }
}