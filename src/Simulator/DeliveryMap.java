package Simulator;

import java.util.ArrayList;
/**
 * The DeliveryMap class represents a map that contains obstacles that robots must navigate around in order to deliver packages.
 * It keeps track of all the obstacles on the map and provides methods to add or remove them. It also provides a method to check
 * whether a given delivery point is valid or not based on whether it is within an obstacle or outside the delivery area.
 *  @author Jude Adam
 *  @version 1.0.0 20/04/2023
 * Delivery point is considered valid if it is within the boundaries of the map, and not surrounded by any obstacles.
 */
public class DeliveryMap {
    private ArrayList<Shape> obstacles;
    /**
     * Constructs a new DeliveryMap with the given list of obstacles.
     * @param obstacles The initial list of obstacles on the map.
     */
    public DeliveryMap(ArrayList<Shape> obstacles){
        this.obstacles = obstacles;
    }
    /**
     * Adds a new obstacle to the map.
     * @param obstacle The obstacle to add to the map.
     */
    public void addObstacle(Shape obstacle){
        this.obstacles.add(obstacle);
    }
    /**
     * Removes an obstacle from the map that contains the given point.
     * @param p The point to check for containing obstacles.
     */
    public void removeObstacle(Point p){
        for(Shape obstacle: obstacles)
            if(obstacle.surrounds(p)){
                obstacles.remove(obstacle);
                break;
            }
    }
    /**
     * Checks whether a delivery point is valid, meaning it is not within an obstacle or outside the delivery area.
     * @param p The delivery point to check for validity.
     * @return True if the delivery point is valid, false otherwise.
     */
    public boolean isDeliveryPointValid(Point p){
        //In any of corners
        if((p.x()>950&&p.y()>950)||(p.x()<50&&p.y()<50)||(p.x()>950&&p.y()<50)||(p.x()<50&&p.y()>950))
            return false;
        for(Shape obstacle: obstacles)
            if(obstacle.surrounds(p))
                return false;
        return true;
    }
    /**
     * @return The list of obstacles on the map.
     */
    public ArrayList<Shape> getObstacles() {
        return obstacles;
    }
}
