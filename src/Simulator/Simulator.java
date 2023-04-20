package Simulator;
import UI.SimulatorUI;
import java.util.*;
/**
 * The Simulator.Simulator class is responsible for controlling the simulation.
 * @author Jude Adam
 * @version 1.0 20/04/2023
 * @implSpec ui!= null
 */
public class Simulator {

    private final SimulatorUI ui;
    /**
     * Creates a new Simulator.Simulator object.
     * @param ui The user interface for the simulator.
     */
    public Simulator(SimulatorUI ui) {
        this.ui = ui;
    }
    /**
     * Checks if the input delivery point is valid.
     * @param deliveryMap The map of the delivery area.
     * @param request The delivery point to check.
     * @return True if the delivery point is valid, false otherwise.
     */
    private boolean validInputCheck(DeliveryMap deliveryMap, Point request) {
        if (deliveryMap.isDeliveryPointValid(request)) return true;
        else System.out.println("Request invalid, please input a new one");
        return false;
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() throws InterruptedException{
        Random generator = new Random();
        // Generate random obstacles
        int nObstacles = ui.askForNumberOfObstacles();
        ArrayList<Shape> obstacles = new ArrayList<>();
        for (int i = 0; i < nObstacles; i++) {
            int option = generator.nextInt(3);
            switch (option) {
                case 0 -> obstacles.add(new Circle(generator));
                case 1 -> obstacles.add(new Rectangle(generator));
                case 2 -> obstacles.add(new Triangle(generator));
            }
        }

        // Create delivery map with obstacles
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);

        // Create and initialize robots
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(0, 0);
        Point chargingPoint1 = new Point(0, 999);
        Point chargingPoint2 = new Point(999, 999);
        Point chargingPoint3 = new Point(999, 0);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);

        // Create robot manager and subscribe robots to it
        RobotManager robotManager = new RobotManager(robots);
        for (Robot robot : robots)
            robot.subscribeToManager(robotManager);

        // Run simulation
        for (int i = 0; true; i++) {
            // Ask for request every 1000 iterations
            if (i%1000==0) {
                Point request;
                do {
                    request = ui.askForPoint();
                }
                while (!validInputCheck(deliveryMap, request));
                robotManager.addRequest(request);
            }

            // Update robot manager and robots
            robotManager.update();
            for (Robot robot : robots)
                robot.update();

            // Display robot status on UI
            ui.displayRobotStatus(i, robots);

            // Wait for 5 milliseconds
            Thread.sleep(5);
        }
    }

}