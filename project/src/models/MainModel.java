package models;

// Detta blir ju typ vår MazeGenerator/solver

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Main model class, responsible for handling the application's data and performing calculations.
 * @author Emma Pesjak
 */
public class MainModel {

    private static final int MAX_PANEL_WIDTH = 700;
    private static final int MAX_PANEL_HEIGHT = 700;


    public MainModel() {

    }

    // Kirra mazarna
    public JPanel calculate(String fileName) throws IOException {

        // Get the image.
        BufferedImage image = ImageIO.read(new File("project/src/mazeImages/" + fileName));

        // Get height and width.
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a binary image of the maze.
        BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        // Calculate the scaling factor for the JPanel size since we have to shrink the image.
        double scale = Math.min((double) MAX_PANEL_WIDTH / width, (double) MAX_PANEL_HEIGHT / height);

        // Calculate the scaled dimensions for the JPanel.
        int panelWidth = (int) (width * scale);
        int panelHeight = (int) (height * scale);

        // Iterate over the pixels of the image.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB value of the pixel.
                int rgb = image.getRGB(x, y);

                // Extract the red, green, and blue components.
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Compute the grayscale value of the pixel.
                int grayscale = (red + green + blue) / 3;

                // Set the grayscale value as the RGB value for the binary image.
                int binaryRGB = (grayscale << 16) | (grayscale << 8) | grayscale;
                binaryImage.setRGB(x, y, binaryRGB);
            }
        }

        // Create a custom JPanel to display the binary image.
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Scale and draw the binary image on the panel.
                g.drawImage(binaryImage, 0, 0, panelWidth, panelHeight, null);
            }
        };

        // Set the preferred size of the panel
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        return panel;

        // Men här räcker det  ju inte att bara rita bilden :) måste lösa med algoritmerna!

        // Returnera true om det gick att lösa.
        //return (dijkstraOne() && dijkstraTwo() && aStar());
    }

    // Heap
    private boolean dijkstraOne() {
        // Returnera true om det gick att lösa.
        return true;
    }

    // Other data structure
    private boolean dijkstraTwo() {
        // Returnera true om det gick att lösa.
        return true;
    }

    private boolean aStar() {
        // Returnera true om det gick att lösa.
        return true;
    }
}
