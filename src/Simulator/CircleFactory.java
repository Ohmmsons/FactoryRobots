package Simulator;

public class CircleFactory implements ShapeFactory {
    private Generator generator;

    public CircleFactory(Generator generator) {
        this.generator = generator;
    }
    @Override
    public Shape createShape() {
        Point center = new Point(generator.nextInt(50, 950), generator.nextInt(50, 950));
        double radius = generator.nextInt(5, 30);
        return new Circle(new Point[]{center}, radius);
    }
}