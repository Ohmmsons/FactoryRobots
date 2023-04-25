package UI;
import Simulator.*;
import Simulator.Point;
import Simulator.Robot;
import Simulator.Shape;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.concurrent.Semaphore;

/**
 * A graphical user interface implementation of the UI.SimulatorUI interface.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 */
public class SimulatorGUI extends JPanel implements SimulatorUI {

    // Add instance variables for colors
    private final Color backgroundColor = new Color(217, 217, 217);
    private final Color grayColor = Color.gray;
    private final Color blackColor = Color.BLACK;
    private final Stroke defaultStroke = new BasicStroke();
    private final Stroke robotStroke = new BasicStroke(2);
    private MouseChecker mouseChecker;
    private KeyChecker keyChecker;
    private Semaphore pointSemaphore;
    private ArrayList<Shape> shapes;
    private Point point;
    private LinkedHashSet<Robot> robots;
    private boolean isKeyPressed;
    private ArrayList<Point> requests;
    private JLabel messageLabel;
    private int currentFrame;
    private boolean hasError;
    /**
     * Constructor for the SimulatorGUI class. Initializes instance variables,
     * adds listeners for key and mouse events, and sets up the initial appearance
     * of the panel.
     */
    public SimulatorGUI() {
        this.hasError = false;
        currentFrame = 0;
        isKeyPressed = false;
        mouseChecker = new MouseChecker(this);
        keyChecker = new KeyChecker(this);
        this.addKeyListener(keyChecker);
        this.addMouseListener(mouseChecker);
        shapes = new ArrayList<>();
        robots = new LinkedHashSet<Robot>();
        requests = new ArrayList<>();
        pointSemaphore = new Semaphore(0);
        setPreferredSize(new Dimension(1000, 1000));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(new LineBorder(Color.BLACK, 2, true));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(Color.WHITE);
        add(messageLabel);
    }
    /**
     * Overrides the paintComponent method of JPanel to draw the entire simulation,
     * including tiles, shapes, robots, and requests.
     *
     * @param g Graphics object used for drawing the components.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawTiles(g);
        drawShapes(g);
        drawRobots(g);
        drawRequests(g);
        drawCurrentFrame(g);
    }

    /**
     * Draws the background grid, consisting of tiles and screws, along with border
     * lines and corner tiles.
     *
     * @param g Graphics object used for drawing the components.
     */
    private void drawTiles(Graphics g) {
        g.setColor(backgroundColor);
        int tileSize = 50;
        int screwSize = 10;
        int numCols = getWidth() / tileSize;
        int numRows = getHeight() / tileSize;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int tileX = col * tileSize;
                int tileY = row * tileSize;

                g.fillRect(tileX, tileY, tileSize, tileSize);

                g.setColor(grayColor);
                g.fillOval(tileX + screwSize, tileY + screwSize, screwSize, screwSize);
                g.fillOval(tileX + tileSize - screwSize * 2, tileY + screwSize, screwSize, screwSize);
                g.fillOval(tileX + screwSize, tileY + tileSize - screwSize * 2, screwSize, screwSize);
                g.fillOval(tileX + tileSize - screwSize * 2, tileY + tileSize - screwSize * 2, screwSize, screwSize);

                g.setColor(backgroundColor);
            }
        }
        g.setColor(blackColor);
        for (int row = 0; row <= numRows; row++) {
            g.drawLine(0, row * tileSize, getWidth(), row * tileSize);
        }
        for (int col = 0; col <= numCols; col++) {
            g.drawLine(col * tileSize, 0, col * tileSize, getHeight());
        }
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 55, 55);
        g.fillRect(0, 945, 55, 55);
        g.fillRect(945, 945, 55, 55);
        g.fillRect(945, 0, 55, 55);
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 50, 50);
        g.fillRect(0, 950, 50, 50);
        g.fillRect(950, 950, 50, 50);
        g.fillRect(950, 0, 50, 50);
    }
    /**
     * Iterates through a list of shapes (circles, triangles, and quadrilaterals) and
     * draws them using Graphics methods such as fillPolygon, fillOval, and drawOval.
     *
     * @param g Graphics object used for drawing the components.
     */
    private void drawShapes(Graphics g) {
        for (Shape shape : shapes) {
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                g.setColor(new Color(220, 19, 19));
                g.fillOval((int) circle.getPoints()[0].x() - (int) circle.getRadius(), circle.getPoints()[0].y() - (int) circle.getRadius(),
                        (int) circle.getRadius() * 2, (int) circle.getRadius() * 2);
                g.setColor(Color.BLACK);
                g.drawOval((int) circle.getPoints()[0].x() - (int) circle.getRadius(), circle.getPoints()[0].y() - (int) circle.getRadius(),
                        (int) circle.getRadius() * 2, (int) circle.getRadius() * 2);
            } else if (shape instanceof Triangle) {
                Triangle tri = (Triangle) shape;
                Point[] points = tri.getPoints();
                int[] xPoints = {points[0].x(), points[1].x(), points[2].x()};
                int[] yPoints = {points[0].y(), points[1].y(), points[2].y()};
                g.setColor(new Color(220, 19, 19));
                g.fillPolygon(xPoints, yPoints, 3);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 3);
            } else if (shape.getPoints().length == 4) {
                Point[] points = shape.getPoints();
                int[] xPoints = {points[0].x(), points[1].x(), points[2].x(), points[3].x()};
                int[] yPoints = {points[0].y(), points[1].y(), points[2].y(), points[3].y()};
                g.setColor(new Color(220, 19, 19));
                g.fillPolygon(xPoints, yPoints, 4);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 4);
            }
        }
    }
    /**
     * Iterates through a list of robots, drawing each robot based on their state
     * (delivering, returning, or standby) and updating their energy levels. It also
     * calls other helper methods to draw robots in each state.
     *
     * @param g Graphics object used for drawing the components.
     */
    private void drawRobots(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(robotStroke);
        int i = 0;
        for (Robot robot: robots) {
            requests.removeIf(request -> robot.getCurrentPosition().equals(request));
            switch(i){
                case 0 -> g.setColor(new Color(21, 21, 21));
                case 1 ->  g.setColor(new Color(223, 81, 81));
                case 2 ->  g.setColor(new Color(98, 109, 85));
                case 3 ->  g.setColor(new Color(29, 50, 190));
            }
            Point posRobot = robot.getCurrentPosition();
            int[] xPoints = {posRobot.x()-7, posRobot.x()-7 + 15, posRobot.x()-7 + 15, posRobot.x()-7};
            int[] yPoints = {posRobot.y(), posRobot.y(), posRobot.y() + 15, posRobot.y() + 15};
            g.fillPolygon(xPoints, yPoints, 4);
            g.setColor(new Color(21, 21, 21));
            g.drawPolygon(xPoints, yPoints, 4);
            double energy = robot.getEnergy() / 100;
            g.setColor(Color.green);
            int[] energyxPoints = {posRobot.x()-7, (int) (posRobot.x()-7 + energy * 15), (int) (posRobot.x()-7 + energy * 15), posRobot.x()-7};
            int[] energyyPoints = {posRobot.y() + 7, posRobot.y() + 7, posRobot.y() + 5, posRobot.y() + 5};
            g.fillPolygon(energyxPoints, energyyPoints, 4);
            switch(robot.getPowerState()){
                case DELIVERING -> drawDeliveringRobot(g, robot);
                case RETURNING ->  drawReturningRobot(g, robot);
                case STANDBY -> drawStandbyRobot(g,robot);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(robot.getPowerState().toString());
            g.drawString(robot.getPowerState().toString(),posRobot.x()-7-textWidth/3,robot.getCurrentPosition().y()+23);
            i++;
            g.setFont(new Font("Arial", Font.BOLD, 14));
        }
        g2d.setStroke(defaultStroke);
    }
    /**
     * Draws a robot in the delivering state, including a crate and connecting lines.
     *
     * @param g     Graphics object used for drawing the components.
     * @param robot Robot object in the delivering state.
     */
    private void drawDeliveringRobot(Graphics g, Robot robot) {
        Point pos = robot.getCurrentPosition();
        g.setColor(new Color(234, 123, 54));
        int[] cratexPoints = {pos.x()-4,pos.x() + 5, pos.x() + 5 , pos.x()-4};
        int[] crateyPoints = {pos.y() , pos.y() , pos.y() -9, pos.y() -9};
        g.fillPolygon(cratexPoints, crateyPoints, 4);
        g.setColor(new Color(21, 21, 21));
        g.drawLine(pos.x()-7,pos.y(),pos.x()-7,pos.y()-4);
        g.drawLine(pos.x()-7,pos.y()-4,pos.x()-2,pos.y()-4);
        g.drawLine(pos.x()+7,pos.y(),pos.x()+7,pos.y()-4);
        g.drawLine(pos.x()+7,pos.y()-4,pos.x()+3,pos.y()-4);
    }
    /**
     * Draws a robot in the returning state, including a battery and a red line.
     *
     * @param g     Graphics object used for drawing the components.
     * @param robot Robot object in the returning state.
     */
    private void drawReturningRobot(Graphics g, Robot robot) {
        Point pos = robot.getCurrentPosition();
        g.setColor(new Color(47, 236, 41));
        int[] batteryXPoints = {pos.x()-4,pos.x() + 3, pos.x() + 3 , pos.x()+5,pos.x()+5, pos.x()+3,pos.x()+3,pos.x()-4};
        int[] batteryYPoints = {pos.y()-2 , pos.y()-2 , pos.y() - 4 ,pos.y()-4, pos.y() - 6, pos.y()- 6, pos.y()-8, pos.y()-8};
        g.fillPolygon(batteryXPoints,batteryYPoints, 8);
        g.setColor(Color.BLACK);
        g.drawPolygon(batteryXPoints,batteryYPoints, 8);
        g.setColor(new Color(220, 19, 19));
        g.drawLine(pos.x()+6,pos.y()-9,pos.x()-5,pos.y()-1);
    }
    /**
     * Draws a robot in the standby state, including a connection line, red dot,
     * and blue arcs.
     *
     * @param g     Graphics object used for drawing the components.
     * @param robot Robot object in the standby state.
     */
    private void drawStandbyRobot(Graphics g, Robot robot) {
        Point pos = robot.getCurrentPosition();
        g.setColor(Color.BLACK);
        g.drawLine(pos.x()+7,pos.y(),pos.x()+7,pos.y()-8);
        g.setColor(new Color(220, 19, 19));
        g.fillOval(pos.x() +7 , pos.y() - 8, 2, 2);
        g.setColor(new Color(0, 0, 255));
        for (int j = 0; j < 6; j += 2) {
            int x = pos.x() + 3 + j;
            int y = pos.y() - 12;
            g.drawArc(x, y, 10, 10, -60, 120);
            x = pos.x() + 3 - j;
            g.drawArc(x, y, 10, 10, 120, 120);
        }
    }
    /**
     * Iterates through a list of requests, drawing black circles and indices for
     * each request.
     *
     * @param g Graphics object used for drawing the components.
     */
    private void drawRequests(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < requests.size(); i++) {
            Point request = requests.get(i);
            g.fillOval(request.x() - 5, request.y() - 5, 10, 10);
            String index = String.valueOf(i);
            int labelWidth = g.getFontMetrics().stringWidth(index);
            g.drawString(index, request.x() - labelWidth / 2, request.y() + 15);
        }
    }
    /**
     * Displays the current frame number at the bottom of the panel.
     *
     * @param g Graphics object used for drawing the components.
     */
    private void drawCurrentFrame(Graphics g) {
        g.setColor(blackColor);
        g.drawString("Frame: " + currentFrame, 450, getHeight() - 20);
    }


    /**
     * Returns the number of obstacles on the map, obtained through the GUI.
     *
     * @return the number of obstacles on the map
     */
    @Override
    public int askForNumberOfObstacles() {
        JLabel label = new JLabel("Choose how many obstacles are on the map:");
        JTextField textField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter number of obstacles", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int n = Integer.parseInt(textField.getText());
                return n;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return askForNumberOfObstacles();
            }
        } else {
            return 0;
        }
    }

    /**
     * Asks the user to input a Simulator.Point (x, y) through the GUI, and returns it.
     *
     * @return the Simulator.Point (x, y) input by the user
     */
    @Override
    public Point askForPoint() {
        // Block the method until the mouse is clicked
        try {
            if (!hasError)
                messageLabel.setText("Click where you want your next delivery");
            pointSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageLabel.setText("Press Space Bar to make new request");
        // Return the point that was clicked
        hasError = false;
        return point;
    }

    /**
     * Returns true if the GUI is asking for a new Simulator.Point, false otherwise.
     *
     * @return true if the GUI is asking for a new Simulator.Point, false otherwise
     */
    @Override
    public boolean isAskingForNewPoint() {
        return isKeyPressed;
    }

    /**
     * Displays the status of the robots on the GUI.
     *
     * @param step   the current step of the simulation
     * @param robots the list of robots to display
     */
    @Override
    public void displayRobotStatus(int step, LinkedHashSet<Robot> robots) {
        currentFrame = step;
        this.robots = robots;
        repaint();
    }

    /**
     * Displays an error message on the GUI.
     *
     * @param message the error message to display
     */
    @Override
    public void displayErrorMessage(String message) {
        messageLabel.setText(message);
        this.hasError = true;
    }

    @Override
    public void sendMapInformation(DeliveryMap map) {
        shapes = map.obstacles();
        messageLabel.setText("Press Space Bar to make new request");
        repaint();
    }



    @Override
    public void addRequest(Point request) {
        this.requests.add(request);
    }


// Inner class to handle mouse clicks
private static class MouseChecker implements MouseListener {
    private SimulatorGUI parent;

    public MouseChecker(SimulatorGUI parent) {
        this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Save the clicked point and release the semaphore to unblock the askForPoint() method
        parent.point = new Point(x, y);
        parent.pointSemaphore.release();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

private static class KeyChecker implements KeyListener {
    private SimulatorGUI parent;

    public KeyChecker(SimulatorGUI parent) {
        this.parent = parent;
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            parent.isKeyPressed = true;
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        parent.isKeyPressed = false;
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
    }

}
}
