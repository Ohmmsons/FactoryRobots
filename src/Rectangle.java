import java.util.Arrays;
import java.util.Random;

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

    Rectangle(Random generator){
        super(generator);
        Point Corner1 = new Point(generator.nextInt(100,950),generator.nextInt(100,950));
        Point Corner2 = new Point(Corner1.x()-generator.nextInt(10,50),Corner1.y()-generator.nextInt(10,50));
        while (Corner1.x()==Corner2.x()||Corner1.y()==Corner2.y()){
            Corner1 = new Point(generator.nextInt(50,950),generator.nextInt(50,950));
            Corner2 = new Point(Corner1.x()-generator.nextInt(10,50),Corner1.y()-generator.nextInt(10,50));
        }
        Point Corner3 = new Point(Corner1.x(), Corner2.y());
        Point Corner4 = new Point(Corner2.x(),Corner1.y());
        this.points = new Point[]{Corner1,Corner2,Corner3,Corner4};
    }

    /*
    isOrthogonal method to check if points a b and c form a right angle
    @params Point a,b,c
    @return True if a,b and c form a right angle
     */
    private boolean isOrthogonal(Point a, Point b, Point c) {
        return (b.x() - a.x()) * (b.x() - c.x()) + (b.y() - a.y()) * (b.y() - c.y()) == 0;
    }

}
