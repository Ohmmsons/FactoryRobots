package Simulator;

import java.util.Random;

public class PointGenerator {

    private final Random rng;

    public PointGenerator(Random rng){this.rng = rng;}

    /**
     * Generates a Point object with x and y coordinates following a Gaussian distribution with mean at the midpoint between start and end points and standard deviation stdDev.
     * The generated point is guaranteed to have x and y coordinates within [0,1000).
     *
     * @param stdDev standard deviation for Gaussian distribution
     * @param start starting point for calculating midpoint
     * @param end ending point for calculating midpoint
     *
     * @return Point object with x and y coordinates following a Gaussian distribution with mean at midpoint between start and end points and standard deviation stdDev
     */
    public Point generateGaussianPoint(double stdDev, Point start, Point end){
        double midX = (start.x() + end.x()) / 2.0;
        double midY = (start.y() + end.y()) / 2.0;
        int x,y;
        do {
            x = (int) (midX + this.rng.nextGaussian() * stdDev);
            y = (int) (midY + this.rng.nextGaussian() * stdDev);
        } while (x < 0 || x >= 1000 || y < 0 || y >= 1000);
        return new Point(x,y);
    }

}
