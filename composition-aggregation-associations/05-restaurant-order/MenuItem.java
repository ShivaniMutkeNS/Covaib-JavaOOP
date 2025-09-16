package composition.restaurant;

/**
 * Menu Item data class
 */
public class MenuItem {
    private final String id;
    private final String name;
    private final double price;
    private final String category;
    private final String description;
    private final boolean isVegetarian;
    private final int preparationTime; // in minutes
    
    public MenuItem(String id, String name, double price, String category, 
                   String description, boolean isVegetarian, int preparationTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.isVegetarian = isVegetarian;
        this.preparationTime = preparationTime;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public boolean isVegetarian() { return isVegetarian; }
    public int getPreparationTime() { return preparationTime; }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", name, price, category);
    }
}
