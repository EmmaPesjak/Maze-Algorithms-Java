package models;

import java.awt.*;

/**
 * Represents a Maze Point used by Dijkstra's algorithm. Implements the comparable interface for
 * comparing paths.
 */
public class MazePointDijkstra extends BaseMazePoint {

    /**
     * Constructor.
     * @param point is the coordinate point.
     */
    public MazePointDijkstra(Point point) {
        super(point);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(BaseMazePoint other) {
        return Integer.compare(this.distance, other.distance);
    }
}
