package models;


// Detta blir ju typ vår MazeGenerator/solver

import support.Stack;


import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Main model class, responsible for handling the application's data and performing calculations.
 * @author Emma Pesjak
 */
public class MainModel {

    private static final int MAX_PANEL_WIDTH = 700;
    private static final int MAX_PANEL_HEIGHT = 700;
    private int[][] binaryMaze;


    public MainModel() {

    }

    // Kirra mazarna
    public JPanel calculate(String fileName) throws IOException {

        // Get the image.
        BufferedImage image = ImageIO.read(new File("project/src/mazeImages/" + fileName));
        generateMaze(image);

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

        System.out.println(binaryImage);

        //generateMaze(binaryImage);

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

    private void generateMaze(BufferedImage binaryImage){
        int height = binaryImage.getHeight();
        int width = binaryImage.getWidth();
        // Create a 2D array to hold the binary image.
        int[][] binaryArray = new int[height][width];

        // Iterate over the pixels of the binary image.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the grayscale value of the pixel.
                int grayscale = binaryImage.getRGB(x, y) & 0xFF;

                // If the grayscale value is 0, set the corresponding value in the array to true (indicating a wall).
                // Otherwise, set it to false (indicating an open space).
                if (grayscale == 0) {
                    binaryArray[y][x] = 1;
                } else {
                    binaryArray[y][x] = 0;
                }
            }
        }

        // The complete maze
        /*for (int i = 0; i < binaryArray.length; i++) {
            for (int j = 0; j < binaryArray[i].length; j++) {
                System.out.print(binaryArray[i][j] + " ");
            }
            System.out.println();
        }*/

        // Identify boundaries of walls, to then be able to find start/destination.
        // Find minimum and maximum x and y coordinates of walls.
        int minX = width - 1, minY = height - 1, maxX = 0, maxY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (binaryArray[y][x] == 1) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        // Calculate width and height of maze.
        int mazeWidth = maxX - minX + 1;
        int mazeHeight = maxY - minY + 1;

        // Create new maze array.
        int[][] mazeArray = new int[mazeHeight][mazeWidth];

        // Copy contents of binaryArray to mazeArray, removing outer walls.
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                mazeArray[y - minY][x - minX] = binaryArray[y][x];
            }
        }

        // The final maze
        /*for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[i].length; j++) {
                System.out.print(mazeArray[i][j] + " ");
            }
            System.out.println();
        }*/

        // Find start and destination points
        findHolesInMaze(mazeArray);

    }

    public void findHolesInMaze(int[][] maze) {
        int width = maze[0].length;
        int height = maze.length;

        // Initialize a boolean matrix to keep track of which cells have been visited.
        boolean[][] visited = new boolean[height][width];

        // Iterate over each cell in the maze.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == 0 && !visited[y][x]) {
                    // This cell is part of a hole.

                    // Initialize a list to hold the cells that we visit during the search.
                    List<Point> holes = new ArrayList<Point>();

                    // Perform a depth-first search to find all cells in this hole.
                    searchForCellsInHole(x, y, maze, visited, holes);

                    // If we found exactly three cells in the hole, we assume that they are the start and end points.
                    if (holes.size() == 3) {
                        Point start = holes.get(0);
                        Point end = holes.get(1);
                        System.out.println("Found hole at (" + start.x + ", " + start.y + ") and (" + end.x + ", " + end.y + ")");
                    }
                }
            }
        }
    }

    private static void searchForCellsInHole(int x, int y, int[][] maze, boolean[][] visited, List<Point> cellsInHole) {
        // Add the current cell to the list of cells in the hole.
        cellsInHole.add(new Point(x, y));

        // Mark the current cell as visited.
        visited[y][x] = true;

        // Check each neighboring cell.
        int[][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : neighbors) {
            int neighborX = x + offset[0];
            int neighborY = y + offset[1];
            if (neighborX >= 0 && neighborX < maze[0].length && neighborY >= 0 && neighborY < maze.length
                    && maze[neighborY][neighborX] == 0 && !visited[neighborY][neighborX]) {
                // This neighboring cell is part of the same hole.
                searchForCellsInHole(neighborX, neighborY, maze, visited, cellsInHole);
            }
        }
    }


    /**
     * Generate a maze from the binary image, and find start and destination points.
     * @param
     */

    /*private int[][] generateMaze(BufferedImage binaryImage) {
        // Assuming binaryImage is a 2D array of 0s and 1s, where 0 represents a white pixel and 1 represents a black pixel

        int width = binaryImage.length;
        int height = binaryImage[0].length;

        // Flood-fill the outermost pixels with gray until the walls are reached
        for (int x = 0; x < width; x++) {
            if (binaryImage[x][0] == 0) {
                floodFill(binaryImage, x, 0, 2, 0); // 2 represents the gray color
            }
            if (binaryImage[x][height-1] == 0) {
                floodFill(binaryImage, x, height-1, 2, 0);
            }
        }
        for (int y = 0; y < height; y++) {
            if (binaryImage[0][y] == 0) {
                floodFill(binaryImage, 0, y, 2, 0);
            }
            if (binaryImage[width-1][y] == 0) {
                floodFill(binaryImage, width-1, y, 2, 0);
            }
        }

        // Search for the start and end points within the maze
        int startX = -1, startY = -1, endX = -1, endY = -1;
        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {
                if (binaryImage[x][y] == 0) {
                    // Check if this is a valid start or end point by checking if it's adjacent to a gray pixel
                    if (binaryImage[x-1][y] == 2 || binaryImage[x+1][y] == 2 || binaryImage[x][y-1] == 2 || binaryImage[x][y+1] == 2) {
                        if (startX == -1 && startY == -1) {
                            startX = x;
                            startY = y;
                        } else {
                            endX = x;
                            endY = y;
                        }
                    }
                }
            }
        }

    }

    public static void floodFill(int[][] maze, int x, int y, int targetColor, int replacementColor) {
        int numRows = maze.length;
        int numCols = maze[0].length;

        if (x < 0 || x >= numCols || y < 0 || y >= numRows) {
            return;
        }

        if (maze[y][x] != targetColor) {
            return;
        }

        maze[y][x] = replacementColor;

        floodFill(maze, x - 1, y, targetColor, replacementColor);
        floodFill(maze, x + 1, y, targetColor, replacementColor);
        floodFill(maze, x, y - 1, targetColor, replacementColor);
        floodFill(maze, x, y + 1, targetColor, replacementColor);
    }*/


    /*private void generateMaze(BufferedImage binaryImage){
        // Define constants for representing walls and open paths in the maze.
        final int WALL = 1;
        final int PATH = 0;
        int height = binaryImage.getHeight();
        int width = binaryImage.getWidth();

        // Define variables for storing the start and destination points.
        int startX = -1, startY = -1;
        int destX = -1, destY = -1;

        // Create a 2D array to represent the maze.
        int[][] maze = new int[width][height];

        // Iterate over the pixels of the binary image.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the grayscale value of the pixel.
                int grayscale = binaryImage.getRGB(x, y) & 0xFF;

                // Set the corresponding cell in the maze array to either 0 or 1.
                maze[x][y] = grayscale < 128 ? PATH : WALL;

                // Check if this cell is part of the outer walls.
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    // Check if this cell is a path.
                    if (maze[x][y] == PATH) {
                        // Check if this is the start point.
                        if (startX == -1 && startY == -1) {
                            startX = x;
                            startY = y;
                        }
                        // Check if this is the destination point.
                        else if (destX == -1 && destY == -1) {
                            destX = x;
                            destY = y;
                        }
                    }
                }
            }
        }

        // Print the maze to verify that it has been created correctly.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(maze[x][y] == WALL ? "#" : " ");
            }
            System.out.println();
        }

        // Print the start and destination points.
        System.out.println("Start point: (" + startX + ", " + startY + ")");
        System.out.println("Destination point: (" + destX + ", " + destY + ")");

    }*/

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
