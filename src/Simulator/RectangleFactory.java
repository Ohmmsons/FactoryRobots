package Simulator;

/**
 * The RectangleFactory class implements the ShapeFactory interface to create Rectangle objects.
 * @author Jude Adam
 * @version 1.0.0 24/04/2023
 */
public class RectangleFactory implements ShapeFactory{
    private final Generator generator;

    /**
     * Constructs a new RectangleFactory object with the specified Generator object.
     * @param generator the Generator object to use for generating random values
     */
    public RectangleFactory(Generator generator){
        this.generator = generator;
    };

    /**
     * Creates a new Rectangle object with four corner points.
     * The first corner point has x and y coordinates between 100 (inclusive) and 900 (exclusive).
     * The second corner point has the same y coordinate as the first corner point and an x coordinate that is between 10 and 50 units greater than the first corner point's x coordinate.
     * The third corner point has the same x coordinate as the second corner point and a y coordinate that is between 10 and 50 units less than the second corner point's y coordinate.
     * The fourth corner point has the same y coordinate as the third corner point and the same x coordinate as the first corner point.
     * @return a new Rectangle object with four corner points
     */
    @Override
    public Shape createShape() {
        Point Corner1 = new Point(generator.nextInt(100,900),generator.nextInt(100,900));
        Point Corner2 = new Point(Corner1.x()+generator.nextInt(10,50),Corner1.y());
        Point Corner3 = new Point(Corner2.x(), Corner2.y()-generator.nextInt(10,50));
        Point Corner4 = new Point(Corner1.x(),Corner3.y());
        return new Rectangle(new Point[]{Corner1,Corner2,Corner3,Corner4});
    }
}