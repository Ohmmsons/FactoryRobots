package simulator;

/**
 * Class Triangle represents a triangle that is defined by a set of points.
 *
 * @author Jude Adam
 * @version 1.0.0 09/03/2023
 * @inv points.length == 3
 * @inv sum of interior angles equals 180 degrees
 */
public class Triangle extends Polygon {
    /**
     * @param points Points of triangle
     * @pre points != null &amp;&amp; points.length == 3
     * @post this.points == points
     * @throws IllegalArgumentException if points.length != 3, or if the points do not form a valid triangle
     */
    public Triangle(Point[] points) {
        super(points);
        if (points.length != 3 || (points[0].x() * (points[1].y() - points[2].y()) + points[1].x() * (points[2].y() - points[0].y()) + points[2].x() * (points[0].y() - points[1].y())) == 0) {
            throw new IllegalArgumentException("Not a valid triangle");
        }
    }
}
