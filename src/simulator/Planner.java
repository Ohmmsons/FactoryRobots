package simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Class Planner
 *
 * @author Jude Adam
 * @version 1.0.0 14/04/2023
 * @inv generator != null
 * @inv population != null
 */
public class Planner {

    private final Random rng;
    private TrajectoryPopulation population;
    private final double pm;
    private final double pa;
    private final double pr;
    private final PointGenerator generator;

    private final ArrayList<Shape> obstacles;

    /**
     * Constructor for Planner class
     *
     * @param pm        mutation probability
     * @param pa        addition probability
     * @param pr        removal probability
     * @param start     starting point
     * @param end       ending point
     * @param lengths   trajectory lengths
     * @param generator Point generator
     * @param obstacles obstacles in map
     * @param rng       RNG
     * @pre start != null && end != null
     * @pre lengths != null && lengths.length > 0
     * @pre generator != null && obstacles != null && rng != null
     * @post population != null && this.generator != null && this.obstacles != null && this.rng != null
     */
    public Planner(double pm, double pa, double pr, Point start, Point end, int[] lengths, PointGenerator generator, ArrayList<Shape> obstacles, Random rng) {
        this.population = new TrajectoryPopulation(start, end, lengths.length, lengths, generator, obstacles, rng);
        this.pm = pm;
        this.pa = pa;
        this.pr = pr;
        this.generator = generator;
        this.obstacles = obstacles;
        this.rng = rng;
    }

    /**
     * Trajectory Finder method, performs a kind of standard genetic algorithm to find a trajectory with no collisions from one point to another. The
     * algorithm performs roulette selection, uniform crossover, mutation, gene addition, and gene removal on the population
     * and replaces the old population with the new one.
     *
     * @return best trajectory found
     * @pre population != null
     * @post result == null || result.calculateCollisions() == 0
     */
    public Trajectory findTrajectory() {
        int maxGenerations = 100;
        int numElites = (int) (0.1 * population.getIndividuals().size()); // 10% elites
        Trajectory bestTrajectory = getBestTrajectory(population);

        // Evolve the population to find the best trajectory
        for (int gen = 0; gen < maxGenerations && bestTrajectory.calculateCollisions() > 0; gen++) {
            TrajectoryPopulation offspring = population.rankBasedSelection();
            ArrayList<Trajectory> offspringIndividuals = generateOffspring(offspring);

            // Apply elitism
            offspringIndividuals.sort(Comparator.comparingDouble(Trajectory::fitness).reversed());
            ArrayList<Trajectory> elites = new ArrayList<>(population.getIndividuals());
            elites.sort(Comparator.comparingDouble(Trajectory::fitness).reversed());
            for (int i = 0; i < numElites; i++) {
                offspringIndividuals.set(i, elites.get(i));
            }
            applyMutations(offspringIndividuals);
            offspring = new TrajectoryPopulation(offspringIndividuals, generator, obstacles, rng);
            bestTrajectory = getBestTrajectory(offspring);
            population = offspring;
        }
        return bestTrajectory.calculateCollisions() > 0 ? null : bestTrajectory;
    }

    /**
     * Gets the best trajectory based on the highest fitness value from a given population.
     *
     * @param population the population of trajectories
     * @return the trajectory with the highest fitness value
     * @pre population != null && population.getIndividuals().size() > 0
     * @post result != null
     */
    private Trajectory getBestTrajectory(TrajectoryPopulation population) {
        return Collections.max(population.getIndividuals(), Comparator.comparingDouble(Trajectory::fitness));
    }

    /**
     * Generates a new offspring population by performing crossover on the tournament winners.
     *
     * @param offspring the offspring population containing tournament winners
     * @return a list of offspring individuals created by crossover
     * @pre offspring != null && offspring.getIndividuals().size() > 0
     * @post result != null && result.size() == offspring.getIndividuals().size()
     */
    private ArrayList<Trajectory> generateOffspring(TrajectoryPopulation offspring) {
        ArrayList<Trajectory> tournamentWinners = offspring.getIndividuals();
        ArrayList<Trajectory> offspringIndividuals = new ArrayList<>();

        // Perform crossover on tournament winners to generate offspring
        while (offspringIndividuals.size() < tournamentWinners.size()) {
            int index1 = rng.nextInt(tournamentWinners.size());
            int index2 = rng.nextInt(tournamentWinners.size());
            Trajectory[] children = tournamentWinners.get(index1).onePointCrossover(tournamentWinners.get(index2));
            offspringIndividuals.add(children[0]);
            offspringIndividuals.add(children[1]);
        }
        return offspringIndividuals;
    }

    /**
     * Applies mutations to the given list of offspring individuals.
     *
     * @param offspringIndividuals the list of offspring individuals to mutate
     * @pre offspringIndividuals != null && offspringIndividuals.size() > 0
     * @post All offspring individuals have had mutation, addition, and removal operations applied
     */
    private void applyMutations(ArrayList<Trajectory> offspringIndividuals) {
        for (Trajectory t : offspringIndividuals) {
            t.mutate(pm);
            t.addPoint(pa);
            t.removePoint(pr);
        }
    }
}

