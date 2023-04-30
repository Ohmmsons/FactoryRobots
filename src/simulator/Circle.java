package simulator;

/**
 * @author Jude Adam
 * @version 1.0.0 16/02/2023
 * @inv r > 0
 * Radius cannot be less than or equal to 0
 */
public class Circle extends Shape {

    private final double r;

    /**
     * Constructor for Circle Class
     *
     * @param points Center point of the circle
     * @param r      Radius of the circle
     * @pre points != null &amp;&amp; points.length == 1 &amp;&amp; r > 0
     * @post this.points == points &amp;&amp; this.r == r
     * @throws IllegalArgumentException if radius is less than or equal to 0
     */
    public Circle(Point[] points, double r) {
        super(points);
        if (r <= 0) {
            throw new IllegalArgumentException("Not a valid circle");
        }
        this.r = r;
    }

    /**
     * Getter for the circle's radius.
     *
     * @return Circle's radius
     */
    public double getRadius() {
        return r;
    }

    /**
     * Determines if the given point is inside the circle.
     *
     * @param p Point
     * @return True if the point is inside the circle, this happens when the distance from the point to the center is less than or equal to the radius
     * @pre p != null
     */
    @Override
    public boolean surrounds(Point p) {
        return p.dist(points[0]) <= r;
    }

    /**
     * Determines if the circle is intercepted by the given segment.
     *
     * @param segment Segment
     * @return True if the segment intersects the caller
     * @pre segment != null
     */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        return segment.shortestDistance(points[0]) <= r;
    }
}
