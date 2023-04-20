import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class RobotTests {
    @Test
    public void testUpdate_movesRobotAndReducesEnergy() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Random());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(2, 2));
        ArrayList<Robot> robots = new ArrayList<>();
        robots.add(robot);
        RobotManager rm = new RobotManager(robots);
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        double initialEnergy = robot.getEnergy();
        robot.update();
        assertEquals(initialEnergy - 0.1, robot.getEnergy(), 0.0001);
    }
    @Test
    public void testCanReachDestination1() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Random());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(1, 1));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testCanReachDestination2() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Random());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(100, 100));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testCanReachDestination3() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Random());
        Trajectory trajectory = robot.findTrajectory(new Point(0, 0), new Point(900, 900));
        assertTrue(robot.canReachDestination(trajectory));
    }

    @Test
    public void testFindTrajectory_findsTrajectoryAroundShape() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        deliveryMap.addObstacle(new Circle(new Point[]{new Point(100, 100)},10));
        Robot robot = new Robot(startingPoint, deliveryMap, new Random());
        Trajectory trajectory = robot.findTrajectory(startingPoint, new Point(300, 300));
        assertFalse(trajectory.getPoints().stream().anyMatch(x -> deliveryMap.getObstacles().get(0).surrounds(x)));
    }

    @Test
    public void testToString() {
        Point startingPoint = new Point(0, 0);
        DeliveryMap deliveryMap = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(startingPoint, deliveryMap, new Random());
        assertEquals("(000,000,100.00,-,STANDBY)", robot.toString());
    }
    @Test
    public void testToString2() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(100, 200), map, new Random());
        String expected = "(100,200,100.00,-,STANDBY)";
        assertEquals(expected, robot.toString());
    }

    @Test
    public void testSetPathUpdatesRobotStateToMoving() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Random());
        Trajectory trajectory = robot.findTrajectory(new Point(0, 0), new Point(500, 500));
        ArrayList<Robot> robots = new ArrayList<>();
        robots.add(robot);
        RobotManager rm = new RobotManager(robots);
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        assertEquals(RobotPowerState.MOVING, robot.getPowerState());
    }

    @Test
    public void testGoesBackToStandbyAfterDelivery() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Random());
        Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(), new Point(1,1));
        ArrayList<Robot> robots = new ArrayList<>();
        robots.add(robot);
        RobotManager rm = new RobotManager(robots);
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        int n = trajectory.getPoints().size();
        for(int i = 0; i<n;i++)
            robot.update();
        assertEquals(RobotPowerState.STANDBY, robot.getPowerState());
    }
    @Test
    public void testGoesToChargeWhenStandByAndEnergyBelow50() {
        DeliveryMap map = new DeliveryMap(new ArrayList<>());
        Robot robot = new Robot(new Point(0, 0), map, new Random());
        //Get out of Spawn
        Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(), new Point(1,1));
        ArrayList<Robot> robots = new ArrayList<>();
        robots.add(robot);
        RobotManager rm = new RobotManager(robots);
        robot.subscribeToManager(rm);
        robot.setPath(trajectory);
        boolean working = false;
        while(true) {
            robot.update();
            if (robot.getEnergy() <= 50.0 && robot.getPowerState() == RobotPowerState.MOVING) {
                working = true;
                break;
            }
            if(robot.getEnergy()<=0.0)
                break;
        }
        assertTrue(working);
    }


}
