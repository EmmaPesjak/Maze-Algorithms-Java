package models;

import java.util.*;
import java.awt.*;
import java.util.List;

/**
 * Class representing Dijkstra's algorithm using a Priority Queue, used for solving the maze and
 * finding the shortest path.
 */
public class Dijkstra extends BaseAlgorithm {

    /**
     * Constructor, sets the maze 2D array, start and end points and initiates a 2D array with
     * MazePointDijkstras with the same size as the original array.
     * @param maze is the maze 2D array.
     * @param start is the start point.
     * @param end is the end point.
     */
    public Dijkstra(boolean[][] maze, Point start, Point end) {
        super(maze, start, end);
    }

    /**
     * Finds the shortest path through the maze using Dijkstra's algorithm with a heap
     * priority queue.
     * @return the shortest path as an array of points.
     */
    public List<Point> solveHeapPath() {
        // Convert the 2D array to a 2D array of MazePointDijkstras.
        convertToMazePoints();

        // Get the start and end.
        MazePointDijkstra startMazePoint = (MazePointDijkstra) mazePoints[start.x][start.y];
        MazePointDijkstra endMazePoint = (MazePointDijkstra) mazePoints[end.x][end.y];

        // Create a priority queue for prioritizing the points by distance. These points have
        // not yet been visited but are considered in the order of the priority.
        PriorityQueue<MazePointDijkstra> openSet = new PriorityQueue<>();

        // Create a boolean array for storing the visited points.
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        // Initial distance is 0.
        startMazePoint.setDistance(0);
        // Add to the priority queue.
        openSet.add(startMazePoint);

        // Loop until the open set (priority queue) is empty.
        while (!openSet.isEmpty()) {
            // Poll the point at the head of the queue.
            MazePointDijkstra currentPoint = openSet.poll();

            // Get the current point's coordinates.
            Point currentCoordinates = currentPoint.getPoint();

            // If the current point has already been visited, skip it.
            if (visited[currentCoordinates.x][currentCoordinates.y]) {
                continue;
            }

            // Mark the current point as visited.
            visited[currentCoordinates.x][currentCoordinates.y] = true;

            // Check if we have reached our goal, if so return the shortest path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Otherwise continue traversing the maze, adding available neighbours to the
            // open set (prio queue).
            List<MazePointDijkstra> neighbors = getNeighbours(currentPoint);
            for (MazePointDijkstra neighbor : neighbors) {
                // Check if they are in the closed set (already visited), do not add.
                if (visited[neighbor.getPoint().x][neighbor.getPoint().y]) {
                    continue;
                }
                // Calculate a new distance by adding 1 (since the neighbours are 1 away from the current point).
                int tentativeDistance = currentPoint.getDistance() + 1;
                // If the neighbour's current distance is higher, we update its previous point to the current point
                // and calculate and set the new distance. Then we add it to the open set.
                if (tentativeDistance < neighbor.getDistance()) {
                    neighbor.setPrevious(currentPoint);
                    neighbor.setDistance(tentativeDistance);
                    openSet.add(neighbor);
                }
            }
        }
        // If we reach this point, it means that no path found, and we return an empty array.
        return Collections.emptyList();
    }

    /**
     * Solve the shortest path using Dequeue and map.
     * @return shortest path.
     */
    public List<Point> solveDequeuePath(){
        // Convert the 2D array to a 2D array of MazePointDijkstras.
        convertToMazePoints();
        // Get the start and end.
        MazePointDijkstra startMazePoint = (MazePointDijkstra) mazePoints[start.x][start.y];
        MazePointDijkstra endMazePoint = (MazePointDijkstra) mazePoints[end.x][end.y];

        // Initialize a deque to store points to be visited.
        Deque<MazePointDijkstra> openSet = new ArrayDeque<>();

        // Create a boolean array for storing the visited points.
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        // Initialize distance of start point to 0.
        startMazePoint.setDistance(0);
        // Add the start point to the deque.
        openSet.add(startMazePoint);

        // Loop until the deque is empty.
        while (!openSet.isEmpty()) {
            // Get the point at the front of the deque.
            MazePointDijkstra currentPoint = openSet.pollFirst();

            // If the current point has already been visited, skip it.
            if (visited[currentPoint.getPoint().x][currentPoint.getPoint().y]) {
                continue;
            }

            // Mark the current point as visited.
            visited[currentPoint.getPoint().x][currentPoint.getPoint().y] = true;


            // Check if we have reached the end, if so, return the shortest path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Get the neighbors of the current point.
            List<MazePointDijkstra> neighbors = getNeighbours(currentPoint);

            // Process the neighbors.
            for (MazePointDijkstra neighbor : neighbors) {
                // Check if they are in the closed set (already visited), do not add.
                if (visited[neighbor.getPoint().x][neighbor.getPoint().y]) {
                    continue;
                }
                // Calculate the new distance from the start point to the neighbor.
                int tentativeDistance = currentPoint.getDistance() + 1;

                // If the neighbor's current distance is higher than the new distance,
                // update its previous point and distance, then add it to the deque.
                if (tentativeDistance < neighbor.getDistance()) {
                    neighbor.setPrevious(currentPoint);
                    neighbor.setDistance(tentativeDistance);
                    openSet.addLast(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void  convertToMazePoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                mazePoints[i][j] = new MazePointDijkstra(new Point(i, j));
            }
        }
    }
}
