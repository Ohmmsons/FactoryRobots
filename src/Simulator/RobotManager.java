package Simulator;

import java.util.*;

/**
 * The RobotManager class manages a list of Robots and handles incoming delivery requests.
 * It subscribes to each robot and sends them new delivery requests as they come in. It also keeps track of
 * the status of each robot and reports this information to the SimulatorUI.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 * @inv subscribers, requests, and distanceSortedSubscribers must not be null.
 * @inv All robots in subscribers must initially be subscribed to this RobotManager.
 * @inv All robots in distanceSortedSubscribers must be in subscribers.
 * @inv Robots in distanceSortedSubscribers must be sorted by their distance to the next request.
 */
public class RobotManager {
    private Set<Robot> subscribers;
    private RequestQueue requests;
    private PriorityQueue<Robot> distanceSortedSubscribers;

    /**
     * Constructor for RobotManager.
     *
     * @param robots   An ArrayList of robots that will subscribe to the RobotManager.
     * @param requests A RequestQueue containing the delivery requests.
     * @pre robots and requests must not be null.
     * @post A new RobotManager is created with the given robots and requests.
     */
    public RobotManager(LinkedHashSet<Robot> robots, RequestQueue requests) {
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
     * @pre robot must not be null.
     * @post The robot is added to the subscribers list.
     */
    private void addSubscriber(Robot robot) {
        subscribers.add(robot);
    }

    /**
     * Remove a robot from the subscribers list.
     *
     * @param robot The robot to be removed from the list.
     * @pre robot must not be null.
     * @post The robot is removed from the subscribers list.
     */
    private void removeSubscriber(Robot robot) {
        subscribers.remove(robot);
    }

    /**
     * If a robot is in standby mode, it will be reinserted to the sorted subscribers list.
     * If a robot is delivering or returning, it will be removed from the sorted subscribers list.
     *
     * @param robot The robot to be updated
     * @pre robot must not be null.
     * @post The robot's position in the sorted subscribers list is updated based on its power state.
     */
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
     *
     * @pre None.
     * @post Robots are assigned delivery requests if they can reach them.
     * Unreachable requests are moved to the end of the queue.
     */
    public void update() {
        if (!subscribers.isEmpty() && !requests.isEmpty()) {
            Request nextRequest = requests.getNextRequest();
            Robot bestRobot = null;
            Trajectory bestTrajectory = null;
            double minDistance = Double.MAX_VALUE;
            for (Robot robot : distanceSortedSubscribers) {
                if (robot.canPerformRequest(nextRequest)) {
                    Trajectory trajectoryToStart = robot.findTrajectory(robot.getCurrentPosition(), nextRequest.start());
                    Trajectory trajectoryStartToEnd = robot.findTrajectory(nextRequest.start(), nextRequest.end());
                    if (trajectoryToStart == null || trajectoryStartToEnd == null) continue;
                    Trajectory trajectory = trajectoryToStart.concatenate(trajectoryStartToEnd);
                    if (trajectory.getLength() < minDistance) {
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
     * @pre sender and event must not be null.
     * @post The subscribers list is updated based on the robot's power state.
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
     * @param deliveryRequest The delivery point to be added to the queue.
     * @pre deliveryRequest must not be null.
     * @post The delivery request is added to the end of the queue
     * and the RobotManager will attempt to assign the request to a robot.
     */
    public void addRequest(Request deliveryRequest) {
        if (deliveryRequest == null) throw new IllegalArgumentException("Delivery Request cannot be null");
        this.requests.addRequest(deliveryRequest);
    }

    /**
     * Returns the queue of requests.
     *
     * @return The queue of requests.
     * @pre None.
     * @post A queue containing the current requests is returned.
     */
    public Queue<Request> getRequests() {
        return this.requests.getRequests();
    }

    /**
     * Returns the set of subscribed robots.
     *
     * @return The set of subscribed robots.
     * @pre None.
     * @post A set containing the current subscribers is returned.
     */
    public Set<Robot> getSubscribers() {
        return subscribers;
    }

    /**
     * Returns the next request in the queue.
     *
     * @return The next request in the queue.
     * @pre None.
     * @post The next request in the queue is returned.
     */
    public Request getNextRequest() {
        return requests.getNextRequest();
    }
}