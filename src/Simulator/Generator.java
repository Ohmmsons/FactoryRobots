package Simulator;

import java.util.Random;

public class Generator{

    public Random rng;
    public Generator(){
        rng = new Random();
    }
    public Generator(int seed){
        rng = new Random(seed);
    }

    public Generator(Random r){
        rng = r;
    }

    public int nextInt(){
        return rng.nextInt();
    }

    public boolean nextBoolean(){
        return rng.nextBoolean();
    }

    public int[] ints(int n, int start, int bound){
        return rng.ints(n, start, bound).toArray();
    }
    public double nextDouble(){
        return rng.nextDouble();
    }

    public int nextInt(int bound){
        return rng.nextInt(bound);
    }

    public int nextInt(int start, int bound){
        return rng.nextInt(start,bound);
    }


    public Point generateGaussianPoint(Point start, Point end){
        double midX = (start.x() + end.x()) / 2.0;
        double midY = (start.y() + end.y()) / 2.0;
        double stdDev = 50.0;
        int x,y;
        do {
            x = (int) (midX + rng.nextGaussian() * stdDev);
            y = (int) (midY + rng.nextGaussian() * stdDev);
        } while (x < 0 || x >= 1000 || y < 0 || y >= 1000);
        return new Point(x,y);
    }
}
