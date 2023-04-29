package simulator;
import ui.SimulatorUI;
import java.util.*;
/**
 * The Simulator class is responsible for controlling the simulation.
 * @author Jude Adam
 * @version 1.0 20/04/2023
 * @inv ui != null
 */
public class Simulator {

    private boolean isRunning;
    private final Random rng;
    private final SimulatorUI ui;
    /**
     * Creates a new Simulator object.
     * @param ui The user interface for the simulator.
     * @pre ui != null
     * @post this.ui == ui
     */
    public Simulator(SimulatorUI ui) {
        if(ui == null) throw new IllegalArgumentException("UI must exist");
        this.ui = ui;
        this.rng = new Random();
    }

    public Simulator(SimulatorUI ui,Random rng) {
        if(ui == null) throw new IllegalArgumentException("UI must exist");
        this.ui = ui;
        this.rng = rng;
    }

    /**
     * Checks if the input delivery request is valid.
     * @param deliveryMap The map of the delivery area.
     * @param request The delivery point to check.
     * @return True if the delivery point is valid, false otherwise.
     * @pre deliveryMap != null && request != null
     */
    public boolean validInputCheck(DeliveryMap deliveryMap, Request request) {
        if (deliveryMap.isDeliveryRequestValid(request)) return true;
        else ui.displayErrorMessage("Request invalid, it is inside an obstacle, please input a new one");
        return false;
    }

    /**
     * Initializes the simulation's robots, one in each corner, all fully charged and in standby.
     *
     * @param generator   The generator used.
     * @param deliveryMap The delivery map.
     * @return Set of 4 robots, one in each corner, all fully charged and in standby.
     * @pre generator != null && deliveryMap != null
     */
    private LinkedHashSet<Robot> initializeRobots(PointGenerator generator, DeliveryMap deliveryMap){
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(4);
        Point chargingPoint0 = new Point(15, 15);
        Point chargingPoint1 = new Point(15, 975);
        Point chargingPoint2 = new Point(975, 975);
        Point chargingPoint3 = new Point(975, 15);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator,rng);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator,rng);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator,rng);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator,rng);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        return robots;
    }

    /**
     * Uses a ShapeGenerator to generate random obstacles using random numbers provided by the Generator variable.
     * @param generator The generator used.
     * @return List of random obstacles.
     * @pre generator != null
     */
    private ArrayList<Shape> generateRandomObstacles(ShapeGenerator generator){
        // Generate random obstacles
        int nObstacles = ui.askForNumberOfObstacles();
        ArrayList<Shape> obstacles = new ArrayList<>();
        for (int i = 0; i < nObstacles; i++) {
            int option = rng.nextInt(3);
            switch (option) {
                case 0 -> obstacles.add(generator.generateShape("Circle"));
                case 1 -> obstacles.add(generator.generateShape("Rectangle"));
                case 2 -> obstacles.add(generator.generateShape("Triangle"));
            }
        }
        return obstacles;
    }

    /**
     * Starts the simulation by initializing a generator, creating a delivery map with obstacles,
     * and initializing robots. The method then enters an infinite loop to run the simulation.
     * During each iteration of the loop, the method checks if the UI is asking for a new point.
     * If it is, the method asks the UI for a point and adds it as a request to both the UI and
     * the RobotManager. The RobotManager and all robots are then updated. The method also displays
     * the robot status on the UI and waits for speed/100 * 5 milliseconds before starting the next iteration
     * of the loop.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     * @pre ui != null
     */
    @SuppressWarnings("BusyWait")
    public void startSimulation() throws InterruptedException{

        //Initialize Request Queue
        RequestQueue requestQueue = new RequestQueue();

        //Initialize generators
        ShapeGenerator shapeGenerator = new ShapeGenerator(rng);
        PointGenerator pointGenerator = new PointGenerator(rng);

        // Create delivery map with obstacles
        DeliveryMap deliveryMap = new DeliveryMap(generateRandomObstacles(shapeGenerator));

        //Ask for speed
        double speed = ui.askForSpeed();

        //Inform the UI about the Map
        ui.sendMapInformation(deliveryMap);

        // Create and initialize robots
        LinkedHashSet<Robot> robots = initializeRobots(pointGenerator,deliveryMap);

        // Create robot manager and subscribe robots to it
        RobotManager robotManager = new RobotManager(robots,requestQueue);

        int step = 0;
        isRunning = true;
        // Run simulation
        while(isRunning) {
            if (ui.isAskingForNewPoint()) {
                Request request;
                do {
                    request = ui.askForRequest();
                }
                while (!validInputCheck(deliveryMap, request));
                robotManager.addRequest(request);
                ui.addRequest(request);
            }

            // Update robot manager and robots
            robotManager.update();
            for (Robot robot : robots)
                robot.update();

            // Display robot status on UI
            ui.displayRobotStatus(step, robots);

            // Wait for 5 milliseconds
            Thread.sleep((long) (500/speed));
            step++;
        }
    }
    public void stopSimulation() {
        isRunning = false;
    }
}