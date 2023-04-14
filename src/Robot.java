import java.util.ArrayList;
import java.util.Random;

public class Robot{
    private final Point currentPosition;
    private final ArrayList<Shape> obstacles;
    private final Random generator;
    private double energy;
    private RobotPowerState powerState;
    private Trajectory trajectory;
    private final Point[] chargingStations;

    public Robot(int startX, int startY, ArrayList<Shape> obstacles, Random generator, Point[] chargingStations){
        this.currentPosition = new Point(startX,startY);
        this.obstacles = obstacles;
        this.generator = generator;
        this.energy = 100.00;
        this.powerState = RobotPowerState.STANDBY;
        this.trajectory = null;
        this.chargingStations = chargingStations;
    }

    public double getEnergy(){
        return energy;
    }

    public void update(){
         switch (powerState){
             case MOVING -> {
                 energy-=0.1;
             }
             case STANDBY -> {
                 energy -= 0.01;
                 if (energy < 10.0) {
                     Point nearestChargingStation = findNearestChargingStation();
                     setPath(findTrajectory(nearestChargingStation.x(), nearestChargingStation.y()));
                 }
             }
             case CHARGING -> {
                 if(energy<100.0)
                     if(energy>=99.0) energy=100.0;
                     else energy+=1.0;
                 else if (energy==100.0) powerState = RobotPowerState.STANDBY;
             }
         }
    }

    public boolean canReachDestination(Trajectory trajectory){
        return energy/0.1 < trajectory.getLength();
    }

    public void setPath(Trajectory trajectory){
        this.trajectory = trajectory;
        this.powerState = RobotPowerState.MOVING;
    }

    public Point findNearestChargingStation(){
        double minDistance = 100000000;
        Point nearestStation = null;
        for(Point station: this.chargingStations) {
            double distance = this.currentPosition.dist(station);
            if (distance < minDistance) {
                nearestStation = station;
                minDistance = distance;
            }
        }
        return nearestStation;
    }

    public Trajectory findTrajectory(int targetX, int targetY){
        int[] lengths = generator.ints(30, 0, 30).toArray();
        Planner planner = new Planner(0.5,0.25,0.25, currentPosition.x(), currentPosition.y(), targetX,targetY,lengths,generator,obstacles);
        return planner.findTrajectory();
    }
}
