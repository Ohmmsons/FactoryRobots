package tests;

import simulator.*;
import ui.SimulatorCLI;
import ui.SimulatorUI;
import ui.TestUI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
      assertTrue(simulator.validInputCheck(deliveryMap,new Request(new Point(500,500),new Point(580,570))));
  }
    @Test
    public void testValidInputCheck2(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        Simulator simulator = new Simulator(new SimulatorCLI());
        assertFalse(simulator.validInputCheck(deliveryMap,new Request(new Point(60,64),new Point(580,570))));
    }
    @Test
    public void testValidInputCheck3(){
        ArrayList<Shape> obstacles = new ArrayList<>();
        Point[] points = new Point[]{new Point(60,60)};
        Circle circle = new Circle(points,5);
        obstacles.add(circle);
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        Simulator simulator = new Simulator(new SimulatorCLI());
        assertThrows(IllegalArgumentException.class,()-> simulator.validInputCheck(deliveryMap,new Request(new Point(-60,64),new Point(580,570))));
    }

    @Test
    public void testSimulation50Obstacles() {
        //RUN SIMULATION AT FULL SPEED FOR 5 SECS AND CHECK IF IT ENCOUNTERS ANY ERRORS DURING EXECUTION
        try {
            Simulator simulator = new Simulator(new TestUI(50));

            // Create an ExecutorService to run the simulation
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(() -> {
                try {
                    simulator.startSimulation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Stop the simulation
            simulator.stopSimulation();

            // Check for exceptions in the simulation thread
            future.get();

            // Shutdown the executor service
            executorService.shutdown();
        } catch (ExecutionException e) {
            Assertions.fail("An exception was thrown during the test: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Assertions.fail("The test was interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testSimulation150Obstacles() {
        //RUN SIMULATION AT FULL SPEED FOR 5 SECS AND CHECK IF IT ENCOUNTERS ANY ERRORS DURING EXECUTION
        try {
            Simulator simulator = new Simulator(new TestUI(150));

            // Create an ExecutorService to run the simulation
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(() -> {
                try {
                    simulator.startSimulation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Stop the simulation
            simulator.stopSimulation();

            // Check for exceptions in the simulation thread
            future.get();

            // Shutdown the executor service
            executorService.shutdown();
        } catch (ExecutionException e) {
            Assertions.fail("An exception was thrown during the test: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Assertions.fail("The test was interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testSimulation500Obstacles() {
        //RUN SIMULATION AT FULL SPEED FOR 5 SECS AND CHECK IF IT ENCOUNTERS ANY ERRORS DURING EXECUTION
        try {
            Simulator simulator = new Simulator(new TestUI(500));

            // Create an ExecutorService to run the simulation
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(() -> {
                try {
                    simulator.startSimulation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Stop the simulation
            simulator.stopSimulation();

            // Check for exceptions in the simulation thread
            future.get();

            // Shutdown the executor service
            executorService.shutdown();
        } catch (ExecutionException e) {
            Assertions.fail("An exception was thrown during the test: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Assertions.fail("The test was interrupted: " + e.getMessage());
        }
    }

}
