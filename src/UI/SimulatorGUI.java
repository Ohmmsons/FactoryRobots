package UI;

import Simulator.*;
import Simulator.Point;
import Simulator.Robot;
import Simulator.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * A graphical user interface implementation of the UI.SimulatorUI interface.
 *  @author Jude Adam
 *  @version 1.0.0 20/04/2023
 */
public class SimulatorGUI extends JPanel implements SimulatorUI {

    private MouseChecker mouseChecker;
    private KeyChecker keyChecker;
    private Semaphore pointSemaphore;
    private ArrayList<Shape> shapes;
    private Point point;
    private List<Robot> robots;
    private boolean isKeyPressed;
    private Scanner scanner;
    private JLabel messageLabel;
    private int currentFrame;

    public SimulatorGUI(){
        currentFrame = 0;
        scanner = new Scanner(System.in);
        isKeyPressed = false;
        mouseChecker = new MouseChecker(this);
        keyChecker = new KeyChecker(this);
        this.addKeyListener(keyChecker);
        this.addMouseListener(mouseChecker);
        shapes = new ArrayList<>();
        robots = new ArrayList<>();
        pointSemaphore = new Semaphore(0);
        setPreferredSize(new Dimension(1000, 1000));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED); // sets the text color to red
        add(messageLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the grid
        for (int i = 0; i <= 1000; i += 10) {
            g.drawLine(i, 0, i, 1000); // Vertical lines
            g.drawLine(0, i, 1000, i); // Horizontal lines
        }


        // Draw the shapes
        for (Shape shape : shapes) {
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                g.setColor(Color.BLUE);
                g.fillOval((int)circle.getPoints()[0].x() - (int)circle.getRadius(), circle.getPoints()[0].y() - (int)circle.getRadius(),
                        (int)circle.getRadius() * 2, (int)circle.getRadius() * 2);
            } else if (shape instanceof Triangle) {
                Triangle tri = (Triangle) shape;
                g.setColor(Color.GREEN);
                Point[] points = tri.getPoints();
                int[] xPoints = { points[0].x(), points[1].x(), points[2].x() };
                int[] yPoints = { points[0].y(),points[1].y(), points[2].y() };
                g.fillPolygon(xPoints, yPoints, 3);
            }
            else if (shape.getPoints().length==4) {
                g.setColor(Color.RED);
                Point[] points = shape.getPoints();
                int[] xPoints = { points[0].x(), points[2].x(), points[1].x(),points[3].x() };
                int[] yPoints = { points[0].y(),points[2].y(), points[1].y(),points[3].y() };
                g.fillPolygon(xPoints, yPoints, 4);
            }

            for (Robot robot: robots){
                g.setColor(Color.BLACK);
                Point pos = robot.getCurrentPosition();
                int[] xPoints = { pos.x(), pos.x()+15, pos.x()+15,pos.x() };
                int[] yPoints = { pos.y(),  pos.y(),pos.y()+15,pos.y()+15 };
                g.fillPolygon(xPoints, yPoints, 4);
                double energy = robot.getEnergy()/100;
                g.setColor(Color.YELLOW);
                int[] energyxPoints = { pos.x(), (int) (pos.x()+ energy*15), (int) (pos.x()+energy*15),pos.x() };
                int[] energyyPoints = { pos.y()+7,  pos.y()+7,pos.y()+5,pos.y()+5 };
                g.fillPolygon(energyxPoints, energyyPoints, 4);
            }

        }
    }


    /**
     * Returns the number of obstacles on the map, obtained through the GUI.
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
            messageLabel.setText( "Click where you want your next delivery" );
            pointSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageLabel.setText("Press any key to make new request");
        // Return the point that was clicked
        return point;
    }

    /**
     * Returns true if the GUI is asking for a new Simulator.Point, false otherwise.
     * @return true if the GUI is asking for a new Simulator.Point, false otherwise
     */
    @Override
    public boolean isAskingForNewPoint() {
        return isKeyPressed;
    }

    /**
     * Displays the status of the robots on the GUI.
     * @param step the current step of the simulation
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
    }

    @Override
    public void sendMapInformation(DeliveryMap map) {
        shapes = map.getObstacles();
        messageLabel.setText("Press any key to make new request");
        repaint();
    }






    // Inner class to handle mouse clicks
    private static class MouseChecker implements MouseListener {
        private SimulatorGUI parent;

        public MouseChecker(SimulatorGUI parent){
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

        public KeyChecker(SimulatorGUI parent){
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
        public synchronized void keyTyped(KeyEvent e) {}

    }
}
