import java.util.Arrays;

/*
Class Rectangle used to form Rectangles
@author Jude Adam
@version 1.0.0 16/02/2023
@inv No 2 points can be equal and 4 right angles must be formed
 */
public class Rectangle extends Polygon {
    /*
    Constructor for Rectangle class
    @params Point points[]
     */
    Rectangle(Point[] points) {
        super(points);
        if (points.length != 4 || Arrays.stream(points).distinct().count()!=points.length || !(isOrthogonal(points[0], points[1], points[2]) && isOrthogonal(points[1], points[2], points[3]) && isOrthogonal(points[2], points[3], points[0]))) {
            System.out.println("Rectangle:vi");
            System.exit(0);
        }
    }

    /*
    isOrthogonal method to check if points a b and c form a right angle
    @params Point a,b,c
    @return True if a,b and c form a right angle
     */
    private boolean isOrthogonal(Point a, Point b, Point c) {
        return (b.getX() - a.getX()) * (b.getX() - c.getX()) + (b.getY() - a.getY()) * (b.getY() - c.getY()) == 0;
    }

}
