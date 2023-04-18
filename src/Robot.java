import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Robot {
    private Point currentPosition;
    private final Random generator;
    private double energy;
    private RobotPowerState powerState;
    private final Point chargingStation;
    private Iterator<Point> trajectoryPointIterator;
    private RobotManager manager;
    private boolean goingToChargingStation;

    private DeliveryMap deliveryMap;

    public Robot(Point startingPoint, DeliveryMap deliveryMap, Random generator) {
        this.currentPosition = startingPoint;
        this.deliveryMap = deliveryMap;
        this.generator = generator;
        this.chargingStation = startingPoint;
        this.energy = 100.00;
        this.powerState = RobotPowerState.STANDBY;
        this.goingToChargingStation = false;
        this.trajectoryPointIterator = null;
    }

    public void subscribeToManager(RobotManager manager) {
        this.manager = manager;
    }

    public double getEnergy() {
        return energy;
    }

    public void update() {
        switch (powerState) {
            case MOVING -> {
                energy -= 0.1;
                this.moveToNextPosition();
            }
            case STANDBY -> {
                if(!this.currentPosition.equals(chargingStation))
                    energy -= 0.01;
                if (energy < 50.0) {
                    this.goingToChargingStation = true;
                    this.goToChargingStation();
                }
            }
            case CHARGING -> {
                if (energy < 100.0)
                    if (energy >= 99.0) energy = 100.0;
                    else energy += 1.0;
                else if (energy == 100.0) powerState = RobotPowerState.STANDBY;
            }
            default -> throw new IllegalStateException("Unexpected value: " + powerState);
        }
    }

    public boolean canReachDestination(Trajectory trajectory) {
        ArrayList<Point> points = trajectory.getPoints();
        Point destination = points.get(points.size() - 1);
        int distanceFromEndToChargingStation = findTrajectory(destination, chargingStation).getPoints().size();
        return energy / 0.1 > distanceFromEndToChargingStation + trajectory.getPoints().size();
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
        String symbol = (this.powerState == RobotPowerState.MOVING && !goingToChargingStation) ? "*" : "-";
        return "(" + formattedX + "," + formattedY + "," + df.format(energy) + "," + symbol + "," + powerState + ")";
    }

    private void moveToNextPosition() {
        if (this.trajectoryPointIterator.hasNext()) {
            this.currentPosition = trajectoryPointIterator.next();
        }
        if (!this.trajectoryPointIterator.hasNext()) {
            if (goingToChargingStation) {
                this.powerState = RobotPowerState.CHARGING;
                goingToChargingStation = false;
            }
            else {
                this.powerState = RobotPowerState.STANDBY;
                manager.notify(this, this.powerState);
            }
        }
    }

    private void goToChargingStation() {
        setPath(findTrajectory(currentPosition, chargingStation));
    }

    public Trajectory findTrajectory(Point start, Point destination) {
        int[] lengths = generator.ints(20, (int) (start.dist(destination)/100+5), (int) (start.dist(destination)/100)+10).toArray();
        Planner planner = new Planner(0.5, 0.25, 0.25, start, destination, lengths, generator, deliveryMap.getObstacles());
        return planner.findTrajectory();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }
}
