package models;

import java.awt.*;

/**
 * Represents a Maze Point used by Dijkstra's algorithm. Implements the comparable interface for
 * comparing paths.
 */
public class MazePointDijkstra implements Comparable<MazePointDijkstra> {
    private final Point point;
    private MazePointDijkstra previous;
    private int distance;

    /**
     * Constructor.
     * @param point is the coordinate point.
     */
    public MazePointDijkstra(Point point) {
        this.point = point;
        this.previous = null;
        this.distance = Integer.MAX_VALUE; // Set the initial to max, so we can update this later when running the algorithm.
    }

    /**
     * Getter for the point coordinate.
     * @return the point.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Getter for the point's previous point.
     * @return the previous point.
     */
    public MazePointDijkstra getPrevious() {
        return previous;
    }

    /**
     * Setter for the point's previous point.
     * @param previous is the previous point.
     */
    public void setPrevious(MazePointDijkstra previous) {
        this.previous = previous;
    }

    /**
     * Getter for the distance.
     * @return the distance.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Setter for the distance.
     * @param distance is the distance.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * {@inheritDoc}
     * Used when inserting in the priority queue.
     */
    @Override
    public int compareTo(MazePointDijkstra other) {
        return Integer.compare(this.distance, other.distance);
    }
}
