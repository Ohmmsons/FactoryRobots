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
    private ArrayList<Individual> trajetorias;
    private Random generator;

    private ArrayList<FiguraGeometrica> obstacles;
    /*
  Constructor for Population Class
  @param int n, int[] lengths, Random generator
   */
    public TrajectoryPopulation(int x1, int y1, int x2, int y2, int n, int[] lengths, Random generator, ArrayList<FiguraGeometrica> obstacles) {
        this.trajetorias = new ArrayList<>();
        this.generator = generator;
        this.obstacles = obstacles;
        for (int i = 0; i < n; i++) {
            ArrayList<Ponto> pontos = new ArrayList<>();
            pontos.add(new Ponto(x1, y1));
            for (int j = 1; j < lengths[i] + 1; j++) {
                pontos.add(j, new Ponto(generator.nextInt(100), generator.nextInt(100)));
            }
            pontos.add(new Ponto(x2, y2));
            this.trajetorias.add(new Trajetoria(pontos,generator,obstacles));
        }
    }

    public TrajectoryPopulation(ArrayList<Individual> trajetorias, Random generator, ArrayList<FiguraGeometrica> obstacles){
        this.trajetorias = trajetorias;
        this.generator = generator;
        this.obstacles = obstacles;
    }

    public String populationInfo(){
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        Trajetoria maxFitness = (Trajetoria) Collections.max(trajetorias, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajetoria minFitness = (Trajetoria) Collections.min(trajetorias, (s1, s2) -> (int) Math.signum(s1.fitness() - s2.fitness()));
        Trajetoria minCollision = (Trajetoria) Collections.min(trajetorias, (s1, s2) -> (int) Math.signum(((Trajetoria)s1).nCollisions() - ((Trajetoria)s2).nCollisions()));
        double average = 0;
        for (Individual t : trajetorias) average += t.fitness();
        average /= trajetorias.size();
        return (df.format(maxFitness.fitness()) + " " + df.format(average) + " " + df.format(minFitness.fitness()) + " " + df.format(minCollision.getLength()) + " " + minCollision.nCollisions());
    }

    public void sortByFitness(){
        trajetorias.sort((s1, s2) -> (int) Math.signum(s2.fitness() - s1.fitness()));
    }
//    public void sortByNCollisions(ArrayList<FiguraGeometrica> obstaculos){
//        Collections.sort(trajetorias,(s1, s2) -> (int) Math.signum(s1.nCollisions(obstaculos)-s2.nCollisions(obstaculos)));
//    }
    public String toString() {
        String str = "";
        for (Individual t : trajetorias) {
            str += t.toString() + "\n";
        }
        return str;
    }

    /*
 tournament method to perform tournament selection on population
 @params Random generator, ArrayList<FiguraGeometrica> obstacles
 @return winners of selection
  */
    public TrajectoryPopulation tournament() {
        ArrayList<Individual> vencedores = new ArrayList<>();
        for (int i = 0; i < trajetorias.size(); i++) {
            int p1 = (generator.nextInt(trajetorias.size()));
            int p2 = (generator.nextInt(trajetorias.size()));
            double f1 = trajetorias.get(p1).fitness();
            double f2 = trajetorias.get(p2).fitness();
            vencedores.add((Trajetoria) (f1 >= f2 ? trajetorias.get(p1) : trajetorias.get(p2)));
        }
        return new TrajectoryPopulation(vencedores,generator,obstacles);
    }

    public ArrayList<Individual> getIndividuals() {
        return this.trajetorias;
    }
}
