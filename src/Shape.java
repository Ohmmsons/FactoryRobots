import java.util.Random;

/**
 * Class Shape
 *
 * @author Jude Adam
 * @version 1.0.0 16/02/2023
 */
public abstract class Shape {
    protected Point[] points;

    /**
     * Constructor method for Shape class
     *
     * @param points Points that make up shape
     */
    Shape(Point[] points) {
        if (points.length == 0) throw new IllegalArgumentException("No points");
        this.points = points;
    }

    /**
     * Constructor method for creating a Random Shape
     *
     * @param generator RNG
     */
    Shape(Random generator) {
    }

    /**
     * Surrounds
     *
     * @param p Point
     * @return True if the point is inside the shape
     */
    public abstract boolean surrounds(Point p);

    /**
     * Method to see if Shape is intercepted by segment
     *
     * @param segment Line segment
     * @return True if caller is intercepted by segment
     */
    public abstract boolean isIntercepted(LineSegment segment);

    /**
     * @return points of shape
     */
    public Point[] getPoints() {
        return points;
    }
}
