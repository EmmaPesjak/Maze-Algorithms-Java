package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for A* and Dijkstra's algorithms, containing common methods and attributes.
 */
public abstract class BaseAlgorithm {
    protected final boolean[][] maze;
    protected final Point start;
    protected final Point end;
    protected final BaseMazePoint[][] mazePoints;

    /**
     * Constructor taking arguments for the maze and start- and end-points.
     * @param maze maze.
     * @param start start-point.
     * @param end end-point.
     */
    public BaseAlgorithm(boolean[][] maze, Point start, Point end) {
        this.maze = maze;
        this.start = start;
        this.end = end;
        this.mazePoints = new BaseMazePoint[maze.length][maze[0].length];
    }

    /**
     * Generates the final shortest path by reconstructing it from the start point to the end point.
     * @param endPoint is the end point.
     * @return the path as an array of points.
     */
    protected List<Point> generateFinalPath(BaseMazePoint endPoint) {
        List<Point> shortestPointPath = new ArrayList<>(); // Create an array for the points.
        // Set the current point as the end point to begin.
        BaseMazePoint currentPoint = endPoint;
        // Loop over, adding the point to the array, continuing to the previous point.
        while (currentPoint != null) {
            shortestPointPath.add(currentPoint.getPoint());
            currentPoint = currentPoint.getPrevious();
        }
        return shortestPointPath;
    }

    /**
     * Creates an array of neighbouring MazePoints.
     * @param point is the current point.
     * @return an array of neighbouring MazePoints.
     */
    protected <T extends BaseMazePoint> List<T> getNeighbours(T point) {
        List<T> neighbours = new ArrayList<>();

        // Get the x and y coordinates.
        int x = point.getPoint().x;
        int y = point.getPoint().y;

        // Define the offsets for neighboring cells.
        int[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Left, Right, Above, Below

        // Add neighbors in all directions if they are within bounds and valid.
        for (int[] offset : offsets) {
            int newX = x + offset[0];
            int newY = y + offset[1];

            if (newX >= 0 && newX < maze.length && newY >= 0 && newY < maze[0].length && maze[newX][newY]) {
                neighbours.add((T) mazePoints[newX][newY]);
            }
        }
        return neighbours;
    }

    /**
     * Iterates over the maze 2D array, creating MazePoints for each point in the maze,
     * which are stored in the mazePoints 2D array.
     */
    protected abstract void convertToMazePoints();
}
