/*
Class Retangulo used to form Rectangles
@author Jude Adam
@version 1.0.0 16/02/2023
@inv No 2 points can be equal and 4 right angles must be formed
 */
public class Retangulo extends Polygon {
    /*
    Constructor for Retangulo class
    @params Ponto pontos[]
     */
    Retangulo(Ponto[] pontos) {
        super(pontos);
        if (pontos.length != 4 || (pontos[0].equals(pontos[1]) || pontos[0].equals(pontos[2]) || pontos[0].equals(pontos[3])) || pontos[1].equals(pontos[2]) || pontos[1].equals(pontos[3]) || pontos[2].equals(pontos[3])
                || !(isOrthogonal(pontos[0], pontos[1], pontos[2]) && isOrthogonal(pontos[1], pontos[2], pontos[3]) && isOrthogonal(pontos[2], pontos[3], pontos[0]))) {
            System.out.println("Retangulo:vi");
            System.exit(0);
        }
    }

    /*
    isOrthogonal method to check if points a b and c form a right angle
    @params Ponto a,b,c
    @return True if a,b and c form a right angle
     */
    private boolean isOrthogonal(Ponto a, Ponto b, Ponto c) {
        return (b.getX() - a.getX()) * (b.getX() - c.getX()) + (b.getY() - a.getY()) * (b.getY() - c.getY()) == 0;
    }

}
