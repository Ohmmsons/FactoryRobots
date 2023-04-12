/*
Class Shape
@author Jude Adam
@version 1.0.0 16/02/2023
 */
public abstract class Shape {
    protected final Point[] points;

    /*
   Constructor method for Shape class
   @params Point[] points
    */
    Shape(Point[] points) {
        this.points = points;
    }


    /*
   Method to see if Shape is intercepted by segment
   @params SegmentoReta segment
   @return True if caller is intertcepted by segment
    */
    public abstract boolean isIntercepted(SegmentoReta segment);

    public Point[] getPoints() {
        return points;
    }
}
