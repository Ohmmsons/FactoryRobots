package Simulator;


/**
Class Polygon
@author Jude Adam
@version 1.0.0 16/02/2023
 Points must be in clockwise or counterclockwise order
 */
public class Polygon extends Shape {

    /**
     Constructor Polygon Class
    @param points Points that make up a polygon
   */
    public Polygon(Point[] points) {
        super(points);
        if(points.length <= 2) throw  new IllegalArgumentException("Not enough points");
    }


    /**
     Surrounds
     @param p Point
     @return True if the point is inside the polygon, this is done using the ray casting algorithm inspired by the reference given.
     @see <a href="https://observablehq.com/@tmcw/understanding-point-in-polygon">...</a>
     */
    @Override
    public boolean surrounds(Point p) {
        boolean inside = false;
        int nPoints = points.length;
        int j = nPoints - 1;
        for (int i = 0; i < nPoints; j = i++) {
            if (((points[i].y() > p.y()) != (points[j].y() > p.y())) && (p.x() < (points[j].x() - points[i].x()) * (p.y() - points[i].y()) / (points[j].y() - points[i].y()) + points[i].x())) {
                inside = !inside;
            }
        }
        return inside;
    }

    /**
    isIntercepted,
    @param segment Line Segment
    @return True if the segment intercepts the caller
     */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        for (int i = 0; i < points.length - 1; i++)
            if (segment.intercepts(new LineSegment(points[i], points[i + 1]))) return true;
        return false;
    }
}
