package Tests;
import Simulator.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;


import static org.junit.Assert.*;
/**
 * @author Jude Adam a71254
 */
public class RobotTests {

    @Test
    public void testConstructor1(){
        assertThrows(IllegalArgumentException.class,() -> new Robot(null, null, new Generator()));
    }
    @Test
    public void testConstructor2(){
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        assertEquals(robot.getCurrentPosition(),startingPoint);
    }


    @Test
    public void testUpdate_movesRobotAndReducesEnergy() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(2, 2));
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
    public void testCanReachDestination1() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(1, 1));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testCanReachDestination2() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(100, 100));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testCanReachDestination3() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Generator());
        Trajectory trajectory = robot.findTrajectory(new Point(0, 0), new Point(300, 300));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testFindTrajectory_findsTrajectoryAroundShape() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        deliveryMap.addObstacle(new Circle(new Point[]{new Point(100, 100)},10));
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(300, 300));
        assertTrue(trajectory.nCollisions()==0);
    }

    @Test
    public void testToString() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Generator());
        Assert.assertEquals("(000,000,100.00,-,STANDBY)", robot.toString());
    }
    @Test
    public void testToString2() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(100, 200), map, new Generator());
        String expected = "(100,200,100.00,-,STANDBY)";
        Assert.assertEquals(expected, robot.toString());
    }

    @Test
    public void testSetPathUpdatesRobotStateToMoving() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Generator());
        Trajectory trajectory = robot.findTrajectory(new Point(0, 0), new Point(500, 500));
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
        Robot robot = new Robot(new Point(0, 0), map, new Generator());
        Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(), new Point(1,1));
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
    public void testGoesToChargeWhenStandByAndEnergyBelow50() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Generator());
        //Get out of Spawn
        Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(), new Point(1,1));
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        robots.add(robot);
        RobotManager rm = new RobotManager(robots, new RequestQueue());
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        boolean working = false;
        while(true) {
            robot.update();
            if (robot.getEnergy() <= 50.0 && robot.getPowerState() == RobotPowerState.RETURNING) {
                working = true;
                break;
            }
            if(robot.getEnergy()<=0.0)
                break;
        }
        assertTrue(working);
    }


}
