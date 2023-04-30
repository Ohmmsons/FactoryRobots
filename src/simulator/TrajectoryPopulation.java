package simulator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Class TrajectoryPopulation, used to perform selection of trajectories based on fitness.
 *
 * @author Jude Adam
 * @version 1.0.0 20/02/2023
 * @inv Individuals in the population must not be null and have a positive size.
 * @inv Trajectories in the population must be in the 1st quadrant.
 */
public class TrajectoryPopulation {

    private final Random rng;
    private final ArrayList<Trajectory> individuals;
    private final ArrayList<Shape> obstacles;

    private final PointGenerator generator;

    /**
     * Constructor for TrajectoryPopulation Class.
     *
     * @param start     starting point of trajectories
     * @param end       ending point of trajectories
     * @param n         size of population
     * @param lengths   lengths of trajectories
     * @param generator Point generator
     * @param obstacles Obstacles
     * @param rng       Random number generator
     * @pre start and end points must be valid Point objects.
     * @pre n must be greater than 0.
     * @pre lengths must be a valid array of integers with the same length as n.
     * @post Creates a population of Trajectory objects with the specified properties.
     */
    public TrajectoryPopulation(Point start, Point end, int n, int[] lengths, PointGenerator generator, ArrayList<Shape> obstacles, Random rng) {
        this.individuals = new ArrayList<>();
        this.generator = generator;
        this.rng = rng;
        this.obstacles = obstacles;
        for (int i = 0; i < n; i++) {
            ArrayList<Point> points = new ArrayList<>();
            points.add(start);
            for (int j = 0; j < lengths[i]; j++) {
                Point p = generator.generateGaussianPoint(50, points.get(0), points.get(points.size() - 1));
                points.add(p);
            }
            points.add(end);
            this.individuals.add(new Trajectory(points, generator, obstacles, rng));
        }
    }

    /**
     * Constructor for TrajectoryPopulation Class with a given list of Trajectory objects.
     *
     * @param individuals List of Trajectory objects
     * @param generator   RNG
     * @param obstacles   Obstacles
     * @param rng         Random number generator
     * @pre individuals must be a valid list of Trajectory objects.
     * @post Creates a population of Trajectory objects with the specified properties.
     */
    public TrajectoryPopulation(ArrayList<Trajectory> individuals, PointGenerator generator, ArrayList<Shape> obstacles, Random rng) {
        this.individuals = individuals;
        this.generator = generator;
        this.obstacles = obstacles;
        this.rng = rng;
    }

    /**
     * @return individuals of the population
     */
    public ArrayList<Trajectory> getIndividuals() {
        return individuals;
    }

    /**
     * @return String representation of the population
     * @post Returns a string representation of the population with maximum fitness, average fitness, minimum fitness, minimum collision trajectory length, and number of collisions.
     */
    public String toString() {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        Trajectory maxFitness = Collections.max(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minFitness = Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajectory minCollision = Collections.min(individuals, (s1, s2) -> (int) Math.signum(s1.getCollisionCount()) - s2.getCollisionCount());
        double average = 0;
        for (Trajectory t : individuals) average += t.fitness();
        average /= individuals.size();
        return (df.format(maxFitness.fitness()) + " " + df.format(average) + " " + df.format(minFitness.fitness()) + " " + df.format(minCollision.getLength()) + " " + minCollision.calculateCollisions());
    }


    /**
     * Rank-based selection.
     *
     * @return winners of selection
     * @pre The population must have a positive size and valid Trajectory objects.
     * @post Returns a new TrajectoryPopulation object containing the winners of the rank-based selection.
     */
    public TrajectoryPopulation rankBasedSelection() {
        ArrayList<Trajectory> winners = new ArrayList<>();
        ArrayList<Trajectory> sortedIndividuals = new ArrayList<>(individuals);
        sortedIndividuals.sort(Comparator.comparingDouble(Trajectory::fitness));

        double totalFitness = 0;
        for (int i = 0; i < sortedIndividuals.size(); i++) {
            totalFitness += (i + 1);
        }

        for (int i = 0; i < individuals.size(); i++) {
            double randomValue = rng.nextDouble() * totalFitness;
            int index = 0;
            while (randomValue > 0) {
                randomValue -= (index + 1);
                index++;
            }
            winners.add(sortedIndividuals.get(index - 1));
        }
        return new TrajectoryPopulation(winners, generator, obstacles, rng);
    }
}
