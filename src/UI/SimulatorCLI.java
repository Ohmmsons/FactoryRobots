package UI;

import Simulator.DeliveryMap;
import Simulator.Point;
import Simulator.Robot;
import java.util.List;
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
    public Point askForPoint() {
        System.out.print("Input your request Coordinates separated by a space: ");
        int x;
        int y;
        x = sc.nextInt();
        y = sc.nextInt();
        return new Point(x, y);
    }

    /**
     * Checks if the user is asking for a new point.
     *
     * @return True if the user is asking for a new point, false otherwise.
     */
    @Override
    public boolean isAskingForNewPoint() {
        return sc.hasNext();
    }

    /**
     * Displays the status of the robots at the current step.
     *
     * @param step The current step.
     * @param robots A list of robots.
     */
    @Override
    public void displayRobotStatus(int step, List<Robot> robots) {
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
    public void addRequest(Point request) {

    }
}
