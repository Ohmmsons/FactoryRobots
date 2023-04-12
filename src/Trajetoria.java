import java.util.ArrayList;
import java.util.Random;

/*
Class Trajetoria used to make paths
@author Jude Adam
@version 1.0.0 20/02/2023
@inv Points in the path must be sequential and in the 1st quadrant
 */
public class Trajetoria implements Individual{
    private final ArrayList<Ponto> points;
    private double length;
    public Random generator;

    private ArrayList<FiguraGeometrica> obstacles;
    /*
    Constructor for Trajetoria class
    @params SegmentoReta[] segments
     */
    Trajetoria(ArrayList<Ponto> pontos, Random generator, ArrayList<FiguraGeometrica> obstacles) {
        this.obstacles = obstacles;
        this.generator = generator;
        ArrayList<Ponto> points = new ArrayList<>();
        length = 0;
        int n = pontos.size();
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (!points.contains(pontos.get(i))) {
                points.add(pontos.get(i));
                if(i>0)length+= points.get(++j).dist(points.get(j-1));
            }
        }
//        if(!isSequential(segments)){System.out.println("Trajetoria:vi"); System.exit(0);}
        this.points = points;
    }

//    /*
//   isSequential method to check if an array of segments form a path with sequential points
//   @params SegmentoReta[] segmentos
//   @return True if a sequential path is formed
//    */
//    private boolean isSequential(SegmentoReta[] segmentos){
//        for(SegmentoReta segmento : segmentos){
//            if((segmento.getP1().getX()>segmento.getP2().getX())&&(segmento.getP1().getY()>segmento.getP2().getY()))
//                return false;
//        }
//        return true;
//    }

    public String toString() {
        if (points.isEmpty()) return "[]";
        StringBuilder str = new StringBuilder("[");
        for (Ponto ponto : points) {
            str.append("(" + (int) ponto.getX() + ";" + (int) ponto.getY() + ") ");
        }
        str.deleteCharAt(str.length() - 1);
        str.append("]");
        return new String(str);
    }

    /*
   nCollisions method to calculate how many collision there are between the path and a given array of rectangles
   @params ArrayList<FiguraGeometrica> obstacles
   @return Number of collisions 
    */
    public int nCollisions() {
        int result = 0;
        int n = points.size();
        for (FiguraGeometrica shape : obstacles) {
            for (int i = 0; i<n-1; i++)
                if (shape.isIntercepted(new SegmentoReta(points.get(i),points.get(i+1)))){
                    result++;
                    break;
                }
        }
        return result;
    }

    /*
  getFitness method to the fitness function of a trajectory
  @return 1/(comprimento_total(T) + exp(T.interseção(obstáculos))
   */
    public double fitness() {
        return 1.00 / (length + Math.exp(nCollisions()));
    }

    /*
      onePointCrossover method to perform one point crossover between two trajectories and generate 2 offspring
      @params Trajetoria other, Random generator
      @return Trajetoria[] offspring
       */
    public Trajetoria[] crossover(Individual other) {
        Trajetoria otherT = (Trajetoria) other;
        ArrayList<Ponto> otherPoints = otherT.getPoints();
        int point1 = generator.nextInt(points.size() - 1) + 1;
        int point2 = generator.nextInt(otherPoints.size() - 1) + 1;
        ArrayList<Ponto> child1 = new ArrayList<>();
        ArrayList<Ponto> child2 = new ArrayList<>();
        for (int i = 0; i < point1; i++) child1.add(points.get(i));
        for (int i = point2; i < otherPoints.size(); i++) child1.add(otherPoints.get(i));
        for (int i = 0; i < point2; i++) child2.add(otherPoints.get(i));
        for (int i = point1; i < points.size(); i++) child2.add(points.get(i));
        return new Trajetoria[]{new Trajetoria(child1,generator,obstacles), new Trajetoria(child2,generator,obstacles)};
    }

    public void mutate(double pm) {
        if (generator.nextDouble() < pm) {
            if (points.size() > 2) {
                int i = generator.nextInt(points.size() - 2) + 1;
                Ponto p = new Ponto(generator.nextInt(100), generator.nextInt(100));
                if (!points.contains(p)) {
                    length-=(points.get(i-1).dist(points.get(i)) + points.get(i).dist(points.get(i+1)));
                    points.set(i, p);
                    length+=(points.get(i-1).dist(p) + p.dist(points.get(i+1)));
                }
            }
        }
    }

    public void addGene(double pa) {
        if(generator.nextDouble() < pa) {
            int i = 0;
            if (points.size() > 2) i = generator.nextInt(points.size() - 2) + 1;
            Ponto p = new Ponto(generator.nextInt(100), generator.nextInt(100));
            if (!points.contains(p)){
                length-=(points.get(i).dist(points.get(i+1)));
                points.add(i + 1, p);
                length+=(points.get(i).dist(p) + p.dist(points.get(i+2)));
            }
        }
    }

    public void removeGene(double pr) {
        if(generator.nextDouble()<pr) {
            int i;
            if (points.size() > 2) {
                i = generator.nextInt(points.size() - 2) + 1;
                length-=(points.get(i-1).dist(points.get(i)) + points.get(i).dist(points.get(i+1)));
                points.remove(i);
                length+=points.get(i-1).dist(points.get(i));
            }
        }
    }

    public ArrayList<Ponto> getPoints() {
        return points;
    }

    public double getLength() {
        return length;
    }
}
