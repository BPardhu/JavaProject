import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

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
            Image scaled = img.getScaledInstance(
                    imageLabel.getWidth(),
                    imageLabel.getHeight(),
                    Image.SCALE_SMOOTH
            );
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load image");
        }
    }

    private void convertImage(ActionEvent e) {
        if (inputFile == null) {
            JOptionPane.showMessageDialog(this, "Please select an image first.");
            return;
        }

        String format = (String) formatBox.getSelectedItem();

        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Save Converted Image");
        saveChooser.setSelectedFile(new File("converted_image." + format));

        if (saveChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputFile = saveChooser.getSelectedFile();

            if (!outputFile.getName().toLowerCase().endsWith("." + format)) {
                outputFile = new File(outputFile.getAbsolutePath() + "." + format);
            }

            try {
                BufferedImage image = ImageIO.read(inputFile);

// Convert PNG with alpha → RGB for JPG/BMP
                if ((format.equals("jpg") || format.equals("jpeg") || format.equals("bmp"))
                        && image.getColorModel().hasAlpha()) {

                    BufferedImage rgbImage = new BufferedImage(
                            image.getWidth(),
                            image.getHeight(),
                            BufferedImage.TYPE_INT_RGB
                    );

                    Graphics2D g = rgbImage.createGraphics();
                    g.setColor(Color.WHITE); // background for transparency
                    g.fillRect(0, 0, image.getWidth(), image.getHeight());
                    g.drawImage(image, 0, 0, null);
                    g.dispose();

                    image = rgbImage;
                }

                boolean result = ImageIO.write(image, format, outputFile);

                if (result) {
                    JOptionPane.showMessageDialog(this,
                            "Image saved successfully:\n" + outputFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Format not supported on this system.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Conversion failed:\n" + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new AdvancedImageConverter().setVisible(true)
        );
    }
}