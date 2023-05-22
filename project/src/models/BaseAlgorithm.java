package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class for A* and Dijkstra's algorithms, containing common methods and attributes.
 */
public abstract class BaseAlgorithm {
    protected final boolean[][] maze;
    protected final Point start;
    protected final Point end;
    protected final BaseMazePoint[][] mazePoints;

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
        // Reverse the path so it begins at the start.
        Collections.reverse(shortestPointPath); // Technically not needed to reverse the path for our program.
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

        // Ensure that we are within the bounds of the 2D array (avoid ArrayIndexOutOfBounds exceptions).
        // Add neighbours in all directions if so.
        if (x > 0 && maze[x - 1][y]) {
            neighbours.add((T) mazePoints[x - 1][y]); // Left neighbour.
        }
        if (x < maze.length - 1 && maze[x + 1][y]) {
            neighbours.add((T) mazePoints[x + 1][y]); // Right neighbour.
        }
        if (y > 0 && maze[x][y - 1]) {
            neighbours.add((T) mazePoints[x][y - 1]); // Above neighbour.
        }
        if (y < maze[0].length - 1 && maze[x][y + 1]) {
            neighbours.add((T) mazePoints[x][y + 1]); // Below neighbour.
        }

        return neighbours;
    }

    /**
     * Iterates over the maze 2D array, creating MazePoints for each point in the maze,
     * which are stored in the mazePoints 2D array.
     */
    protected abstract void convertToMazePoints();

}
