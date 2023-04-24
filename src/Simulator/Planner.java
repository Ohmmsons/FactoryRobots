package Simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
Class Planner
@author Jude Adam
@version 1.0.0 14/04/2023
generator != null population != null
 */
public class Planner {

    private TrajectoryPopulation population;
    private final double pm;
    private final double pa;
    private final double pr;
    private final Generator generator;

    private ArrayList<Shape> obstacles;
    /**
       Constructor for SGA class
       @param pm  mutation probability
       @param pa  addition probability
       @param pr  removal probability
       @param start starting point
       @param lengths trajectory lengths
       @param generator RNG
       @param obstacles obstacles in map
        */
    public Planner(double pm, double pa, double pr, Point start, Point end, int[] lengths, Generator generator, ArrayList<Shape> obstacles) {
        this.population = new TrajectoryPopulation(start, end, lengths.length, lengths, generator, obstacles);
        this.pm = pm;
        this.pa = pa;
        this.pr = pr;
        this.generator = generator;
    }
    /**
        Simulator.Trajectory Finder method ,perfoms a kind of standard genetic algorithm to find a trajectory with no collisions from one point to another, the
        algorithm performs tournament selection, crossover , mutation, gene addition and gene removal on the population
        and replaces the old population with the new one.
        @return best trajectory found
    */
    public Trajectory findTrajectory() {
        int i = 0;
        Trajectory bestTrajectory = Collections.max(population.getIndividuals(), (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        while (bestTrajectory.nCollisions() > 0 && i<100) {
            TrajectoryPopulation offspring = population.roulette();
            ArrayList<Trajectory> tournamentWinners = offspring.getIndividuals();
            ArrayList<Trajectory> offspringIndividuals = new ArrayList<>();
            while (true) {
                int index1 = generator.nextInt(tournamentWinners.size());
                int index2 = generator.nextInt(tournamentWinners.size());
                Trajectory[] filhos = tournamentWinners.get(index1).uniformCrossover(tournamentWinners.get(index2));
                offspringIndividuals.add(filhos[0]);
                if (offspringIndividuals.size() == tournamentWinners.size()) break;
                offspringIndividuals.add(filhos[1]);
                if (offspringIndividuals.size() == tournamentWinners.size()) break;
            }
            for (Trajectory t : offspringIndividuals) {
                t.mutate(pm);
                t.addPoint(pa);
                t.removePoint(pr);
            }
            offspring = new TrajectoryPopulation(offspringIndividuals, generator, obstacles);
            bestTrajectory = Collections.max(offspringIndividuals, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
            population = offspring;
            i++;
        }
        if(i == 100) bestTrajectory = null;
        return bestTrajectory;
    }
}
