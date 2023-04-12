import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TrajectoryTests {
    @Test
    public void testNCollisions(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(3.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        assertEquals(trajectory.nCollisions(),1);
    }
    @Test
    public void testFitness(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(3.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        assertEquals(trajectory.fitness(),0.1802870870702775);
    }
    @Test
    public void testCrossover1(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(3.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(5.0,5.0),new Point(6.0,6.0),new Point(7.0,7.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        Trajectory trajectory1 = new Trajectory(pontosAL1,generator,figuras);
        Trajectory[] children = trajectory.crossover(trajectory1);
        Point[] testpoints = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(7.0,7.0)};
        ArrayList<Point> testpontosAL = new ArrayList<>();
        Collections.addAll(testpontosAL,testpoints);
        Point[] testpoints1 = new Point[]{new Point(5.0,5.0),new Point(6.0,6.0),new Point(3.0,3.0)};
        ArrayList<Point> testpontosAL1 = new ArrayList<>();
        Collections.addAll(testpontosAL1, testpoints1);
        Trajectory testtrajectory = new Trajectory(testpontosAL,generator,figuras);
        Trajectory testtrajectory1 = new Trajectory(testpontosAL1,generator,figuras);
        Trajectory[] testChildren = new Trajectory[]{testtrajectory,testtrajectory1};
        System.out.println(Arrays.toString(children));
        assertArrayEquals(children,testChildren);
    }
    @Test
    public void testCrossover2(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(1.0,5.0),new Point(7.0,6.0),new Point(12.0,7.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        Trajectory trajectory1 = new Trajectory(pontosAL1,generator,figuras);
        Trajectory[] children = trajectory.crossover(trajectory1);
        Point[] testpoints = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(12.0,7.0)};
        ArrayList<Point> testpontosAL = new ArrayList<>();
        Collections.addAll(testpontosAL,testpoints);
        Point[] testpoints1 = new Point[]{new Point(1.0,5.0),new Point(7.0,6.0),new Point(9.0,3.0)};
        ArrayList<Point> testpontosAL1 = new ArrayList<>();
        Collections.addAll(testpontosAL1, testpoints1);
        Trajectory testtrajectory = new Trajectory(testpontosAL,generator,figuras);
        Trajectory testtrajectory1 = new Trajectory(testpontosAL1,generator,figuras);
        Trajectory[] testChildren = new Trajectory[]{testtrajectory,testtrajectory1};
        System.out.println(Arrays.toString(children));
        assertArrayEquals(children,testChildren);
    }
    @Test
    public void testMutate1(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(47.0,15.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.mutate(1);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    } @Test
    public void testMutate2(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.mutate(0);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    }
    @Test
    public void testAddGene1(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(47.0,15.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.addGene(1);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    }
    @Test
    public void testAddGene2(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.addGene(0);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    }
    @Test
    public void testRemoveGene1(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.removeGene(1);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    }
    @Test
    public void testRemoveGene2(){
        Random generator = new Random(0);
        Point[] points = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL = new ArrayList<>();
        Collections.addAll(pontosAL, points);
        Point[] points1 = new Point[]{new Point(2.0,1.0),new Point(6.0,2.0),new Point(9.0,3.0)};
        ArrayList<Point> pontosAL1 = new ArrayList<>();
        Collections.addAll(pontosAL1, points1);
        ArrayList<Shape> figuras = new ArrayList<>();
        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
        Trajectory trajectory = new Trajectory(pontosAL,generator,figuras);
        trajectory.removeGene(0);
        assertEquals(trajectory,new Trajectory(pontosAL1,generator,figuras));
    }

}
