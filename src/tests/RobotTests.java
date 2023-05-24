package tests;
import simulator.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Jude Adam a71254
 */
public class RobotTests {

    private DeliveryMap deliveryMap;
    private PointGenerator generator;
    private Point startingPoint;
    private Random rng;

    @Test
    void testRobotConstructor1() {
        assertThrows(IllegalArgumentException.class, () -> new Robot(null, deliveryMap, generator, rng));
        assertThrows(IllegalArgumentException.class, () -> new Robot(startingPoint, null, generator, rng));
        assertThrows(IllegalArgumentException.class, () -> new Robot(startingPoint, deliveryMap, null, rng));
    }
    @Test
    public void testConstructor2(){
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new PointGenerator(new Random()),new Random());
        assertEquals(robot.getCurrentPosition(),startingPoint);
    }



    @Test
    public void testUpdate_movesRobotAndReducesEnergy() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new PointGenerator(new Random()),new Random());
        Trajectory trajectory = robot.getTrajectory(startingPoint, new Point(2, 2));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots,new RequestQueue());
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        double initialEnergy = robot.getEnergy();
        robot.update();
        Assert.assertEquals(initialEnergy - 0.1, robot.getEnergy(), 0.0001);
    }


    @Test
    void testGetEnergy() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map,  new PointGenerator(new Random()),new Random());
        Trajectory trajectory = robot.getTrajectory(new Point(0, 0), new Point(500, 500));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots,new RequestQueue());
        robot.subscribeToManager(rm);
        assertEquals(100.0, robot.getEnergy(),0.0001);
    }

    @Test
    void testUpdateEnergyBelowZero() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map,  new PointGenerator(new Random()),new Random()){
            @Override
                public double getEnergy(){
                this.energy = -1;
                return this.energy;
             }
        };
        Trajectory trajectory = robot.getTrajectory(new Point(0, 0), new Point(500, 500));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots,new RequestQueue());
        robot.subscribeToManager(rm);
        robot.getEnergy();
        assertThrows(IllegalStateException.class, robot::update);
    }

    @Test
    public void testFindTrajectory_findsTrajectoryAroundShape() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        deliveryMap.addObstacle(new Circle(new Point(100, 100),10));
        Robot robot = new Robot(startingPoint, deliveryMap, new PointGenerator(new Random()),new Random());
        Trajectory trajectory = robot.getTrajectory(startingPoint, new Point(300, 300));
        assertEquals(0, trajectory.calculateCollisions());
    }

    @Test
    public void testToString() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap,  new PointGenerator(new Random()),new Random());
        Assert.assertEquals("(000,000,100.00,-)", robot.toString());
    }
    @Test
    public void testToString2() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(100, 200), map,  new PointGenerator(new Random()),new Random());
        String expected = "(100,200,100.00,-)";
        Assert.assertEquals(expected, robot.toString());
    }

    @Test
    public void testSetPathUpdatesRobotStateToMoving() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map,  new PointGenerator(new Random()),new Random());
        Trajectory trajectory = robot.getTrajectory(new Point(0, 0), new Point(500, 500));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots,new RequestQueue());
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        Assert.assertEquals(RobotPowerState.DELIVERING, robot.getPowerState());
    }

    @Test
    public void testGoesBackToStandbyAfterDelivery() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new PointGenerator(new Random()),new Random());
        Trajectory trajectory = robot.getTrajectory(robot.getCurrentPosition(), new Point(1,1));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots, new RequestQueue());
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        int n = trajectory.calculatePointsAlongTrajectory().size();
        for(int i = 0; i<n;i++)
            robot.update();
        Assert.assertEquals(RobotPowerState.STANDBY, robot.getPowerState());
    }
    @Test
    public void testGoesToChargeWhenStandByAndLowEneergy() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map,  new PointGenerator(new Random()),new Random());
        //Get out of Spawn
        Trajectory trajectory = robot.getTrajectory(robot.getCurrentPosition(), new Point(1,1));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots, new RequestQueue());
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        boolean working = false;
        while(true) {
            robot.update();
            if (robot.getPowerState() == RobotPowerState.RETURNING) {
                working = true;
                break;
            }
            if(robot.getEnergy()<=0.0)
                break;
        }
        assertTrue(working);
    }




}
