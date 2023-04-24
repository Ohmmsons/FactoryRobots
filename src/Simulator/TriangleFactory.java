package Simulator;

public class TriangleFactory implements ShapeFactory{

    private final Generator generator;

    public TriangleFactory(Generator generator){
        this.generator = generator;
    }
    @Override
    public Shape createShape() {
        Point Point1,Point2,Point3;
        do{
            Point1 = new Point(generator.nextInt(50,950),generator.nextInt(50,950));
            Point2 = new Point(Point1.x()+generator.nextInt(10,50),Point1.y()+generator.nextInt(10,50));
            Point3 =  new Point(Point1.x()+generator.nextInt(10,50),Point1.y()+generator.nextInt(10,50));
        }
        while ((Point1.x() * (Point2.y() - Point3.y()) + Point2.x() * (Point3.y() - Point1.y()) + Point3.x() * (Point1.y() - Point2.y())) == 0);
        return new Triangle(new Point[]{Point1,Point2,Point3});
    }
}
