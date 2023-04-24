package Simulator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * A robot that can move around and deliver packages on a delivery map.
 * The robot can recharge at its charging point and must return to it
 * periodically to avoid running out of power.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 * A robot's battery level must always be between 0 and 100.
 * If a robot's battery level drops to 0, it must return to its charging point
 * to recharge.
 */
public class Robot {
    private Point currentPosition;
    private final Generator generator;
    private double energy;
    private RobotPowerState powerState;
    private final Point chargingStation;
    private Iterator<Point> trajectoryPointIterator;
    private RobotManager manager;
    private boolean goingToChargingStation;

    private final DeliveryMap deliveryMap;

    /**
     * Robot Constructor
     *
     * @param startingPoint the starting point of the robot
     * @param deliveryMap   the delivery map that the robot will navigate through
     * @param generator     the random number generator used to generate trajectory lengths
     */
    public Robot(Point startingPoint, DeliveryMap deliveryMap, Generator generator) {
        if (startingPoint == null || deliveryMap == null || generator == null)
            throw new IllegalArgumentException("Can't be constructed with null arguments");
        this.currentPosition = startingPoint;
        this.deliveryMap = deliveryMap;
        this.generator = generator;
        this.chargingStation = startingPoint;
        this.energy = 100.00;
        this.powerState = RobotPowerState.STANDBY;
        this.goingToChargingStation = false;
        this.trajectoryPointIterator = null;
    }

    /**
     * Returns the power state of the robot.
     *
     * @return the power state of the robot
     */
    public RobotPowerState getPowerState() {
        return this.powerState;
    }

    /**
     * Subscribes the robot to a robot manager.
     *
     * @param manager the robot manager to subscribe to
     */
    public void subscribeToManager(RobotManager manager) {
        this.manager = manager;
    }

    /**
     * @return the current energy level of the robot
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Updates the state of the robot. Depending on the robot's power state, this may involve moving to a new position,
     * charging the battery, or updating the robot manager with its current state.
     */
    public void update() {
        switch (powerState) {
            case DELIVERING, RETURNING -> {
                energy -= 0.1;
                this.moveToNextPosition();
            }
            case STANDBY -> {
                if (!this.currentPosition.equals(chargingStation)) {
                    energy -= 0.01;
                    if (energy / 0.1 <= distanceToChargingStation() + 1) {
                        this.goToChargingStation();
                    }
                }
            }
            case CHARGING -> {
                if (energy < 100.0)
                    if (energy >= 99.0) energy = 100.0;
                    else energy += 1.0;
                else if (energy == 100.0) {
                    powerState = RobotPowerState.STANDBY;
                    manager.notify(this, powerState);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + powerState);
        }
    }


    /**
     * Determines whether the robot can reach its destination given a trajectory.
     *
     * @param trajectory the trajectory to check
     * @return true if the robot can reach its destination, false otherwise
     */
    public boolean canReachDestination(Trajectory trajectory) {
        if (trajectory == null)
            return false;
        int distanceFromCurrentPosToEnd = trajectory.calculatePointsAlongTrajectory().size();
        if (energy / 0.1 <= distanceFromCurrentPosToEnd)
            return false;
        ArrayList<Point> points = trajectory.getPoints();
        Point destination = points.get(points.size() - 1);
        Trajectory trajectoryBackToChargingStation = findTrajectory(destination, chargingStation);
        int distanceFromEndToChargingStation;
        if (trajectoryBackToChargingStation == null)
            return false;
        else
            distanceFromEndToChargingStation = trajectoryBackToChargingStation.calculatePointsAlongTrajectory().size();
        return energy / 0.1 > distanceFromEndToChargingStation + distanceFromCurrentPosToEnd;
    }

    /**
     * Determines whether the robot can reach the charging station from its current position without running out of energy.
     *
     * @return true if the robot can reach the charging station without running out of energy, false otherwise
     */
    public int distanceToChargingStation() {
        Trajectory trajectoryBackToChargingStation = findTrajectory(currentPosition, chargingStation);
        if (trajectoryBackToChargingStation == null)
            return 100000000;
        return trajectoryBackToChargingStation.calculatePointsAlongTrajectory().size();
    }

    public Point getChargingStation(){
        return  chargingStation;
    }
    /**
     * Sets the trajectory of the robot to the given one, and notifies the manager that the robot is delivering.
     * This method also sets the power state of the robot to DELIVERING.
     *
     * @param trajectory the trajectory that the robot will follow
     */
    public void setPath(Trajectory trajectory) {
        this.trajectoryPointIterator = trajectory.calculatePointsAlongTrajectory().iterator();
        this.powerState = RobotPowerState.DELIVERING;
        manager.notify(this, this.powerState);
    }

    /**
     * Formats the robot's current position, energy level, power state, and whether it's delivering or returning to the
     * charging station into a string representation. The * symbol is used to denote that the robot is delivering.
     *
     * @return a string representation of the robot
     */
    public String toString() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        String formattedX = String.format("%03d", currentPosition.x());
        String formattedY = String.format("%03d", currentPosition.y());
        String symbol = (this.powerState == RobotPowerState.DELIVERING && !goingToChargingStation) ? "*" : "-";
        return "(" + formattedX + "," + formattedY + "," + df.format(energy) + "," + symbol + "," + powerState + ")";
    }

    /**
     * Moves the robot to the next position in the trajectory. If the robot has reached its destination, it changes its
     * power state to CHARGING if it was RETURNING, or to STANDBY if it was DELIVERING, and notifies the manager.
     */
    private void moveToNextPosition() {
        //Move to next point
        if (this.trajectoryPointIterator.hasNext()) {
            this.currentPosition = trajectoryPointIterator.next();
        }
        //Check if arrives at destination
        if (!this.trajectoryPointIterator.hasNext()) {
            switch (powerState) {
                case RETURNING -> {
                    this.powerState = RobotPowerState.CHARGING;
                }
                case DELIVERING -> {
                    this.powerState = RobotPowerState.STANDBY;
                    manager.notify(this, this.powerState);
                }
            }
        }
    }

    /**
     * Moves the robot to the next position in the trajectory. If the robot has reached its destination, it changes its
     * power state to CHARGING if it was RETURNING, or to STANDBY if it was DELIVERING, and notifies the manager.
     */
    private void goToChargingStation() {
        setPath(findTrajectory(currentPosition, chargingStation));
        this.powerState = RobotPowerState.RETURNING;
        manager.notify(this, this.powerState);
    }

    /**
     * Finds a trajectory between the start and destination points using the provided planner.
     * @param start the starting point of the trajectory
     * @param destination the destination point of the trajectory
     * @return the trajectory between the start and destination points
     */
    public Trajectory findTrajectory(Point start, Point destination) {
        int[] lengths = generator.ints(200, 0, 2);
        Planner planner = new Planner(0.1, 0.1, 0.1, start, destination, lengths, generator, deliveryMap.getObstacles());
        return planner.findTrajectory();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }
}
