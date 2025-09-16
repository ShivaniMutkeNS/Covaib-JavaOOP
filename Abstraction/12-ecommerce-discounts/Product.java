/**
 * Represents a product in the e-commerce system
 */
public class Product {
    private String productId;
    private String name;
    private String category;
    private double basePrice;
    private int stockQuantity;
    private String brand;
    private boolean isOnSale;
    private double weight; // for shipping calculations
    private String[] tags;
    
    public Product(String productId, String name, String category, double basePrice, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.stockQuantity = stockQuantity;
        this.brand = "Generic";
        this.isOnSale = false;
        this.weight = 1.0;
        this.tags = new String[0];
    }
    
    public Product(String productId, String name, String category, double basePrice, 
                  int stockQuantity, String brand, double weight, String[] tags) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.stockQuantity = stockQuantity;
        this.brand = brand;
        this.isOnSale = false;
        this.weight = weight;
        this.tags = tags != null ? tags.clone() : new String[0];
    }
    
    public boolean hasTag(String tag) {
        for (String t : tags) {
            if (t.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isInStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }
    
    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        }
    }
    
    public void addStock(int quantity) {
        stockQuantity += quantity;
    }
    
    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getBasePrice() { return basePrice; }
    public int getStockQuantity() { return stockQuantity; }
    public String getBrand() { return brand; }
    public boolean isOnSale() { return isOnSale; }
    public double getWeight() { return weight; }
    public String[] getTags() { return tags.clone(); }
    
    // Setters
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setOnSale(boolean onSale) { this.isOnSale = onSale; }
    public void setBrand(String brand) { this.brand = brand; }
    
    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f) [Stock: %d]", 
            productId, name, basePrice, stockQuantity);
    }
    
    public String getDetailedInfo() {
        return String.format(
            "🛍️ Product Details:\n" +
            "ID: %s\n" +
            "Name: %s\n" +
            "Category: %s\n" +
            "Brand: %s\n" +
            "Price: $%.2f\n" +
            "Stock: %d units\n" +
            "Weight: %.1f kg\n" +
            "On Sale: %s\n" +
            "Tags: %s",
            productId, name, category, brand, basePrice, stockQuantity, weight,
            isOnSale ? "Yes" : "No",
            tags.length > 0 ? String.join(", ", tags) : "None"
        );
    }
}
