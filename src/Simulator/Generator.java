package Simulator;

import java.util.Random;

/**
 * The Generator class provides methods for generating random numbers and shapes.
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 */
public class Generator extends Random{

    public Generator(int seed){super(seed);}

    public Generator(){super();}
    /**
     * Generates a Point object with x and y coordinates following a Gaussian distribution with mean at the midpoint between start and end points and standard deviation stdDev.
     * The generated point is guaranteed to have x and y coordinates within [0,1000).
     *
     * @param stdDev standard deviation for Gaussian distribution
     * @param start starting point for calculating midpoint
     * @param end ending point for calculating midpoint
     *
     * @return Point object with x and y coordinates following a Gaussian distribution with mean at midpoint between start and end points and standard deviation stdDev
     */
    public Point generateGaussianPoint(double stdDev, Point start, Point end){
        double midX = (start.x() + end.x()) / 2.0;
        double midY = (start.y() + end.y()) / 2.0;
        int x,y;
        do {
            x = (int) (midX + this.nextGaussian() * stdDev);
            y = (int) (midY + this.nextGaussian() * stdDev);
        } while (x < 0 || x >= 1000 || y < 0 || y >= 1000);
        return new Point(x,y);
    }

    /**
     * Generates a Shape object of the specified type using the appropriate factory class.
     *
     * @param shapeType the type of shape to generate ("circle", "rectangle", or "triangle")
     * @return a Shape object of the specified type
     * @throws IllegalArgumentException if shapeType is not a valid shape type
     */
    public Shape generateShape(String shapeType) {
        return switch (shapeType.toLowerCase()) {
            case "circle" -> new CircleFactory(this).createShape();
            case "rectangle" -> new RectangleFactory(this).createShape();
            case "triangle" -> new TriangleFactory(this).createShape();
            default -> throw new IllegalArgumentException("Invalid shape type: " + shapeType);
        };
    }
}