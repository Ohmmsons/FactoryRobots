import java.util.Random;

/*
Class Circle
@author Jude Adam
@version 1.0.0 16/02/2023
@inv Radius cannot be less than or equal to 0
 */
public class Circle extends Shape {

    private final double r;

    /*
    Constructor for Circle Class
    @param Point[] points, double r
 */
    Circle(Point[] points, double r) {
        super(points);
        if (r < 0) {
            System.out.println("Circle:vi");
            System.exit(0);
        }
        this.r = r;
    }

    Circle(Random generator){
        super(generator);
        this.points = new Point[]{new Point(generator.nextInt(50,950),generator.nextInt(50,950))};
        this.r = generator.nextInt(5,30);
    }

    /*
   Method to see shortest distance from Segment s to point p
   @params LineSegment s, Point p
   @return double value of shortest distance from s to point p
    */
    private double shortestDistance(LineSegment s, Point p) {
        double x1 = s.p1().x();
        double y1 = s.p1().y();
        double x2 = s.p2().x();
        double y2 = s.p2().y();
        double x3 = p.x();
        double y3 = p.y();
        double px = x2 - x1;
        double py = y2 - y1;
        double temp = (px * px) + (py * py);
        double u = ((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if (u > 1) u = 1;
        else if (u < 0) u = 0;
        double x = x1 + u * px;
        double y = y1 + u * py;
        double dx = x - x3;
        double dy = y - y3;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /*
     surrounds,
     @params Point p
     @return True if the point is inside the circle, this happens when the distance from the point to the center <= radius
      */
    @Override
    public boolean surrounds(Point p) {
        return p.dist(points[0])<=r;
    }

    /*
       Method to see if circle is intercepted by segment
       @params LineSegment segment
       @return true if segment intersects caller
        */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        return shortestDistance(segment, points[0]) <= r;
    }
}
