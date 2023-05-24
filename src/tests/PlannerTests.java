package tests;

import org.junit.jupiter.api.Test;
import simulator.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jude Adam 71254
 */
class PlannerTests {

    @Test
    void testFindTrajectory() {
        // Define test parameters
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(10, 10);
        Point end = new Point(990, 990);
        int[] lengths = {5, 10, 15, 20};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        obstacles.add(new Circle(new Point(500, 500), 50));

         Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();

        Trajectory trajectory = planner.findTrajectory();
        assertNotNull(trajectory, "The trajectory found should not be null");

        assertEquals(0, trajectory.calculateCollisions(), "The trajectory found should have no collisions");
    }
    @Test
    void testNoObstacles() {
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(10, 10);
        Point end = new Point(990, 990);
        int[] lengths = {5, 10, 15, 20};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>(); // No obstacles

        Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();


        Trajectory trajectory = planner.findTrajectory();
        assertNotNull(trajectory, "The trajectory found should not be null");
        assertEquals(0, trajectory.calculateCollisions(), "The trajectory found should have no collisions");
    }

    @Test
    void testImpossibleTrajectory() {
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(10, 10);
        Point end = new Point(990, 990);
        int[] lengths = {5, 10, 15, 20};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        obstacles.add(new Circle(new Point(500, 500), 495)); // Impossible trajectory

        Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();


        Trajectory trajectory = planner.findTrajectory();
        assertNull(trajectory, "The trajectory found should be null");
    }

    @Test
    void testStartInsideObstacle() {
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(500, 500);
        Point end = new Point(990, 990);
        int[] lengths = {5, 10, 15, 20};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        obstacles.add(new Circle(new Point(500, 500), 50)); // Start inside obstacle

        Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();


        Trajectory trajectory = planner.findTrajectory();
        assertNull(trajectory, "The trajectory found should be null");
    }

    @Test
    void testEndInsideObstacle() {
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(10, 10);
        Point end = new Point(500, 500);
        int[] lengths = {5, 10, 15, 20};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        obstacles.add(new Circle(new Point(500, 500), 50)); // End inside obstacle

        Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();


        Trajectory trajectory = planner.findTrajectory();
        assertNull(trajectory, "The trajectory found should be null");
    }

    @Test
    void testTrajectoryWithLimitedLengths() {
        Random rng = new Random(42);
        double pm = 0.1;
        double pa = 0.05;
        double pr = 0.05;
        Point start = new Point(10, 10);
        Point end = new Point(990, 990);
        int[] lengths = {5}; // Only allow trajectories with 5 points
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();

        Planner planner = new Planner.Builder().pm(pm).pa(pa).pr(pr).start(start).end(end).lengths(lengths).generator(generator).obstacles(obstacles).rng(rng).build();


        Trajectory trajectory = planner.findTrajectory();
        assertNotNull(trajectory, "The trajectory found should not be null");
        assertEquals(0, trajectory.calculateCollisions(), "The trajectory found should have no collisions");
        assertEquals(7, trajectory.getPoints().size(), "The trajectory found should have exactly 7 points");
    }

}
