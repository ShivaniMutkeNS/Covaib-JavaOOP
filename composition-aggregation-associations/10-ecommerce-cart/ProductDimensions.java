package composition.ecommerce;

/**
 * Product Dimensions data class
 */
public class ProductDimensions {
    private final double length;
    private final double width;
    private final double height;
    
    public ProductDimensions(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }
    
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getVolume() { return length * width * height; }
}
