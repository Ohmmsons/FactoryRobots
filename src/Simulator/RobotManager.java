package Simulator;

import java.util.*;

/**
 * The RobotManager class manages a list of Robots and handles incoming delivery requests.
 * It subscribes to each robot and sends them new delivery requests as they come in. It also keeps track of
 * the status of each robot and reports this information to the SimulatorUI.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 */
public class RobotManager {
    private Set<Robot> subscribers;
    private RequestQueue requests;
    private PriorityQueue<Robot> distanceSortedSubscribers;

    /**
     * Constructor for RobotManager.
     *
     * @param robots An ArrayList of robots that will subscribe to the RobotManager.
     */
    public RobotManager(LinkedHashSet<Robot> robots,RequestQueue requests) {
        if (robots == null) throw new IllegalArgumentException("Can't be constructed with null arguments");
        this.requests = requests;
        this.subscribers = new LinkedHashSet<>(robots);
        for (Robot robot : robots)
            robot.subscribeToManager(this);
        this.distanceSortedSubscribers = new PriorityQueue<>(Comparator.comparingDouble(Robot::getDistanceToNextRequest));
        this.distanceSortedSubscribers.addAll(robots);
    }

    /**
     * Add a robot to the subscribers list.
     *
     * @param robot The robot to be added to the list.
     */
    private void addSubscriber(Robot robot) {
        subscribers.add(robot);
    }

    /**
     * Remove a robot from the subscribers list.
     *
     * @param robot The robot to be removed from the list.
     */
    private void removeSubscriber(Robot robot) {
        subscribers.remove(robot);
    }


    private void updateSortedSubscribers(Robot robot) {
        switch (robot.getPowerState()) {
            case STANDBY -> {
                distanceSortedSubscribers.remove(robot);
                distanceSortedSubscribers.add(robot);
            }
            case DELIVERING, RETURNING -> distanceSortedSubscribers.remove(robot);
        }

    }

    /**
     * Update the subscribers with the latest delivery requests.
     * If a robot can reach a delivery request, it will be assigned to that robot.
     * If no robot can reach the request, the request will be moved to the end of the queue.
     */
    public void update() {
        if (!subscribers.isEmpty() && !requests.isEmpty()) {
            Point nextRequest = requests.getNextRequest();
            Robot bestRobot = null;
            Trajectory bestTrajectory = null;
            double minDistance = 1000000000;

            for (Robot robot : distanceSortedSubscribers) {
                if (robot.getCurrentPosition().dist(nextRequest) + nextRequest.dist(robot.getChargingStation()) <= robot.getEnergy() / 0.1) {
                    Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(), nextRequest);
                    if (robot.canReachDestination(trajectory) && trajectory.getLength() < minDistance) {
                        minDistance = trajectory.getLength();
                        bestRobot = robot;
                        bestTrajectory = trajectory;
                    }
                }
            }

            // If there is a robot that can reach the request
            if (bestRobot != null) {
                bestRobot.setPath(bestTrajectory);
                requests.removeRequest();
            }
            // If no robot can reach the request's destination send request to end of queue
            else {
                requests.removeRequest();
                requests.addRequest(nextRequest);
            }
        }
    }

    /**
     * Notifies the RobotManager of a change in a robot's power state.
     * If a robot is in standby mode, it will be added to the subscribers list.
     * If a robot is delivering, it will be removed from the subscribers list.
     *
     * @param sender The robot that sent the notification.
     * @param event  The power state event that occurred.
     */
    public void notify(Robot sender, RobotPowerState event) {
        if (sender == null || event == null) throw new IllegalArgumentException("Sender and event cannot be null");
        switch (event) {
            case STANDBY -> {
                addSubscriber(sender);
            }
            case DELIVERING, RETURNING -> removeSubscriber(sender);
        }
        updateSortedSubscribers(sender);
    }

    /**
     * Adds a delivery request to the end of the queue.
     *
     * @param deliveryPoint The delivery point to be added to the queue.
     */
    public void addRequest(Point deliveryPoint) {
        if (deliveryPoint == null) throw new IllegalArgumentException("Delivery point cannot be null");
        this.requests.addRequest(deliveryPoint);
    }


    public Queue<Point> getRequests() {
        return this.requests.getRequests();
    }

    public Set<Robot> getSubscribers() {
        return subscribers;
    }

    public Point getNextRequest() {
        return requests.getNextRequest();
    }
}
