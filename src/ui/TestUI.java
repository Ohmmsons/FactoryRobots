package ui;

import simulator.DeliveryMap;
import simulator.Point;
import simulator.Request;
import simulator.Robot;

import java.util.LinkedHashSet;
import java.util.Random;

/**
 * TestUI is a simple implementation of the SimulatorUI interface used for testing.
 * @inv step >= 0
 */
public class TestUI implements SimulatorUI {
    private final int nObstacles;
    private int step;

    /**
     * Constructs a new TestUI instance.
     * @post step == 0
     */
    public TestUI(int nObstacles) {
        this.step = 0;
        this.nObstacles = nObstacles;
    }

    /**
     * Returns a fixed number of obstacles.
     * @return the number of obstacles
     * @post result == nObstacles
     */
    @Override
    public int askForNumberOfObstacles() {
        return nObstacles;
    }

    /**
     * Generates a random request with random start and end points.
     * @return the generated request
     */
    @Override
    public Request askForRequest() {
        Random random = new Random();
        return new Request(new Point(random.nextInt(100, 900), random.nextInt(100, 900)), new Point(random.nextInt(100, 900), random.nextInt(100, 900)));
    }

    /**
     * Returns the maximum speed for the simulation.
     * @return the maximum speed
     * @post result == Double.MAX_VALUE
     */
    @Override
    public double askForSpeed() {
        return Double.MAX_VALUE;
    }

    /**
     * Checks if the simulation is asking for a new point.
     * @return true if the simulation should request a new point, false otherwise
     */
    @Override
    public boolean isAskingForNewPoint() {
        return step % 100 == 0;
    }

    /**
     * Updates the robot status with the current step.
     * @param step the current step of the simulation
     * @param robots the list of robots to display
     * @pre step >= 0
     * @post this.step == step
     */
    @Override
    public void displayRobotStatus(int step, LinkedHashSet<Robot> robots) {
        this.step = step;
    }

    /**
     * Displays an error message. This method does nothing in TestUI.
     * @param message the error message to display
     */
    @Override
    public void displayErrorMessage(String message) {
        // Do nothing.
    }

    /**
     * Sends map information. This method does nothing in TestUI.
     * @param map the DeliveryMap object containing map information
     */
    @Override
    public void sendMapInformation(DeliveryMap map) {
        // Do nothing.
    }

    /**
     * Adds a request. This method does nothing in TestUI.
     * @param request the request to add
     */
    @Override
    public void addRequest(Request request) {
        // Do nothing.
    }

    @Override
    public int askForNRobots() {
        return 8;
    }
}
