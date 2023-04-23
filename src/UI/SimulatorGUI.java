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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * A graphical user interface implementation of the UI.SimulatorUI interface.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 */
public class SimulatorGUI extends JPanel implements SimulatorUI {

    private MouseChecker mouseChecker;
    private KeyChecker keyChecker;
    private Semaphore pointSemaphore;
    private ArrayList<Shape> shapes;
    private Point point;
    private List<Robot> robots;
    private boolean isKeyPressed;
    private ArrayList<Point> requests;
    private Scanner scanner;
    private JLabel messageLabel;
    private int currentFrame;
    private boolean hasError;

    public SimulatorGUI() {
        this.hasError = false;
        currentFrame = 0;
        scanner = new Scanner(System.in);
        isKeyPressed = false;
        mouseChecker = new MouseChecker(this);
        keyChecker = new KeyChecker(this);
        this.addKeyListener(keyChecker);
        this.addMouseListener(mouseChecker);
        shapes = new ArrayList<>();
        robots = new ArrayList<>();
        requests = new ArrayList<>();
        pointSemaphore = new Semaphore(0);
        setPreferredSize(new Dimension(1000, 1000));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(new LineBorder(Color.BLACK , 2, true));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(Color.WHITE);
        add(messageLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw brick tiles with screws
        g.setColor(new Color(217, 217, 217));
        int tileSize = 50;
        int screwSize = 10;
        int numCols = getWidth() / tileSize;
        int numRows = getHeight() / tileSize;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int tileX = col * tileSize;
                int tileY = row * tileSize;

                g.fillRect(tileX, tileY, tileSize, tileSize);

                g.setColor(Color.gray);
                g.fillOval(tileX + screwSize, tileY + screwSize, screwSize, screwSize);
                g.fillOval(tileX + tileSize - screwSize * 2, tileY + screwSize, screwSize, screwSize);
                g.fillOval(tileX + screwSize, tileY + tileSize - screwSize * 2, screwSize, screwSize);
                g.fillOval(tileX + tileSize - screwSize * 2, tileY + tileSize - screwSize * 2, screwSize, screwSize);

                g.setColor(new Color(217, 217, 217));
            }
        }
        g.setColor(Color.BLACK);
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

        g.setColor(new Color(220, 19, 19));
        // Draw the shapes
        for (Shape shape : shapes) {
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                g.fillOval((int) circle.getPoints()[0].x() - (int) circle.getRadius(), circle.getPoints()[0].y() - (int) circle.getRadius(),
                        (int) circle.getRadius() * 2, (int) circle.getRadius() * 2);
            } else if (shape instanceof Triangle) {
                Triangle tri = (Triangle) shape;
                Point[] points = tri.getPoints();
                int[] xPoints = {points[0].x(), points[1].x(), points[2].x()};
                int[] yPoints = {points[0].y(), points[1].y(), points[2].y()};
                g.fillPolygon(xPoints, yPoints, 3);
            } else if (shape.getPoints().length == 4) {
                Point[] points = shape.getPoints();
                int[] xPoints = {points[0].x(), points[1].x(), points[2].x(), points[3].x()};
                int[] yPoints = {points[0].y(), points[1].y(), points[2].y(), points[3].y()};
                g.fillPolygon(xPoints, yPoints, 4);
            }
        }
        for (int i = 0; i<robots.size(); i++) {
            Robot robot = robots.get(i);
            requests.removeIf(request -> robot.getCurrentPosition().equals(request));
            switch(i){
                case 0 -> g.setColor(new Color(21, 21, 21));
                case 1 ->  g.setColor(new Color(223, 81, 81));
                case 2 ->  g.setColor(new Color(98, 109, 85));
                case 3 ->  g.setColor(new Color(29, 50, 190));
            }
            Point pos = robot.getCurrentPosition();
            int[] xPoints = {pos.x()-7, pos.x()-7 + 15, pos.x()-7 + 15, pos.x()-7};
            int[] yPoints = {pos.y()-7, pos.y()-7, pos.y()-7 + 15, pos.y()-7 + 15};
            g.fillPolygon(xPoints, yPoints, 4);
            g.setColor(new Color(21, 21, 21));
            g.drawPolygon(xPoints, yPoints, 4);
            double energy = robot.getEnergy() / 100;
            g.setColor(Color.green);
            int[] energyxPoints = {pos.x()-7, (int) (pos.x()-7 + energy * 15), (int) (pos.x()-7 + energy * 15), pos.x()-7};
            int[] energyyPoints = {pos.y()-7 + 7, pos.y()-7 + 7, pos.y()-7 + 5, pos.y()-7 + 5};
            g.fillPolygon(energyxPoints, energyyPoints, 4);
            switch(robot.getPowerState()){
                case DELIVERING -> {
                    g.setColor(new Color(234, 123, 54));
                    int[] cratexPoints = {pos.x()-7+3,pos.x()-7 + 12, pos.x()-7 + 12 , pos.x()-7+3};
                    int[] crateyPoints = {pos.y()-7 , pos.y()-7 , pos.y()-7 -9, pos.y()-7 -9};
                    g.fillPolygon(cratexPoints, crateyPoints, 4);
                    g.setColor(new Color(21, 21, 21));
                    g.drawLine(pos.x()-7,pos.y()-7,pos.x()-7,pos.y()-7-4);
                    g.drawLine(pos.x()-7,pos.y()-7-4,pos.x()-7+5,pos.y()-7-4);
                    g.drawLine(pos.x()-7+14,pos.y()-7,pos.x()-7+14,pos.y()-7-4);
                    g.drawLine(pos.x()-7+14,pos.y()-7-4,pos.x()-7+10,pos.y()-7-4);
                }
                case RETURNING -> {
                    g.setColor(new Color(47, 236, 41));
                    int[] batteryXPoints = {pos.x()-7+3,pos.x()-7 + 10, pos.x()-7 + 10 , pos.x()-7+12,pos.x()-7+12, pos.x()-7+10,pos.x()-7+10,pos.x()-7+3};
                    int[] batteryYPoints = {pos.y()-7-2 , pos.y()-7-2 , pos.y()-7 - 4 ,pos.y()-7-4, pos.y()-7 - 6, pos.y()-7- 6, pos.y()-7-8, pos.y()-7-8};
                    g.fillPolygon(batteryXPoints,batteryYPoints, 8);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(batteryXPoints,batteryYPoints, 8);
                    g.setColor(new Color(220, 19, 19));
                    g.drawLine(pos.x()-7+13,pos.y()-7-9,pos.x()-7+2,pos.y()-7-1);
                }
                case STANDBY -> {
                    g.setColor(Color.BLACK);
                    g.drawLine(pos.x()-7+14,pos.y()-7,pos.x()-7+14,pos.y()-7-8);
                    g.setColor(new Color(220, 19, 19));
                    g.fillOval(pos.x()-7 +14 , pos.y()-7 - 8, 2, 2);
                    g.setColor(new Color(0, 0, 255));
                    for (int j = 0; j < 6; j += 2) {
                        int x = pos.x()-7 + 10 + j;
                        int y = pos.y()-7 - 12;
                        g.drawArc(x, y, 10, 10, -60, 120);
                        x = pos.x()-7 + 10 - j;
                        g.drawArc(x, y, 10, 10, 120, 120);
                    }
                }
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(robot.getPowerState().toString());
            g.drawString(robot.getPowerState().toString(),robot.getCurrentPosition().x()-textWidth/3,robot.getCurrentPosition().y()+23);
        }
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 12);
        g.setFont(font);
        for (int i = 0; i < requests.size(); i++) {
            Point request = requests.get(i);
            g.fillOval(request.x() - 5, request.y() - 5, 10, 10);
            String index = String.valueOf(i);
            int labelWidth = g.getFontMetrics().stringWidth(index);
            g.drawString(index, request.x() - labelWidth / 2, request.y() + 15);
        }
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        String text = "Frame " + currentFrame;
        g.drawString(text, 500, getHeight() - 20);
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
        messageLabel.setText("Press any key to make new request");
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
    public void displayRobotStatus(int step, List<Robot> robots) {
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
        shapes = map.getObstacles();
        messageLabel.setText("Press any key to make new request");
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
