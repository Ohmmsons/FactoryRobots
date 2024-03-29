package tests;

import simulator.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.Assert.*;
/**
 * @author Jude Adam a71254
 */
public class RobotManagerTests {
    @Test
    public void testConstructor1(){
        assertThrows(IllegalArgumentException.class,()-> new RobotManager(null,null));
    }
    @Test
    public void testConstructor2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60 ,60);
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator,rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator,rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator,rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator,rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RobotManager manager = new RobotManager(robots,new RequestQueue());
        assertTrue(manager.getRequests().isEmpty());
        assertEquals(4, manager.getSubscribers().size());
    }

    @Test
    public void testRobotUnsubscribesWhenDelivering1(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60, 60);
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator,rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator,rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator,rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator,rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RequestQueue queue = new  RequestQueue();
        queue.addRequest(new Request (new Point(900,900),new Point(850,850)));
        RobotManager manager = new RobotManager(robots,queue);
        for(Robot robot:robots)
            robot.subscribeToManager(manager);
        manager.update();
        for(Robot robot:robots)
            robot.update();
        assertEquals(3, manager.getSubscribers().size());
    }
    @Test
    public void testRobotUnsubscribesWhenDelivering2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60,60);
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator,rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator,rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator,rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator,rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RequestQueue queue = new  RequestQueue();
        queue.addRequest(new Request (new Point(900,900),new Point(850,850)));
        RobotManager manager = new RobotManager(robots,queue);
        for(Robot robot:robots)
            robot.subscribeToManager(manager);
        manager.update();
        for(Robot robot:robots)
            robot.update();
        queue.addRequest(new Request (new Point(100,900),new Point(150,900)));
        manager.update();
        for(Robot robot:robots)
            robot.update();
        assertEquals(2, manager.getSubscribers().size());
    }
    @Test
    public void testRobotSubscribesAfterDelivering() {
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60,60);
        Circle circle = new Circle(points, 5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator, rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator, rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator, rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator, rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RequestQueue queue = new RequestQueue();
        queue.addRequest(new Request(new Point(900, 900), new Point(800, 800)));
        RobotManager manager = new RobotManager(robots, queue);
        for (Robot robot : robots) {
            robot.subscribeToManager(manager);
        }
        manager.update();
        for (Robot robot : robots) {
            robot.update();
        }
        queue.addRequest(new Request(new Point(200, 900), new Point(150, 800)));
        manager.update();

        int i = 0;
        // Simulate delivery completed
        while(i<4){
            i = 0;
            for (Robot robot : robots) {
                robot.update();
                if(robot.getPowerState() == RobotPowerState.STANDBY)
                    i++;
            }
        }
        assertEquals(4, manager.getSubscribers().size());
    }
    @Test
    public void testAllRobotsAreSubscribed() {
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60,60);
        Circle circle = new Circle(points, 5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator, rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator, rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator, rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator, rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RequestQueue queue = new RequestQueue();
        RobotManager manager = new RobotManager(robots, queue);
        for (Robot robot : robots) {
            robot.subscribeToManager(manager);
        }
        Set<Robot> subscribedRobots = manager.getSubscribers();
        for (Robot robot : robots) {
            assertTrue(subscribedRobots.contains(robot));
        }
    }

    @Test
    public void testNoDuplicateSubscriptions() {
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point points = new Point(60,60);
        Circle circle = new Circle(points, 5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator, rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator, rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator, rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator, rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RequestQueue queue = new RequestQueue();
        RobotManager manager = new RobotManager(robots, queue);
        for (Robot robot : robots) {
            robot.subscribeToManager(manager);
        }
        for (Robot robot : robots) {
            robot.subscribeToManager(manager);
        }

        assertEquals(4, manager.getSubscribers().size());
    }

}
