import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


/*
Class Trajetoria used to make paths
@author Jude Adam
@version 1.0.0 20/02/2023
@inv Points in the path must be sequential and in the 1st quadrant
 */
public class Trajetoria {
    private final ArrayList<SegmentoReta> segments;
    private final ArrayList<Ponto> points;
    private double length;

    /*
    Constructor for Trajetoria class
    @params SegmentoReta[] segments
     */
    Trajetoria(ArrayList<Ponto> pontos) {
        ArrayList<SegmentoReta> segments = new ArrayList<>();
        ArrayList<Ponto> points = new ArrayList<>();
        length = 0;
        int n = pontos.size();
        for (int i = 0; i < n; i++) {
            if (!points.contains(pontos.get(i))) {
                points.add(pontos.get(i));
                if (i < (n - 1)) {
                    SegmentoReta seg = new SegmentoReta(pontos.get(i), pontos.get(i + 1));
                    segments.add(seg);
                    length += seg.length();
                }
            }
        }
//        if(!isSequential(segments)){System.out.println("Trajetoria:vi"); System.exit(0);}
        this.segments = segments;
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
    public int nCollisions(ArrayList<FiguraGeometrica> obstacles) {
        int result = 0;
        for (FiguraGeometrica shape : obstacles) {
            for (SegmentoReta s : segments)
                if (shape.isIntercepted(s)) {
                    result++;
                    break;
                }
        }
        return result;
    }

    /*
  getFitness method to the fitness function of a trajectory
  @params ArrayList<FiguraGeometrica> obstacles
  @return 1/(comprimento_total(T) + exp(T.interseção(obstáculos))
   */
    public double getFitness(ArrayList<FiguraGeometrica> obstacles) {
        return 1.00 / (this.length + Math.exp(nCollisions(obstacles)));
    }
    /*
      onePointCrossover method to perform one point crossover between two trajectories and generate 2 offspring
      @params Trajetoria other, Random generator
      @return Trajetoria[] offspring
       */
    public Trajetoria[] onePointCrossover(Trajetoria other, Random generator) {
        ArrayList<Ponto> otherPoints = other.getPoints();
        int point1 = generator.nextInt( points.size() - 1) + 1;
        int point2 = generator.nextInt( otherPoints.size() - 1) + 1;
        ArrayList<Ponto> child1 = new ArrayList<>();
        ArrayList<Ponto> child2 = new ArrayList<>();
        for(int i = 0; i<point1; i++) child1.add(points.get(i));
        for(int i = point2; i<otherPoints.size(); i++) child1.add(otherPoints.get(i));
        for(int i = 0; i<point2; i++) child2.add(otherPoints.get(i));
        for(int i = point1; i<points.size(); i++) child2.add(points.get(i));
        return new Trajetoria[]{new Trajetoria(child1), new Trajetoria(child2)};
    }

    public void mutation(Random generator){
        int i = generator.nextInt(points.size()-1)+1;
        Ponto p = new Ponto(generator.nextInt(100),generator.nextInt(100));
        if(points.contains(p)) return;
        else points.set(i,p);
    }
    public ArrayList<Ponto> getPoints() {
        return points;
    }

    public double getLength() {
        return length;
    }
}
