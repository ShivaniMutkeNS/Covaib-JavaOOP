package composition.restaurant;

import java.util.*;

/**
 * Bill class handling pricing calculations and discounts
 */
public class Bill {
    private final String orderId;
    private final List<MenuItem> items;
    private final List<DiscountStrategy> appliedDiscounts;
    private double subtotal;
    private double taxRate;
    private double serviceChargeRate;
    private boolean isFinalized;
    
    public Bill(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.appliedDiscounts = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxRate = 0.08; // 8% tax
        this.serviceChargeRate = 0.10; // 10% service charge
        this.isFinalized = false;
    }
    
    public void addItem(MenuItem item) {
        if (isFinalized) {
            throw new IllegalStateException("Cannot modify finalized bill");
        }
        
        items.add(item);
        subtotal += item.getPrice();
    }
    
    public void removeItem(MenuItem item) {
        if (isFinalized) {
            throw new IllegalStateException("Cannot modify finalized bill");
        }
        
        if (items.remove(item)) {
            subtotal -= item.getPrice();
        }
    }
    
    public void applyDiscount(DiscountStrategy discount) {
        if (isFinalized) {
            throw new IllegalStateException("Cannot modify finalized bill");
        }
        
        appliedDiscounts.add(discount);
    }
    
    public void removeDiscount(DiscountStrategy discount) {
        if (isFinalized) {
            throw new IllegalStateException("Cannot modify finalized bill");
        }
        
        appliedDiscounts.remove(discount);
    }
    
    public void finalize() {
        isFinalized = true;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getTotalDiscount() {
        return appliedDiscounts.stream()
            .mapToDouble(discount -> discount.calculateDiscount(subtotal))
            .sum();
    }
    
    public double getDiscountedSubtotal() {
        return Math.max(0, subtotal - getTotalDiscount());
    }
    
    public double getTax() {
        return getDiscountedSubtotal() * taxRate;
    }
    
    public double getServiceCharge() {
        return getDiscountedSubtotal() * serviceChargeRate;
    }
    
    public double getTotalAmount() {
        double discountedSubtotal = getDiscountedSubtotal();
        return discountedSubtotal + getTax() + getServiceCharge();
    }
    
    public void displayBill() {
        System.out.printf("Subtotal: $%.2f\n", subtotal);
        
        if (!appliedDiscounts.isEmpty()) {
            System.out.println("Discounts:");
            for (DiscountStrategy discount : appliedDiscounts) {
                System.out.printf("- %s: -$%.2f\n", 
                    discount.getDescription(), 
                    discount.calculateDiscount(subtotal));
            }
            System.out.printf("After Discounts: $%.2f\n", getDiscountedSubtotal());
        }
        
        System.out.printf("Tax (%.0f%%): $%.2f\n", taxRate * 100, getTax());
        System.out.printf("Service Charge (%.0f%%): $%.2f\n", serviceChargeRate * 100, getServiceCharge());
        System.out.printf("TOTAL: $%.2f\n", getTotalAmount());
    }
    
    public List<MenuItem> getItems() { return new ArrayList<>(items); }
    public boolean isFinalized() { return isFinalized; }
}
