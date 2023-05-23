package models;

import java.awt.*;

/**
 * Represents a Maze Point used by the A* algorithm. Implements the comparable interface for
 * comparing paths. Similar to the MazePointDijkstra class but have the added functionality
 * of heuristic value specifically used by the A* algorithm.
 */
public class MazePointAStar extends BaseMazePoint{
    protected int heuristicValue; // Represents the heuristic value of the point, used to estimate the distance.

    /**
     * Constructor.
     * @param point is the coordinate point.
     */
    public MazePointAStar(Point point) {
        super(point);
        this.heuristicValue = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(BaseMazePoint otherPoint) {
        int totalCost = this.distance + this.heuristicValue;
        int otherTotalCost = otherPoint.distance + ((MazePointAStar) otherPoint).heuristicValue;
        return Integer.compare(totalCost, otherTotalCost);
    }
}
