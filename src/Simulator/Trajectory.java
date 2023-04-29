package Simulator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 * Class Trajectory
 *
 * @author Jude Adam
 * @version 1.0.0 20/02/2023
 * @inv Points in the path must be sequential and in the 1st quadrant
 */
public class Trajectory {
    private final ArrayList<Point> points;
    private final Random rng;
    private double length;
    public PointGenerator generator;
    private int collisionCount;
    private final ArrayList<Shape> obstacles;

    /**
     * Creates a new Trajectory object.
     *
     * @param pontos    - the list of points that define the trajectory.
     * @param generator - the random number generator used for mutation and crossover operations.
     * @param obstacles - the list of obstacles that the trajectory must avoid.
     * @param rng       - the random number generator
     * @pre pontos != null && generator != null && obstacles != null && rng != null
     */
    public Trajectory(ArrayList<Point> pontos, PointGenerator generator, ArrayList<Shape> obstacles, Random rng) {
        this.obstacles = obstacles;
        this.generator = generator;
        ArrayList<Point> points = new ArrayList<>();
        this.rng = rng;
        length = 0;
        int n = pontos.size();
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (!points.contains(pontos.get(i))) {
                points.add(pontos.get(i));
                if (i > 0) length += points.get(++j).dist(points.get(j - 1));
            }
        }
        this.points = points;
        this.collisionCount = calculateCollisions();
    }

    /**
     * @return a string representation of the Trajectory object.
     * @post The returned string is non-empty.
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
     *
     * @return an ArrayList of all the points along the trajectory.
     * @post The returned list is non-empty.
     */
    public ArrayList<Point> calculatePointsAlongTrajectory() {
        LinkedHashSet<Point> set = new LinkedHashSet<>();
        int n = points.size();
        for (int i = 0; i < n - 1; i++) {
            LineSegment lineSegment = new LineSegment(points.get(i), points.get(i + 1));
            set.addAll(lineSegment.drawLine());
        }
        return new ArrayList<>(set.stream().toList());
    }

    /**
     * Calculates the number of collisions between the trajectory and a given list of obstacles.
     *
     * @return the number of collisions between the trajectory and the obstacles.
     */
    public int calculateCollisions() {
        int result = 0;
        int n = points.size();
        for (Shape shape : obstacles)
            for (int i = 0; i < n - 1; i++)
                if (shape.isIntercepted(new LineSegment(points.get(i), points.get(i + 1)))) {
                    result++;
                    break;
                }
        return result;
    }

    /**
     * Calculates the fitness ofthe trajectory.
     *
     * @return the fitness of the trajectory.
     */
    public double fitness() {
        return Math.exp((200 / length) * (collisionCount == 0 ? 1 : 0)) - 1;
    }

    /**
     * onePointCrossover method to perform one point crossover between two trajectories and generate 2 offspring
     *
     * @param other other trajectory
     * @return offspring
     * @pre other != null
     */
    public Trajectory[] onePointCrossover(Trajectory other) {
        ArrayList<Point> otherPoints = other.getPoints();
        int point1 = rng.nextInt(points.size() - 1) + 1;
        int point2 = rng.nextInt(otherPoints.size() - 1) + 1;
        ArrayList<Point> child1 = new ArrayList<>();
        ArrayList<Point> child2 = new ArrayList<>();
        for (int i = 0; i < point1; i++) child1.add(points.get(i));
        for (int i = point2; i < otherPoints.size(); i++) child1.add(otherPoints.get(i));
        for (int i = 0; i < point2; i++) child2.add(otherPoints.get(i));
        for (int i = point1; i < points.size(); i++) child2.add(points.get(i));
        return new Trajectory[]{new Trajectory(child1, generator, obstacles, rng), new Trajectory(child2, generator, obstacles, rng)};
    }

    /**
     * mutate method to perform one point mutation with probability pm
     *
     * @param pm mutation probability
     * @pre 0 <= pm <= 1
     */
    public void mutate(double pm) {
        if (points.size() > 2) {
            if (rng.nextDouble() < pm) {
                int i = rng.nextInt(points.size() - 2) + 1;
                Point p = generator.generateGaussianPoint(50, points.get(0), points.get(points.size() - 1));
                do {
                    if (!points.contains(p)) {
                        //Mutate point and update length
                        length -= (points.get(i - 1).dist(points.get(i)) + points.get(i).dist(points.get(i + 1)));
                        points.set(i, p);
                        length += (points.get(i - 1).dist(p) + p.dist(points.get(i + 1)));
                    }
                    else p = generator.generateGaussianPoint(50, points.get(0), points.get(points.size() - 1));
                }while(points.contains(p));
                this.collisionCount = calculateCollisions();
            }
        }
    }

    /**
     * Equals method
     *
     * @param other other trajectory
     * @return true if trajectories are equal
     */
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        Trajectory otherTrajectory = (Trajectory) other;
        ArrayList<Point> otherTrajectoryPoints = otherTrajectory.getPoints();
        for (int i = 0; i < points.size(); i++)
            if (!points.get(i).equals(otherTrajectoryPoints.get(i)))
                return false;
        return true;
    }

    /**
     * addPoint method to add a random point in a random spot in the trajectory with probability pa
     *
     * @param pa addition probability
     * @pre 0 <= pa <= 1
     */
    public void addPoint(double pa) {
        if (rng.nextDouble() < pa) {
            int i = 0;
            if (points.size() > 2) i = rng.nextInt(points.size() - 2) + 1;
            Point p = generator.generateGaussianPoint(50, points.get(0), points.get(points.size() - 1));
            do {
                if (!points.contains(p)) {
                    // Add point and update length
                    length += (points.get(i - 1).dist(p) + p.dist(points.get(i))) - points.get(i - 1).dist(points.get(i));
                    points.add(i, p);
                }
                else  p = generator.generateGaussianPoint(50, points.get(0), points.get(points.size() - 1));
            }while(points.contains(p));
            this.collisionCount = calculateCollisions();
        }
    }

    /**
     * removePoint method to remove a random point from the trajectory with probability pr
     *
     * @param pr removal probability
     * @pre 0 <= pr <= 1
     */
    public void removePoint(double pr) {
        if (points.size() > 2 && rng.nextDouble() < pr) {
            int i = rng.nextInt(points.size() - 2) + 1;
            Point p = points.remove(i);
            // Update length
            length -= (points.get(i - 1).dist(p) + p.dist(points.get(i))) - points.get(i - 1).dist(points.get(i));
            this.collisionCount = calculateCollisions();
        }
    }

    /**
     * Getter for points.
     *
     * @return points - the list of points that define the trajectory.
     */
    public ArrayList<Point> getPoints() {
        return new ArrayList<>(points);
    }

    /**
     * Getter for length.
     *
     * @return length - the length of the trajectory.
     */
    public double getLength() {
        return length;
    }

    /**
     * Getter for collisionCount.
     *
     * @return collisionCount - the number of collisions between the trajectory and the obstacles.
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Concatenates two trajectories by adding the points from the second trajectory to the first trajectory.
     *
     * @param other the second trajectory to be concatenated
     * @pre other != null
     * @post newtrajectory.size() == this.size() + other.size() - 1
     * @return concatenated trajectory
     */
    public Trajectory concatenate(Trajectory other) {
        if(other == null) throw new IllegalArgumentException("other trajectory can't be null");
        ArrayList<Point> points1 = new ArrayList<>(this.points);
        ArrayList<Point> points2 = new ArrayList<>(other.getPoints());
        points1.addAll(points2.subList(1, points2.size()));
        return new Trajectory(points1, generator, obstacles, rng);
    }
}
