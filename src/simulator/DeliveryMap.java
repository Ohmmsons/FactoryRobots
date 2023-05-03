package simulator;

import java.util.List;

/**
 * The DeliveryMap class represents a map that contains obstacles that robots must navigate around in order to deliver packages.
 * It keeps track of all the obstacles on the map and provides methods to add or remove them. It also provides a method to check
 * whether a given delivery point is valid or not based on whether it is within an obstacle or outside the delivery area.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 * @inv obstacles != null
 */
public record DeliveryMap(List<Shape> obstacles) {

    private static final int MIN_BOUND = 50;
    private static final int MAX_BOUND = 950;

    /**
     * Constructs a new DeliveryMap with the given list of obstacles.
     *
     * @param obstacles The initial list of obstacles on the map.
     * @pre obstacles != null
     * @post A new DeliveryMap instance is created with the specified obstacles list.
     */
    public DeliveryMap {
        if (obstacles == null) {
            throw new IllegalArgumentException("Obstacles list cannot be null");
        }
    }

    /**
     * Adds a new obstacle to the map.
     *
     * @param obstacle The obstacle to add to the map.
     * @pre obstacle != null
     * @post The obstacle is added to the list of obstacles.
     */
    public void addObstacle(Shape obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException("Obstacle cannot be null");
        }
        this.obstacles.add(obstacle);
    }



    /**
     * Checks whether a delivery request is valid, meaning it is not within an obstacle or outside the delivery area.
     *
     * @param request The delivery request to check for validity.
     * @return True if the delivery request is valid, false otherwise.
     * @pre request != null
     */
    public boolean isDeliveryRequestValid(Request request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        // In any of the corners
        if (pointOutOfBounds(request.start()) || pointOutOfBounds(request.end()))
            return false;
        for (Shape obstacle : obstacles)
            if (obstacle.surrounds(request.start()) || obstacle.surrounds(request.end()))
                return false;
        return true;
    }

    private boolean pointOutOfBounds(Point p) {
        return (p.x() < MIN_BOUND || p.x() > MAX_BOUND || p.y()< MIN_BOUND || p.y() > MAX_BOUND);
    }

}
