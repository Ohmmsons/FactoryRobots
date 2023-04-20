import java.util.List;

/**
 * A graphical user interface implementation of the SimulatorUI interface.
 *  @author Jude Adam
 *  @version 1.0.0 20/04/2023
 */
public class SimulatorGUI implements SimulatorUI {

    /**
     * Returns the number of obstacles on the map, obtained through the GUI.
     * @return the number of obstacles on the map
     */
    @Override
    public int askForNumberOfObstacles() {
        return 0;
    }

    /**
     * Asks the user to input a Point (x, y) through the GUI, and returns it.
     *
     * @return the Point (x, y) input by the user
     */
    @Override
    public Point askForPoint() {
        return null;
    }

    /**
     * Returns true if the GUI is asking for a new Point, false otherwise.
     * @return true if the GUI is asking for a new Point, false otherwise
     */
    @Override
    public boolean isAskingForNewPoint() {
        return false;
    }

    /**
     * Displays the status of the robots on the GUI.
     * @param step the current step of the simulation
     * @param robots the list of robots to display
     */
    @Override
    public void displayRobotStatus(int step, List<Robot> robots) {

    }

    /**
     * Displays an error message on the GUI.
     *
     * @param message the error message to display
     */
    @Override
    public void displayErrorMessage(String message) {

    }
}
