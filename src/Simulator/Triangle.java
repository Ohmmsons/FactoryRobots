package Simulator;

import Simulator.Point;

import java.util.Random;

/**
Class Triangle
@author Jude Adam
@version 1.0.0 09/03/2023
  The figure must have 3 points and have an angle sum of 180
 */
public class Triangle extends Polygon {
    /**
     Constructor method for Simulator.Triangle class
     @param points Points of triangle
     */
    public Triangle(Point[] points) {
        super(points);
        if (points.length != 3 || (points[0].x() * (points[1].y() - points[2].y()) + points[1].x() * (points[2].y() - points[0].y()) + points[2].x() * (points[0].y() - points[1].y())) == 0) {
            throw new IllegalArgumentException("Not a valid triangle");
        }
    }
}
