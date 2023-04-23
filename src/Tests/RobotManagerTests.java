package Tests;

import Simulator.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * @author Jude Adam a71254
 */
public class RobotManagerTests {
    @Test
    public void testConstructor1(){
        assertThrows(IllegalArgumentException.class,()-> new RobotManager(null));
    }
    @Test
    public void testConstructor2(){
        Generator generator = new Generator(0);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RobotManager manager = new RobotManager(robots);
        assertTrue(manager.getRequests().isEmpty());
        assertTrue(manager.getSubscribers().size()==4);
    }

    @Test
    public void testRobotUnsubscribesWhenDelivering1(){
        Generator generator = new Generator(0);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RobotManager manager = new RobotManager(robots);
        for(Robot robot:robots)
            robot.subscribeToManager(manager);
        manager.addRequest(new Point(900,900));
        manager.update();
        for(Robot robot:robots)
            robot.update();
        assertTrue(manager.getSubscribers().size()==3);
    }
    @Test
    public void testRobotUnsubscribesWhenDelivering2(){
        Generator generator = new Generator(0);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RobotManager manager = new RobotManager(robots);
        for(Robot robot:robots)
            robot.subscribeToManager(manager);
        manager.addRequest(new Point(900,900));
        manager.update();
        for(Robot robot:robots)
            robot.update();
        manager.addRequest(new Point(200,900));
        manager.update();
        for(Robot robot:robots)
            robot.update();
        assertTrue(manager.getSubscribers().size()==2);
    }
}
