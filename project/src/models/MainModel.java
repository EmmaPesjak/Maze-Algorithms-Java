package models;

// Detta blir ju typ vår MazeGenerator/solver

import support.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main model class, responsible for handling the application's data and performing calculations.
 */
public class MainModel {

    private int panelWidth;
    private int panelHeight;

    private int startX = -1, startY = -1, endX = -1, endY = -1;
    private BufferedImage image;
    //private BufferedImage binaryImage;
    private boolean showPoints = true; // Flag to control the display of start and end points.
    private JPanel panel;

    private boolean[][] maze;
    private Point start;
    private Point end;
    private int scale;

    private boolean aStarHasPath, heapHasPath, dequeHasPath = false; // kolla läget om de faktiskt finns shortest path

    public JPanel getMaze(String fileName) throws IOException {
        // Get the image.
        image = ImageIO.read(new File("project/src/mazeImages/" + fileName));
        generateMaze(image);


        // Get height and width.
        int width = image.getWidth();
        int height = image.getHeight();

//        // Create a binary image of the maze.
//        binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        // Calculate the scaling factor for the JPanel size since we have to shrink the image.
        scale = (int) Math.min((float) Constants.MAX_PANEL_WIDTH / width, (float) Constants.MAX_PANEL_HEIGHT / height);

        // Can't have scale 0. TODO: detta är ju kanske inte optimalt...
        if (scale == 0) {
            scale = 1;
        }

        // Calculate the scaled dimensions for the JPanel.
        panelWidth = (width * scale);
        panelHeight = (height * scale);
//
        //TODO kommenterar bort binaryImage, ska vi radera den helt?
//        // Iterate over the pixels of the image.
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                // Get the RGB value of the pixel.
//                int rgb = image.getRGB(x, y);
//
//                // Extract the red, green, and blue components.
//                int red = (rgb >> 16) & 0xFF;
//                int green = (rgb >> 8) & 0xFF;
//                int blue = rgb & 0xFF;
//
//                // Compute the grayscale value of the pixel.
//                int grayscale = (red + green + blue) / 3;
//
//                // Set the grayscale value as the RGB value for the binary image.
//                int binaryRGB = (grayscale << 16) | (grayscale << 8) | grayscale;
//                binaryImage.setRGB(x, y, binaryRGB);
//            }
//        }


        // Create a custom JPanel to display the binary image.
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Scale and draw the binary image on the panel.
                g.drawImage(image, 0, 0, panelWidth, panelHeight, null);
            }
        };

        // Set the preferred size of the panel
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        // Wrap the path drawing code in SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> {
            panel.repaint();
        });


        return panel;
    }

    public JPanel displayPath(Point startPoint, Point finish, String algo){
        if (startPoint != null) {
            startX = startPoint.x;
            startY = startPoint.y;
            endX = finish.x;
            endY = finish.y;
            this.start = startPoint;
            this.end = finish;
        } else {
            return null; // or handle the case when startPoint is null
        }

        //generateMaze(image);


        // TODO: denna får vi ju typ ha men skriva på skärmen att användaren klickade på en vägg och de ska picka om?
        // dubbelkolla så att skiten faktiskt är path ????????
        /*assert start != null;
        if (!maze[start.x][start.y] || !maze[finish.x][finish.y]){
            System.out.println("de är ju förfan inte path");
            return null;
        }*/


        // Create a custom JPanel to display the binary image.
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Scale and draw the binary image on the panel.
                g.drawImage(image, 0, 0, panelWidth, panelHeight, null);

                // Draw the start and end points
                if (showPoints && startX != -1) {
                    g.setColor(Constants.COLOR_START);
                    g.fillOval(startX-5, startY-5, 10, 10); // -5 to center.
                    g.setColor(Constants.COLOR_END);
                    g.fillOval(endX-5, endY-5, 10, 10); // -5 to center.
                }

                Dijkstra dijkstra = new Dijkstra(maze, start, end);

                // Draw the calculated paths.
                switch (algo) {
                    case (Constants.DIJK_HEAP) -> {
                        System.out.println("Varför körs denna 2 gånger????");

                        List<Point> shortestPath = dijkstra.solveHeapPath();

                        if (shortestPath.size() != 0){
                            heapHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                    case (Constants.DIJK_DEQ) -> {
                        List<Point> shortestPath = dijkstra.solveDequeuePath();

                        if (shortestPath.size() != 0){
                            dequeHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                    case (Constants.ASTAR) -> {
                        // Solve the maze with the A* algorithm and get a list of points with the path.
                        AStar aStar = new AStar(maze, start, end);
                        List<Point> shortestPath = aStar.solvePath();

                        if (shortestPath.size() != 0){
                            aStarHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                }

                // vet inte vad jag tänkte göra med de här. det är sent på kvällen nu yolo.
                if (!aStarHasPath){
                    String msg1 = "No path was found with A*.";
                }

                if (!heapHasPath){
                    String msg2 = "No path was found with Heap and Dijkstra's.";
                }

                if (!dequeHasPath){
                    String msg3 = "No path was found with Deque and Dijkstra's.";
                }
            }
        };

        // Set the preferred size of the panel
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        // Wrap the path drawing code in SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> {
            panel.repaint();
        });

        return panel;
    }

    /**
     * Draws a line showing the path through the maze.
     * @param g is the graphics.
     * @param path is the path list of points.
     */
    private void drawPath(Graphics g, List<Point> path) {
        g.setColor(Constants.COLOR_PATH);

        // Create Graphics 2D, so we can set the stroke thickness.
        Graphics2D g2d = (Graphics2D) g;

        // Set the thickness for the path lines.
        g2d.setStroke(new BasicStroke(3));

        // Draw lines between the cells of the path.
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);

            int cellSize = 1;
            double startX = current.x * cellSize * scale + cellSize / 2.0;
            double startY = current.y * cellSize * scale + cellSize / 2.0;
            double endX = next.x * cellSize * scale + cellSize / 2.0;
            double endY = next.y * cellSize * scale + cellSize / 2.0;

            g2d.drawLine((int) startX, (int) startY, (int) endX, (int) endY);
        }
    }


    //TODO: finns det något bättre sätt att skapa mazen eftersom man får 34857394857349857349875 celler så blir algoritmerna aslångsamma
    /**
     * Hej
     * @param image
     */
    private void generateMaze(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        long start, end;

        // Create the 2D boolean array representing the maze
        maze = new boolean[width][height];

        start = System.currentTimeMillis();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Convert binary image coordinates to maze coordinates

                maze[x][y] = image.getRGB(x, y) == Color.WHITE.getRGB();
            }
        }
        end = System.currentTimeMillis();

        System.out.println("Total for creating a maze: " + (end - start));
    }


    /**
     * Method for removing the start and finish points.
     */
    private void removePoints() {
        showPoints = false;
        panel.repaint();
    }

    /**
     * Method for showing the start and finish points.
     */
    public void showPoints() {
        showPoints = true;
    }

    /**
     * Method for resetting the start and finish points.
     */
    public void clearPoints() {
        removePoints();
        startX = -1;
        startY = -1;
        endX = -1;
        endY = -1;
        aStarHasPath = false;
        heapHasPath = false;
        dequeHasPath = false;
    }
}
