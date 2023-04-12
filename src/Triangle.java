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
        if (points.length != 3 || (points[0].getX() * (points[1].getY() - points[2].getY()) + points[1].getX() * (points[2].getY() - points[0].getY()) + points[2].getX() * (points[0].getY() - points[1].getY())) == 0) {
            System.out.println("Triangle:vi");
            System.exit(0);
        }
    }
}
