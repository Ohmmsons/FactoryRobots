import java.util.Random;

/*
Class Polygon
@author Jude Adam
@version 1.0.0 16/02/2023
 */
public class Polygon extends Shape {

    /*
     Constructor for Polygon Class
    @param Point[] points
   */
    Polygon(Point[] points) {
        super(points);
        if(points.length <= 2) throw  new IllegalArgumentException("Not enough points");
    }

    /*
         Constructor for creating random polygon
        @param Random generator
       */
    Polygon(Random generator) {
        super(generator);
    }

    /*
     surrounds,
     @params Point p
     @return True if the point is inside the polygon, this is done using the ray casting algorithm inspired by the reference given.
     @see https://observablehq.com/@tmcw/understanding-point-in-polygon
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

    /*
    isIntercepted,
    @params LineSegment segment
    @return True if the segment intercepts the caller
     */
    @Override
    public boolean isIntercepted(LineSegment segment) {
        for (int i = 0; i < points.length - 1; i++)
            if (segment.intercepts(new LineSegment(points[i], points[i + 1]))) return true;
        return false;
    }
}
