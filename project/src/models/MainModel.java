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
    private final int cellSize = 5;

    int minX, minY, maxX, maxY;

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

    public boolean checkIfValid(Point startPoint, Point finish){
        // Check so that the start/finish points are within boundaries
        if (!maze[startPoint.x / cellSize][startPoint.y / cellSize]) {
            return false;
        }

        if (!maze[finish.x / cellSize][finish.y / cellSize]) {
            return false;
        }

        return true;
    }

    public JPanel displayPath(Point startPoint, Point finish, String algo){

        // Adjust the start and end coordinates to match the cell size
        startX = startPoint.x / cellSize;
        startY = startPoint.y / cellSize;
        endX = finish.x / cellSize;
        endY = finish.y / cellSize;

        start = new Point(startX, startY);
        end = new Point(endX, endY);


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

                    // Gör så att det adjust till den nya mazen
                    int scaledStartX = startX * cellSize * scale + cellSize / 2;
                    int scaledStartY = startY * cellSize * scale + cellSize / 2;
                    int scaledEndX = endX * cellSize * scale + cellSize / 2;
                    int scaledEndY = endY * cellSize * scale + cellSize / 2;

                    g.setColor(Constants.COLOR_START);
                    g.fillOval(scaledStartX - 5, scaledStartY - 5, 10, 10); // -5 to center.
                    g.setColor(Constants.COLOR_END);
                    g.fillOval(scaledEndX - 5, scaledEndY - 5, 10, 10); // -5 to center.

                }

                Dijkstra dijkstra = new Dijkstra(maze, start, end);

                // Draw the calculated paths.
                switch (algo) {
                    case (Constants.DIJK_HEAP) -> {
                        System.out.println("DIJK HEAP: Varför printas denna efter algoritmerna???");

                        List<Point> shortestPath = dijkstra.solveHeapPath();

                        if (shortestPath.size() != 0){
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                    case (Constants.DIJK_DEQ) -> {
                        System.out.println("DIJK DEQ: Varför printas denna efter algoritmerna???");

                        List<Point> shortestPath = dijkstra.solveDequeuePath();

                        if (shortestPath.size() != 0){
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                    case (Constants.ASTAR) -> {
                        System.out.println("A*: Varför printas denna efter algoritmerna???");
                        // Solve the maze with the A* algorithm and get a list of points with the path.
                        AStar aStar = new AStar(maze, start, end);
                        List<Point> shortestPath = aStar.solvePath();

                        if (shortestPath.size() != 0){
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
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

            //int cellSize = 1;
            double startX = current.x * cellSize * scale + cellSize / 2.0;
            double startY = current.y * cellSize * scale + cellSize / 2.0;
            double endX = next.x * cellSize * scale + cellSize / 2.0;
            double endY = next.y * cellSize * scale + cellSize / 2.0;

            g2d.drawLine((int) startX, (int) startY, (int) endX, (int) endY);
        }
    }

    /**
     * Hej
     * @param image
     */
    private void generateMaze(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int mazeWidth = width / cellSize;
        int mazeHeight = height / cellSize;
        long start, end;

        // Create the 2D boolean array representing the maze
        maze = new boolean[mazeWidth][mazeHeight];

        start = System.currentTimeMillis();

        // Threshold, in this case, 85% of the pixels need to be white to be considered to be white.
        int threshold = (int) (0.85 * (cellSize * cellSize));

        identifyBoundaries();

        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {

                // Start/end coordinates of the cell.
                int startX = x * cellSize;
                int startY = y * cellSize;
                int endX = startX + cellSize;
                int endY = startY + cellSize;

                int whiteCount = 0;

                for (int i = startX; i < endX; i++) {
                    for (int j = startY; j < endY; j++) {
                        if (image.getRGB(i, j) == Color.WHITE.getRGB()) {
                            whiteCount++;
                        }
                    }
                }

                // Set to true if amount of white pixels are more than the threshold AND within walls.
                maze[x][y] = isWithinBoundary(x, y) && whiteCount >= threshold;

            }
        }

        identifyBoundaries();

        // Set cells outside the boundaries to false
        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {
                if (!isWithinBoundary(x, y)) {
                    maze[x][y] = false;
                }
            }
        }

        end = System.currentTimeMillis();

        System.out.println("Total for creating a maze: " + (end - start));

        System.out.println("Total for creating a maze: " + (end - start));
        System.out.println("Amount of cells: " + (maze.length * maze[0].length));
    }

    private void identifyBoundaries(){
        int mazeWidth = maze.length;
        int mazeHeight = maze[0].length;

        // Initialize min and max values.
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        maxY = Integer.MIN_VALUE;

        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {
                if (!maze[x][y]) { // Check if the cell is a wall
                    // Update the boundary-values.
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
    }

    private boolean isWithinBoundary(int x, int y) {
        // Check if the cell is within the boundaries
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
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
    }



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
}
