import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discount calculation engine that manages and applies multiple discounts
 * Handles discount combination rules and optimization
 */
public class DiscountEngine {
    private List<Discount> availableDiscounts;
    private boolean allowDiscountStacking;
    private double maxStackingDiscount; // Maximum percentage when stacking
    
    public DiscountEngine() {
        this.availableDiscounts = new ArrayList<>();
        this.allowDiscountStacking = true;
        this.maxStackingDiscount = 50.0; // Max 50% total discount when stacking
    }
    
    public void addDiscount(Discount discount) {
        availableDiscounts.add(discount);
        System.out.println("➕ Added discount: " + discount.getName());
    }
    
    public void removeDiscount(String discountId) {
        availableDiscounts.removeIf(discount -> discount.getDiscountId().equals(discountId));
        System.out.println("➖ Removed discount: " + discountId);
    }
    
    public List<Discount> getApplicableDiscounts(List<CartItem> cartItems) {
        return availableDiscounts.stream()
                .filter(discount -> discount.isApplicable(cartItems))
                .collect(Collectors.toList());
    }
    
    public DiscountResult calculateBestDiscount(List<CartItem> cartItems) {
        List<Discount> applicableDiscounts = getApplicableDiscounts(cartItems);
        
        if (applicableDiscounts.isEmpty()) {
            return new DiscountResult(0.0, new ArrayList<>(), "No applicable discounts");
        }
        
        if (allowDiscountStacking) {
            return calculateStackedDiscounts(cartItems, applicableDiscounts);
        } else {
            return calculateBestSingleDiscount(cartItems, applicableDiscounts);
        }
    }
    
    private DiscountResult calculateBestSingleDiscount(List<CartItem> cartItems, List<Discount> applicableDiscounts) {
        double bestDiscount = 0.0;
        Discount bestDiscountObj = null;
        
        for (Discount discount : applicableDiscounts) {
            double discountAmount = discount.calculateDiscount(cartItems);
            if (discountAmount > bestDiscount) {
                bestDiscount = discountAmount;
                bestDiscountObj = discount;
            }
        }
        
        List<Discount> appliedDiscounts = new ArrayList<>();
        if (bestDiscountObj != null) {
            appliedDiscounts.add(bestDiscountObj);
            bestDiscountObj.incrementUsage();
        }
        
        return new DiscountResult(bestDiscount, appliedDiscounts, "Best single discount applied");
    }
    
    private DiscountResult calculateStackedDiscounts(List<CartItem> cartItems, List<Discount> applicableDiscounts) {
        List<Discount> stackableDiscounts = applicableDiscounts.stream()
                .filter(discount -> discount.getDiscountType().canCombine())
                .collect(Collectors.toList());
        
        List<Discount> exclusiveDiscounts = applicableDiscounts.stream()
                .filter(discount -> !discount.getDiscountType().canCombine())
                .collect(Collectors.toList());
        
        // Calculate best exclusive discount
        DiscountResult exclusiveResult = exclusiveDiscounts.isEmpty() ? 
            new DiscountResult(0.0, new ArrayList<>(), "") :
            calculateBestSingleDiscount(cartItems, exclusiveDiscounts);
        
        // Calculate stacked combinable discounts
        DiscountResult stackedResult = calculateCombinableDiscounts(cartItems, stackableDiscounts);
        
        // Choose the better option
        if (exclusiveResult.getTotalDiscount() > stackedResult.getTotalDiscount()) {
            return new DiscountResult(exclusiveResult.getTotalDiscount(), 
                                    exclusiveResult.getAppliedDiscounts(),
                                    "Best exclusive discount applied");
        } else {
            return new DiscountResult(stackedResult.getTotalDiscount(),
                                    stackedResult.getAppliedDiscounts(),
                                    "Stacked discounts applied");
        }
    }
    
    private DiscountResult calculateCombinableDiscounts(List<CartItem> cartItems, List<Discount> stackableDiscounts) {
        double totalDiscount = 0.0;
        List<Discount> appliedDiscounts = new ArrayList<>();
        double orderTotal = cartItems.stream().mapToDouble(CartItem::getOriginalTotal).sum();
        
        for (Discount discount : stackableDiscounts) {
            double discountAmount = discount.calculateDiscount(cartItems);
            if (discountAmount > 0) {
                totalDiscount += discountAmount;
                appliedDiscounts.add(discount);
                discount.incrementUsage();
            }
        }
        
        // Apply maximum stacking limit
        double maxAllowedDiscount = orderTotal * (maxStackingDiscount / 100.0);
        if (totalDiscount > maxAllowedDiscount) {
            totalDiscount = maxAllowedDiscount;
        }
        
        return new DiscountResult(totalDiscount, appliedDiscounts, "Multiple discounts stacked");
    }
    
    public void applyDiscounts(List<CartItem> cartItems, DiscountResult discountResult) {
        if (discountResult.getTotalDiscount() <= 0) {
            return;
        }
        
        // Reset existing discounts
        for (CartItem item : cartItems) {
            item.resetDiscount();
        }
        
        // Apply new discounts proportionally
        double orderTotal = cartItems.stream().mapToDouble(CartItem::getOriginalTotal).sum();
        double discountRatio = discountResult.getTotalDiscount() / orderTotal;
        
        for (CartItem item : cartItems) {
            double itemDiscount = item.getOriginalTotal() * discountRatio;
            item.applyDiscount(itemDiscount);
        }
        
        System.out.println("✅ Applied discounts totaling $" + String.format("%.2f", discountResult.getTotalDiscount()));
    }
    
    public void printAvailableDiscounts() {
        System.out.println("\n🎫 AVAILABLE DISCOUNTS");
        System.out.println("=".repeat(50));
        
        if (availableDiscounts.isEmpty()) {
            System.out.println("No discounts available");
            return;
        }
        
        for (Discount discount : availableDiscounts) {
            System.out.println("• " + discount.getName());
            System.out.println("  " + discount.getDiscountDescription());
            System.out.println("  Status: " + (discount.isValid() ? "Active" : "Inactive"));
            System.out.println();
        }
    }
    
    // Getters and Setters
    public List<Discount> getAvailableDiscounts() { return new ArrayList<>(availableDiscounts); }
    public boolean isAllowDiscountStacking() { return allowDiscountStacking; }
    public void setAllowDiscountStacking(boolean allowDiscountStacking) { this.allowDiscountStacking = allowDiscountStacking; }
    public double getMaxStackingDiscount() { return maxStackingDiscount; }
    public void setMaxStackingDiscount(double maxStackingDiscount) { this.maxStackingDiscount = maxStackingDiscount; }
    
    /**
     * Inner class to hold discount calculation results
     */
    public static class DiscountResult {
        private double totalDiscount;
        private List<Discount> appliedDiscounts;
        private String description;
        
        public DiscountResult(double totalDiscount, List<Discount> appliedDiscounts, String description) {
            this.totalDiscount = totalDiscount;
            this.appliedDiscounts = new ArrayList<>(appliedDiscounts);
            this.description = description;
        }
        
        public double getTotalDiscount() { return totalDiscount; }
        public List<Discount> getAppliedDiscounts() { return new ArrayList<>(appliedDiscounts); }
        public String getDescription() { return description; }
        
        @Override
        public String toString() {
            return String.format("Discount: $%.2f (%s) - %d discount(s) applied", 
                totalDiscount, description, appliedDiscounts.size());
        }
    }
}
