import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShapeTests {
    @Test
    public void testInterceptTriangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(1,3),new Point(2,2)};
        Triangle triangle = new Triangle(points);
        LineSegment segmento = new LineSegment(new Point(1,4),new Point(1,1));
        assertTrue(triangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptTriangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(1,3),new Point(2,2)};
        Triangle triangle = new Triangle(points);
        LineSegment segmento = new LineSegment(new Point(3,1),new Point(3,4));
        assertFalse(triangle.isIntercepted(segmento));
    }

    @Test
    public void testInterceptRectangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,3),new Point(1,3)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segmento = new LineSegment(new Point(1,1),new Point(3,4));
        assertTrue(rectangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptRectangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,3),new Point(1,3)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segmento = new LineSegment(new Point(5,1),new Point(5,4));
        assertFalse(rectangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptCircle1(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        LineSegment segmento = new LineSegment(new Point(1,1),new Point(2,4));
        assertTrue(circle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptCircle2(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        LineSegment segmento = new LineSegment(new Point(5,1),new Point(5,4));
        assertFalse(circle.isIntercepted(segmento));
    }


}

