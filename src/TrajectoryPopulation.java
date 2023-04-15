/*
Class Population used to generate and manage Populations of Trajectories
@author Jude Adam
@version 1.0.0 09/03/2023
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TrajectoryPopulation {

    private  ArrayList<Trajectory> individuals;
    private final ArrayList<Shape> obstacles;

    private Random generator;
    /*
  Constructor for TrajectoryPopulation Class
  @param int n, int[] lengths, Random generator
   */
    public TrajectoryPopulation(Point start, Point end, int n, int[] lengths, Random generator, ArrayList<Shape> obstacles) {
        this.individuals = new ArrayList<>();
        this.generator = generator;
        this.obstacles = obstacles;
        for (int i = 0; i < n; i++) {
            ArrayList<Point> points = new ArrayList<>();
            points.add(start);
            for (int j = 1; j < lengths[i] + 1; j++) {
                points.add(j, new Point(generator.nextInt(100), generator.nextInt(100)));
            }
            points.add(end);
            this.individuals.add(new Trajectory(points,generator,obstacles));
        }
    }

    public TrajectoryPopulation(ArrayList<Trajectory> individuals, Random generator, ArrayList<Shape> obstacles){
        this.individuals = individuals;
        this.generator = generator;
        this.obstacles = obstacles;
    }

    public ArrayList<Trajectory> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Trajectory> individuals) {
       this.individuals = individuals;
    }

    public String populationInfo(){
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        Trajectory maxFitness = Collections.max(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minFitness = Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minCollision =  Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.nCollisions() - s2.nCollisions()));
        double average = 0;
        for (Trajectory t : individuals) average += t.fitness();
        average /= individuals.size();
        return (df.format(maxFitness.fitness()) + " " + df.format(average) + " " + df.format(minFitness.fitness()) + " " + df.format(minCollision.getLength()) + " " + minCollision.nCollisions());
    }

    public void sortByFitness(){
        individuals.sort((s1, s2) -> (int) Math.signum(s2.fitness() - s1.fitness()));
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Trajectory t : individuals) {
            str.append(t.toString()).append("\n");
        }
        return str.toString();
    }

    /*
 tournament method to perform tournament selection on population
 @params Random generator, ArrayList<Shape> obstacles
 @return winners of selection
  */
    public TrajectoryPopulation tournament() {
        ArrayList<Trajectory> vencedores = new ArrayList<>();
        for (int i = 0; i < individuals.size(); i++) {
            int p1 = (generator.nextInt(individuals.size()));
            int p2 = (generator.nextInt(individuals.size()));
            double f1 = individuals.get(p1).fitness();
            double f2 = individuals.get(p2).fitness();
            vencedores.add(f1 >= f2 ? individuals.get(p1) : individuals.get(p2));
        }
        return new TrajectoryPopulation(vencedores,generator,obstacles);
    }
}
