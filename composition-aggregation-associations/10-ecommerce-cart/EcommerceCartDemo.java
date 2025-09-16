package composition.ecommerce;

import java.util.*;

/**
 * MAANG-Level E-commerce Shopping Cart Demo
 * Demonstrates runtime strategy swapping, cart operations, checkout process, and analytics
 */
public class EcommerceCartDemo {
    
    public static void main(String[] args) {
        System.out.println("🛒 MAANG-Level E-commerce Shopping Cart System Demo");
        System.out.println("==================================================");
        
        // Create shopping cart
        ShoppingCart cart = new ShoppingCart("CART_001", "CUSTOMER_123");
        
        // Add event listener for monitoring
        cart.addEventListener(new CartEventListener() {
            @Override
            public void onCartEvent(String cartId, String message, CartSummary summary) {
                System.out.println("📢 [" + cartId + "] " + message);
            }
        });
        
        System.out.println("\n=== 1. Product Catalog Setup ===");
        
        // Create sample products
        Product laptop = new Product("LAPTOP001", "Gaming Laptop", 1299.99, "Electronics", 
                                   "TechBrand", 2.5, new ProductDimensions(35, 25, 3), false, "laptop.jpg");
        Product phone = new Product("PHONE001", "Smartphone", 699.99, "Electronics", 
                                  "PhoneCorp", 0.2, new ProductDimensions(15, 7, 1), false, "phone.jpg");
        Product headphones = new Product("HEADPHONES001", "Wireless Headphones", 199.99, "Electronics", 
                                       "AudioTech", 0.3, new ProductDimensions(20, 18, 8), false, "headphones.jpg");
        Product tablet = new Product("TABLET001", "Tablet", 399.99, "Electronics", 
                                   "TabletInc", 0.5, new ProductDimensions(25, 17, 1), false, "tablet.jpg");
        Product watch = new Product("WATCH001", "Smart Watch", 249.99, "Electronics", 
                                  "WatchCo", 0.1, new ProductDimensions(4, 4, 1), false, "watch.jpg");
        
        System.out.println("📦 Product catalog initialized with 5 products");
        
        System.out.println("\n=== 2. Adding Items to Cart ===");
        
        // Add items to cart
        cart.addItem(laptop, 1);
        cart.addItem(phone, 2);
        cart.addItem(headphones, 1);
        
        cart.displayCartContents();
        
        System.out.println("\n=== 3. Cart Operations ===");
        
        // Update quantities
        cart.updateQuantity("PHONE001", 1);
        cart.addItem(tablet, 1);
        
        // Try to add more items than available
        System.out.println("\nTrying to add excessive quantity...");
        CartOperationResult result = cart.addItem(watch, 100);
        System.out.println("Result: " + result.getMessage());
        
        cart.displayCartContents();
        
        System.out.println("\n=== 4. Strategy Pattern - Pricing ===");
        
        // Switch to premium pricing
        System.out.println("\nSwitching to Premium Pricing Strategy...");
        cart.setPricingStrategy(new PremiumPricingStrategy());
        cart.displayCartContents();
        
        // Switch back to standard pricing
        System.out.println("\nSwitching back to Standard Pricing Strategy...");
        cart.setPricingStrategy(new StandardPricingStrategy());
        cart.displayCartContents();
        
        System.out.println("\n=== 5. Shipping Calculator Swapping ===");
        
        // Switch to express shipping
        System.out.println("\nSwitching to Express Shipping...");
        cart.setShippingCalculator(new ExpressShippingCalculator());
        cart.displayCartContents();
        
        // Switch back to standard shipping
        System.out.println("\nSwitching back to Standard Shipping...");
        cart.setShippingCalculator(new StandardShippingCalculator());
        cart.displayCartContents();
        
        System.out.println("\n=== 6. Cart Persistence ===");
        
        // Create snapshot
        CartSnapshot snapshot = cart.createSnapshot();
        System.out.println("📸 Cart snapshot created");
        
        // Modify cart
        cart.addItem(watch, 1);
        cart.removeItem("HEADPHONES001");
        System.out.println("\nCart modified:");
        cart.displayCartContents();
        
        // Restore from snapshot
        System.out.println("\nRestoring from snapshot...");
        cart.restoreFromSnapshot(snapshot);
        cart.displayCartContents();
        
        System.out.println("\n=== 7. Wishlist Integration ===");
        
        WishlistManager wishlistManager = new WishlistManager();
        
        // Move item to wishlist
        cart.moveToWishlist("HEADPHONES001", wishlistManager);
        cart.displayCartContents();
        
        // Add back from wishlist
        cart.addFromWishlist(headphones, 1, wishlistManager);
        cart.displayCartContents();
        
        System.out.println("\n=== 8. Recommendations ===");
        
        RecommendationEngine recommendationEngine = new SimpleRecommendationEngine();
        List<Product> recommendations = cart.getRecommendations(recommendationEngine);
        
        System.out.println("\n🎯 Recommended products:");
        for (Product product : recommendations) {
            System.out.printf("   • %s - $%.2f\n", product.getName(), product.getPrice());
        }
        
        System.out.println("\n=== 9. Cart Merging ===");
        
        // Create another cart for merging
        ShoppingCart guestCart = new ShoppingCart("GUEST_CART", "GUEST_USER");
        guestCart.addItem(watch, 1);
        guestCart.addItem(tablet, 1);
        
        System.out.println("\nGuest cart contents:");
        guestCart.displayCartContents();
        
        System.out.println("\nMerging guest cart with main cart...");
        cart.mergeWith(guestCart);
        cart.displayCartContents();
        
        System.out.println("\n=== 10. Analytics ===");
        
        CartAnalytics analytics = cart.getAnalytics();
        System.out.println("\n📊 Cart Analytics:");
        System.out.println("   Items Added: " + analytics.getMetrics().getItemsAdded());
        System.out.println("   Items Removed: " + analytics.getMetrics().getItemsRemoved());
        System.out.println("   Cart Clears: " + analytics.getMetrics().getCartClears());
        System.out.println("   Session Duration: " + analytics.getSessionDuration() + "ms");
        System.out.printf("   Average Item Price: $%.2f\n", analytics.getAverageItemPrice());
        
        if (analytics.getMostExpensiveItem() != null) {
            System.out.printf("   Most Expensive Item: %s ($%.2f)\n", 
                analytics.getMostExpensiveItem().getProduct().getName(),
                analytics.getMostExpensiveItem().getProduct().getPrice());
        }
        
        System.out.println("\n=== 11. Checkout Process ===");
        
        // Create shipping address and payment method
        ShippingAddress address = new ShippingAddress(
            "John Doe", "123 Main St", "Anytown", "CA", "12345", "USA");
        PaymentMethod paymentMethod = new PaymentMethod("Credit Card", "****1234", "Visa ending in 1234");
        
        System.out.println("\nInitiating checkout...");
        CheckoutResult checkoutResult = cart.initiateCheckout(address, paymentMethod);
        
        if (checkoutResult.isSuccess()) {
            System.out.println("✅ Checkout successful!");
            System.out.println("Order ID: " + checkoutResult.getOrder().getOrderId());
            System.out.println("Transaction ID: " + checkoutResult.getPaymentResult().getTransactionId());
            System.out.printf("Total Paid: $%.2f\n", checkoutResult.getPaymentResult().getAmount());
        } else {
            System.out.println("❌ Checkout failed: " + checkoutResult.getMessage());
        }
        
        System.out.println("\n=== 12. Post-Checkout State ===");
        cart.displayCartContents();
        
        System.out.println("\n=== 13. Final Analytics ===");
        
        CartAnalytics finalAnalytics = cart.getAnalytics();
        System.out.println("\n📊 Final Cart Analytics:");
        System.out.println("   Successful Checkouts: " + finalAnalytics.getMetrics().getSuccessfulCheckouts());
        System.out.println("   Failed Checkouts: " + finalAnalytics.getMetrics().getFailedCheckouts());
        System.out.println("   Total Operations: " + (
            finalAnalytics.getMetrics().getItemsAdded() + 
            finalAnalytics.getMetrics().getItemsRemoved() + 
            finalAnalytics.getMetrics().getCartClears()));
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("✅ E-commerce Shopping Cart successfully demonstrated:");
        System.out.println("   • Runtime strategy swapping (pricing, shipping)");
        System.out.println("   • Dynamic inventory management");
        System.out.println("   • Cart persistence and recovery");
        System.out.println("   • Wishlist integration");
        System.out.println("   • Cart merging capabilities");
        System.out.println("   • Product recommendations");
        System.out.println("   • Complete checkout process");
        System.out.println("   • Comprehensive analytics");
        System.out.println("   • Event-driven architecture");
        System.out.println("   • State management and validation");
    }
}
