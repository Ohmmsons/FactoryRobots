/*
Class Point used for forming line segments
@author Jude Adam
@version 1.0.0 16/02/2023
@inv Point must be in first quadrant (x>=0 && y>=0)
 */
public class Point {
    private final double x;
    private final double y;

    /*
    Constructor for Point Class
    @param double x, double y
     */
    Point(double x, double y) {
        if (x < 0 || y < 0) {
            System.out.println("Point:vi");
            System.exit(0);
        }
        this.x = x;
        this.y = y;
    }

    /*
        Equals comparator for Point Class
        @param Point other
        @return True if x and y are equal to other's x and y
        */

    @Override
    public String toString() {
        return "("+(int)this.getX()+";"+(int)this.getY()+")";
    }

    @Override
    public boolean equals(Object o) {
        Point x = (Point) o;
        return (o.getClass() == this.getClass()) && x.getX() == this.x && x.getY() == this.y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (71 * hash + this.x);
        hash = (int) (71 * hash + this.y);
        return hash;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
