package models;

import java.awt.*;

/**
 * Abstract class representing a MazePoint, containing common methods for derived MazePoint classes.
 */
public abstract class BaseMazePoint implements Comparable<BaseMazePoint>{
    protected Point point;
    protected BaseMazePoint previous;
    protected int distance;
    protected int heuristicValue;   // Used for A*.

    /**
     * Initialize MazePoint by setting the initial values.
     * @param point point.
     */
    public BaseMazePoint(Point point){
        this.point = point;
        this.previous = null;
        this.distance = Integer.MAX_VALUE;
        this.heuristicValue = 0;
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
     * Getter for the point's previous point.
     * @return the previous point.
     */
    public BaseMazePoint getPrevious() {
        return previous;
    }

    /**
     * Setter for the point's previous point.
     * @param previous is the previous point.
     */
    public void setPrevious(BaseMazePoint previous) {
        this.previous = previous;
    }


    /**
     * {@inheritDoc}
     * Used when inserting in the priority queue/queue for the corresponding algorithms.
     */
    @Override
    public abstract int compareTo(BaseMazePoint other);

    /**
     * Setter for the heuristic value, user for A*.
     * @param heuristic is the heuristic value.
     */
    public void setHeuristicValue(int heuristic) {
        this.heuristicValue = heuristic;
    }
}
