package models;

import support.Constants;
import support.RePickWhenNoPath;
import views.CustomPanel;

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
    private boolean showPoints = true; // Flag to control the display of start and end points.
    private CustomPanel panel;

    private boolean[][] maze;
    private int scale;
    private final int cellSize = 5;
    private int minX, minY, maxX, maxY;

    private List<Point> shortestPath;
    private Dijkstra dijkstra;
    private AStar aStar;

    private final RePickWhenNoPath rePick;

    /**
     * Constructor.
     * @param rePick is the rePick interface.
     */
    public MainModel(RePickWhenNoPath rePick) {
        this.rePick = rePick;
    }

    public CustomPanel getMaze(String fileName) throws IOException {
        // Get the image.
        image = ImageIO.read(new File("project/src/mazeImages/" + fileName));
        generateMaze(image);

        // Get height and width.
        int width = image.getWidth();
        int height = image.getHeight();

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
        panel = new CustomPanel() {
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

    public CustomPanel displayPath(Point startPoint, Point finish, String algo){

        // Adjust the start and end coordinates to match the cell size
        startX = startPoint.x / cellSize;
        startY = startPoint.y / cellSize;
        endX = finish.x / cellSize;
        endY = finish.y / cellSize;

        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);

        // Create a custom JPanel to display the binary image.
        panel = new CustomPanel() {
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
                        shortestPath = dijkstra.solveHeapPath();

                        // If no path was found. Only necessary to check in one of the algorithms.
                        if (shortestPath.size() == 0) {
                            try {
                                rePick.rePick();
                                return;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // Draw the shortest path.
                        drawPath(g, shortestPath);
                    }

                    case (Constants.DIJK_DEQ) -> {
                        dijkstra = new Dijkstra(maze, start, end);

                        shortestPath = dijkstra.solveDequeuePath();

                        if (shortestPath.size() != 0){
                            // Draw the shortest path.
                            drawPath(g, shortestPath);
                        }

                    }
                    case (Constants.ASTAR) -> {
                        // Solve the maze with the A* algorithm and get a list of points with the path.
                        aStar = new AStar(maze, start, end);
                        shortestPath = aStar.solvePath();

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
     * In this drawPath method, we create two arrays, xPoints and yPoints, to store the
     * x and y coordinates of the points in the path. Instead of drawing individual lines between points,
     * we pass these arrays to the drawPolyline method of the Graphics2D object. This method draws a polyline
     * connecting all the points in a single method call, resulting in better performance and reduced time
     * complexity compared to drawing individual lines.
     * @param g is the graphics.
     * @param path is the path of points to draw.
     */
    private void drawPath(Graphics g, List<Point> path) {
        g.setColor(Constants.COLOR_PATH);

        // Create Graphics 2D, so we can set the stroke thickness.
        Graphics2D g2d = (Graphics2D) g;

        // Set the thickness for the path lines.
        g2d.setStroke(new BasicStroke(3));

        int numPoints = path.size();
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        // Populate the x and y coordinate arrays for drawing the polyline.
        for (int i = 0; i < numPoints; i++) {
            Point point = path.get(i);
            xPoints[i] = (int) (point.x * cellSize * scale + cellSize / 2.0);
            yPoints[i] = (int) (point.y * cellSize * scale + cellSize / 2.0);
        }

        // Draw the polyline representing the path.
        g2d.drawPolyline(xPoints, yPoints, numPoints);
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

        // Create the 2D boolean array representing the maze
        maze = new boolean[mazeWidth][mazeHeight];

        // Define threshold, in this case, 85% of the pixels need to be white to be considered to be a white cell.
        int threshold = (int) (0.85 * (cellSize * cellSize));

        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {

                // Start/end coordinates of the cell.
                int startXCell = x * cellSize;
                int startYCell = y * cellSize;
                int endXCell = startXCell + cellSize;
                int endYCell = startYCell + cellSize;

                int whiteCount = 0;

                for (int i = startXCell; i < endXCell; i++) {
                    for (int j = startYCell; j < endYCell; j++) {
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

    /**
     * Check if the start and finish coordinates are within maze/not at a wall.
     * @param startPoint start-point.
     * @param finish end-point.
     * @return whether it's valid or not.
     */
    public boolean checkIfValid(Point startPoint, Point finish){
        // Check so that the start/finish points are within boundaries
        return maze[startPoint.x / cellSize][startPoint.y / cellSize] || !maze[finish.x / cellSize][finish.y / cellSize];
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
}
