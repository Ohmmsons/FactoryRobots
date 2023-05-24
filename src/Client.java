import simulator.Simulator;
import ui.SimulatorCLI;
import ui.SimulatorGUI;

import javax.swing.*;
import java.util.Scanner;

public class Client {
    public  static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int mode = -1;
        do {
            System.out.println("Choose your UI mode");
            System.out.println("0 - CLI");
            System.out.println("1 - GUI");
            if(sc.hasNextInt())
                mode = sc.nextInt();
            else
                sc.next();
        } while(mode != 0 && mode != 1);
        Simulator simulator;
        if(mode == 0)
            simulator = new Simulator(new SimulatorCLI());
        else {
            SimulatorGUI gui = new SimulatorGUI();
            simulator = new Simulator(gui);
            JFrame frame = new JFrame("simulator");
            frame.add(gui);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            System.out.println("Opened Java Swing Window");
        }

        try {
            simulator.  startSimulation();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}