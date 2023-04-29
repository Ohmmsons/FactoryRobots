package UI;

import Simulator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * The `UI.SimulatorCLI` class is responsible for handling the command-line interface of the robot delivery simulator.
 * It implements the `UI.SimulatorUI` interface to provide an interface to the user to interact with the simulator.
 *  @author Jude Adam
 *  @version 1.0.0 20/04/2023
 */
public class SimulatorCLI implements SimulatorUI {
    private Scanner sc;
    private ArrayList<Request> requests;
    private ArrayList<Shape> shapes;

    /**
     * Constructor that initializes the `Scanner` and `Console` objects.
     * @post sc != null && requests != null
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


    private boolean pointOutOfBounds(int x, int y) {
        if ((x > 950 && y > 950) || (x < 50 && y < 50) || (x > 950 && y < 50) || (x < 50 && y > 950)) {
            System.out.println(x + "," + y + " Is out of bounds please input a point inside the 1000x1000 area and outside the charging stations");
            return true;
        }
        return false;
    }

    /**
     * Asks the user for a point.
     *
     * @return The point input by the user.
     * @post return != null
     */
    @Override
    public Request askForRequest() {
        int x1;
        int y1;
        int x2;
        int y2;
        do {
            System.out.println("Input your request Coordinates separated by a space: ");
            System.out.println("Start: ");
            x1 = sc.nextInt();
            y1 = sc.nextInt();
            System.out.println("End: ");
            x2 = sc.nextInt();
            y2 = sc.nextInt();
        }while(pointOutOfBounds(x1,y1) || pointOutOfBounds(x2,y2));
        return new Request(new Point(x1,y1),new Point(x2,y2));
    }

    /**
     * Asks the user for the speed of the simulation.
     *
     * @return The speed of the simulation.
     * @post return > 0 && return <= 100
     */
    @Override
    public double askForSpeed() {
        double n;
        do {
            System.out.println("From 1 to 100 how fast fast do you want the simulation to be");
            n = sc.nextDouble();
        }while (n>100 || n<0);
        System.out.println("Speed set to "+n+". Press enter to enter requests\n Simulation Starting...");
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
     * @param step The current step.
     * @param robots A list of robots.
     * @pre step >= 0 && robots != null
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
}
