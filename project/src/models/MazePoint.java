package models;

import java.awt.*;

public class MazePoint implements Comparable<MazePoint> {
    private final Point point;
    private int distance;

    public MazePoint(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");  // TODO Ska vi verkligen throwa här? bättre att skriva något felmeddelande eller blir det det automatiskt?
        }
        this.point = point;
        this.distance = Integer.MAX_VALUE;
    }

    public Point getPoint() {
        return point;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(MazePoint other) {
        return Integer.compare(this.distance, other.distance);
    }
}

