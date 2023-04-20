package Simulator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

/**
Class Trajectory
@author Jude Adam
@version 1.0.0 20/02/2023
Points in the path must be sequential and in the 1st quadrant
 */
public class Trajectory {
    private final ArrayList<Point> points;
    private double length;
    public Random generator;

    private final ArrayList<Shape> obstacles;
    /**
     * Creates a new Trajectory object.
     * @param pontos - the list of points that define the trajectory.
     * @param generator - the random number generator used for mutation and crossover operations.
     * @param obstacles - the list of obstacles that the trajectory must avoid.
     */
    public Trajectory(ArrayList<Point> pontos, Random generator, ArrayList<Shape> obstacles) {
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


    /**
     * @return a string representation of the Simulator.Trajectory object.
     */
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
    /**
     * Calculates all the points along the trajectory.
     * @return an ArrayList of all the points along the trajectory.
     */
    public ArrayList<Point> calculatePointsAlongTrajectory(){
        LinkedHashSet<Point> set = new LinkedHashSet<>();
        int n = points.size();
        for (int i = 0; i < n - 1; i++){
            LineSegment lineSegment = new LineSegment(points.get(i), points.get(i+1));
            set.addAll(lineSegment.drawLine());
        }
        return new ArrayList<>(set.stream().toList());
    }

    /**
     * Calculates the number of collisions between the trajectory and a given list of obstacles.
     * @return the number of collisions between the trajectory and the obstacles.
     */
    public int nCollisions() {
        int result = 0;
        int n = points.size();
        for (Shape shape : obstacles)
            for (int i = 0; i<n-1; i++)
                if (shape.isIntercepted(new LineSegment(points.get(i),points.get(i+1)))){
                    result++;
                    break;
                }
        return result;
    }

    /**
     * Calculates the fitness of the trajectory.
     * @return the fitness of the trajectory.
     */
    public double fitness() {
        return 1.00 / (length + Math.exp(nCollisions()));
    }

    /**
      onePointCrossover method to perform one point crossover between two trajectories and generate 2 offspring
      @param other other trajectory
      @return offspring
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
    /**
          mutate method to perform one point mutation with probability pm
          @param pm mutation probability
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

    /**
     * Equals method
     * @param other other trajectory
     * @return true if trajectories are equal
     */
    public boolean equals(Object other){
        if(other.getClass()!=this.getClass())return false;
        Trajectory otherTrajectory = (Trajectory) other;
        ArrayList<Point> otherTrajectoryPoints = otherTrajectory.getPoints();
        for(int i = 0; i<points.size(); i++)
            if(!points.get(i).equals(otherTrajectoryPoints.get(i)))
                return false;
        return true;
    }
    /**
          addPoint method to add a random point in a random spot in the trajectory with probability pa
          @param pa addition probability
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
    /**
         removePoint method to remove a random point with probability pr
         @param pr removal probability
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
    /**
     hashCode method
     @return hashcode of trajectory
     */
    @Override
    public int hashCode() {
        int hash = 7;
        for(int i = 0; i<points.size(); i++) hash+= i*points.get(i).hashCode();
        return hash;
    }

    /**
     @return Points of trajectory
     */
    public ArrayList<Point> getPoints() {
        return points;
    }
    /**
     @return Length of trajectory
     */
    public double getLength() {
        return length;
    }
}
