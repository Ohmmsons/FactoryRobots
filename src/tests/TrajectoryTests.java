package tests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import simulator.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Jude Adam a71254
 */
public class TrajectoryTests {
    @Test
    public void testNCollisions(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(1,1),new Point(2,2),new Point(3,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        Assertions.assertEquals(trajectory.calculateCollisions(),1);
    }
    @Test
    public void testFitness(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(1,1),new Point(2,2),new Point(3,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        Assertions.assertEquals(trajectory.fitness(),0);
    }
    @Test
    public void testCrossover1(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(1,1),new Point(2,2),new Point(3,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(5,5),new Point(6,6),new Point(7,7)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        Trajectory trajectory1 = new Trajectory(pontosAL1,generator,figuras,rng);
        Trajectory[] children = trajectory.onePointCrossover(trajectory1);
        Point[] testpoints = new Point[]{new Point(1,1),new Point(2,2),new Point(7,7)};
        ArrayList<Point> testpontosAL = new ArrayList<>();
        Collections.addAll(testpontosAL,testpoints);
        Point[] testpoints1 = new Point[]{new Point(5,5),new Point(6,6),new Point(3,3)};
        ArrayList<Point> testpontosAL1 = new ArrayList<>();
        Collections.addAll(testpontosAL1, testpoints1);
        Trajectory testtrajectory = new Trajectory(testpontosAL,generator,figuras,rng);
        Trajectory testtrajectory1 = new Trajectory(testpontosAL1,generator,figuras,rng);
        Trajectory[] testChildren = new Trajectory[]{testtrajectory,testtrajectory1};
        assertArrayEquals(children,testChildren);
    }
    @Test
    public void testCrossover2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(1,5),new Point(7,6),new Point(12,7)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        Trajectory trajectory1 = new Trajectory(pontosAL1,generator,figuras,rng);
        Trajectory[] children = trajectory.onePointCrossover(trajectory1);
        Point[] testpoints = new Point[]{new Point(2,1),new Point(6,2),new Point(12,7)};
        ArrayList<Point> testpontosAL = new ArrayList<>();
        Collections.addAll(testpontosAL,testpoints);
        Point[] testpoints1 = new Point[]{new Point(1,5),new Point(7,6),new Point(9,3)};
        ArrayList<Point> testpontosAL1 = new ArrayList<>();
        Collections.addAll(testpontosAL1, testpoints1);
        Trajectory testtrajectory = new Trajectory(testpontosAL,generator,figuras,rng);
        Trajectory testtrajectory1 = new Trajectory(testpontosAL1,generator,figuras,rng);
        Trajectory[] testChildren = new Trajectory[]{testtrajectory,testtrajectory1};
        Assertions.assertArrayEquals(children,testChildren);
    }
    @Test
    public void testMutate1(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(21,3),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.mutate(1);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    } @Test
    public void testMutate2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.mutate(0);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    }
    @Test
    public void testAddPoint1(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(6,2),new Point(21,3),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.addPoint(1);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    }
    @Test
    public void testAddPoint2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.addPoint(0);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    }
    @Test
    public void testRemovePoint1(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.removePoint(1);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    }
    @Test
    public void testRemovePoint2(){
        Random rng = new Random(0);
        PointGenerator generator = new PointGenerator(rng);
        Point[] points = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2,1),new Point(6,2),new Point(9,3)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circle(new Point[]{new Point(1,2)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras,rng);
        trajectory.removePoint(0);
        Assertions.assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras,rng));
    }

}
