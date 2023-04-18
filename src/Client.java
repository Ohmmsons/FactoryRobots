import java.io.IOException;
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
        else
         simulator = new Simulator(new SimulatorGUI());
        try {
            simulator.startSimulation();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
