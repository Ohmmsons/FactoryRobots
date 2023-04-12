import java.util.ArrayList;
import java.util.Random;

/*
Class Shape
@author Jude Adam
@version 1.0.0 12/04/2023
@inv generator != null population != null
 */
public class SGA {

    private Population population;
    private final double pm;
    private final double pa;
    private final double pr;
    private final Random generator;

    /*
       Constructor for SGA class
       @params double pm, double pa, double pr, Random generator, Population population
        */
    public SGA(double pm, double pa, double pr, Random generator, Population population) {
        this.population = population;
        this.pm = pm;
        this.pa = pa;
        this.pr = pr;
        this.generator = generator;
    }

    /*
        sga method , standard genetic algortihm on the population for g generations, the
        algorithm performs tournament selection, crossover , mutation, gene addition and gene removal on the population
        and replaces the old population with the new one. It also prints the information of each generation in the format:
        "i: maxf avgf minf etc." where i is the generation number maxf is the max fitness avg fitness is the average fitness
        minf is the minimum fitness and etc. is any additional information about the population depending on the implementation.
        @params int g
        @return Population offspring after g generations
    */
    public Population sga(int g) {
        for (int i = 0; i < g; i++) {
            Population offspring = population.tournament();
            ArrayList<Individual> tournamentWinners = offspring.getIndividuals();
            ArrayList<Individual> offspringIndividuals = new ArrayList<>();
            while (true) {
                int index1 = generator.nextInt(tournamentWinners.size());
                int index2 = generator.nextInt(tournamentWinners.size());
                Individual[] filhos = tournamentWinners.get(index1).crossover(tournamentWinners.get(index2));
                offspringIndividuals.add(filhos[0]);
                if (offspringIndividuals.size() == tournamentWinners.size()) break;
                offspringIndividuals.add(filhos[1]);
                if (offspringIndividuals.size() == tournamentWinners.size()) break;
            }
            for (Individual ind : offspringIndividuals) {
                ind.mutate(pm);
            }
            for (Individual ind : offspringIndividuals) {
                ind.addGene(pa);
            }
            for (Individual ind : offspringIndividuals) {
                ind.removeGene(pr);
            }
            offspring.setIndividuals(offspringIndividuals);
            population = offspring;
            System.out.println(i + ": " + population.populationInfo());
        }
        return population;
    }
}
