import java.util.List;

public interface SimulatorUI {
    int askForNumberOfObstacles();
    Point askForPoint();
    void displayRobotStatus(int step, List<Robot> robots);
    void displayErrorMessage(String message);
}