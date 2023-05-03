package simulator;

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
    private final Set<Robot> subscribers;
    private final RequestQueue requests;
    private final Map<Robot, Request> currentRequests;

    /**
     * Constructor for RobotManager.
     *
     * @param robots   An ArrayList of robots that will subscribe to the RobotManager.
     * @param requests A RequestQueue containing the delivery requests.
     * @pre robots and requests must not be null.
     * @post A new RobotManager is created with the given robots and requests.
     */
    public RobotManager(Set<Robot> robots, RequestQueue requests) {
        if (robots == null || requests == null)
            throw new IllegalArgumentException("Can't be constructed with null arguments");
        this.requests = requests;
        this.subscribers = new LinkedHashSet<>(robots);
        for (Robot robot : robots)
            robot.subscribeToManager(this);
        this.currentRequests = new HashMap<>();
    }


    /**
     * Returns the current request assigned to the specified robot.
     *
     * @param robot The robot whose current request is to be fetched.
     * @return The current request assigned to the robot, or null if there is no request assigned.
     * @pre robot must not be null.
     * @post A request object is returned if there is a current request assigned to the robot, otherwise null is returned.
     */
    public Request getCurrentRequest(Robot robot) {
        if (robot == null) {
            throw new IllegalArgumentException("Robot cannot be null");
        }
        return currentRequests.get(robot);
    }


    /**
     * Simple record class to pair a robot and a trajectory for better code reabdability.
     *
     * @param bestRobot
     * @param bestTrajectory
     */
    private record RobotBestTrajectory(Robot bestRobot, Trajectory bestTrajectory) {
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

            RobotBestTrajectory robotBestTrajectory = findBestRobotAndTrajectory(nextRequest);

            // If there is a robot that can reach the request
            if (robotBestTrajectory.bestRobot() != null) {
                assignRequestToRobot(robotBestTrajectory.bestRobot(), nextRequest);
            }
            // If no robot can reach the request's destination, send the request to the end of the queue
            else {
                moveToQueueEnd(nextRequest);
            }
        }
    }

    /**
     * Find the best robot and trajectory for the given request.
     *
     * @param nextRequest The next request in the queue.
     * @return RobotBestTrajectory object containing the best robot and its trajectory for the given request.
     * @pre nextRequest is non-null.
     * @post Returns a RobotBestTrajectory object with the robot having the shortest trajectory to the request and the corresponding trajectory.
     * If no robot can reach the request, both fields in RobotBestTrajectory will be null.
     */
    private RobotBestTrajectory findBestRobotAndTrajectory(Request nextRequest) {
        Robot bestRobot = null;
        Trajectory bestTrajectory = null;
        double minDistance = Double.MAX_VALUE;

        // Find the robot with the shortest trajectory to the next request
        for (Robot robot : subscribers) {
            if (robot.canPerformRequest(nextRequest)) {
                Trajectory candidateTrajectory = getCandidateTrajectory(robot, nextRequest);
                if (candidateTrajectory != null && candidateTrajectory.getLength() < minDistance) {
                    minDistance = candidateTrajectory.getLength();
                    bestRobot = robot;
                    bestTrajectory = candidateTrajectory;
                }
            }
        }

        return new RobotBestTrajectory(bestRobot, bestTrajectory);
    }

    /**
     * Get the candidate trajectory for a given robot and request.
     *
     * @param robot       The robot to evaluate.
     * @param nextRequest The request to be fulfilled.
     * @return The concatenated trajectory for the given robot and request, or null if not possible.
     * @pre robot and nextRequest are non-null.
     * @post Returns the concatenated trajectory from the robot's current position to the request start and from the start to the request end.
     * If either of the two trajectories are not possible, returns null.
     */
    private Trajectory getCandidateTrajectory(Robot robot, Request nextRequest) {
        Trajectory trajectoryToStart = robot.getTrajectory(robot.getCurrentPosition(), nextRequest.start());
        Trajectory trajectoryStartToEnd = robot.getTrajectory(nextRequest.start(), nextRequest.end());
        if (trajectoryToStart == null || trajectoryStartToEnd == null) {
            return null;
        }
        return trajectoryToStart.concatenate(trajectoryStartToEnd);
    }

    /**
     * Assign the given request to the best robot and update the current requests map.
     *
     * @param bestRobot   The robot that will perform the request.
     * @param nextRequest The request to be assigned to the robot.
     * @pre bestRobot and nextRequest are non-null.
     * @post The bestRobot is assigned the request, and the currentRequests map is updated.
     */
    private void assignRequestToRobot(Robot bestRobot, Request nextRequest) {
        currentRequests.put(bestRobot, nextRequest);
        bestRobot.assignRequest(nextRequest);
        requests.removeRequest();
    }

    /**
     * Move the given request to the end of the queue.
     *
     * @param nextRequest The request to be moved.
     * @pre nextRequest is non-null.
     * @post The request is removed from the current position and added to the end of the queue.
     */
    private void moveToQueueEnd(Request nextRequest) {
        requests.removeRequest();
        requests.addRequest(nextRequest);
    }


    /**
     * Updates the subscribers list based on the robot's power state.
     * If a robot is in standby mode, it will be added to the subscribers list.
     * If a robot is delivering, returning, or enroute, it will be removed from the subscribers list.
     *
     * @param robot The robot whose power state has changed.
     * @param event The power state event that occurred.
     * @pre robot and event must not be null.
     * @post The subscribers list is updated based on the robot's power state.
     */
    private void updateSubscriberList(Robot robot, RobotPowerState event) {
        if (robot == null || event == null) throw new IllegalArgumentException("Robot and event cannot be null");
        // Update subscribers list based on the robot's power state, if standby, the robot starts listening, otherwise it stops receiving
        switch (event) {
            case STANDBY -> subscribers.add(robot);
            case DELIVERING, RETURNING, ENROUTE -> subscribers.remove(robot);
            default -> throw new IllegalStateException("Robot in illegal state");
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
        updateSubscriberList(sender, event);
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

}