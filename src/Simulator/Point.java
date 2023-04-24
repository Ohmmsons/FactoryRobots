package Simulator;

/**
 * Class Point used for forming line segments.
 * Point must be in the 1000x1000 area.
 * @author Jude Adam
 * @version 1.0.0 16/02/2023
 */
public record Point(int x, int y) {
    /**
     * Constructs a new Point object with the specified x and y coordinates.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @throws IllegalArgumentException if either coordinate is less than 0 or greater than 999
     */
    public Point {
        if (x < 0 || y < 0 || x > 999 || y > 999) throw new IllegalArgumentException("Invalid Point");
    }

    /**
     * Returns a string representation of this point.
     * @return a string representation of this point in the format "(x;y)"
     */
    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    /**
     * Returns a hash code value for this point.
     * @return a hash code value for this point
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = (71 * hash + this.x);
        hash = (71 * hash + this.y);
        return hash;
    }

    /**
     * Method to see distance from this point to point p.
     * @param p other point
     * @return double value of distance from this to point p
     */
    public double dist(Point p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}