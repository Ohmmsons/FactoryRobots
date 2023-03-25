/*
Class Circunferencia
@author Jude Adam
@version 1.0.0 16/02/2023
@inv Radius cannot be less than or equal to 0
 */
public class Circunferencia extends FiguraGeometrica {

    private double r;

    /*
    Constructor for Circunferencia Class
    @param Ponto[] pontos, double r
 */
    Circunferencia(Ponto[] pontos, double r) {
        super(pontos);
        if (r < 0) {
            System.out.println("Circunferencia:vi");
            System.exit(0);
        }
        this.r = r;
    }

    /*
   Method to see shortest distance from Segment s to point p
   @params Ponto p
   @return double value of shortest distance from s to point p
    */
    private double shortestDistance(SegmentoReta s, Ponto p) {
        double x1 = s.getP1().getX();
        double y1 = s.getP1().getY();
        double x2 = s.getP2().getX();
        double y2 = s.getP2().getY();
        double x3 = p.getX();
        double y3 = p.getY();
        double px = x2 - x1;
        double py = y2 - y1;
        double temp = (px * px) + (py * py);
        double u = ((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }
        double x = x1 + u * px;
        double y = y1 + u * py;
        double dx = x - x3;
        double dy = y - y3;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist;

    }

    /*
   Method to see if circle is intercepted by segment
   @params SegmentoReta segment
   @return true if segment intersects caller
    */
    @Override
    public boolean isIntercepted(SegmentoReta segment) {
        return shortestDistance(segment, pontos[0]) <= r;
    }
}
