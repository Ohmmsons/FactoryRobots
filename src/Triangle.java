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
    Triangle(Point[] points) {
        super(points);
        if (points.length != 3 || (points[0].x() * (points[1].y() - points[2].y()) + points[1].x() * (points[2].y() - points[0].y()) + points[2].x() * (points[0].y() - points[1].y())) == 0) {
            System.out.println("Triangle:vi");
            System.exit(0);
        }
    }
}
