import java.util.ArrayList;

public class DeliveryMap {
    private ArrayList<Shape> obstacles;

    public DeliveryMap(ArrayList<Shape> obstacles){
        this.obstacles = obstacles;
    }

    public void addObstacle(Shape obstacle){
        this.obstacles.add(obstacle);
    }

    public void removeObstacle(Point p){
        for(Shape obstacle: obstacles)
            if(obstacle.surrounds(p)){
                obstacles.remove(obstacle);
                break;
            }
    }

    public boolean isDeliveryPointValid(Point p){
        for(Shape obstacle: obstacles)
            if(obstacle.surrounds(p))
                return false;
        return true;
    }

    public ArrayList<Shape> getObstacles() {
        return obstacles;
    }
}
