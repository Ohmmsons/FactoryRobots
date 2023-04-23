package Simulator;

import java.util.Random;

/**
Class Circle
@author Jude Adam
@version 1.0.0 16/02/2023
  Radius cannot be less than or equal to 0
 */
public class Circle extends Shape {

    private final double r;

    /**
    Constructor for Circle Class
    @param points Center
    @param r radius
 */
    public Circle(Point[] points, double r) {
        super(points);
        if (r < 0) {
            throw  new IllegalArgumentException("Not a valid circle");
        }
        this.r = r;
    }

    public double getRadius(){
        return r;
    }

    public Circle(Generator generator){
        super(generator);
        this.points = new Point[]{new Point(generator.nextInt(50,950),generator.nextInt(50,950))};
        this.r = generator.nextInt(5,30);
    }


    /**
     surrounds,
     @param p Simulator.Point
     @return True if the point is inside the circle, this happens when the distance from the point to the center less or equal radius
      */
    @Override
    public boolean surrounds(Point p) {
        return p.dist(points[0])<=r;
    }

    /*
       Method to see if circle is intercepted by segment
       @params Simulator.Simulator.LineSegment segment
       @return true if segment intersects caller
        */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        return segment.shortestDistance(points[0]) <= r;
    }
}
