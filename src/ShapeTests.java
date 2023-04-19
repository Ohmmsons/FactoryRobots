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

    @Test
    public void testSurroundsCircle1(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        Point p = new Point(2,3);
        assertTrue(circle.surrounds(p));
    }
    @Test
    public void testSurroundsCircle2(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        Point p = new Point(4,3);
        assertFalse(circle.surrounds(p));
    }

    @Test
    public void testSurroundsTriangle1(){
        Point[] points = new Point[]{new Point(1,0),new Point(1,6),new Point(5,2)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(2,3);
        assertTrue(triangle.surrounds(p));
    }

    @Test
    public void testSurroundsTriangle2(){
        Point[] points = new Point[]{new Point(1,0),new Point(1,6),new Point(5,2)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(0,3);
        assertFalse(triangle.surrounds(p));
    }

    @Test
    public void testSurroundsRectangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,4),new Point(1,4)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(2,3);
        assertTrue(rectangle.surrounds(p));
    }

    @Test
    public void testSurroundsRectangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,4),new Point(1,4)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(0,3);
        assertFalse(rectangle.surrounds(p));
    }
    @Test
    public void testSurroundsRectangle3(){
        Point[] points = new Point[]{new Point(60,60), new Point(80,60), new Point(80,80), new Point(60,80)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(70,70);
        assertTrue(rectangle.surrounds(p));
    }

}

