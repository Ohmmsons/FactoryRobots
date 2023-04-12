import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShapeTests {
    @Test
    public void testInterceptTriangle1(){
        Point[] points = new Point[]{new Point(1.0,2.0),new Point(1.5,3.0),new Point(2.0,2.0)};
        Triangle triangle = new Triangle(points);
        SegmentoReta segmento = new SegmentoReta(new Point(1.5,1.0),new Point(1.5,4.0));
        assertTrue(triangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptTriangle2(){
        Point[] points = new Point[]{new Point(1.0,2.0),new Point(1.5,3.0),new Point(2.0,2.0)};
        Triangle triangle = new Triangle(points);
        SegmentoReta segmento = new SegmentoReta(new Point(3.5,1.0),new Point(3.5,4.0));
        assertFalse(triangle.isIntercepted(segmento));
    }

    @Test
    public void testInterceptRectangle1(){
        Point[] points = new Point[]{new Point(1.0,2.0),new Point(3.0,2.0),new Point(3.0,3.0),new Point(1.0,3.0)};
        Rectangle rectangle = new Rectangle(points);
        SegmentoReta segmento = new SegmentoReta(new Point(1.5,1.0),new Point(1.5,4.0));
        assertTrue(rectangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptRectangle2(){
        Point[] points = new Point[]{new Point(1.0,2.0),new Point(3.0,2.0),new Point(3.0,3.0),new Point(1.0,3.0)};
        Rectangle rectangle = new Rectangle(points);
        SegmentoReta segmento = new SegmentoReta(new Point(5.5,1.0),new Point(5.5,4.0));
        assertFalse(rectangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptCircle1(){
        Point[] points = new Point[]{new Point(1.0,2.0)};
        Circumference circle = new Circumference(points,2.0);
        SegmentoReta segmento = new SegmentoReta(new Point(1.0,1.0),new Point(2.5,4.0));
        assertTrue(circle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptCircle2(){
        Point[] points = new Point[]{new Point(1.0,2.0)};
        Circumference circle = new Circumference(points,2.0);
        SegmentoReta segmento = new SegmentoReta(new Point(5.0,1.0),new Point(5.5,4.0));
        assertFalse(circle.isIntercepted(segmento));
    }


}

