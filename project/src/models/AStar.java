package models;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class representing the A* algorithm deriving from BaseAlgorithm, used for solving the maze and finding the
 * shortest path.
 *
 * SKA VI HA DETTA?
 * Slightly smarter (in some cases, not always in mazes since the shortest path may actually lead
 * away from the finish point) than Dijkstra's algorithm since it takes a heuristic value into consideration,
 * going towards the goal. The heuristic value is an estimation of the remaining distance from the
 * current point to the goal point, not taking the wall obstacles of the maze into consideration.
 */
public class AStar extends BaseAlgorithm {

    /**
     * Constructor, sets the maze 2D array, start and end points and initiates a 2D array with
     * MazePointAStars with the same size as the original array.
     * @param maze is the maze 2D array.
     * @param start is the start point.
     * @param end is the end point.
     */
    public AStar(boolean[][] maze, Point start, Point end) {
        super(maze, start, end);
    }

    /**
     * Finds the shortest path through the maze using the A* algorithm.
     * @return the shortest path as an array of points.
     */
    public List<Point> solvePath() {
        // Convert the 2D array to a 2D array of MazePointAStars.
        convertToMazePoints();
        // Get the start and end.
        MazePointAStar startMazePoint = (MazePointAStar) mazePoints[start.x][start.y];
        MazePointAStar endMazePoint = (MazePointAStar) mazePoints[end.x][end.y];

        // Create a priority queue for prioritizing the points by distance and heuristic. These points have
        // not yet been visited but are considered in the order of the priority.
        PriorityQueue<MazePointAStar> openSet = new PriorityQueue<>();

        // Create a boolean 2d-array for the already visited points.
        boolean[][] visited = new boolean[maze.length][maze[0].length];

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

            // Mark the current point as visited.
            visited[currentPoint.getPoint().x][currentPoint.getPoint().y] = true;

            // Check if we have reached our goal, if so return the shortest path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Otherwise continue traversing the maze, adding available neighbours to the
            // open set (prio queue).
            List<MazePointAStar> neighbours = getNeighbours(currentPoint);
            for (MazePointAStar neighbor : neighbours) {
                // Check if they are in the closed set (already visited), do not add.
                if (visited[neighbor.getPoint().x][neighbor.getPoint().y]) {
                    continue;
                }

                // Calculate a new distance by adding 1 (since the neighbours are 1 away from the current point).
                int newDistance = currentPoint.getDistance() + 1;
                // If the neighbour's current distance is higher, we update its previous point to the current point
                // and calculate and set the new distance and heuristics. Then we add it to the open set.
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setPrevious(currentPoint);
                    neighbor.setDistance(newDistance);
                    neighbor.setHeuristicValue(calculateTheHeuristicValue(neighbor, endMazePoint));
                    openSet.add(neighbor);
                }
            }
        }
        // If we reach this point, it means that no path found, and we return an empty array.
        return Collections.emptyList();
    }


    /**
     * Method for calculating the heuristic value between a point and the end point. The heuristic
     * value is an estimation of the remaining distance from the current point to the goal point,
     * not taking the wall obstacles of the maze into consideration and allowing diagonal movement.
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
     * {@inheritDoc}
     */
    @Override
    protected void convertToMazePoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                mazePoints[i][j] = new MazePointAStar(new Point(i, j));
            }
        }
    }
}
