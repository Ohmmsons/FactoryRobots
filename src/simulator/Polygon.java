package simulator;

/**
 * Class Polygon represents a polygon that is defined by a set of points.
 * Points must be in clockwise or counterclockwise order.
 *
 * @author Jude Adam
 * @version 1.0.0 16/02/2023
 * @inv points != null &amp;&amp; points.length > 2
 */
public class Polygon extends Shape {

    /**
     * Constructor Polygon Class
     *
     * @param points Points that make up a polygon
     * @pre points != null &amp;&amp; points.length > 2
     * @post this.points == points
     * @throws IllegalArgumentException if points.length &le; 2
     */
    public Polygon(Point[] points) {
        super(points);
        if (points.length <= 2) throw new IllegalArgumentException("Not enough points");
    }

    /**
     * Determines if the given point is inside the polygon using the ray casting algorithm.
     *
     * @param p Point
     * @return True if the point is inside the polygon
     * @see <a href="https://observablehq.com/@tmcw/understanding-point-in-polygon">...</a>
     * @pre p != null
     */
    @Override
    public boolean surrounds(Point p) {
        boolean inside = false;
        int nPoints = points.length;
        int j = nPoints - 1;
        for (int i = 0; i < nPoints; j = i++) {
            if (isPointOnDifferentSides(p, points[i], points[j])) {
                inside = !inside;
            }
        }
        return inside;
    }

    /**
     * Determines if the point is on different sides of the line segment formed by points A and B.
     *
     * @param p Point to check
     * @param a First point of the line segment
     * @param b Second point of the line segment
     * @return True if the point is on different sides of the line segment
     * @pre p != null &amp;&amp; a != null &amp;&amp; b != null
     */
    private boolean isPointOnDifferentSides(Point p, Point a, Point b) {
        return ((a.y() > p.y()) != (b.y() > p.y())) &&
                (p.x() < (b.x() - a.x()) * (p.y() - a.y()) / (b.y() - a.y()) + a.x());
    }

    /**
     * Determines if the shape is intercepted by the given segment.
     *
     * @param segment Line Segment
     * @return True if the segment intercepts the caller
     * @pre segment != null
     */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        for (int i = 0; i < points.length - 1; i++) {
            if (segment.intercepts(new LineSegment(points[i], points[i + 1]))) return true;
        }
        return false;
    }
}
