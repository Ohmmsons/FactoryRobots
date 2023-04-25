package Simulator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
/**
 Class Trajectory Population, used to perform selection of trajectories based on fitness
 @author Jude Adam
 @version 1.0.0 20/02/2023
 Points in the path must be sequential and in the 1st quadrant
 */
public class TrajectoryPopulation {

    private ArrayList<Trajectory> individuals;
    private final ArrayList<Shape> obstacles;

    private Generator generator;

    /**
    Constructor for TrajectoryPopulation Class
     @param start starting point of trajectories
     @param end ending point of trajectories
     @param n size of population
     @param lengths lengths of trajectories
     @param generator RNG
     @param obstacles Obstacles
   */
    public TrajectoryPopulation(Point start, Point end, int n, int[] lengths, Generator generator, ArrayList<Shape> obstacles) {
        this.individuals = new ArrayList<>();
        this.generator = generator;
        this.obstacles = obstacles;
        for (int i = 0; i < n; i++) {
            ArrayList<Point> points = new ArrayList<>();
            points.add(start);
            for (int j = 0; j < lengths[i]; j++) {
                Point p = generator.generateGaussianPoint(50, points.get(0),points.get(points.size()-1));
                points.add(p);
            }
            points.add(end);
            this.individuals.add(new Trajectory(points, generator, obstacles));
        }
    }

    public TrajectoryPopulation(ArrayList<Trajectory> individuals, Generator generator, ArrayList<Shape> obstacles) {
        this.individuals = individuals;
        this.generator = generator;
        this.obstacles = obstacles;
    }

    /**
     * @return individuals of the population
     */
    public ArrayList<Trajectory> getIndividuals() {
        return individuals;
    }


    /**
     *
     * @return String representation of population
     */
    public String toString() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        Trajectory maxFitness = Collections.max(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minFitness = Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minCollision = Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.nCollisions() - s2.nCollisions()));
        double average = 0;
        for (Trajectory t : individuals) average += t.fitness();
        average /= individuals.size();
        return (df.format(maxFitness.fitness()) + " " + df.format(average) + " " + df.format(minFitness.fitness()) + " " + df.format(minCollision.getLength()) + " " + minCollision.nCollisions());
    }

    /**
     * tournament method to perform tournament selection on population
     *
     * @return winners of selection
     */
    public TrajectoryPopulation tournament() {
        ArrayList<Trajectory> winners = new ArrayList<>();
        for (int i = 0; i < individuals.size(); i++) {
            int p1 = (generator.nextInt(individuals.size()));
            int p2 = (generator.nextInt(individuals.size()));
            double f1 = individuals.get(p1).fitness();
            double f2 = individuals.get(p2).fitness();
            winners.add(f1 >= f2 ? individuals.get(p1) : individuals.get(p2));
        }
        return new TrajectoryPopulation(winners, generator, obstacles);
    }

    /**
     * roulette method to perform roulette selection on population
     * @return winners of selection
     */
    public TrajectoryPopulation roulette() {
        ArrayList<Trajectory> winners = new ArrayList<>();
        double totalFitness = individuals.stream().mapToDouble(Trajectory::fitness).sum();
        for (int i = 0; i < individuals.size(); i++) {
            double randomValue = generator.nextDouble() * totalFitness;
            for (Trajectory individual : individuals) {
                randomValue -= individual.fitness();
                if (randomValue <= 0) {
                    winners.add(individual);
                    break;
                }
            }
        }
        return new TrajectoryPopulation(winners, generator, obstacles);
    }
}
