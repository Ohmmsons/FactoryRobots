package Simulator;

import java.util.Random;

/**
 * The TriangleFactory class implements the ShapeFactory interface to create Triangle objects.
 *
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 * @inv generator != null
 */
public class TriangleFactory implements ShapeFactory {

    private final Random generator;

    /**
     * Constructs a new TriangleFactory with the specified Generator.
     *
     * @param generator the Generator used to generate random values
     * @pre generator != null
     * @post A TriangleFactory instance is created with the specified generator.
     */
    public TriangleFactory(Random generator) {
        if (generator == null) {
            throw new IllegalArgumentException("Generator cannot be null");
        }
        this.generator = generator;
    }

    /**
     * Creates a new Triangle object with random points.
     *
     * @return a new Triangle object
     * @post The returned Triangle has valid points.
     */
    @Override
    public Shape createShape() {
        Point Point1, Point2, Point3;
        do {
            Point1 = new Point(generator.nextInt(100, 900), generator.nextInt(100, 900));
            Point2 = new Point(Point1.x() + generator.nextInt(-50, 50), Point1.y() + generator.nextInt(-50, 50));
            Point3 = new Point(Point1.x() + generator.nextInt(-50, 50), Point1.y() + generator.nextInt(-50, 50));
        }
        while ((Point1.x() * (Point2.y() - Point3.y()) + Point2.x() * (Point3.y() - Point1.y()) + Point3.x() * (Point1.y() - Point2.y())) == 0);
        return new Triangle(new Point[]{Point1, Point2, Point3});
    }
}
