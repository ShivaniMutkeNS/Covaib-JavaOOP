package composition.travel;

/**
 * Activity component for travel packages
 */
public class Activity {
    private final String name;
    private final String description;
    private final double price;
    private final int durationHours;
    private final String category;
    private final boolean requiresBooking;
    
    public Activity(String name, String description, double price, int durationHours, 
                   String category, boolean requiresBooking) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationHours = durationHours;
        this.category = category;
        this.requiresBooking = requiresBooking;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getDurationHours() { return durationHours; }
    public String getCategory() { return category; }
    public boolean requiresBooking() { return requiresBooking; }
}
