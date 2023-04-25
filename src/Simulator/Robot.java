package Simulator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

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
    private final int CACHE_SIZE = 15;

    private final Map<String, Trajectory> trajectoryCache;
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
        this.trajectoryPointIterator = null;
        this.trajectoryCache = new HashMap<>();
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
        if(energy<0) throw new IllegalStateException("ENERGY BELOW 0");
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
            default -> throw new IllegalStateException("Unexpected power state: " + powerState);
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
        ArrayList<Point> points = trajectory.getPoints();
        Point destination = points.get(points.size() - 1);
        int totalDistance = distanceToDestination(currentPosition, destination) + distanceToDestination(destination, chargingStation);
        return energy / 0.1 > totalDistance;
    }


    /**
     * Determines whether the robot can reach the charging station from its current position without running out of energy.
     *
     * @return true if the robot can reach the charging station without running out of energy, false otherwise
     */
    public int distanceToDestination(Point start, Point destination) {
        if(start==null||destination==null) throw new IllegalArgumentException("Start and destination must be non null");
        Trajectory trajectoryBackToChargingStation = findTrajectory(start,destination);
        if (trajectoryBackToChargingStation == null)
            return 100000000;
        return trajectoryBackToChargingStation.calculatePointsAlongTrajectory().size();
    }
    public int distanceToChargingStation() {
        return distanceToDestination(currentPosition, chargingStation);
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
        if (trajectory == null)
            throw new IllegalArgumentException("Trajectory cannot be null");
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
        String symbol = (this.powerState == RobotPowerState.DELIVERING) ? "*" : "-";
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
     * Finds a trajectory (if it hasn't already been found), between the start and destination points using the provided planner.
     * @param start the starting point of the trajectory
     * @param destination the destination point of the trajectory
     * @return the trajectory between the start and destination points
     */
    public Trajectory findTrajectory(Point start, Point destination) {
        if (start == null || destination == null)
            throw new IllegalArgumentException("Start and destination points cannot be null");

        String cacheKey = start + "-" + destination;
        Trajectory cachedTrajectory = trajectoryCache.get(cacheKey);
        if (cachedTrajectory != null) {
            return cachedTrajectory;
        }

        int[] lengths = generator.ints(200, 0, 2).toArray();
        Planner planner = new Planner(0.045, 0.15, 0.15, start, destination, lengths, generator, deliveryMap.obstacles());
        Trajectory trajectory = planner.findTrajectory();

        trajectoryCache.put(cacheKey, trajectory);
        return trajectory;
    }

    public double getDistanceToNextRequest() {
        if (manager.getNextRequest() == null) {
            return Double.MAX_VALUE;
        }
        return currentPosition.dist(manager.getNextRequest());
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }
}
