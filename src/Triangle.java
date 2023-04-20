import java.util.Random;

/*
Class Triangle
@author Jude Adam
@version 1.0.0 09/03/2023
@inv The figure must have 3 points and have an angle sum of 180
 */
public class Triangle extends Polygon {
    /*
   Constructor method for Triangle class
   @params Point[] points
    */
    Triangle(Random generator){
        super(generator);
        Point Point1 = new Point(generator.nextInt(100,950),generator.nextInt(100,950));
        Point Point2 = new Point(Point1.x()-generator.nextInt(10,50),Point1.y()-generator.nextInt(10,50));
        Point Point3 =  new Point(Point1.x()-generator.nextInt(10,50),Point1.y()-generator.nextInt(10,50));
        while ((Point1.x() * (Point2.y() - Point3.y()) + Point2.x() * (Point3.y() - Point1.y()) + Point3.x() * (Point1.y() - Point2.y())) == 0){
            Point1 = new Point(generator.nextInt(50,950),generator.nextInt(50,950));
            Point2 = new Point(Point1.x()-generator.nextInt(10,50),Point1.y()-generator.nextInt(10,50));
            Point3 =  new Point(Point1.x()-generator.nextInt(10,50),Point1.y()-generator.nextInt(10,50));
        }
        this.points = new Point[]{Point1,Point2,Point3};
    }

    Triangle(Point[] points) {
        super(points);
        if (points.length != 3 || (points[0].x() * (points[1].y() - points[2].y()) + points[1].x() * (points[2].y() - points[0].y()) + points[2].x() * (points[0].y() - points[1].y())) == 0) {
            throw new IllegalArgumentException("Not a valid triangle");
        }
    }
}
