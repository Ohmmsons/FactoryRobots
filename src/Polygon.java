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
