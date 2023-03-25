/*
Class Population used to generate and manage Populations of Trajectories
@author Jude Adam
@version 1.0.0 09/03/2023
 */

import java.util.ArrayList;
import java.util.Random;

public class Population {
    private Trajetoria[] trajetorias;
    private Random generator;

    /*
  Constructor for Population Class
  @param int n, int[] lengths, Random generator
   */
    public Population(int x1, int y1, int x2, int y2, int n, int[] lengths, Random generator) {
        this.trajetorias = new Trajetoria[n];
        this.generator = generator;
        for (int i = 0; i < n; i++) {
            ArrayList<Ponto> pontos = new ArrayList<>();
            pontos.add(new Ponto(x1, y1));
            for (int j = 1; j < lengths[i] + 1; j++) {
                pontos.add(j, new Ponto(generator.nextInt(100), generator.nextInt(100)));
            }
            pontos.add(new Ponto(x2, y2));
            this.trajetorias[i] = new Trajetoria(pontos);
        }
    }

    public String toString() {
        String str = "";
        for (Trajetoria t : trajetorias) {
            str += t.toString() + "\n";
        }
        return str;
    }

    /*
 tournament method to perform tournament selection on population
 @params Random generator, ArrayList<FiguraGeometrica> obstacles
 @return winners of selection
  */
    public Trajetoria[] tournament(ArrayList<FiguraGeometrica> obstaculos) {
        Trajetoria[] vencedores = new Trajetoria[trajetorias.length];
        for (int i = 0; i < trajetorias.length; i++) {
            int p1 = (generator.nextInt(trajetorias.length));
            int p2 = (generator.nextInt(trajetorias.length));
            double f1 = trajetorias[p1].getFitness(obstaculos);
            double f2 = trajetorias[p2].getFitness(obstaculos);
            vencedores[i] = f1 >= f2 ? trajetorias[p1] : trajetorias[p2];
        }
        return vencedores;
    }

    public Trajetoria[] getTrajetorias() {
        return this.trajetorias;
    }
}
