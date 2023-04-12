//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TrajectoryTests {
//    @Test
//    public void testNCollisions(){
//        Point[] points = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(3.0,3.0)};
//        ArrayList<Point> pontosAL = new ArrayList<>();
//        Collections.addAll(pontosAL, points);
//        Trajectory trajectory = new Trajectory(pontosAL);
//        ArrayList<Shape> figuras = new ArrayList<>();
//        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
//        assertEquals(trajectory.nCollisions(figuras),1);
//    }
//    @Test
//    public void testFitness(){
//        Point[] points = new Point[]{new Point(1.0,1.0),new Point(2.0,2.0),new Point(3.0,3.0)};
//        ArrayList<Point> pontosAL = new ArrayList<>();
//        Collections.addAll(pontosAL, points);
//        Trajectory trajectory = new Trajectory(pontosAL);
//        ArrayList<Shape> figuras = new ArrayList<>();
//        figuras.add(new Circumference(new Point[]{new Point(1.0,2.0)},1));
//        assertEquals(trajectory.getFitness(figuras),0.1802870870702775);
//    }
//}
