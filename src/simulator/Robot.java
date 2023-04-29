package simulator;

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
 *
 * @inv A robot's battery level must always be between 0 and 100.
 * @inv If a robot's battery level drops to 0, it must return to its charging point to recharge.
 * @inv currentPosition, deliveryMap, generator, chargingStation, trajectoryCache, and rng must not be null.
 * @inv currentPosition and chargingStation must be within the deliveryMap.
 */
public class Robot {
    private final Random rng;
    private Point currentPosition;
    private final PointGenerator generator;
    private double energy;
    private RobotPowerState powerState;
    private final Point chargingStation;
    private Iterator<Point> trajectoryPointIterator;
    private RobotManager manager;

    private final Map<String, Trajectory> trajectoryCache;
    private final DeliveryMap deliveryMap;

    /**
     * Robot Constructor
     *
     * @param startingPoint the starting point of the robot
     * @param deliveryMap   the delivery map that the robot will navigate through
     * @param generator     the random number generator used to generate trajectory lengths
     * @pre startingPoint, deliveryMap, and generator must be non-null
     * @post A Robot object is created with the provided starting point, delivery map, and generator
     * @throws IllegalArgumentException if any of the input parameters are null
     */
    public Robot(Point startingPoint, DeliveryMap deliveryMap, PointGenerator generator,Random rng) {
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
        this.rng = rng;
    }

    /**
     * Returns the power state of the robot.
     *
     * @return the power state of the robot
     * @post Returns the current power state of the robot
     */
    public RobotPowerState getPowerState() {
        return this.powerState;
    }


    /**
     * Subscribes the robot to a robot manager.
     *
     * @param manager the robot manager to subscribe to
     * @pre manager must be non-null
     * @post The robot is subscribed to the provided manager
     */
    public void subscribeToManager(RobotManager manager) {
        this.manager = manager;
    }

    /**
     * @return the current energy level of the robot
     * @post Returns the current energy level of the robot
     */
    public double getEnergy() {
        return energy;
    }


    /**
     * Updates the state of the robot. Depending on the robot's power state, this may involve moving to a new position,
     * charging the battery, or updating the robot manager with its current state.
     *
     * @pre energy >= 0
     * @post energy is updated based on the robot's power state
     * @throws IllegalStateException if the energy level is below 0
     */
    public void update() {
        if((int)energy < 0) throw new IllegalStateException("Energy level cannot be below 0.");

        switch (powerState) {
            case DELIVERING, RETURNING, ENROUTE -> handleMovingState();
            case STANDBY -> handleStandbyState();
            case CHARGING -> handleChargingState();
            default -> throw new IllegalStateException("Unexpected power state: " + powerState);
        }
    }

    /**
     * Handles the logic for when the robot is in the moving state (DELIVERING or RETURNING).
     *
     * @pre Robot must be in DELIVERING or RETURNING state
     * @post Updates energy consumption and moves the robot to the next position in its trajectory
     */
    private void handleMovingState() {
        energy -= 0.1;
        this.moveToNextPosition();
    }

    /**
     * Handles the logic for when the robot is in the STANDBY state.
     *
     * @pre Robot must be in STANDBY state
     * @post Updates energy consumption and checks if the robot needs to go to the charging station
     */
    private void handleStandbyState() {
        if (!this.currentPosition.equals(chargingStation)) {
            energy -= 0.01;
            if (energy / 0.1 <= distanceToChargingStation()) {
                this.goToChargingStation();
            }
        }
    }

    /**
     * Handles the logic for when the robot is in the CHARGING state.
     *
     * @pre Robot must be in CHARGING state
     * @post Updates energy levels and changes the robot state to STANDBY if fully charged
     */
    private void handleChargingState() {
        if (energy < 100.0) {
            if (energy >= 99.0) {
                energy = 100.0;
            } else {
                energy += 1.0;
            }
        } else if (energy == 100.0) {
            powerState = RobotPowerState.STANDBY;
            manager.notify(this, powerState);
        }
    }

    /**
     * Determines whether the robot can reach its destination given a trajectory.
     *
     * @param trajectory the trajectory to check
     * @return true if the robot can reach its destination, false otherwise
     * @pre trajectory must be non-null
     * @post Returns true if the robot can reach its destination, false otherwise
     */
    public boolean canReachDestination(Trajectory trajectory) {
        if (trajectory == null)
            return false;
        ArrayList<Point> points = trajectory.getPoints();
        Point destination = points.get(points.size() - 1);
        double totalDistance = distanceToDestination(currentPosition, destination) + distanceToDestination(destination, chargingStation);
        return energy / 0.1 >= totalDistance;
    }


    /**
     * Determines whether the robot can reach the charging station from its current position without running out of energy.
     *
     * @return the distance to the charging station from the robot's current position
     * @pre start and destination points are non-null
     * @post returns the distance to the charging station
     * @throws IllegalArgumentException if either the start or destination points are null
     */
    public double distanceToDestination(Point start, Point destination) {
        if(start==null||destination==null) throw new IllegalArgumentException("Start and destination must be non null");
        Trajectory trajectoryToDestination = findTrajectory(start,destination);
        if (trajectoryToDestination == null)
            return Double.MAX_VALUE;
        return trajectoryToDestination.getLength();
    }
    /**
     * Calculates the distance to the charging station from the robot's current position.
     *
     * @pre currentPosition and chargingStation must be non-null
     * @return the distance to the charging station
     * @post Returns a non-negative integer representing the distance to the charging station
     */
    public double distanceToChargingStation() {
        return distanceToDestination(currentPosition, chargingStation);
    }
    /**
     * Returns the charging station point.
     *
     * @return the charging station point
     * @post Returns the charging station point
     */
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
     * @post Returns a string representation of the robot's current state
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
     *
     * @pre trajectoryPointIterator must be non-null
     * @post The robot's position is updated based on the trajectory, and its power state is updated if it reaches its destination
     */
    private void moveToNextPosition() {
        //Move to next point
        if (this.trajectoryPointIterator.hasNext()) {
            this.currentPosition = trajectoryPointIterator.next();
        }
        //Check if arrives at destination
        if (!this.trajectoryPointIterator.hasNext()) {
            switch (powerState) {
                case RETURNING -> this.powerState = RobotPowerState.CHARGING;
                case DELIVERING -> {
                    this.powerState = RobotPowerState.STANDBY;
                    manager.notify(this, this.powerState);
                }
                case ENROUTE -> {
                    Request currentRequest = manager.getCurrentRequest(this);
                    if (currentRequest != null) {
                        Trajectory trajectoryFromStartToEnd = findTrajectory(currentPosition, currentRequest.end());
                        setPath(trajectoryFromStartToEnd);
                        this.powerState = RobotPowerState.DELIVERING;
                        manager.notify(this, this.powerState);
                    }
                }
            }
        }
    }

    /**
     * Moves the robot to the next position in the trajectory. If the robot has reached its destination, it changes its
     * power state to CHARGING if it was RETURNING, or to STANDBY if it was DELIVERING, and notifies the manager.
     *
     * @pre The robot's energy level must be sufficient to reach the charging station
     * @post The robot's trajectory is set to reach the charging station, and its power state is set to RETURNING
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
     * @pre start and destination points must be non-null
     * @post Returns a trajectory between the start and destination points, or null if no trajectory is found
     * @throws IllegalArgumentException if either start or destination points are null
     */
    public Trajectory findTrajectory(Point start, Point destination) {
        if (start == null || destination == null)
            throw new IllegalArgumentException("Start and destination points cannot be null");
        String cacheKey = start + "-" + destination;
        Trajectory cachedTrajectory = trajectoryCache.get(cacheKey);
        if (cachedTrajectory != null) {
            return cachedTrajectory;
        }
        int[] lengths = rng.ints(200, 0, 2).toArray();
        Planner planner = new Planner(0.045, 0.15, 0.15, start, destination, lengths, generator, deliveryMap.obstacles(),rng);
        Trajectory trajectory = planner.findTrajectory();
        trajectoryCache.put(cacheKey, trajectory);
        return trajectory;
    }

    /**
     * Determines if the robot can perform the given request.
     *
     * @param request the request to check if the robot can perform
     * @return true if the robot can perform the request, false otherwise
     * @pre request must be non-null
     * @post Returns true if the robot can perform the request, false otherwise
     */
    public boolean canPerformRequest(Request request) {
        if (request == null) {
            return false;
        }

        Point startPoint = request.start();
        Point endPoint = request.end();

        // Check if a trajectory disregarding collisions (straight lines) would be possible given the current energy
        double euclideanDistanceToStart = getCurrentPosition().dist(startPoint);
        double euclideanDistanceStartToEnd = startPoint.dist(endPoint);
        double euclideanDistanceEndToChargingStation = endPoint.dist(getChargingStation());
        double totalDistance = euclideanDistanceToStart + euclideanDistanceStartToEnd + euclideanDistanceEndToChargingStation;

        if ((energy / 0.1) <= totalDistance)
            return false;

        // Check if a trajectory now with collisions in mind (planned trajectories) would be possible given the current energy
        double distanceToStart = distanceToDestination(currentPosition, startPoint);
        double distanceToEnd = distanceToDestination(startPoint, endPoint);
        double distanceToChargingStation = distanceToDestination(endPoint, chargingStation);

        totalDistance = (distanceToStart + distanceToEnd + distanceToChargingStation);
        return (energy / 0.1) > totalDistance;
    }

    /**
     * Assigns a delivery request to the robot and sets its path to the start point of the request.
     * The robot's power state is set to ENROUTE, and the manager is notified of the change in power state.
     *
     * @param request The delivery request to be assigned to the robot.
     * @pre request must not be null.
     * @post The robot's path is set to the start point of the request.
     *       The robot's power state is set to ENROUTE.
     *       The manager is notified of the change in power state.
     */
    public void assignRequest(Request request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        Point startPoint = request.start();
        Trajectory trajectoryToStart = findTrajectory(currentPosition, startPoint);
        setPath(trajectoryToStart);
        this.powerState = RobotPowerState.ENROUTE;
        manager.notify(this, this.powerState);
    }



    /**
 * Gets the current position of the robot.
 *
 * @return the current position of the robot
 * @post Returns the current position  of the robot
 */
    public Point getCurrentPosition() {
        return currentPosition;
    }
}
