package ui;

import simulator.DeliveryMap;
import simulator.Point;
import simulator.Request;
import simulator.Robot;

import java.util.LinkedHashSet;
import java.util.Random;

public class TestUI implements SimulatorUI{
    private int step;

    public TestUI(){
        this.step = 0;
    }
    @Override
    public int askForNumberOfObstacles() {
        return 150;
    }

    @Override
    public Request askForRequest() {
        Random random = new Random();
        return new Request(new Point(random.nextInt(100,900),random.nextInt(100,900)),new Point(random.nextInt(100,900),random.nextInt(100,900)));
    }

    @Override
    public double askForSpeed() {
        return Double.MAX_VALUE;
    }

    @Override
    public boolean isAskingForNewPoint() {
        return step%100 == 0;
    }

    @Override
    public void displayRobotStatus(int step, LinkedHashSet<Robot> robots) {
        this.step = step;
    }

    @Override
    public void displayErrorMessage(String message) {

    }

    @Override
    public void sendMapInformation(DeliveryMap map) {

    }

    @Override
    public void addRequest(Request request) {

    }
}
