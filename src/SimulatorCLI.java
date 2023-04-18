import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SimulatorCLI implements SimulatorUI {
    private Scanner sc;

    public SimulatorCLI() {
        sc = new Scanner(System.in);
    }

    @Override
    public int askForNumberOfObstacles() {
        System.out.println("Choose how many obstacles are on the map");
        int n;
        n = sc.nextInt();
        return n;
    }

    @Override
    public Point askForPoint() {
        System.out.print("Input your request Coordinates separated by a space: ");
        int x;
        int y;
        x = sc.nextInt();
        y = sc.nextInt();
        return new Point(x, y);
    }

    @Override
    public void displayRobotStatus(int step, List<Robot> robots) {
        StringBuilder info = new StringBuilder("Step " + step);
        for (Robot robot : robots)
            info.append(robot);
        System.out.println(info);
    }

    @Override
    public void displayErrorMessage(String message) {

    }
}
