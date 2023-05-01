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
     * @pre deliveryMap != null &amp;&amp; request != null
     */
    public boolean validInputCheck(DeliveryMap deliveryMap, Request request) {
        if (deliveryMap.isDeliveryRequestValid(request)) return true;
        else ui.displayErrorMessage("Request invalid, it is inside an obstacle, please input a new one");
        return false;
    }

    /**
     * Initializes the simulation's robots, all distributed along the perimeter of the map, all fully charged and in standby.
     *
     * @param generator   The generator used.
     * @param deliveryMap The delivery map.
     * @return Set of n robots, one in each corner, all fully charged and in standby.
     * @pre generator != null &amp;&amp; deliveryMap != null
     */
    private LinkedHashSet<Robot> initializeRobots(int n, PointGenerator generator, DeliveryMap deliveryMap) {
        // Create robots at evenly distributed points along the perimeter of the map
        LinkedHashSet<Robot> robots = new LinkedHashSet<>(n);
        int mapSize = 1000;
        int perimeter = (mapSize - 30) * 4;
        int gap = perimeter / n;
        int offset = 15;

        for (int i = 0; i < n; i++) {
            int perimeterPosition = i * gap;
            int x, y;
            if (perimeterPosition < (mapSize - 2 * offset)) {
                x = offset + perimeterPosition;
                y = offset;
            } else if (perimeterPosition < 2 * (mapSize - offset)) {
                x = mapSize - offset;
                y = offset + (perimeterPosition - (mapSize - 2 * offset));
            } else if (perimeterPosition < 3 * (mapSize - offset)) {
                x = mapSize - offset - (perimeterPosition - 2 * (mapSize - offset));
                y = mapSize - offset;
            } else {
                x = offset;
                y = mapSize - offset - (perimeterPosition - 3 * (mapSize - offset));
            }
            Point chargingPoint = new Point(x, y);
            Robot robot = new Robot(chargingPoint, deliveryMap, generator, rng);
            robots.add(robot);
        }

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
                case 0 -> obstacles.add(generator.generateShape(ShapeType.CIRCLE));
                case 1 -> obstacles.add(generator.generateShape(ShapeType.RECTANGLE));
                case 2 -> obstacles.add(generator.generateShape(ShapeType.TRIANGLE));
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

        int nRobots;
        do {
            nRobots = ui.askForNRobots();
        }while(nRobots>=100 || nRobots<0);

        // Create and initialize robots
        LinkedHashSet<Robot> robots = initializeRobots(nRobots,pointGenerator,deliveryMap);

        // Create robot manager and subscribe robots to it
        RobotManager robotManager = new RobotManager(robots,requestQueue);

        int step = 0;
        isRunning = true;
        // Continue running the simulation until the user stops it
        while(isRunning) {
            if (ui.isAskingForNewPoint()) {
                Request request;
                do {
                    request = ui.askForRequest();
                }
                while (!validInputCheck(deliveryMap, request));
                requestQueue.addRequest(request);
                ui.addRequest(request);
            }

            // Update robot manager and robots
            robotManager.update();
            for (Robot robot : robots)
                robot.update();

            // Display robot status on UI
            ui.displayRobotStatus(step, robots);

            // Wait for 1/speed seconds
            Thread.sleep((long) (1000/speed));
            step++;
        }
    }

    /**
     * Stops simulation
     * @post Simulation is stopped
     */
    public void stopSimulation() {
        isRunning = false;
    }
}