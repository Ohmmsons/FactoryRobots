import java.util.ArrayList;
import java.util.Random;

public class SGA {

    private Population population;
    private double pm;
    private double pa;
    private double pr;
    private Random generator;

    public SGA(double pm, double pa, double pr, Random generator, Population population) {
        this.population = population;
        this.pm = pm;
        this.pa = pa;
        this.pr = pr;
        this.generator = generator;
    }

    public Population sga(int g) {
        for(int i = 0; i<g; i++) {
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
            System.out.println(i+": " + population.populationInfo());
        }
        return population;
    }
}
