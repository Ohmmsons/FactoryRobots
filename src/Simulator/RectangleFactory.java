package Simulator;

public class RectangleFactory implements ShapeFactory{
    private final Generator generator;

    public RectangleFactory(Generator generator){
        this.generator = generator;
    };
    @Override
    public Shape createShape() {
        Point Corner1 = new Point(generator.nextInt(100,900),generator.nextInt(100,900));
        Point Corner2 = new Point(Corner1.x()+generator.nextInt(10,50),Corner1.y());
        Point Corner3 = new Point(Corner2.x(), Corner2.y()-generator.nextInt(10,50));
        Point Corner4 = new Point(Corner1.x(),Corner3.y());
        return new Rectangle(new Point[]{Corner1,Corner2,Corner3,Corner4});
    }
}
