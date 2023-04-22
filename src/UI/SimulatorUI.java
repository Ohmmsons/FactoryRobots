package UI;

import Simulator.DeliveryMap;
import Simulator.Point;
import Simulator.Robot;
import java.util.List;

/**
 * An interface for a Simulator.Simulator UI which defines methods for communicating with the user
 * during the simulation.
 *  @author Jude Adam
 *  @version 1.0.0 20/04/2023
 */
public interface SimulatorUI {
    /**
     * Asks the user for the number of obstacles to add to the map.
     *
     * @return the number of obstacles the user wants to add
     */
    int askForNumberOfObstacles();

    /**
     * Asks the user for a point to add to the map.
     *
     * @return a Simulator.Point object representing the location of the point to add
     */
    Point askForPoint();

    /**
     * Checks if the UI is currently asking the user for a new point.
     *
     * @return true if the UI is asking for a new point, false otherwise
     */
    boolean isAskingForNewPoint();

    /**
     * Displays the current status of the robots to the user.
     *
     * @param step   the current step of the simulation
     * @param robots a List of Simulator.Simulator.Robot objects representing the robots in the simulation
     */
    void displayRobotStatus(int step, List<Robot> robots);

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    void displayErrorMessage(String message);

    /**
     * Simulator sends map to UI so it can display it.
     *
     * @param map The map of the simulation
     */
    void sendMapInformation(DeliveryMap map);
}