package UI;

import Simulator.DeliveryMap;
import Simulator.Point;
import Simulator.Request;
import Simulator.Robot;

import java.io.IOException;
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

    /**
     * Constructor that initializes the `Scanner` and `Console` objects.
     */
    public SimulatorCLI() {
        sc = new Scanner(System.in);
    }

    /**
     * Asks the user for the number of obstacles on the map.
     *
     * @return The number of obstacles on the map.
     */
    @Override
    public int askForNumberOfObstacles() {
        System.out.println("Choose how many obstacles are on the map");
        int n;
        n = sc.nextInt();
        return n;
    }

    /**
     * Asks the user for a point.
     *
     * @return The point input by the user.
     */
    @Override
    public Request askForRequest() {
        System.out.println("Input your request Coordinates separated by a space: ");
        System.out.println("Start: ");
        int x1;
        int y1;
        x1 = sc.nextInt();
        y1 = sc.nextInt();
        System.out.println("End: ");
        int x2;
        int y2;
        x2 = sc.nextInt();
        y2 = sc.nextInt();
        return new Request(new Point(x1,y1),new Point(x2,y2));
    }

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
     */
    @Override
    public void displayRobotStatus(int step, LinkedHashSet<Robot> robots) {
        StringBuilder info = new StringBuilder("Step " + step);
        for (Robot robot : robots)
            info.append(robot);
        System.out.println(info);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    @Override
    public void displayErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void sendMapInformation(DeliveryMap map) {

    }

    @Override
    public void addRequest(Request request) {

    }
}
