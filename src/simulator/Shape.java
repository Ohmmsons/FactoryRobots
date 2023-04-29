package simulator;

/**
 * Class Shape represents a shape that is defined by a set of points.
 *
 * @author Jude Adam
 * @version 1.0.0 16/02/2023
 * @inv points != null && points.length > 0
 */
public abstract class Shape {
    protected Point[] points;

    /**
     * Constructor method for Shape class
     *
     * @param points Points that make up shape
     * @pre points != null && points.length > 0
     * @post this.points == points
     * @throws IllegalArgumentException if points is null or points.length is 0
     */
    Shape(Point[] points) {
        if (points == null || points.length == 0) throw new IllegalArgumentException("No points");
        this.points = points;
    }

    /**
     * Determines if the given point is inside the shape.
     *
     * @param p Point
     * @return True if the point is inside the shape
     * @pre p != null
     */
    public abstract boolean surrounds(Point p);

    /**
     * Determines if the shape is intercepted by the given segment.
     *
     * @param segment Line segment
     * @return True if the shape is intercepted by segment
     * @pre segment != null
     */
    public abstract boolean isIntercepted(LineSegment segment);

    /**
     * Gets the points that make up the shape.
     *
     * @return points of shape
     * @post return != null && return.length > 0
     */
    public Point[] getPoints() {
        return points;
    }
}
