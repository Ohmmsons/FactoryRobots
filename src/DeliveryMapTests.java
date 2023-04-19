import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeliveryMapTests {
    @Test
    void  testisDeliveryPointValid1() {
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60),new Point(65,70),new Point(70,60)};
        Triangle triangle = new Triangle(points);
        obstacles.add(triangle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertTrue(deliveryMap.isDeliveryPointValid(new Point(70,50)));
    }
    @Test
    void  testisDeliveryPointValid2() {
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60),new Point(65,70),new Point(70,60)};
        Triangle triangle = new Triangle(points);
        obstacles.add(triangle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(65,65 )));
    }
    @Test
    void  testisDeliveryPointValid3() {
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60),new Point(80,60),new Point(80,80),new Point(60,80)};
        Rectangle rectangle = new Rectangle(points);
        obstacles.add(rectangle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(70,70 )));
    }
    @Test
    void  testisDeliveryPointValid4(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60),new Point(80,60),new Point(80,80),new Point(60,80)};
        Rectangle rectangle = new Rectangle(points);
        obstacles.add(rectangle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertTrue(deliveryMap.isDeliveryPointValid(new Point(90,70 )));
    }

    @Test
    void  testisDeliveryPointValid5(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertTrue(deliveryMap.isDeliveryPointValid(new Point(90,70 )));
    }

    @Test
    void  testisDeliveryPointValid6(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(60,63 )));
    }
    @Test
    void  testisDeliveryPointValid7(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(40,40 )));
    }
    @Test
    void  testisDeliveryPointValid8(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(960,10 )));
    }
    @Test
    void  testisDeliveryPointValid9(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(960,960 )));
    }
    @Test
    void  testisDeliveryPointValid10(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        assertFalse(deliveryMap.isDeliveryPointValid(new Point(40,980 )));
    }


}
