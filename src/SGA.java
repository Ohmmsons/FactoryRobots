import java.util.ArrayList;
import java.util.Random;

public class SGA {

    private TrajectoryPopulation population;
    private double pm;
    private double pa;
    private double pr;
    private ArrayList<FiguraGeometrica> obstaculos;

    private Random generator;

    public SGA(double pm, double pa, double pr, int x1, int y1, int x2, int y2, int n, int[] lengths, Random generator, ArrayList<FiguraGeometrica> obstaculos) {
        this.population = new TrajectoryPopulation(x1, y1, x2, y2, n, lengths, generator,obstaculos);
        this.pm = pm;
        this.pa = pa;
        this.pr = pr;
        this.generator = generator;
        this.obstaculos = obstaculos;
    }

    public TrajectoryPopulation sga(int g) {
        for(int i = 0; i<g; i++) {
            population = population.tournament();
            ArrayList<Individual> trajetorias = population.getIndividuals();
            ArrayList<Individual> trajetoriasFilho = new ArrayList<>();
            while (true) {
                int index1 = generator.nextInt(trajetorias.size());
                int index2 = generator.nextInt(trajetorias.size());
                Individual[] filhos = trajetorias.get(index1).crossover(trajetorias.get(index2));
                trajetoriasFilho.add(filhos[0]);
                if (trajetoriasFilho.size() == trajetorias.size()) break;
                trajetoriasFilho.add(filhos[1]);
                if (trajetoriasFilho.size() == trajetorias.size()) break;
            }
            for (Individual t : trajetoriasFilho) {
                t.mutate(pm);
            }
            for (Individual t : trajetoriasFilho) {
                t.addGene(pa);
            }
            for (Individual t : trajetoriasFilho) {
                t.removeGene(pr);
            }
            population = new TrajectoryPopulation(trajetoriasFilho, generator, obstaculos);
            System.out.println(i+": " + population.populationInfo());
        }
        return population;
    }
}
