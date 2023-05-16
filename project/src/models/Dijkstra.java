package models;

import java.util.*;
import java.awt.*;
import java.util.List;

/**
 * Class representing Dijkstra's algorithm using a Priority Queue, used for solving the maze and
 * finding the shortest path.
 */
public class Dijkstra {

//    private final int INF = Constants.INFINITY;
//    private final boolean[][] maze;
//    private final MazePoint start;
//    private final MazePoint end;
//    private List<MazePoint> shortestPath;
//
//
//    public Dijkstra(boolean[][] maze, Point start, Point end){
//        this.maze = maze;
//        this.start = new MazePoint(start);
//        this.end = new MazePoint(end);
//    }
//
//    /**
//     * Försöker fan gå igenom hela mazen tills den har hittat slutmöget. Men den hittar fan aldrig skiten.
//     * Mög.
//     * @return
//     */
//    public List<MazePoint> solvePath(){
//        int width = maze.length;
//        int height = maze[0].length;
//
//        // Create a map of distances to each node.
//        Map<MazePoint, Integer> distances = new HashMap<>();
//
//        // Initialize all points to infinity
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                MazePoint point = new MazePoint(new Point(i, j));
//                distances.put(point, INF);
//            }
//        }
//
//        // Initiate a priority-queue to hold points.
//        PriorityQueue<MazePoint> pq = new PriorityQueue<>(Comparator.naturalOrder());
//        distances.put(start, 0); // Set start point's distance to 0
//        pq.offer(start); // Add start point to the priority queue
//
//        System.out.println("nu går vi in här ja");
//
//        // VARFÖR FORTSÄTTER DENNA FOREVER? HITTAR TYP INTE END POINT??
//        while (!pq.isEmpty()){
//            MazePoint current = pq.poll();
//
//            // Check if the current point is the end point
//            // VARFÖR HITTAR DEN ALDRIG JÄVELN
//            if (current.equals(end)) {
//                System.out.println("nu jävlar bidde det end");
//                break;
//            }
//
//            // Explore neighbors of the current point and update distances
//            for (MazePoint neighbor : getNeighbors(current, width, height)) {
//                int newDistance = distances.get(current) + 1; // +1 as each weight is 1.
//
//                // If the newDistance is smaller than a possible defined distance-value (or infinity),
//                // update the distance and add neighbour in the PriorityQueue.
//                if (newDistance < distances.getOrDefault(neighbor, INF)) {
//                    distances.put(neighbor, newDistance);
//                    pq.offer(neighbor);
//                }
//            }
//        }
//
//        // Hit kommer den aldrig :((((((((
//        System.out.println("klar med första loopfan");
//
//        // Reconstruct the shortest path from the end-point.
//        shortestPath = new ArrayList<>();
//        MazePoint current = end;
//        System.out.println(current);
//
//        // Perform comparison until the loop has reached the start-point.
//        while (current != null && !current.equals(start)) {
//            System.out.println(current);
//
//            // Add current point.
//            shortestPath.add(current);
//
//            // Initialize minDistance to infinity and minNeighbor.
//            int minDistance = INF;
//            MazePoint minNeighbor = null;
//
//            // For each neighbor to current point...
//            for (MazePoint neighbor : getNeighbors(current, width, height)) {
//                // Get value of distance (or infinity if no distance has been defined).
//                int distance = distances.getOrDefault(neighbor, INF);
//
//                // If the distance is smaller than minDistance (currently infinity),
//                // update the distance and closest neighbor.
//                if (distance < minDistance) {
//                    minDistance = distance;
//                    minNeighbor = neighbor;
//                }
//            }
//
//            // Continue on closest neighbor.
//            current = minNeighbor;
//        }
//
//        // Add the start point to the shortest path
//        shortestPath.add(start);
//
//        // Reverse the path to get it from start to end
//        Collections.reverse(shortestPath);
//
//
//        return shortestPath;
//    }
//
//    /**
//     * Get the neighbours of the current coordinate.
//     * @param point current point.
//     * @param width maze-width.
//     * @param height maze-height.
//     * @return list of neighbours.
//     */
//    private List<MazePoint> getNeighbors(MazePoint point, int width, int height) {
//        int x = point.getPoint().x;
//        int y = point.getPoint().y;
//        List<MazePoint> neighbors = new ArrayList<>();
//
//        // Define the possible neighbor coordinates
//        int[] dx = {-1, 1, 0, 0};
//        int[] dy = {0, 0, -1, 1};
//
//        // Explore each neighbor
//        for (int i = 0; i < dx.length; i++) {
//            int nx = x + dx[i];
//            int ny = y + dy[i];
//
//            // Check if the neighbor is within the maze boundaries
//            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
//                // Check if the neighbor is true (white pixel = open path)
//                if (maze[nx][ny]) {
//                    neighbors.add(new MazePoint(new Point(nx, ny)));
//                }
//
//                if (nx == end.getPoint().x && ny == end.getPoint().y){
//                    System.out.println("inne i neighbours or de vidde bra");
//                }
//            }
//        }
//
//        return neighbors;
//    }

    private final boolean[][] maze;
    private final MazePointDijkstra[][] mazePoints;
    private final Point start;
    private final Point end;

    /**
     * Constructor, sets the maze 2D array, start and end points and initiates a 2D array with
     * MazePointDijkstras with the same size as the original array.
     * @param maze is the maze 2D array.
     * @param start is the start point.
     * @param end is the end point.
     */
    public Dijkstra(boolean[][] maze, Point start, Point end) {
        this.maze = maze;
        this.mazePoints = new MazePointDijkstra[maze.length][maze[0].length];
        this.start = start;
        this.end = end;
    }

    /**
     * Finds the shortest path through the maze using Dijkstra's algorithm.
     * @return the shortest path as an array of points.
     */
    public List<Point> solveHeapPath() {
        // Convert the 2D array to a 2D array of MazePointDijkstras.
        convertToMazePoints();
        // Get the start and end.
        MazePointDijkstra startMazePoint = mazePoints[start.x][start.y];
        MazePointDijkstra endMazePoint = mazePoints[end.x][end.y];
        // Create a priority queue for prioritizing the points by distance. These points have
        // not yet been visited but are considered in the order of the priority.
        PriorityQueue<MazePointDijkstra> openSet = new PriorityQueue<>();
        // Create an array for the already visited points.
        List<MazePointDijkstra> closedSet = new ArrayList<>();
        // Initial distance is 0.
        startMazePoint.setDistance(0);
        // Add to the priority queue.
        openSet.add(startMazePoint);

        // Loop until the open set (priority queue) is empty.
        while (!openSet.isEmpty()) {
            // Poll the point at the head of the queue.
            MazePointDijkstra currentPoint = openSet.poll();
            // This has now been visited, add to the closed set.
            closedSet.add(currentPoint);
            // Check if we have reached our goal, if so return the shortest path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Otherwise continue traversing the maze, adding available neighbours to the
            // open set (prio queue).
            List<MazePointDijkstra> neighbors = getNeighbors(currentPoint);
            for (MazePointDijkstra neighbor : neighbors) {
                // Check if they are in the closed set (already visited), do not add.
                if (closedSet.contains(neighbor)) {
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
    public List<Point> solveOtherPath(){
        // Convert the 2D array to a 2D array of MazePointDijkstras.
        convertToMazePoints();
        // Get the start and end.
        MazePointDijkstra startMazePoint = mazePoints[start.x][start.y];
        MazePointDijkstra endMazePoint = mazePoints[end.x][end.y];

        // Initialize a deque to store points to be visited.
        Deque<MazePointDijkstra> openSet = new ArrayDeque<>();

        // Create an array for the already visited points.
        List<MazePointDijkstra> closedSet = new ArrayList<>();
        // Initial distance is 0.
        startMazePoint.setDistance(0);
        // Add to the priority queue.
        openSet.add(startMazePoint);

        // Initialize Map for storing visited points and its distance from start.
        //Map<MazePointDijkstra, Integer> distances = new HashMap<>();

        // Push start into deque and set distance to 0.
        //deque.push(startMazePoint);
        //distances.put(startMazePoint, 0);

        // While deque has points, get current point and perform algorithm.
        while (!openSet.isEmpty()) {
            MazePointDijkstra currentPoint = openSet.pop();

            // If it has reached the end, generate final path.
            if (currentPoint == endMazePoint) {
                return generateFinalPath(currentPoint);
            }

            // Otherwise continue traversing the maze, adding available neighbours to the
            // open set (prio queue).
            List<MazePointDijkstra> neighbors = getNeighbors(currentPoint);
            for (MazePointDijkstra neighbor : neighbors) {
                // Check if they are in the closed set (already visited), do not add.
                if (closedSet.contains(neighbor)) {
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

            // Get neighbours to current point.
            /*List<MazePointDijkstra> neighbors = getNeighbors(currentPoint);
            for (MazePointDijkstra neighbor : neighbors) {

                // Get distance and compare the distances values
                int distance = distances.get(currentPoint) + 1;

                // If it hasn't been visited or if distance is smaller than current, update the map and
                // continue search on neighbor.
                if (!distances.containsKey(neighbor) || distance < distances.get(neighbor)) {
                    distances.put(neighbor, distance);
                    deque.push(neighbor);
                }
            }*/
        }

        return Collections.emptyList();
    }

    /**
     * Iterates over the maze 2D array, creating MazePointDijkstras for each point in the maze,
     * which are stored in the mazePoints 2D array. The MazePointDijkstras are then used
     * during the algorithm path finding to store and update distance and previous point.
     */
    private void  convertToMazePoints() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                mazePoints[i][j] = new MazePointDijkstra(new Point(i, j));
            }
        }
    }

    /**
     * Creates an array of neighbouring MazePointDijkstras.
     * @param point is the current point.
     * @return an array of neighbouring MazePointDijkstras.
     */
    private List<MazePointDijkstra> getNeighbors(MazePointDijkstra point) {
        List<MazePointDijkstra> neighbors = new ArrayList<>();// Create an array for the neighbours.
        // Get the x and y coordinates.
        int x = point.getPoint().x;
        int y = point.getPoint().y;
        // Ensure that we are within the bounds of the 2D array (avoid ArrayIndexOutOfBounds exceptions).
        // Add neighbours in all directions if so.
        if (x > 0 && maze[x - 1][y]) {
            neighbors.add(mazePoints[x - 1][y]); // Left neighbour.
        }
        if (x < maze.length - 1 && maze[x + 1][y]) {
            neighbors.add(mazePoints[x + 1][y]); // Right neighbour.
        }
        if (y > 0 && maze[x][y - 1]) {
            neighbors.add(mazePoints[x][y - 1]); // Above neighbour.
        }
        if (y < maze[0].length - 1 && maze[x][y + 1]) {
            neighbors.add(mazePoints[x][y + 1]); // Below neighbour.
        }
        return neighbors;
    }

    /**
     * Generates the final shortest path by reconstructing it from the start point to the end point.
     * @param endPoint is the end point.
     * @return the path as an array of points.
     */
    private List<Point> generateFinalPath(MazePointDijkstra endPoint) {
        List<Point> shortestPointPath = new ArrayList<>(); // Create an array for the points.
        // Set the current point as the end point to begin.
        MazePointDijkstra currentPoint = endPoint;
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
