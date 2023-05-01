package simulator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Rectangle used to form Rectangles
 *
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 * @inv  The rectangle has 4 points, no 2 points can be equal, and the rectangle must have 4 right angles
 */
public class Rectangle extends Polygon {
    /**
     * Constructor for Rectangle class
     *
     * @param points Array of points that make up a rectangle
     * @pre points != null &amp;&amp; points.length == 4
     * @post this.points == points
     * @throws IllegalArgumentException if points.length != 4, or if the points do not form a valid rectangle
     */
    public Rectangle(Point[] points) {
        super(points);
        if (!isValidRectangle(points)) {
            throw new IllegalArgumentException("Not a valid rectangle");
        }
    }

    /**
     * Checks if the given points form a valid rectangle.
     *
     * @param points Array of points that make up a rectangle
     * @return True if the points form a valid rectangle, false otherwise
     * @pre points != null && points.length == 4
     * @post Returns true if the points form a valid rectangle with 4 distinct points and 4 right angles, false otherwise
     */
    private boolean isValidRectangle(Point[] points) {
        if (points.length != 4) {
            return false;
        }

        Set<Point> distinctPoints = new HashSet<>(Arrays.asList(points));
        if (distinctPoints.size() != points.length) {
            return false;
        }

        return isOrthogonal(points[0], points[1], points[2]) && isOrthogonal(points[1], points[2], points[3]) && isOrthogonal(points[2], points[3], points[0]);
    }

    /**
     * isOrthogonal method to check if points a, b, and c form a right angle.
     *
     * @param a Point a
     * @param b Point b
     * @param c Point c
     * @return True if a, b, and c form a right angle
     * @pre a != null &amp;&amp; b != null &amp;&amp; c != null
     */
    private boolean isOrthogonal(Point a, Point b, Point c) {
        return (b.x() - a.x()) * (b.x() - c.x()) + (b.y() - a.y()) * (b.y() - c.y()) == 0;
    }
}
