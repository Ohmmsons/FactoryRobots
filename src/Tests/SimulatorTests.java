package Tests;

import Simulator.*;
import UI.SimulatorCLI;
import UI.SimulatorGUI;
import UI.SimulatorUI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * @author Jude Adam a71254
 */
public class SimulatorTests {

    @Test
    public void testConstructor1(){
        assertThrows(IllegalArgumentException.class,()-> new Simulator(null));
    }
    @Test
    public void testConstructor2(){
        SimulatorUI ui = new SimulatorCLI();
        Simulator sim = new Simulator(ui);
        assertNotNull(sim);
    }
  @Test
    public void testValidInputCheck1(){
      ArrayList<Shape> obstacles = new ArrayList<>();
      Point[] points = new Point[]{new Point(60,60)};
      Circle circle = new Circle(points,5);
      obstacles.add(circle);
      DeliveryMap deliveryMap = new DeliveryMap(obstacles);
      Simulator simulator = new Simulator(new SimulatorCLI());
      assertTrue(simulator.validInputCheck(deliveryMap,new Point(500,500)));
  }
    @Test
    public void testValidInputCheck2(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        Simulator simulator = new Simulator(new SimulatorCLI());
        assertFalse(simulator.validInputCheck(deliveryMap,new Point(60,64)));
    }
    @Test
    public void testValidInputCheck3(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        Simulator simulator = new Simulator(new SimulatorCLI());
        assertThrows(IllegalArgumentException.class,()-> simulator.validInputCheck(deliveryMap,new Point(-60,64)));
    }
}
