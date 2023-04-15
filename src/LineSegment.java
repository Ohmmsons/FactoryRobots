/*
Class LineSegment used to form Shapes
@author Jude Adam
@version 1.0.0 16/02/2023
@inv The two points must have different coordinates
 */
public class LineSegment {
    private final Point p1, p2;
    /*
    Constructor method for LineSegment class
    @params Point p1,p2
     */
    LineSegment(Point p1, Point p2){
        if(p1.x()==p2.x() && p1.y()==p2.y()){System.out.println("Segmento:vi"); System.exit(0);}
        this.p1=p1;
        this.p2=p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    /*
    ccw method to see if 3 points are in counter clocwise order
    @params Point a,b,c
    @return True if a,b and c are in counter clockwise order
    @see https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean ccw(Point a, Point b, Point c){
        return (c.y()-a.y())*(b.x()-a.x()) > (b.y()-a.y())*(c.x()-a.x());
    }
    /*
    Intercepts, method to see if a segment intersects another
    @params LineSegment other
    @return True if the caller intersects the other segment
    @see https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    public boolean intercepts(LineSegment other){
        return ccw(this.p1,other.getP1(),other.getP2()) != ccw(this.p2,other.getP1(),other.getP2()) &&
                ccw(this.p1,this.p2,other.getP1()) != ccw(this.p1,this.p2,other.getP2());
    }

    public double length(){
        return p1.dist(p2);
    }
}