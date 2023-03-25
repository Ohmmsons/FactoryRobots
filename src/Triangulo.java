/*
Class Triangulo
@author Jude Adam
@version 1.0.0 09/03/2023
@inv The figure must have 3 points and have an angle sum of 180
 */
public class Triangulo extends Polygon {
    /*
   Constructor method for Triangulo class
   @params Ponto[] pontos
    */
    Triangulo(Ponto[] pontos) {
        super(pontos);
        if (pontos.length != 3 || (pontos[0].getX() * (pontos[1].getY() - pontos[2].getY()) + pontos[1].getX() * (pontos[2].getY() - pontos[0].getY()) + pontos[2].getX() * (pontos[0].getY() - pontos[1].getY())) == 0) {
            System.out.println("Triangulo:vi");
            System.exit(0);
        }
    }
}
