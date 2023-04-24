package Simulator;

public class ShapeGenerator {
    private final Generator generator;

    public ShapeGenerator(Generator generator) {
        this.generator = generator;
    }

    public Shape generateShape(String shapeType) {
        return switch (shapeType.toLowerCase()) {
            case "circle" -> new CircleFactory(generator).createShape();
            case "rectangle" -> new RectangleFactory(generator).createShape();
            case "triangle" -> new TriangleFactory(generator).createShape();
            default -> throw new IllegalArgumentException("Invalid shape type: " + shapeType);
        };
    }
}