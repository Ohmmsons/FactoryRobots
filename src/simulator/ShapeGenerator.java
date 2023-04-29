package simulator;

import java.util.Random;

/**
 * The Shape Generator class provides methods for generating random numbers and shapes.
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 * @inv rng != null
 */
public class ShapeGenerator{

    private final Random rng;

    /**
     * Constructor for ShapeGenerator Class
     * @param rng Random number generator
     * @pre rng != null
     * @post this.rng == rng
     */
    public ShapeGenerator(Random rng){
        this.rng = rng;
    }

    /**
     * Generates a Shape object of the specified type using the appropriate factory class.
     *
     * @param shapeType the type of shape to generate ("circle", "rectangle", or "triangle")
     * @pre shapeType != null
     * @return a Shape object of the specified type
     * @throws IllegalArgumentException if shapeType is not a valid shape type
     */
    public Shape generateShape(String shapeType) {
        return switch (shapeType.toLowerCase()) {
            case "circle" -> new CircleFactory(rng).createShape();
            case "rectangle" -> new RectangleFactory(rng).createShape();
            case "triangle" -> new TriangleFactory(rng).createShape();
            default -> throw new IllegalArgumentException("Invalid shape type: " + shapeType);
        };
    }
}
