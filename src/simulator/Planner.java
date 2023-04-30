package simulator;

import java.util.*;

/**
 * Class Planner, used to find trajectories without collisions using a Genetic algorithm
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
     * Constructor for Planner class. This constructor is private and can only be accessed by the Builder.
     *
     * @param builder The Builder object containing all required fields for Planner construction.
     * @pre builder != null
     * @post this.generator != null &amp;&amp; this.obstacles != null &amp;&amp; this.rng != null
     */
    // Make the Planner constructor private
    private Planner(Builder builder) {
        this.pm = builder.pm;
        this.pa = builder.pa;
        this.pr = builder.pr;
        this.generator = builder.generator;
        this.obstacles = builder.obstacles;
        this.rng = builder.rng;
        this.population = new TrajectoryPopulation(builder.start, builder.end, builder.lengths.length, builder.lengths, generator, obstacles, rng);
    }

    /**
     *The Builder class is used to construct instances of the Planner class with customizable parameters.
     *It contains methods to set the values of the parameters and a build() method to create a new Planner instance.
     *@author Jude Adam
     *@version 1.0.0 30/04/2023
     */
    public static class Builder {
        // Declare Builder class fields, corresponding to Planner class fields
        private double pm = 0.045; //default is 0.045
        private double pa = 0.1; // default is 0.1
        private double pr = 0.1;// default is 0.1
        private Point start;
        private Point end;
        private int[] lengths;
        private PointGenerator generator;
        private ArrayList<Shape> obstacles;
        private Random rng;

        /**
         Setter method for the mutation probability parameter.
         @param pm The mutation probability value.
         @return This Builder instance with the specified mutation probability value.
         @pre pm &ge; 0
         @post The mutation probability parameter is set to the specified value.
         */
        public Builder pm(double pm) {
            this.pm = pm;
            return this;
        }

        /**
         Setter method for the point addition probability parameter.
         @param pa The point addition probability value.
         @return This Builder instance with the specified point addition probability value.
         @pre pa &ge; 0
         @post The point addition probability parameter is set to the specified value.
         */
        public Builder pa(double pa) {
            this.pa = pa;
            return this;
        }

        /**
         Setter method for the point removal probability parameter.
         @param pr The point removal probability value.
         @return This Builder instance with the specified point removal probability value.
         @pre pr &ge; 0
         @post The point removal probability parameter is set to the specified value.
         */
        public Builder pr(double pr) {
            this.pr = pr;
            return this;
        }

        /**
         Setter method for the starting point parameter.
         @param start The starting point value.
         @return This Builder instance with the specified starting point value.
         @pre start != null
         @post The starting point parameter is set to the specified value.
         */
        public Builder start(Point start) {
            this.start = start;
            return this;
        }

        /**
         Setter method for the ending point parameter.
         @param end The ending point value.
         @return This Builder instance with the specified ending point value.
         @pre end != null
         @post The ending point parameter is set to the specified value.
         */
        public Builder end(Point end) {
            this.end = end;
            return this;
        }

        /**
         Setter method for the trajectory lengths parameter.
         @param lengths The trajectory lengths value.
         @return This Builder instance with the specified trajectory lengths value.
         @pre lengths != null
         @post The trajectory lengths parameter is set to the specified value.
         */
        public Builder lengths(int[] lengths) {
            this.lengths = lengths;
            return this;
        }

        /**
         Setter method for the point generator parameter.
         @param generator The point generator value.
         @return This Builder instance with the specified point generator value.
         @pre generator != null
         @post The point generator parameter is set to the specified value.
         */
        public Builder generator(PointGenerator generator) {
            this.generator = generator;
            return this;
        }

        /**
         * Setter for the obstacles field.
         *
         * @param obstacles The obstacles to set.
         * @return This Builder instance with the obstacles field set to the provided value.
         * @pre obstacles != null
         * @post This Builder instance has the obstacles field set to the provided value.
         */
        public Builder obstacles(ArrayList<Shape> obstacles) {
            this.obstacles = obstacles;
            return this;
        }

        /**
         * Setter for the rng field.
         *
         * @param rng The random number generator to set.
         * @return This Builder instance with the rng field set to the provided value.
         * @pre rng != null
         * @post This Builder instance has the rng field set to the provided value.
         */
        public Builder rng(Random rng) {
            this.rng = rng;
            return this;
        }

        /**
         * Builds and returns a new Planner instance using the values stored in this Builder instance.
         *
         * @return A new Planner instance.
         * @post A new Planner instance is returned using the values stored in this Builder instance.
         */
        public Planner build() {
            return new Planner(this);
        }
    }

    /**
     * Trajectory Finder method, performs a kind of standard genetic algorithm to find a trajectory with no collisions from one point to another. The
     * algorithm performs rank based selection, elitism, one point crossover, mutation, point addition, and point removal on the population
     * and replaces the old population with the new one.
     *
     * @return best trajectory found
     * @pre population != null
     * @post result == null || result.calculateCollisions() == 0
     */
    public Trajectory findTrajectory() {
        int maxGenerations = 150;
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
     * @pre population != null &amp;&amp; population.getIndividuals().size() > 0
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
     * @pre offspring != null &amp;&amp; offspring.getIndividuals().size() > 0
     * @post result != null &amp;&amp; result.size() == offspring.getIndividuals().size()
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
     * @pre offspringIndividuals != null &amp;&amp; offspringIndividuals.size() > 0
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

