import java.util.ArrayList;
import java.util.Random;

/*
Class Trajectory
@author Jude Adam
@version 1.0.0 20/02/2023
@inv Points in the path must be sequential and in the 1st quadrant
 */
public class Trajectory {
    private final ArrayList<Point> points;
    private double length;
    public Random generator;

    private final ArrayList<Shape> obstacles;
    /*
    Constructor for Trajectory class
    @params LineSegment[] segments
     */
    Trajectory(ArrayList<Point> pontos, Random generator, ArrayList<Shape> obstacles) {
        this.obstacles = obstacles;
        this.generator = generator;
        ArrayList<Point> points = new ArrayList<>();
        length = 0;
        int n = pontos.size();
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (!points.contains(pontos.get(i))) {
                points.add(pontos.get(i));
                if(i>0)length+= points.get(++j).dist(points.get(j-1));
            }
        }
        this.points = points;
    }


    public String toString() {
        if (points.isEmpty()) return "[]";
        StringBuilder str = new StringBuilder("[");
        for (Point point : points) {
            str.append(point);
        }
        str.deleteCharAt(str.length() - 1);
        str.append("]");
        return new String(str);
    }

    /*
   nCollisions method to calculate how many collision there are between the path and a given array of rectangles
   @params ArrayList<Shape> obstacles
   @return Number of collisions 
    */
    public int nCollisions() {
        int result = 0;
        int n = points.size();
        for (Shape shape : obstacles) {
            for (int i = 0; i<n-1; i++)
                if (shape.isIntercepted(new LineSegment(points.get(i),points.get(i+1)))){
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
      @params Trajectory other, Random generator
      @return Trajectory[] offspring
       */
    public Trajectory[] crossover(Trajectory other) {
        ArrayList<Point> otherPoints = other.getPoints();
        int point1 = generator.nextInt(points.size() - 1) + 1;
        int point2 = generator.nextInt(otherPoints.size() - 1) + 1;
        ArrayList<Point> child1 = new ArrayList<>();
        ArrayList<Point> child2 = new ArrayList<>();
        for (int i = 0; i < point1; i++) child1.add(points.get(i));
        for (int i = point2; i < otherPoints.size(); i++) child1.add(otherPoints.get(i));
        for (int i = 0; i < point2; i++) child2.add(otherPoints.get(i));
        for (int i = point1; i < points.size(); i++) child2.add(points.get(i));
        return new Trajectory[]{new Trajectory(child1,generator,obstacles), new Trajectory(child2,generator,obstacles)};
    }
    /*
          mutate method to perform one point mutation with probability pm
          @params double pm
    */
    public void mutate(double pm) {
        if (points.size() > 2) {
        if (generator.nextDouble() < pm) {
                int i = generator.nextInt(points.size() - 2) + 1;
                Point p = new Point(generator.nextInt(100), generator.nextInt(100));
                if (!points.contains(p)) {
                    length-=(points.get(i-1).dist(points.get(i)) + points.get(i).dist(points.get(i+1)));
                    points.set(i, p);
                    length+=(points.get(i-1).dist(p) + p.dist(points.get(i+1)));
                }
            }
        }
    }

    public boolean equals(Object other){
        Trajectory otherTrajectory = (Trajectory) other;
        ArrayList<Point> otherTrajectoryPoints = otherTrajectory.getPoints();
        for(int i = 0; i<points.size(); i++)
            if(!points.get(i).equals(otherTrajectoryPoints.get(i)))
                return false;
        return true;
    }
    /*
          addPoint method to add a random point in a random spot in the trajectory with probability pa
          @params double pa
    */
    public void addPoint(double pa) {
        if(generator.nextDouble() < pa) {
            int i = 0;
            if (points.size() > 2) i = generator.nextInt(points.size() - 2) + 1;
            Point p = new Point(generator.nextInt(100), generator.nextInt(100));
            if (!points.contains(p)){
                length-=(points.get(i).dist(points.get(i+1)));
                points.add(i + 1, p);
                length+=(points.get(i).dist(p) + p.dist(points.get(i+2)));
            }
        }
    }
    /*
         removePoint method to remove a random point with probability pr
         @params double pr
   */
    public void removePoint(double pr) {
        if (points.size() > 2){
            if(generator.nextDouble()<pr) {
            int i;
                i = generator.nextInt(points.size() - 2) + 1;
                length-=(points.get(i-1).dist(points.get(i)) + points.get(i).dist(points.get(i+1)));
                points.remove(i);
                length+=points.get(i-1).dist(points.get(i));
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for(int i = 0; i<points.size(); i++) hash+= i*points.get(i).hashCode();
        return hash;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public double getLength() {
        return length;
    }
}
