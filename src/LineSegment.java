import java.util.ArrayList;

/**
Class LineSegment used to form Shapes
@author Jude Adam
@version 1.0.0 16/02/2023
@implSpec  The two points must have different coordinates
 */
public record LineSegment(Point p1, Point p2) {
    /**
    Constructor method for LineSegment class
    @param p1 Point 1
     @param p2 Point 2
     */
    public LineSegment {
        if (p1.x() == p2.x() && p1.y() == p2.y()) {
            throw new IllegalArgumentException("Invalid Line Segment");
        }
    }

    /**
     ccw method to see if 3 points are in counter clockwise order
     @param a Point a
     @param b Point b
     @param c Point c
     @return True if a,b and c are in counter clockwise order
     @see <a href="https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/">...</a>
     */
    private boolean ccw(Point a, Point b, Point c) {
        return (c.y() - a.y()) * (b.x() - a.x()) > (b.y() - a.y()) * (c.x() - a.x());
    }

    /**
     Intercepts, method to see if a segment intersects another
     @param other Other Line Segment
     @return True if the caller intersects the other segment
     @see <a href="https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/">...</a>
     */
    public boolean intercepts(LineSegment other) {
        return ccw(this.p1, other.p1(), other.p2()) != ccw(this.p2, other.p1(), other.p2()) &&
                ccw(this.p1, this.p2, other.p1()) != ccw(this.p1, this.p2, other.p2());
    }

    /**
     Length method
     @return Distance from p1 to p2
     */
    public double length() {
        return p1.dist(p2);
    }

    /**
     * drawLine method, calculates the points that go along the line segment, this being done using the Bresenham Line Drawing Algorithm
     * @return array list containing the points that go along the line segment
     * @see <a href="https://www.sanfoundry.com/java-program-bresenham-line-algorithm/">...</a>
     */

    public ArrayList<Point> drawLine() {
        ArrayList<Point> result = new ArrayList<>();
        int x0 = p1.x();
        int y0 = p1.y();
        int x1 = p2.x();
        int y1 = p2.y();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            result.add(new Point(x0, y0));
            if (x0 == x1 && y0 == y1)
                break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return result;
    }

    /**
     Method to see Segment  to point P
     @param p Point
     @return double value of shortest distance from s to point p
     */
    public double shortestDistance(Point p) {
        double x1 = p1().x();
        double y1 = p1().y();
        double x2 = p2().x();
        double y2 = p2().y();
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
}
