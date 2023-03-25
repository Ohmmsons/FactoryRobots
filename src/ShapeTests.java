import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShapeTests {
    @Test
    public void testInterceptTriangle1(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0),new Ponto(1.5,3.0),new Ponto(2.0,2.0)};
        Triangulo triangle = new Triangulo(pontos);
        SegmentoReta segmento = new SegmentoReta(new Ponto(1.5,1.0),new Ponto(1.5,4.0));
        assertEquals(triangle.isIntercepted(segmento),true);
    }
    @Test
    public void testInterceptTriangle2(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0),new Ponto(1.5,3.0),new Ponto(2.0,2.0)};
        Triangulo triangle = new Triangulo(pontos);
        SegmentoReta segmento = new SegmentoReta(new Ponto(3.5,1.0),new Ponto(3.5,4.0));
        assertEquals(triangle.isIntercepted(segmento),false);
    }

    @Test
    public void testInterceptRectangle1(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0),new Ponto(3.0,2.0),new Ponto(3.0,3.0),new Ponto(1.0,3.0)};
        Retangulo rectangle = new Retangulo(pontos);
        SegmentoReta segmento = new SegmentoReta(new Ponto(1.5,1.0),new Ponto(1.5,4.0));
        assertEquals(rectangle.isIntercepted(segmento),true);
    }
    @Test
    public void testInterceptRectangle2(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0),new Ponto(3.0,2.0),new Ponto(3.0,3.0),new Ponto(1.0,3.0)};
        Retangulo rectangle = new Retangulo(pontos);
        SegmentoReta segmento = new SegmentoReta(new Ponto(5.5,1.0),new Ponto(5.5,4.0));
        assertEquals(rectangle.isIntercepted(segmento),false);
    }
    @Test
    public void testInterceptCircle1(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0)};
        Circunferencia circle = new Circunferencia(pontos,2.0);
        SegmentoReta segmento = new SegmentoReta(new Ponto(1.0,1.0),new Ponto(2.5,4.0));
        assertEquals(circle.isIntercepted(segmento),true);
    }
    @Test
    public void testInterceptCircle2(){
        Ponto[] pontos = new Ponto[]{new Ponto(1.0,2.0)};
        Circunferencia circle = new Circunferencia(pontos,2.0);
        SegmentoReta segmento = new SegmentoReta(new Ponto(5.0,1.0),new Ponto(5.5,4.0));
        assertEquals(circle.isIntercepted(segmento),false);
    }


}

