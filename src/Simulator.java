import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;

public class Simulator {

    private final SimulatorUI ui;

    public Simulator(SimulatorUI ui) {
        this.ui = ui;
    }

    private boolean validInputCheck(DeliveryMap deliveryMap, Point request) {
        if (deliveryMap.isDeliveryPointValid(request)) return true;
        else System.out.println("Request invalid, please input a new one");
        return false;
    }

    public void startSimulation() throws InterruptedException, IOException {
        KeyChecker kc = new KeyChecker();
        Random generator = new Random();
        int nObstacles = ui.askForNumberOfObstacles();
        ArrayList<Shape> obstacles = new ArrayList<>();
        for (int i = 0; i < nObstacles; i++) {
            int option = generator.nextInt(3);
            switch (option) {
                case 0 -> obstacles.add(new Circle(generator));
                case 1 -> obstacles.add(new Rectangle(generator));
                case 2 -> obstacles.add(new Triangle(generator));
            }
        }
        DeliveryMap deliveryMap = new DeliveryMap(obstacles);
        ArrayList<Robot> robots = new ArrayList<>(4);
        Point chargingPoint0 = new Point(0, 0);
        Point chargingPoint1 = new Point(0, 999);
        Point chargingPoint2 = new Point(999, 999);
        Point chargingPoint3 = new Point(999, 0);
        Robot robot0 = new Robot(chargingPoint0, deliveryMap, generator);
        Robot robot1 = new Robot(chargingPoint1, deliveryMap, generator);
        Robot robot2 = new Robot(chargingPoint2, deliveryMap, generator);
        Robot robot3 = new Robot(chargingPoint3, deliveryMap, generator);
        robots.add(robot0);
        robots.add(robot1);
        robots.add(robot2);
        robots.add(robot3);
        RobotManager robotManager = new RobotManager(robots);
        boolean dog = true;
        for (Robot robot : robots)
            robot.subscribeToManager(robotManager);
        for (int i = 0; true; i++) {

            if (i%1000 == 0) {
                Point request;
                do {
                    request = ui.askForPoint();
                }
                while (!validInputCheck(deliveryMap, request));
                dog = !dog;
                robotManager.addRequest(request);
            }
            robotManager.update();
            for (Robot robot : robots)
                robot.update();
            //SEND INFORMATION TO INTERFACE
            ui.displayRobotStatus(i, robots);
            Thread.sleep(5);
        }
    }

}