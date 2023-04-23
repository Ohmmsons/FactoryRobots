package Simulator;
import UI.SimulatorUI;
import java.util.*;
/**
 * The Simulator class is responsible for controlling the simulation.
 * @author Jude Adam
 * @version 1.0 20/04/2023
 * ui!= null
 */
public class Simulator {

    private final SimulatorUI ui;
    /**
     * Creates a new Simulator object.
     * @param ui The user interface for the simulator.
     */
    public Simulator(SimulatorUI ui) {
        if(ui == null) throw new IllegalArgumentException("UI must exist");
        this.ui = ui;
    }
    /**
     * Checks if the input delivery point is valid.
     * @param deliveryMap The map of the delivery area.
     * @param request The delivery point to check.
     * @return True if the delivery point is valid, false otherwise.
     */
    public boolean validInputCheck(DeliveryMap deliveryMap, Point request) {
        if (deliveryMap.isDeliveryPointValid(request)) return true;
        else ui.displayErrorMessage("Request invalid, please input a new one");
        return false;
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() throws InterruptedException{
        Generator generator = new Generator();
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

        //Inform the UI about the Map
        ui.sendMapInformation(deliveryMap);

        // Create and initialize robots
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
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
            if (ui.isAskingForNewPoint()) {
                Point request;
                do {
                    request = ui.askForPoint();
                }
                while (!validInputCheck(deliveryMap, request));
                ui.addRequest(request);
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