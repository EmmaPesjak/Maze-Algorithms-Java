package models;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class representing the A* algorithm, used for solving the maze and finding the shortest path.
 * Slightly smarter (in some cases, not always in mazes since the shortest path may actually lead
 * away from the finish point) than Dijkstra's algorithm since it takes a heuristic value into consideration,
 * going towards the goal. The heuristic value is an estimation of the remaining distance from the
 * current point to the goal point, not taking the wall obstacles of the maze into consideration.
 */
public class AStar {
    private final boolean[][] maze;
    private final MazePointAStar[][] mazePoints;
    private final Point start;
    private final Point end;

    /**
     * Constructor, sets the maze 2D array, start and end points and initiates a 2D array with
     * MazePointAStars with the same size as the original array.
     * @param maze is the maze 2D array.
     * @param start is the start point.
     * @param end is the end point.
     */
    public AStar(boolean[][] maze, Point start, Point end) {
        this.maze = maze;
        this.mazePoints = new MazePointAStar[maze.length][maze[0].length];
        this.start = start;
        this.end = end;
    }

    /**
     * Iterates over the maze 2D array, creating MazePointAStars for each point in the maze,
     * which are stored in the mazePoints 2D array. The MazePointAStars are then used
     * during the A* algorithm path finding to store and update heuristics, distance, and previous point.
     */
    private void convertToMazePoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                mazePoints[i][j] = new MazePointAStar(new Point(i, j));
            }
        }
    }

    /**
     * Finds the shortest path through the maze using the A* algorithm.
     * @return the shortest path as an array of points.
     */
    public List<Point> solvePath() {
        // Convert the 2D array to a 2D array of MazePointAStars.
        convertToMazePoints();
        // Get the start and end.
        MazePointAStar startMazePoint = mazePoints[start.x][start.y];
        MazePointAStar endMazePoint = mazePoints[end.x][end.y];
        // Create a priority queue for prioritizing the points by distance and heuristic. These points have
        // not yet been visited but are considered in the order of the priority.
        PriorityQueue<MazePointAStar> openSet = new PriorityQueue<>();
        // Create an array for the already visited points.
        List<MazePointAStar> closedSet = new ArrayList<>();
        // Initial distance is 0.
        startMazePoint.setDistance(0);
        // Set the heuristic value.
        startMazePoint.setHeuristicValue(calculateTheHeuristicValue(startMazePoint, endMazePoint));
        // Add to the priority queue.
        openSet.add(startMazePoint);

        // Loop until the open set (priority queue) is empty.
        while (!openSet.isEmpty()) {
            // Poll the point at the head of the queue.
            MazePointAStar currentPoint = openSet.poll();
            // This has now been visited, add to the closed set.
            closedSet.add(currentPoint);
            // Check if we have reached our goal, if so return the shortest path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Otherwise continue traversing the maze, adding available neighbours to the
            // open set (prio queue).
            List<MazePointAStar> neighbours = getNeighbours(currentPoint);
            for (MazePointAStar neighbour : neighbours) {
                // Check if they are in the closed set (already visited), do not add.
                if (closedSet.contains(neighbour)) {
                    continue;
                }
                // Calculate a new distance by adding 1 (since the neighbours are 1 away from the current point).
                int newDistance = currentPoint.getDistance() + 1;
                // If the neighbour's current distance is higher, we update its previous point to the current point
                // and calculate and set the new distance and heuristics. Then we add it to the open set.
                if (newDistance < neighbour.getDistance()) {
                    neighbour.setPrevious(currentPoint);
                    neighbour.setDistance(newDistance);
                    neighbour.setHeuristicValue(calculateTheHeuristicValue(neighbour, endMazePoint));
                    openSet.add(neighbour);
                }
            }
        }
        // If we reach this point, it means that no path found, and we return an empty array.
        return Collections.emptyList();
    }

    /**
     * Creates an array of neighbouring MazePointAStars.
     * @param point is the current point.
     * @return an array of neighbouring MazePointAStars.
     */
    private List<MazePointAStar> getNeighbours(MazePointAStar point) {
        List<MazePointAStar> neighbours = new ArrayList<>(); // Create an array for the neighbours.
        // Get the x and y coordinates.
        int x = point.getPoint().x;
        int y = point.getPoint().y;
        // Ensure that we are within the bounds of the 2D array (avoid ArrayIndexOutOfBounds exceptions).
        // Add neighbours in all directions if so.
        if (x > 0 && maze[x - 1][y]) {
            neighbours.add(mazePoints[x - 1][y]); // Left neighbour.
        }
        if (x < maze.length - 1 && maze[x + 1][y]) {
            neighbours.add(mazePoints[x + 1][y]); // Right neighbour.
        }
        if (y > 0 && maze[x][y - 1]) {
            neighbours.add(mazePoints[x][y - 1]); // Above neighbour.
        }
        if (y < maze[0].length - 1 && maze[x][y + 1]) {
            neighbours.add(mazePoints[x][y + 1]); // Below neighbour.
        }
        return neighbours;
    }

    /**
     * Method for calculating the heuristic value between a point and the end point. The heuristic
     * value is an estimation of the remaining distance from the current point to the goal point,
     * not taking the wall obstacles of the maze into consideration.
     * @param point is the current point.
     * @param end is the end point.
     * @return the heuristic value.
     */
    private int calculateTheHeuristicValue(MazePointAStar point, MazePointAStar end) {
        // Use the absolute value of the difference in distance.
        int differenceX = Math.abs(point.getPoint().x - end.getPoint().x);
        int differenceY = Math.abs(point.getPoint().y - end.getPoint().y);
        // Sum up the x and y differences and return it as the heuristic value.
        return differenceX + differenceY;
    }

    /**
     * Generates the final shortest path by reconstructing it from the start point to the end point.
     * @param endPoint is the end point.
     * @return the path as an array of points.
     */
    private List<Point> generateFinalPath(MazePointAStar endPoint) {
        List<Point> shortestPointPath = new ArrayList<>(); // Create an array for the points.
        // Set the current point as the end point to begin.
        MazePointAStar currentPoint = endPoint;
        // Loop over, adding the point to the array, continuing to the previous point.
        while (currentPoint != null) {
            shortestPointPath.add(currentPoint.getPoint());
            currentPoint = currentPoint.getPrevious();
        }
        // Reverse the path so it begins at the start.
        Collections.reverse(shortestPointPath); // Technically not needed to reverse the path for our program.
        return shortestPointPath;
    }
}
