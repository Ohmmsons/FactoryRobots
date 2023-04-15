import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Robot {
    private Point currentPosition;
    private final ArrayList<Shape> obstacles;
    private final Random generator;
    private double energy;
    private RobotPowerState powerState;
    private final Point chargingStation;
    private Iterator<Point> trajectoryPointIterator;
    private RobotManager manager;

    public Robot(Point start, ArrayList<Shape> obstacles, Random generator, Point chargingStation, RobotManager manager) {
        this.currentPosition = start;
        this.obstacles = obstacles;
        this.generator = generator;
        this.energy = 100.00;
        this.powerState = RobotPowerState.STANDBY;
        this.chargingStation = chargingStation;
        this.trajectoryPointIterator = null;
        this.manager = manager;
    }

    public double getEnergy() {return energy;}

    public void update() {
        switch (powerState) {
            case MOVING -> {
                energy -= 0.1;
                this.moveToNextPosition();
            }
            case STANDBY -> {
                energy -= 0.01;
                if (energy < 10.0) {
                    this.goToChargingStation();
                }
            }
            case CHARGING -> {
                if (energy < 100.0)
                    if (energy >= 99.0) energy = 100.0;
                    else energy += 1.0;
                else if (energy == 100.0) powerState = RobotPowerState.STANDBY;
            }
        }
    }

    public boolean canReachDestination(Trajectory trajectory) {
        int distanceFromEndToChargingStation = findTrajectory(trajectory.getPoints().get(trajectory.getPoints().size() - 1), chargingStation).getPoints().size();
        return energy / 0.1 < distanceFromEndToChargingStation + trajectory.getPoints().size();
    }

    public void setPath(Trajectory trajectory) {
        this.trajectoryPointIterator = trajectory.getPoints().iterator();
        this.powerState = RobotPowerState.MOVING;
        manager.notify(this, this.powerState);
    }

    public String toString() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        String formattedX = String.format("%03d", currentPosition.x());
        String formattedY = String.format("%03d", currentPosition.y());
        String symbol = this.energy < 10 ? "-" : "*";
        return "(" + formattedX + "," + formattedY + "," + df.format(energy) + "," + symbol + ")";
    }

    private void moveToNextPosition() {this.currentPosition = trajectoryPointIterator.next();}

    private void goToChargingStation() {setPath(findTrajectory(currentPosition, chargingStation));}

    public Trajectory findTrajectory(Point start, Point destination) {
        int[] lengths = generator.ints(30, 0, 30).toArray();
        Planner planner = new Planner(0.5, 0.25, 0.25, start, destination, lengths, generator, obstacles);
        return planner.findTrajectory();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }
}
