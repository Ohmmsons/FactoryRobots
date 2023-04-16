import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RobotManager {
    private ArrayList<Robot> subscribers;
    private Queue<Point> requests;

    public RobotManager(ArrayList<Robot> robots){
        this.subscribers = robots;
    }

    private void addSubscriber(Robot robot){
        subscribers.add(robot);
    }
    private void removeSubscriber(Robot robot){
        subscribers.remove(robot);
    }
    public void update(){
        Point nextRequest = requests.peek();
        Robot bestRobot = null;
        Trajectory bestTrajectory = null;
        double minDistance = 1000000000;
        for(Robot robot: subscribers){
            Trajectory trajectory = robot.findTrajectory(robot.getCurrentPosition(),nextRequest);
            if(robot.canReachDestination(trajectory) && trajectory.getLength()<minDistance) {
                bestRobot = robot;
                bestTrajectory = trajectory;
            }
        }
        //If there is a robot that can reach the request
        if(bestRobot!=null){
            bestRobot.setPath(bestTrajectory);
        }
        //If no robot can reach the request's destination send request to end of queue
        else{
            requests.poll();
            requests.add(nextRequest);
        }
    }
    public void notify(Robot sender, RobotPowerState event){
        switch (event){
            case STANDBY -> addSubscriber(sender);
            case MOVING -> removeSubscriber(sender);
        }
    }

    public void addRequest(Point deliveryPoint){
        this.requests.add(deliveryPoint);
    }

}
