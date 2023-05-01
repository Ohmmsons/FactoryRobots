package ui;

import simulator.*;

import java.io.IOException;
import java.util.*;

/**
 * The `UI.SimulatorCLI` class is responsible for handling the command-line interface of the robot delivery simulator.
 * It implements the `UI.SimulatorUI` interface to provide an interface to the user to interact with the simulator.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 */
public class SimulatorCLI implements SimulatorUI {
    private final Scanner sc;
    private final ArrayList<Request> requests;
    private List<Shape> shapes;

    /**
     * Constructor that initializes the `Scanner` and `Console` objects.
     *
     * @post sc != null &amp;&amp; requests != null
     */
    public SimulatorCLI() {
        sc = new Scanner(System.in);
        this.requests = new ArrayList<>();
    }

    /**
     * Asks the user for the number of obstacles on the map.
     *
     * @return The number of obstacles on the map.
     * @post return >= 0
     */
    @Override
    public int askForNumberOfObstacles() {
        System.out.println("Choose how many obstacles are on the map");
        int n;
        n = sc.nextInt();
        return n;
    }


    /**
     * Asks the user for a request.
     *
     * @return The point input by the user.
     * @post return != null
     */
    @Override
    public Request askForRequest() {
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;
        Point p1;
        Point p2;
        Request req = null;
        while (x1 == -1 || y1 == -1 || x2 == -1 || y2 == -1 || req == null) {
            try {
                System.out.println("Input your request Coordinates separated by a space: ");
                System.out.print("Start: ");
                x1 = sc.nextInt();
                y1 = sc.nextInt();
                System.out.print("End: ");
                x2 = sc.nextInt();
                y2 = sc.nextInt();
                p1 = new Point(x1,y1);
                p2 = new Point(x2,y2);
                req = new Request(p1,p2);
            } catch (IllegalArgumentException | InputMismatchException ex){
                System.out.println("Invalid input. Please enter a valid request.");
                sc.nextLine();
            }
        }
        return req;
    }


    /**
     * Asks the user for the speed of the simulation.
     *
     * @return The speed of the simulation.
     * @post return > 0 &amp;&amp; return &le; 100
     */
    @Override
    public double askForSpeed() {
        double n;
        do {
            System.out.println("From 1 to 100 how fast fast do you want the simulation to be (Steps per second)");
            n = sc.nextDouble();
        } while (n > 100 || n < 0);
        System.out.println("Speed set to " + n + ". Press enter to enter requests\n Simulation Starting...");
        return n;
    }

    /**
     * Checks if the user is asking for a new point.
     *
     * @return True if the user is asking for a new point, false otherwise.
     */
    @Override
    public boolean isAskingForNewPoint() {
        try {
            int available = System.in.available();
            if (available > 0) {
                sc.nextLine(); // Consume the input
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Displays the status of the robots at the current step.
     *
     * @param step   The current step.
     * @param robots A list of robots.
     * @pre step >= 0 &amp;&amp; robots != null
     */
    @Override
    public void displayRobotStatus(int step, LinkedHashSet<Robot> robots) {
        StringBuilder info = new StringBuilder("Step " + step);
        for (Robot robot : robots) {
            info.append(robot);
            requests.removeIf(request -> robot.getCurrentPosition().equals(request.end()));
        }
        info.append("Requests: ");
        info.append(requests);
        info.append("Obstacles: ").append(shapes.size());
        info.append(" PRESS ENTER TO INPUT NEW REQUEST");
        System.out.println(info);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     * @pre message != null
     */
    @Override
    public void displayErrorMessage(String message) {
        System.out.println(message);
    }

    /**
     * Sends map information to the UI so it can display it.
     *
     * @param map The map of the simulation
     * @pre map != null
     * @post shapes != null
     */
    @Override
    public void sendMapInformation(DeliveryMap map) {
        shapes = map.obstacles();
    }

    /**
     * Adds a request to the UI.
     *
     * @param request The request to add
     * @pre request != null
     * @post requests.contains(request)
     */
    @Override
    public void addRequest(Request request) {
        this.requests.add(request);
    }

    @Override
    public int askForNRobots() {
        System.out.println("Choose how many robots are on the map, between 1 and 99");
        int n = -1;
        boolean validInput;
        do {
            try {
                n = sc.nextInt();
                if (n <= 0) {
                    System.out.println("The number of robots must be greater than 0. Please try again.");
                    validInput = false;
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // Clear the invalid input
                validInput = false;
            }
        } while (!validInput);
        return n;
    }
}
