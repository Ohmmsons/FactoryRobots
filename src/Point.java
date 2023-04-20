/**
Class Point used for forming line segments
@author Jude Adam
@version 1.0.0 16/02/2023
@implSpec Point must be in the 1000x1000 area
 */
public record Point(int x, int y) {
    public Point{if(x<0||y<0||x>999||y>999) throw new IllegalArgumentException("Invalid Point");}
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
