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
    private int scale;
    private final int cellSize = 5;

    private int minX, minY, maxX, maxY;
    private boolean heapHasPath = false;
    private boolean dequeHasPath = false;
    private boolean astarHasPath = false;

    List<Point> shortestPath;
    Dijkstra dijkstra;
    AStar aStar;

    // Temporärt sket
    int listSize;

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

    /**
     * Check if the start and finish coordinates are within maze/not at a wall.
     * @param startPoint start-point.
     * @param finish end-point.
     * @return whether it's valid or not.
     */
    public boolean checkIfValid(Point startPoint, Point finish){
        // Check so that the start/finish points are within boundaries
        if (!maze[startPoint.x / cellSize][startPoint.y / cellSize]) {
            return false;
        }

        return maze[finish.x / cellSize][finish.y / cellSize];
    }

    /*public JPanel displayPath(Point startPoint, Point finish, String algo) {
        // Adjust the start and end coordinates to match the cell size
        startX = startPoint.x / cellSize;
        startY = startPoint.y / cellSize;
        endX = finish.x / cellSize;
        endY = finish.y / cellSize;

        start = new Point(startX, startY);
        end = new Point(endX, endY);

        boolean hasPath = false;
        List<Point> shortestPath = null;
        Dijkstra dijkstra;

        // Draw the calculated paths.
        switch (algo) {
            case Constants.DIJK_HEAP -> {
                System.out.println("DIJK HEAP: Varför printas denna efter algoritmerna???");
                dijkstra = new Dijkstra(maze, start, end);
                shortestPath = dijkstra.solveHeapPath();
            }
            case Constants.DIJK_DEQ -> {
                System.out.println("DIJK DEQ: Varför printas denna efter algoritmerna???");
                dijkstra = new Dijkstra(maze, start, end);
                shortestPath = dijkstra.solveDequeuePath();
            }
            case Constants.ASTAR -> {
                System.out.println("A*: Varför printas denna efter algoritmerna???");
                AStar aStar = new AStar(maze, start, end);
                shortestPath = aStar.solvePath();
            }
        }

        if (shortestPath != null && shortestPath.size() > 0) {
            hasPath = true;
        }

        if (hasPath) {
            // Create and return the panel.
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Scale and draw the binary image on the panel.
                    g.drawImage(image, 0, 0, panelWidth, panelHeight, null);

                    // Draw the start and end points.
                    if (showPoints && startX != -1) {
                        // Adjust the coordinates to the cell-size of maze.
                        int scaledStartX = startX * cellSize * scale + cellSize / 2;
                        int scaledStartY = startY * cellSize * scale + cellSize / 2;
                        int scaledEndX = endX * cellSize * scale + cellSize / 2;
                        int scaledEndY = endY * cellSize * scale + cellSize / 2;

                        g.setColor(Constants.COLOR_START);
                        g.fillOval(scaledStartX - 5, scaledStartY - 5, 10, 10); // -5 to center.
                        g.setColor(Constants.COLOR_END);
                        g.fillOval(scaledEndX - 5, scaledEndY - 5, 10, 10); // -5 to center.
                    }
                }
            };

            // Set the preferred size of the panel.
            panel.setPreferredSize(new Dimension(panelWidth, panelHeight));

            // Wrap the path drawing code in SwingUtilities.invokeLater.
            SwingUtilities.invokeLater(panel::repaint);

            return panel;
        } else {
            // No shortest path available
            return null;
        }
    }*/



    public JPanel displayPath(Point startPoint, Point finish, String algo){

        // Adjust the start and end coordinates to match the cell size
        startX = startPoint.x / cellSize;
        startY = startPoint.y / cellSize;
        endX = finish.x / cellSize;
        endY = finish.y / cellSize;

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        // Create a custom JPanel to display the binary image.
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                // Scale and draw the binary image on the panel.
                g.drawImage(image, 0, 0, panelWidth, panelHeight, null);

                // Draw the start and end points
                if (showPoints && startX != -1) {

                    // Adjust the coordinates to the cell-size of maze.
                    int scaledStartX = startX * cellSize * scale + cellSize / 2;
                    int scaledStartY = startY * cellSize * scale + cellSize / 2;
                    int scaledEndX = endX * cellSize * scale + cellSize / 2;
                    int scaledEndY = endY * cellSize * scale + cellSize / 2;

                    g.setColor(Constants.COLOR_START);
                    g.fillOval(scaledStartX - 5, scaledStartY - 5, 10, 10); // -5 to center.
                    g.setColor(Constants.COLOR_END);
                    g.fillOval(scaledEndX - 5, scaledEndY - 5, 10, 10); // -5 to center.

                }


                // Draw the calculated paths.
                switch (algo) {
                    case (Constants.DIJK_HEAP) -> {
                        dijkstra = new Dijkstra(maze, start, end);
                        //System.out.println("DIJK HEAP: Varför printas denna efter algoritmerna???");
                        shortestPath = dijkstra.solveHeapPath();
                        //listSize = shortestPath.size();

                        if (shortestPath.size() != 0){
                            //heapHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }
                    }
                    case (Constants.DIJK_DEQ) -> {
                        dijkstra = new Dijkstra(maze, start, end);
                        //System.out.println("DIJK DEQ: Varför printas denna efter algoritmerna???");

                        shortestPath = dijkstra.solveDequeuePath();

                        //listSize = shortestPath.size();
                        if (shortestPath.size() != 0){
                            //dequeHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }

                    }
                    case (Constants.ASTAR) -> {
                        //System.out.println("A*: Varför printas denna efter algoritmerna???");
                        // Solve the maze with the A* algorithm and get a list of points with the path.
                        aStar = new AStar(maze, start, end);
                        shortestPath = aStar.solvePath();

                        //listSize = shortestPath.size();
                        if (shortestPath.size() != 0){
                            //astarHasPath = true;
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }

                    }
                }
            }
        };

        // Set the preferred size of the panel
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        //System.out.println("panel.setPreferredSize(new Dimension(panelWidth, panelHeight))");

        // Wrap the path drawing code in SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> {
            //System.out.println("panel.repaint();");
            panel.repaint();
        });

        //System.out.println("Before returning: " +heapHasPath);
        return panel;
    }

    /**
     * Draws a line showing the path through the maze.
     * @param g is the graphics.
     * @param path is the path list of points.
     */
    private void drawPath(Graphics g, List<Point> path) {
        //System.out.println("draw path is being called");
        g.setColor(Constants.COLOR_PATH);

        // Create Graphics 2D, so we can set the stroke thickness.
        Graphics2D g2d = (Graphics2D) g;

        // Set the thickness for the path lines.
        g2d.setStroke(new BasicStroke(3));

        // Draw lines between the cells of the path.
        for (int i = 0; i < path.size() - 1; i++) {
            Point current = path.get(i);
            Point next = path.get(i + 1);

            double startX = current.x * cellSize * scale + cellSize / 2.0;
            double startY = current.y * cellSize * scale + cellSize / 2.0;
            double endX = next.x * cellSize * scale + cellSize / 2.0;
            double endY = next.y * cellSize * scale + cellSize / 2.0;

            g2d.drawLine((int) startX, (int) startY, (int) endX, (int) endY);
        }
    }

    /**
     * Generate a 2d-boolean array to represent the maze.
     * @param image image containing the maze.
     */
    private void generateMaze(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int mazeWidth = width / cellSize;
        int mazeHeight = height / cellSize;
        long start, end;

        // Create the 2D boolean array representing the maze
        maze = new boolean[mazeWidth][mazeHeight];

        start = System.currentTimeMillis();  // bara för att kolla läget

        // Define threshold, in this case, 85% of the pixels need to be white to be considered to be white.
        int threshold = (int) (0.85 * (cellSize * cellSize));

        // Identify the boundaries of the walls.
        //identifyBoundaries();

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
                maze[x][y] = whiteCount >= threshold;

            }
        }

        // Identify the boundaries of the walls.
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
        System.out.println("Amount of cells: " + (maze.length * maze[0].length));
    }

    /**
     * Find the outer walls of maze.
     */
    private void identifyBoundaries(){
        int mazeWidth = maze.length;
        int mazeHeight = maze[0].length;

        // Initialize min and max values.
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        maxY = Integer.MIN_VALUE;

        // For all cells in maze...
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

    /**
     * Check if the cell is within the boundaries.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return if it's within or not.
     */
    private boolean isWithinBoundary(int x, int y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public int getListSize(){
        return listSize;
    }

    public boolean checkIfPath(String algo){
        System.out.println("checking in checkIfPath");
        switch (algo){
            case Constants.DIJK_HEAP -> {
                return heapHasPath;
            }
            case Constants.DIJK_DEQ -> {
                return dequeHasPath;
            }
            case Constants.ASTAR -> {
                return astarHasPath;
            }
        }
        return false;
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
        astarHasPath = false;
        heapHasPath = false;
        dequeHasPath = false;
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
