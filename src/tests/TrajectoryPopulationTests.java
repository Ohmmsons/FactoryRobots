package tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.TrajectoryPopulation;

import java.util.ArrayList;
import simulator.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TrajectoryPopulationTests{

    private TrajectoryPopulation population;
    private Random rng;

    @BeforeEach
    void setUp() {
        rng = new Random(42);
        Point start = new Point(10, 10);
        Point end = new Point(990, 990);
        int[] lengths = {10, 15, 20, 25, 30};
        PointGenerator generator = new PointGenerator(rng);
        ArrayList<Shape> obstacles = new ArrayList<>();
        population = new TrajectoryPopulation(start, end, 5, lengths, generator, obstacles, rng);
    }

    @Test
    void testPopulationSize() {
        assertEquals(5, population.getIndividuals().size(), "The population size should be 5");
    }

    @Test
    void testRankBasedSelection() {
        TrajectoryPopulation selectedPopulation = population.rankBasedSelection();
        assertEquals(population.getIndividuals().size(), selectedPopulation.getIndividuals().size(), "The selected population size should be the same as the original population size");
    }

    @Test
    void testRankBasedSelectionNotSamePopulation() {
        TrajectoryPopulation selectedPopulation = population.rankBasedSelection();
        assertNotEquals(population.getIndividuals(), selectedPopulation.getIndividuals(), "The selected population should not be the same as the original population");
    }

    @Test
    void testTrajectoriesStartAndEnd() {
        for (Trajectory t : population.getIndividuals()) {
            assertEquals(new Point(10, 10), t.getPoints().get(0), "Trajectory should start at (10, 10)");
            assertEquals(new Point(990, 990), t.getPoints().get(t.getPoints().size() - 1), "Trajectory should end at (990, 990)");
        }
    }

    @Test
    void testTrajectoriesInFirstQuadrant() {
        for (Trajectory t : population.getIndividuals()) {
            for (Point p : t.getPoints()) {
                assertTrue(p.x() >= 0, "All points should have non-negative X coordinates");
                assertTrue(p.y() >= 0, "All points should have non-negative Y coordinates");
            }
        }
    }

    @Test
    void testToStringNotNull() {
        String str = population.toString();
        assertNotEquals(null, str, "toString() should return a non-null string representation");
    }

    @Test
    void testGeneratedTrajectoriesHaveDistinctPoints() {
        for (Trajectory t : population.getIndividuals()) {
            for (int i = 1; i < t.getPoints().size() - 1; i++) {
                Point point = t.getPoints().get(i);
                for (int j = 0; j < t.getPoints().size(); j++) {
                    if (i != j) {
                        assertNotEquals(point, t.getPoints().get(j), "Trajectory points should be distinct");
                    }
                }
            }
        }
    }


}
