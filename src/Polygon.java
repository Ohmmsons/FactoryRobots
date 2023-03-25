/*
Class Polygon
@author Jude Adam
@version 1.0.0 16/02/2023
 */
public class Polygon extends FiguraGeometrica {

    /*
     Constructor for Polygon Class
    @param Ponto[] pontos
   */
    Polygon(Ponto[] pontos) {
        super(pontos);
    }

    /*
    isIntercepted,
    @params SegmentoReta segment
    @return True if the segment intercepts the caller
     */
    @Override
    public boolean isIntercepted(SegmentoReta segment) {
        for (int i = 0; i < pontos.length - 1; i++)
            if (segment.intersseta(new SegmentoReta(pontos[i], pontos[i + 1]))) return true;
        return false;
    }
}
