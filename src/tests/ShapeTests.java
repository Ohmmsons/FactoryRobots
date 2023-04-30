package tests;
import simulator.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jude Adam a71254
 */
public class ShapeTests {
    @Test
    public void testInterceptTriangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(1,3),new Point(2,2)};
        Triangle triangle = new Triangle(points);
        LineSegment segmento = new LineSegment(new Point(1,4),new Point(1,1));
        Assertions.assertTrue(triangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptTriangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(1,3),new Point(2,2)};
        Triangle triangle = new Triangle(points);
        LineSegment segmento = new LineSegment(new Point(3,1),new Point(3,4));
        Assertions.assertFalse(triangle.isIntercepted(segmento));
    }

    @Test
    public void testInterceptTriangle3() {
        Point[] points = new Point[]{new Point(0, 0), new Point(4, 0), new Point(2, 4)};
        Triangle triangle = new Triangle(points);
        LineSegment segment = new LineSegment(new Point(2, 2), new Point(4, 4));
        Assertions.assertTrue(triangle.isIntercepted(segment));
    }

    @Test
    public void testInterceptTriangle4() {
        Point[] points = new Point[]{new Point(0, 0), new Point(4, 0), new Point(2, 4)};
        Triangle triangle = new Triangle(points);
        LineSegment segment = new LineSegment(new Point(5, 1), new Point(7, 3));
        Assertions.assertFalse(triangle.isIntercepted(segment));
    }

    @Test
    public void testInterceptRectangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,3),new Point(1,3)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segmento = new LineSegment(new Point(1,1),new Point(3,4));
        Assertions.assertTrue(rectangle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptRectangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,3),new Point(1,3)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segmento = new LineSegment(new Point(5,1),new Point(5,4));
        Assertions.assertFalse(rectangle.isIntercepted(segmento));
    }

    @Test
    public void testInterceptRectangle3() {
        Point[] points = new Point[]{new Point(0, 0), new Point(4, 0), new Point(4, 4), new Point(0, 4)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segment = new LineSegment(new Point(1, 1), new Point(3, 3));
        Assertions.assertFalse(rectangle.isIntercepted(segment));
    }


    @Test
    public void testInterceptRectangle4() {
        Point[] points = new Point[]{new Point(0, 0), new Point(4, 0), new Point(4, 4), new Point(0, 4)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segment = new LineSegment(new Point(6, 1), new Point(8, 3));
        Assertions.assertFalse(rectangle.isIntercepted(segment));
    }

    @Test
    public void testInterceptRectangle5() {
        Point[] points = new Point[]{new Point(1, 1), new Point(5, 1), new Point(5, 4), new Point(1, 4)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segment = new LineSegment(new Point(0, 2), new Point(6, 2));
        Assertions.assertTrue(rectangle.isIntercepted(segment));
    }

    @Test
    public void testInterceptRectangle6() {
        Point[] points = new Point[]{new Point(1, 1), new Point(5, 1), new Point(5, 4), new Point(1, 4)};
        Rectangle rectangle = new Rectangle(points);
        LineSegment segment = new LineSegment(new Point(0, 5), new Point(6, 5));
        Assertions.assertFalse(rectangle.isIntercepted(segment));
    }

    @Test
    public void testInterceptCircle1(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        LineSegment segmento = new LineSegment(new Point(1,1),new Point(2,4));
        Assertions.assertTrue(circle.isIntercepted(segmento));
    }
    @Test
    public void testInterceptCircle2(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        LineSegment segmento = new LineSegment(new Point(5,1),new Point(5,4));
        Assertions.assertFalse(circle.isIntercepted(segmento));
    }

    @Test
    public void testInterceptCircle3() {
        Point[] points = new Point[]{new Point(3, 3)};
        Circle circle = new Circle(points, 3);
        LineSegment segment = new LineSegment(new Point(1, 1), new Point(5, 5));
        Assertions.assertTrue(circle.isIntercepted(segment));
    }

    @Test
    public void testInterceptCircle4() {
        Point[] points = new Point[]{new Point(3, 3)};
        Circle circle = new Circle(points, 3);
        LineSegment segment = new LineSegment(new Point(7, 1), new Point(9, 3));
        Assertions.assertFalse(circle.isIntercepted(segment));
    }

    @Test
    public void testInterceptCircle5() {
        Point[] points = new Point[]{new Point(4, 4)};
        Circle circle = new Circle(points, 3);
        LineSegment segment = new LineSegment(new Point(1, 5), new Point(7, 5));
        Assertions.assertTrue(circle.isIntercepted(segment));
    }

    @Test
    public void testInterceptCircle6() {
        Point[] points = new Point[]{new Point(4, 4)};
        Circle circle = new Circle(points, 3);
        LineSegment segment = new LineSegment(new Point(1, 8), new Point(7, 8));
        Assertions.assertFalse(circle.isIntercepted(segment));
    }

    @Test
    public void testSurroundsCircle1(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        Point p = new Point(2,3);
        Assertions.assertTrue(circle.surrounds(p));
    }
    @Test
    public void testSurroundsCircle2(){
        Point[] points = new Point[]{new Point(1,2)};
        Circle circle = new Circle(points,2);
        Point p = new Point(4,3);
        Assertions.assertFalse(circle.surrounds(p));
    }

    @Test
    public void testSurroundsCircle3() {
        Point[] points = new Point[]{new Point(4, 4)};
        Circle circle = new Circle(points, 3);
        Point p = new Point(6, 4);
        Assertions.assertTrue(circle.surrounds(p));
    }

    @Test
    public void testSurroundsCircle4() {
        Point[] points = new Point[]{new Point(4, 4)};
        Circle circle = new Circle(points, 3);
        Point p = new Point(1, 1);
        Assertions.assertFalse(circle.surrounds(p));
    }


    @Test
    public void testInterceptTriangle6() {
        Point[] points = new Point[]{new Point(0, 0), new Point(4, 0), new Point(2, 4)};
        Triangle triangle = new Triangle(points);
        LineSegment segment = new LineSegment(new Point(2, 5), new Point(2, 7));
        Assertions.assertFalse(triangle.isIntercepted(segment));
    }

    @Test
    public void testSurroundsTriangle1(){
        Point[] points = new Point[]{new Point(1,0),new Point(1,6),new Point(5,2)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(2,3);
        Assertions.assertTrue(triangle.surrounds(p));
    }

    @Test
    public void testSurroundsTriangle2(){
        Point[] points = new Point[]{new Point(1,0),new Point(1,6),new Point(5,2)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(0,3);
        Assertions.assertFalse(triangle.surrounds(p));
    }

    @Test
    public void testSurroundsTriangle3() {
        Point[] points = new Point[]{new Point(1, 1), new Point(1, 5), new Point(4, 3)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(2, 2);
        Assertions.assertFalse(triangle.surrounds(p));
    }


    @Test
    public void testSurroundsTriangle4() {
        Point[] points = new Point[]{new Point(1, 1), new Point(1, 5), new Point(4, 3)};
        Triangle triangle = new Triangle(points);
        Point p = new Point(5, 5);
        Assertions.assertFalse(triangle.surrounds(p));
    }


    @Test
    public void testSurroundsRectangle1(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,4),new Point(1,4)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(2,3);
        Assertions.assertTrue(rectangle.surrounds(p));
    }

    @Test
    public void testSurroundsRectangle2(){
        Point[] points = new Point[]{new Point(1,2),new Point(3,2),new Point(3,4),new Point(1,4)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(0,3);
        Assertions.assertFalse(rectangle.surrounds(p));
    }
    @Test
    public void testSurroundsRectangle3(){
        Point[] points = new Point[]{new Point(60,60), new Point(80,60), new Point(80,80), new Point(60,80)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(70,70);
        Assertions.assertTrue(rectangle.surrounds(p));
    }

    @Test
    public void testSurroundsRectangle4() {
        Point[] points = new Point[]{new Point(0, 0), new Point(6, 0), new Point(6, 3), new Point(0, 3)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(3, 1);
        Assertions.assertTrue(rectangle.surrounds(p));
    }

    @Test
    public void testSurroundsRectangle5() {
        Point[] points = new Point[]{new Point(0, 0), new Point(6, 0), new Point(6, 3), new Point(0, 3)};
        Rectangle rectangle = new Rectangle(points);
        Point p = new Point(7, 1);
        Assertions.assertFalse(rectangle.surrounds(p));
    }




}

