package models;

import java.awt.*;

/**
 * Represents a Maze Point used by the A* algorithm. Implements the comparable interface for
 * comparing paths. Similar to the other implemented MazePoint class but have the added functionality
 * of heuristic value specifically used by the A* algorithm.
 */
public class MazePointAStar implements Comparable<MazePointAStar> {
    private final Point point;
    private int distance;
    private int heuristicValue; // Represents the heuristic value of the point, used to estimate the distance.
    private MazePointAStar previous; // Used to link the path together to be able to reconstruct it in the GUI later.

    /**
     * Constructor.
     * @param point is the coordinate point.
     */
    public MazePointAStar(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");//TODO Ska vi verkligen throwa här? bättre att skriva något felmeddelande eller blir det det automatiskt?
        }
        this.point = point;
        this.distance = Integer.MAX_VALUE; // Set the initial to max, so we can update this later when running the algorithm.
        this.heuristicValue = 0;
        this.previous = null;
    }

    /**
     * Getter for the point coordinate.
     * @return the point.
     */
    public Point getPoint() {
        return point;
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
     * Getter for the heuristic value. Not currently used by other classes since we compare
     * heuristics in the compareTo() method, remove?
     * @return the heuristic value.
     */
    public int getHeuristicValue() {
        return heuristicValue;
    }

    /**
     * Setter for the heuristic value.
     * @param heuristic is the heuristic value.
     */
    public void setHeuristicValue(int heuristic) {
        this.heuristicValue = heuristic;
    }

    /**
     * Getter for the point's previous point.
     * @return the previous point.
     */
    public MazePointAStar getPrevious() {
        return previous;
    }

    /**
     * Setter for the point's previous point.
     * @param previous is the previous point.
     */
    public void setPrevious(MazePointAStar previous) {
        this.previous = previous;
    }

    /**
     * {@inheritDoc}
     * Used when inserting in the priority queue.
     */
    @Override
    public int compareTo(MazePointAStar otherPoint) {
        // Compare both the heuristic value and the distance.
        int totalCost = this.distance + this.heuristicValue;
        int otherTotalCost = otherPoint.distance + otherPoint.heuristicValue;
        return Integer.compare(totalCost, otherTotalCost);
    }
}
