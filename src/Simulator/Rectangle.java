package Simulator;

import java.util.Arrays;

/**
Class Rectangle used to form Rectangles
@author Jude Adam
@version 1.0.0 24/04/2023
 The rectangle has 4 points, no 2 points can be equal and the rectangle must have 4 right angles
 */
public class Rectangle extends Polygon {
    /**
    Constructor for Rectangle class
    @param points
     */
    public Rectangle(Point[] points) {
        super(points);
        if (points.length != 4 || Arrays.stream(points).distinct().count()!=points.length || !(isOrthogonal(points[0], points[1], points[2]) && isOrthogonal(points[1], points[2], points[3]) && isOrthogonal(points[2], points[3], points[0]))) {
            throw  new IllegalArgumentException("Not a valid rectangle");
        }
    }

    /**
    isOrthogonal method to check if points a b and c form a right angle
    @param a Point a
    @param b Point b
    @param c Point c
    @return True if a,b and c form a right angle
     */
    private boolean isOrthogonal(Point a, Point b, Point c) {
        return (b.x() - a.x()) * (b.x() - c.x()) + (b.y() - a.y()) * (b.y() - c.y()) == 0;
    }

}
