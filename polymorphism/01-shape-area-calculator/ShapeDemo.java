 
public class ShapeDemo {
    public static void main(String[] args) {
        Shape[] shapes = new Shape[] {
            new Circle(3.0),
            new Rectangle(4.0, 5.0),
            new Triangle(6.0, 2.5)
        };

        double totalArea = 0.0;
        for (Shape s : shapes) {
            double area = s.calculateArea();
            totalArea += area;
            System.out.println(s.getName() + " area: " + String.format("%.2f", area));
        }
        System.out.println("Total area: " + String.format("%.2f", totalArea));
    }
}


