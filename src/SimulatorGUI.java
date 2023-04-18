import java.util.List;

public class SimulatorGUI implements SimulatorUI{
    @Override
    public int askForNumberOfObstacles() {
        return 0;
    }

    @Override
    public Point askForPoint() {
        return null;
    }

    @Override
    public void displayRobotStatus(int step, List<Robot> robots) {

    }

    @Override
    public void displayErrorMessage(String message) {

    }
}
