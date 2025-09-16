package composition.ecommerce;

/**
 * Product data class for E-commerce system
 */
public class Product {
    private final String productId;
    private final String name;
    private final String description;
    private final double price;
    private final String category;
    private final String brand;
    private final double weight;
    private final ProductDimensions dimensions;
    private final boolean isDigital;
    private final String imageUrl;
    
    public Product(String productId, String name, String description, double price, 
                  String category, String brand, double weight, ProductDimensions dimensions, 
                  boolean isDigital, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.weight = weight;
        this.dimensions = dimensions;
        this.isDigital = isDigital;
        this.imageUrl = imageUrl;
    }
    
    public Product(String productId, String name, double price, String category) {
        this(productId, name, "", price, category, "", 0.0, 
             new ProductDimensions(0, 0, 0), false, "");
    }
    
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public double getWeight() { return weight; }
    public ProductDimensions getDimensions() { return dimensions; }
    public boolean isDigital() { return isDigital; }
    public String getImageUrl() { return imageUrl; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId.equals(product.productId);
    }
    
    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}
