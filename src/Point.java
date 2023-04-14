/*
Class Point used for forming line segments
@author Jude Adam
@version 1.0.0 16/02/2023
@inv Point must be in first quadrant (x>=0 && y>=0)
 */
public record Point(int x, int y) {
    /*
    Constructor for Point Class
    @param double x, double y
     */
    @Override
    public String toString() {
        return "("+x+";"+y+")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (71 * hash + this.x);
        hash = (71 * hash + this.y);
        return hash;
    }
    /*
    Method to see distance from this point to point p
    @params Point p
    @return double value of distance from this to point p
     */
    public double dist(Point p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
