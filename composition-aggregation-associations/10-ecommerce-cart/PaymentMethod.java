package composition.ecommerce;

/**
 * Payment Method data class
 */
public class PaymentMethod {
    private final String type;
    private final String identifier;
    private final String displayName;
    
    public PaymentMethod(String type, String identifier, String displayName) {
        this.type = type;
        this.identifier = identifier;
        this.displayName = displayName;
    }
    
    public String getType() { return type; }
    public String getIdentifier() { return identifier; }
    public String getDisplayName() { return displayName; }
}
