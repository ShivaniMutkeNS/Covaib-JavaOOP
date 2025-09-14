
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo class to demonstrate E-commerce Shopping Cart
 */
public class ShoppingCartDemo {
    public static void main(String[] args) {
        System.out.println("=== E-commerce Shopping Cart Demo ===\n");
        
        // Test basic cart operations
        testBasicCartOperations();
        
        // Test cart validation
        testCartValidation();
        
        // Test discount system
        testDiscountSystem();
        
        // Test cart limits
        testCartLimits();
        
        // Test immutability
        testImmutability();
    }
    
    private static void testBasicCartOperations() {
        System.out.println("=== Basic Cart Operations Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_001");
        System.out.println("Cart created: " + cart);
        
        // Add items
        boolean added1 = cart.addItem("PROD_001", "Laptop", new BigDecimal("999.99"), 1);
        System.out.println("Added laptop: " + (added1 ? "SUCCESS" : "FAILED"));
        
        boolean added2 = cart.addItem("PROD_002", "Mouse", new BigDecimal("29.99"), 2);
        System.out.println("Added 2 mice: " + (added2 ? "SUCCESS" : "FAILED"));
        
        boolean added3 = cart.addItem("PROD_003", "Keyboard", new BigDecimal("79.99"), 1);
        System.out.println("Added keyboard: " + (added3 ? "SUCCESS" : "FAILED"));
        
        // Display cart
        System.out.println("Cart items:");
        for (ShoppingCart.CartItem item : cart.getCartItems()) {
            System.out.println("  " + item);
        }
        
        System.out.println("Total items: " + cart.getTotalItemCount());
        System.out.println("Subtotal: $" + cart.getSubtotal());
        System.out.println("Total: $" + cart.getTotal());
        
        System.out.println();
    }
    
    private static void testCartValidation() {
        System.out.println("=== Cart Validation Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_002");
        
        // Test invalid item additions
        boolean invalid1 = cart.addItem("", "Product", new BigDecimal("10.00"), 1);
        System.out.println("Added item with empty ID: " + (invalid1 ? "SUCCESS" : "FAILED"));
        
        boolean invalid2 = cart.addItem("PROD_001", "", new BigDecimal("10.00"), 1);
        System.out.println("Added item with empty name: " + (invalid2 ? "SUCCESS" : "FAILED"));
        
        boolean invalid3 = cart.addItem("PROD_001", "Product", new BigDecimal("-10.00"), 1);
        System.out.println("Added item with negative price: " + (invalid3 ? "SUCCESS" : "FAILED"));
        
        boolean invalid4 = cart.addItem("PROD_001", "Product", new BigDecimal("10.00"), 0);
        System.out.println("Added item with zero quantity: " + (invalid4 ? "SUCCESS" : "FAILED"));
        
        boolean invalid5 = cart.addItem("PROD_001", "Product", new BigDecimal("10.00"), -1);
        System.out.println("Added item with negative quantity: " + (invalid5 ? "SUCCESS" : "FAILED"));
        
        // Test valid addition
        boolean valid = cart.addItem("PROD_001", "Valid Product", new BigDecimal("10.00"), 1);
        System.out.println("Added valid item: " + (valid ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testDiscountSystem() {
        System.out.println("=== Discount System Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_003");
        
        // Add items
        cart.addItem("PROD_001", "Laptop", new BigDecimal("1000.00"), 1);
        cart.addItem("PROD_002", "Mouse", new BigDecimal("50.00"), 2);
        
        System.out.println("Subtotal: $" + cart.getSubtotal());
        
        // Create discounts
        ShoppingCart.Discount discount1 = new ShoppingCart.Discount(
            "SAVE10", "10% off", ShoppingCart.DiscountType.PERCENTAGE, 
            new BigDecimal("10"), new BigDecimal("100"), LocalDateTime.now().plusDays(30)
        );
        
        ShoppingCart.Discount discount2 = new ShoppingCart.Discount(
            "SAVE50", "$50 off", ShoppingCart.DiscountType.FIXED_AMOUNT, 
            new BigDecimal("50"), new BigDecimal("500"), LocalDateTime.now().plusDays(30)
        );
        
        // Apply discounts
        boolean applied1 = cart.applyDiscount(discount1);
        System.out.println("Applied 10% discount: " + (applied1 ? "SUCCESS" : "FAILED"));
        
        boolean applied2 = cart.applyDiscount(discount2);
        System.out.println("Applied $50 discount: " + (applied2 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Total discount: $" + cart.getTotalDiscount());
        System.out.println("Final total: $" + cart.getTotal());
        
        // Try to apply same discount again
        boolean applied3 = cart.applyDiscount(discount1);
        System.out.println("Applied same discount again: " + (applied3 ? "SUCCESS" : "FAILED"));
        
        // Remove discount
        boolean removed = cart.removeDiscount("SAVE10");
        System.out.println("Removed discount: " + (removed ? "SUCCESS" : "FAILED"));
        
        System.out.println("Total after removing discount: $" + cart.getTotal());
        
        System.out.println();
    }
    
    private static void testCartLimits() {
        System.out.println("=== Cart Limits Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_004");
        
        // Test quantity limits
        boolean added1 = cart.addItem("PROD_001", "Product 1", new BigDecimal("10.00"), 5);
        System.out.println("Added 5 items: " + (added1 ? "SUCCESS" : "FAILED"));
        
        boolean added2 = cart.addItem("PROD_001", "Product 1", new BigDecimal("10.00"), 3);
        System.out.println("Added 3 more items (total 8): " + (added2 ? "SUCCESS" : "FAILED"));
        
        boolean added3 = cart.addItem("PROD_001", "Product 1", new BigDecimal("10.00"), 5);
        System.out.println("Added 5 more items (total 13): " + (added3 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Final quantity: " + cart.getCartItem("PROD_001").getQuantity());
        
        // Test item limits
        for (int i = 1; i <= 55; i++) {
            boolean added = cart.addItem("PROD_" + i, "Product " + i, new BigDecimal("10.00"), 1);
            if (!added) {
                System.out.println("Failed to add item " + i + " (cart full)");
                break;
            }
        }
        
        System.out.println("Total products in cart: " + cart.getProductCount());
        System.out.println("Is cart full: " + cart.isFull());
        
        System.out.println();
    }
    
    private static void testImmutability() {
        System.out.println("=== Immutability Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_005");
        cart.addItem("PROD_001", "Product 1", new BigDecimal("10.00"), 2);
        
        // Get cart items
        List<ShoppingCart.CartItem> items = cart.getCartItems();
        System.out.println("Cart items: " + items.size());
        
        // Try to modify the returned list
        try {
            items.add(new ShoppingCart.CartItem("PROD_002", "Product 2", new BigDecimal("20.00"), 1));
            System.out.println("ERROR: Should not be able to modify cart items list");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Correctly prevented modification of cart items list");
        }
        
        // Try to modify a cart item
        ShoppingCart.CartItem item = items.get(0);
        System.out.println("Original item: " + item);
        
        // Cart items are immutable, so we can't modify them directly
        System.out.println("Item ID: " + item.getProductId());
        System.out.println("Item name: " + item.getProductName());
        System.out.println("Item price: " + item.getPrice());
        System.out.println("Item quantity: " + item.getQuantity());
        System.out.println("Item total: " + item.getTotalPrice());
        
        System.out.println();
    }
    
    /**
     * Test cart operations
     */
    private static void testCartOperations() {
        System.out.println("=== Cart Operations Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_006");
        
        // Add items
        cart.addItem("PROD_001", "Laptop", new BigDecimal("1000.00"), 1);
        cart.addItem("PROD_002", "Mouse", new BigDecimal("50.00"), 2);
        cart.addItem("PROD_003", "Keyboard", new BigDecimal("100.00"), 1);
        
        System.out.println("Initial cart: " + cart.getProductCount() + " products");
        
        // Update quantity
        boolean updated = cart.updateQuantity("PROD_002", 3);
        System.out.println("Updated mouse quantity to 3: " + (updated ? "SUCCESS" : "FAILED"));
        
        // Remove some items
        boolean removed = cart.removeItem("PROD_002", 1);
        System.out.println("Removed 1 mouse: " + (removed ? "SUCCESS" : "FAILED"));
        
        // Remove item completely
        boolean removedCompletely = cart.removeItemCompletely("PROD_003");
        System.out.println("Removed keyboard completely: " + (removedCompletely ? "SUCCESS" : "FAILED"));
        
        // Clear cart
        boolean cleared = cart.clearCart();
        System.out.println("Cleared cart: " + (cleared ? "SUCCESS" : "FAILED"));
        
        System.out.println("Final cart: " + cart.getProductCount() + " products");
        System.out.println("Is empty: " + cart.isEmpty());
        
        System.out.println();
    }
    
    /**
     * Test cart summary
     */
    private static void testCartSummary() {
        System.out.println("=== Cart Summary Test ===");
        
        ShoppingCart cart = new ShoppingCart("CART_007");
        
        // Add items
        cart.addItem("PROD_001", "Laptop", new BigDecimal("1000.00"), 1);
        cart.addItem("PROD_002", "Mouse", new BigDecimal("50.00"), 2);
        cart.addItem("PROD_003", "Keyboard", new BigDecimal("100.00"), 1);
        
        // Apply discount
        ShoppingCart.Discount discount = new ShoppingCart.Discount(
            "SAVE10", "10% off", ShoppingCart.DiscountType.PERCENTAGE, 
            new BigDecimal("10"), new BigDecimal("100"), LocalDateTime.now().plusDays(30)
        );
        cart.applyDiscount(discount);
        
        // Display cart summary
        System.out.println(cart.getCartSummary());
        
        System.out.println();
    }
}
