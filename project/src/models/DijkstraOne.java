package models;

import support.Constants;

import java.util.*;
import java.awt.*;
import java.util.List;

public class DijkstraOne {

    private final int INF = Constants.INFINITY;
    private final boolean[][] maze;
    private final MazePoint start;
    private final MazePoint end;
    private List<MazePoint> shortestPath;


    public DijkstraOne(boolean[][] maze, Point start, Point end){
        this.maze = maze;
        this.start = new MazePoint(start);
        this.end = new MazePoint(end);
    }

    /**
     * Försöker fan gå igenom hela mazen tills den har hittat slutmöget. Men den hittar fan aldrig skiten.
     * Mög.
     * @return
     */
    public List<MazePoint> solvePath(){
        int width = maze.length;
        int height = maze[0].length;

        // Create a map of distances to each node.
        Map<MazePoint, Integer> distances = new HashMap<>();

        // Initialize all points to infinity
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                MazePoint point = new MazePoint(new Point(i, j));
                distances.put(point, INF);
            }
        }

        // Initiate a priority-queue to hold points.
        PriorityQueue<MazePoint> pq = new PriorityQueue<>(Comparator.naturalOrder());
        distances.put(start, 0); // Set start point's distance to 0
        pq.offer(start); // Add start point to the priority queue

        System.out.println("nu går vi in här ja");

        // VARFÖR FORTSÄTTER DENNA FOREVER? HITTAR TYP INTE END POINT??
        while (!pq.isEmpty()){
            MazePoint current = pq.poll();

            // Check if the current point is the end point
            // VARFÖR HITTAR DEN ALDRIG JÄVELN
            if (current.equals(end)) {
                System.out.println("nu jävlar bidde det end");
                break;
            }

            // Explore neighbors of the current point and update distances
            for (MazePoint neighbor : getNeighbors(current, width, height)) {
                int newDistance = distances.get(current) + 1; // +1 as each weight is 1.

                // If the newDistance is smaller than a possible defined distance-value (or infinity),
                // update the distance and add neighbour in the PriorityQueue.
                if (newDistance < distances.getOrDefault(neighbor, INF)) {
                    distances.put(neighbor, newDistance);
                    pq.offer(neighbor);
                }
            }
        }

        // Hit kommer den aldrig :((((((((
        System.out.println("klar med första loopfan");

        // Reconstruct the shortest path from the end-point.
        shortestPath = new ArrayList<>();
        MazePoint current = end;
        System.out.println(current);

        // Perform comparison until the loop has reached the start-point.
        while (current != null && !current.equals(start)) {
            System.out.println(current);

            // Add current point.
            shortestPath.add(current);

            // Initialize minDistance to infinity and minNeighbor.
            int minDistance = INF;
            MazePoint minNeighbor = null;

            // For each neighbor to current point...
            for (MazePoint neighbor : getNeighbors(current, width, height)) {
                // Get value of distance (or infinity if no distance has been defined).
                int distance = distances.getOrDefault(neighbor, INF);

                // If the distance is smaller than minDistance (currently infinity),
                // update the distance and closest neighbor.
                if (distance < minDistance) {
                    minDistance = distance;
                    minNeighbor = neighbor;
                }
            }

            // Continue on closest neighbor.
            current = minNeighbor;
        }

        // Add the start point to the shortest path
        shortestPath.add(start);

        // Reverse the path to get it from start to end
        Collections.reverse(shortestPath);


        return shortestPath;
    }

    /**
     * Get the neighbours of the current coordinate.
     * @param point current point.
     * @param width maze-width.
     * @param height maze-height.
     * @return list of neighbours.
     */
    private List<MazePoint> getNeighbors(MazePoint point, int width, int height) {
        int x = point.getPoint().x;
        int y = point.getPoint().y;
        List<MazePoint> neighbors = new ArrayList<>();

        // Define the possible neighbor coordinates
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Explore each neighbor
        for (int i = 0; i < dx.length; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            // Check if the neighbor is within the maze boundaries
            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                // Check if the neighbor is true (white pixel = open path)
                if (maze[nx][ny]) {
                    neighbors.add(new MazePoint(new Point(nx, ny)));
                }

                if (nx == end.getPoint().x && ny == end.getPoint().y){
                    System.out.println("inne i neighbours or de vidde bra");
                }
            }
        }

        return neighbors;
    }

}
