package ui;

import simulator.DeliveryMap;
import simulator.Request;
import simulator.Robot;

import java.util.LinkedHashSet;

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
     * @post return >= 0
     */
    int askForNumberOfObstacles();

    /**
     * Asks the user for a point to add to the map.
     *
     * @return a Simulator.Point object representing the location of the point to add
     * @post return != null
     */
    Request askForRequest();

    /**
     * Asks the user for the speed of the simulation.
     *
     * @return the speed of the simulation
     * @post return > 0
     */
    double askForSpeed();

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
     * @param robots a List of Robot objects representing the robots in the simulation
     * @pre step >= 0 && robots != null
     */
    void displayRobotStatus(int step, LinkedHashSet<Robot> robots);

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     * @pre message != null
     */
    void displayErrorMessage(String message);

    /**
     * Simulator sends map to UI so it can display it.
     *
     * @param map The map of the simulation
     * @pre map != null
     */
    void sendMapInformation(DeliveryMap map);

    /**
     * Adds a request to the UI for displaying.
     *
     * @param request The request to be added
     * @pre request != null
     */
    void addRequest(Request request);
}
