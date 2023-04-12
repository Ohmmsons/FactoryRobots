/*
Class Ponto used for forming line segments
@author Jude Adam
@version 1.0.0 16/02/2023
@inv Point must be in first quadrant (x>=0 && y>=0)
 */
public class Ponto {
    private final double x;
    private final double y;

    /*
    Constructor for Ponto Class
    @param double x, double y
     */
    Ponto(double x, double y) {
        if (x < 0 || y < 0) {
            System.out.println("Ponto:vi");
            System.exit(0);
        }
        this.x = x;
        this.y = y;
    }

    /*
        Equals comparator for Ponto Class
        @param Ponto other
        @return True if x and y are equal to other's x and y
        */

    @Override
    public String toString() {
        return "("+(int)this.getX()+";"+(int)this.getY()+")";
    }

    @Override
    public boolean equals(Object o) {
        Ponto x = (Ponto) o;
        return x.getX() == this.x && x.getY() == this.y;
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
    @params Ponto p
    @return double value of distance from this to point p
     */
    public double dist(Ponto p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
