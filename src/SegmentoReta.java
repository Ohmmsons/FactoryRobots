/*
Class SegmentoReta used to form Shapes
@author Jude Adam
@version 1.0.0 16/02/2023
@inv The two points must have different coordinates
 */
public class SegmentoReta{
    private final Ponto p1, p2;
    /*
    Constructor method for SegmentoReta class
    @params Ponto p1,p2
     */
    SegmentoReta(Ponto p1, Ponto p2){
//        if(p1.getX()==p2.getX() && p1.getY()==p2.getY()){System.out.println("Segmento:vi"); System.exit(0);}
        this.p1=p1;
        this.p2=p2;
    }

    public Ponto getP1() {
        return p1;
    }

    public Ponto getP2() {
        return p2;
    }

    /*
    ccw method to see if 3 points are in counter clocwise order
    @params Ponto a,b,c
    @return True if a,b and c are in counter clockwise order
    @see https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean ccw(Ponto a, Ponto b, Ponto c){
        return (c.getY()-a.getY())*(b.getX()-a.getX()) > (b.getY()-a.getY())*(c.getX()-a.getX());
    }
    /*
    Intersseta, method to see if a segment intersects another
    @params SegmentoReta other
    @return True if the caller intersects the other segment
    @see https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    public boolean intersseta(SegmentoReta other){
        return ccw(this.p1,other.getP1(),other.getP2()) != ccw(this.p2,other.getP1(),other.getP2()) &&
                ccw(this.p1,this.p2,other.getP1()) != ccw(this.p1,this.p2,other.getP2());
    }

    public double length(){
        return p1.dist(p2);
    }
}
