package Simulator;

import java.util.Random;

/**
 * The CircleFactory class implements the ShapeFactory interface to create Circle objects.
 *
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 * @inv generator != null
 */
public class CircleFactory implements ShapeFactory {
    private final Random generator;

    /**
     * Constructs a new CircleFactory object with the specified Generator object.
     *
     * @param generator the Generator object to use for generating random values
     * @pre generator != null
     * @post A CircleFactory instance is created with the specified generator.
     */
    public CircleFactory(Random generator) {
        if (generator == null) {
            throw new IllegalArgumentException("Generator cannot be null");
        }
        this.generator = generator;
    }

    /**
     * Creates a new Circle object with a random center point and radius.
     * The center point has x and y coordinates between 50 (inclusive) and 950 (exclusive).
     * The radius is between 5 (inclusive) and 30 (exclusive).
     *
     * @return a new Circle object with a random center point and radius
     * @post The returned Circle has a valid center point and radius.
     */
    @Override
    public Shape createShape() {
        Point center = new Point(generator.nextInt(50, 950), generator.nextInt(50, 950));
        double radius = generator.nextInt(5, 30);
        return new Circle(new Point[]{center}, radius);
    }
}
