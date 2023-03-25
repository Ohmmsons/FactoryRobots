/*
Class FiguraGeometrica
@author Jude Adam
@version 1.0.0 16/02/2023
 */
public abstract class FiguraGeometrica {
    protected final Ponto[] pontos;

    /*
   Constructor method for FiguraGeometrica class
   @params Ponto[] pontos
    */
    FiguraGeometrica(Ponto[] pontos) {
        this.pontos = pontos;
    }


    /*
   Method to see if FiguraGeometrica is intercepted by segment
   @params SegmentoReta segment
   @return True if caller is intertcepted by segment
    */
    public abstract boolean isIntercepted(SegmentoReta segment);

    public Ponto[] getPontos() {
        return pontos;
    }
}
