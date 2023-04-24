package Simulator;

import java.util.Random;

/**
 * The Generator class provides methods for generating random numbers and shapes.
 */
public class Generator{

    public Random rng;

    /**
     * Constructs a new Generator object with a default seed for the Random object.
     */
    public Generator(){
        rng = new Random();
    }

    /**
     * Constructs a new Generator object with the specified seed for the Random object.
     * @param seed the seed for the Random object
     */
    public Generator(int seed){
        rng = new Random(seed);
    }

    /**
     * Constructs a new Generator object with the specified Random object.
     * @param r the Random object to use
     */
    public Generator(Random r){
        rng = r;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
     * @return the next pseudorandom, uniformly distributed int value from this random number generator's sequence
     */
    public int nextInt(){
        return rng.nextInt();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from this random number generator's sequence.
     * @return the next pseudorandom, uniformly distributed boolean value from this random number generator's sequence
     */
    public boolean nextBoolean(){
        return rng.nextBoolean();
    }

    /**
     * Returns an array of pseudorandom, uniformly distributed int values between start (inclusive) and bound (exclusive) from this random number generator's sequence.
     * @param n the number of values to generate
     * @param start the lower bound (inclusive) of the range of generated values
     * @param bound the upper bound (exclusive) of the range of generated values
     * @return an array of pseudorandom, uniformly distributed int values between start (inclusive) and bound (exclusive) from this random number generator's sequence
     */
    public int[] ints(int n, int start, int bound){
        return rng.ints(n, start, bound).toArray();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
     * @return the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence
     */
    public double nextDouble(){
        return rng.nextDouble();
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
     * @param bound the upper bound (exclusive) on the range of generated values
     * @return a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence
     */
    public int nextInt(int bound){
        return rng.nextInt(bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between start (inclusive) and bound (exclusive), drawn from this random number generator's sequence.
     * @param start the lower bound (inclusive) on the range of generated values
     * @param bound the upper bound (exclusive) on the range of generated values
     * @return a pseudorandom, uniformly distributed int value between start (inclusive) and bound (exclusive), drawn from this random number generator's sequence
     */
    public int nextInt(int start, int bound){
        return rng.nextInt(start,bound);
    }


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
            x = (int) (midX + rng.nextGaussian() * stdDev);
            y = (int) (midY + rng.nextGaussian() * stdDev);
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