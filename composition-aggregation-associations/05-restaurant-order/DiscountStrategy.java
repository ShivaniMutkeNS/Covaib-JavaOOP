package composition.restaurant;

/**
 * Discount Strategy Interface
 */
public interface DiscountStrategy {
    double calculateDiscount(double subtotal);
    String getDescription();
    boolean isApplicable(double subtotal);
}
