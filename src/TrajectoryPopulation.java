/*
Class Population used to generate and manage Populations of Trajectories
@author Jude Adam
@version 1.0.0 09/03/2023
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrajectoryPopulation implements Population {

    private  ArrayList<Individual> individuals;
    private final ArrayList<Shape> obstacles;

    private Random generator;
    /*
  Constructor for Population Class
  @param int n, int[] lengths, Random generator
   */
    public TrajectoryPopulation(int x1, int y1, int x2, int y2, int n, int[] lengths, Random generator, ArrayList<Shape> obstacles) {
        this.individuals = new ArrayList<>();
        this.generator = generator;
        this.obstacles = obstacles;
        for (int i = 0; i < n; i++) {
            ArrayList<Point> points = new ArrayList<>();
            points.add(new Point(x1, y1));
            for (int j = 1; j < lengths[i] + 1; j++) {
                points.add(j, new Point(generator.nextInt(100), generator.nextInt(100)));
            }
            points.add(new Point(x2, y2));
            this.individuals.add(new Trajectory(points,generator,obstacles));
        }
    }

    public TrajectoryPopulation(ArrayList<Individual> individuals, Random generator, ArrayList<Shape> obstacles){
        this.individuals = individuals;
        this.generator = generator;
        this.obstacles = obstacles;
    }

    @Override
    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    @Override
    public void setIndividuals(ArrayList<Individual> individuals) {
       this.individuals = individuals;
    }

    public String populationInfo(){
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        Trajectory maxFitness = (Trajectory) Collections.max(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minFitness = (Trajectory) Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minCollision = (Trajectory) Collections.min(individuals, (s1, s2) -> (int) Math.signum(((Trajectory)s1).nCollisions() - ((Trajectory)s2).nCollisions()));
        double average = 0;
        for (Individual t : individuals) average += t.fitness();
        average /= individuals.size();
        return (df.format(maxFitness.fitness()) + " " + df.format(average) + " " + df.format(minFitness.fitness()) + " " + df.format(minCollision.getLength()) + " " + minCollision.nCollisions());
    }

    public void sortByFitness(){
        individuals.sort((s1, s2) -> (int) Math.signum(s2.fitness() - s1.fitness()));
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Individual t : individuals) {
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
        ArrayList<Individual> vencedores = new ArrayList<>();
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
