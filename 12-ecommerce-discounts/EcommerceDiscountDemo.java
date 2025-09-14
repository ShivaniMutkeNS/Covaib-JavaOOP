/**
 * Comprehensive demonstration of the e-commerce discount system abstraction
 * Shows polymorphism, discount strategies, and real-world shopping scenarios
 */
public class EcommerceDiscountDemo {
    
    public static void main(String[] args) {
        System.out.println("🛒 E-commerce Discount System Demonstration");
        System.out.println("===========================================\n");
        
        // Create sample products
        Product[] products = createSampleProducts();
        
        // Create different discount types
        Discount[] discounts = createSampleDiscounts();
        
        // Demonstrate polymorphism with different discount types
        demonstratePolymorphism(products, discounts);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate discount-specific features
        demonstrateDiscountSpecificFeatures(products);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate shopping cart scenarios
        demonstrateShoppingCartScenarios(products, discounts);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate discount stacking and optimization
        demonstrateDiscountOptimization(products, discounts);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate real-world e-commerce scenarios
        demonstrateRealWorldScenarios(products, discounts);
    }
    
    private static Product[] createSampleProducts() {
        return new Product[] {
            new Product("P001", "Wireless Headphones", "Electronics", 99.99, 50, "Sony", 0.5, new String[]{"wireless", "audio"}),
            new Product("P002", "Running Shoes", "Sports", 129.99, 30, "Nike", 1.2, new String[]{"footwear", "running"}),
            new Product("P003", "Coffee Maker", "Appliances", 79.99, 25, "Breville", 3.5, new String[]{"kitchen", "coffee"}),
            new Product("P004", "Smartphone Case", "Electronics", 24.99, 100, "Generic", 0.1, new String[]{"phone", "protection"}),
            new Product("P005", "Yoga Mat", "Sports", 39.99, 40, "Manduka", 2.0, new String[]{"yoga", "fitness"}),
            new Product("P006", "Bluetooth Speaker", "Electronics", 59.99, 35, "JBL", 0.8, new String[]{"wireless", "audio"})
        };
    }
    
    private static Discount[] createSampleDiscounts() {
        // Percentage discount
        PercentageDiscount electronics20 = new PercentageDiscount("ELEC20", "Electronics 20% Off", 20.0);
        electronics20.setApplicableCategories(new String[]{"Electronics"});
        electronics20.setMinimumOrderAmount(50.0);
        
        // Fixed amount discount
        FixedAmountDiscount save15 = new FixedAmountDiscount("SAVE15", "$15 Off Your Order", 15.0);
        save15.setMinimumOrderAmount(75.0);
        
        // BOGO discount
        BuyOneGetOneDiscount bogoSports = new BuyOneGetOneDiscount("BOGO_SPORTS", "Sports BOGO 50% Off", 1, 1, 50.0);
        bogoSports.setApplicableCategories(new String[]{"Sports"});
        
        // Bulk discount
        BulkDiscount bulk3 = new BulkDiscount("BULK3", "Buy 3+ Get 15% Off", 3, 15.0, true);
        
        return new Discount[] { electronics20, save15, bogoSports, bulk3 };
    }
    
    private static void demonstratePolymorphism(Product[] products, Discount[] discounts) {
        System.out.println("🎯 POLYMORPHISM DEMONSTRATION");
        System.out.println("Same interface, different discount calculations\n");
        
        // Create a test cart
        ShoppingCart cart = new ShoppingCart("demo_customer");
        cart.addItem(products[0], 2); // 2x Wireless Headphones
        cart.addItem(products[1], 1); // 1x Running Shoes
        
        System.out.println("Test Cart Contents:");
        for (CartItem item : cart.getItems()) {
            System.out.println("  " + item.toString());
        }
        System.out.println("Cart Total: $" + String.format("%.2f", cart.getOriginalSubtotal()));
        System.out.println();
        
        // Test each discount type polymorphically
        for (Discount discount : discounts) {
            System.out.println("Discount Type: " + discount.getClass().getSimpleName());
            System.out.println("Description: " + discount.getDiscountDescription());
            
            boolean applicable = discount.isApplicable(cart.getItems());
            System.out.println("Applicable: " + applicable);
            
            if (applicable) {
                double discountAmount = discount.calculateDiscount(cart.getItems());
                System.out.println("Discount Amount: $" + String.format("%.2f", discountAmount));
            }
            System.out.println();
        }
    }
    
    private static void demonstrateDiscountSpecificFeatures(Product[] products) {
        System.out.println("🌟 DISCOUNT-SPECIFIC FEATURES");
        System.out.println("Each discount type has unique characteristics\n");
        
        // Percentage Discount Features
        System.out.println("📊 Percentage Discount Features:");
        PercentageDiscount seasonalSale = new PercentageDiscount("SUMMER25", "Summer Sale 25% Off", 25.0);
        seasonalSale.setMaximumDiscountAmount(50.0);
        seasonalSale.setApplicableBrands(new String[]{"Nike", "Sony"});
        System.out.println(seasonalSale.getDetailedInfo());
        System.out.println();
        
        // BOGO Discount Features
        System.out.println("🎁 BOGO Discount Features:");
        BuyOneGetOneDiscount bogoFree = new BuyOneGetOneDiscount("BOGO_FREE", "Buy 2 Get 1 Free", 2, 1, 100.0);
        System.out.println("Buy Quantity: " + bogoFree.getBuyQuantity());
        System.out.println("Get Quantity: " + bogoFree.getGetQuantity());
        System.out.println("Get Discount: " + bogoFree.getGetDiscountPercentage() + "%");
        System.out.println();
        
        // Bulk Discount Features
        System.out.println("📦 Bulk Discount Features:");
        BulkDiscount volumeDiscount = new BulkDiscount("VOLUME5", "Volume Discount 5+ Items", 5, 20.0, false);
        System.out.println("Minimum Quantity: " + volumeDiscount.getMinimumQuantity());
        System.out.println("Apply to All Items: " + volumeDiscount.isApplyToAllItems());
        System.out.println();
    }
    
    private static void demonstrateShoppingCartScenarios(Product[] products, Discount[] discounts) {
        System.out.println("🛒 SHOPPING CART SCENARIOS");
        System.out.println("Real shopping experiences with discounts\n");
        
        // Scenario 1: Electronics Shopping
        System.out.println("Scenario 1: Electronics Shopping Spree");
        ShoppingCart electronicsCart = new ShoppingCart("tech_enthusiast");
        
        // Add discount to cart's engine
        for (Discount discount : discounts) {
            electronicsCart.getDiscountEngine().addItem(discount);
        }
        
        electronicsCart.addItem(products[0], 1); // Wireless Headphones
        electronicsCart.addItem(products[3], 2); // 2x Smartphone Cases
        electronicsCart.addItem(products[5], 1); // Bluetooth Speaker
        
        electronicsCart.calculateShipping();
        electronicsCart.printCart();
        
        System.out.println("Applying best discounts...");
        electronicsCart.applyBestDiscounts();
        electronicsCart.printCart();
        
        // Scenario 2: Sports Equipment Shopping
        System.out.println("Scenario 2: Sports Equipment Shopping");
        ShoppingCart sportsCart = new ShoppingCart("fitness_lover");
        
        for (Discount discount : discounts) {
            sportsCart.getDiscountEngine().addDiscount(discount);
        }
        
        sportsCart.addItem(products[1], 2); // 2x Running Shoes (BOGO eligible)
        sportsCart.addItem(products[4], 1); // Yoga Mat
        
        sportsCart.calculateShipping();
        sportsCart.printCart();
        
        System.out.println("Applying best discounts...");
        sportsCart.applyBestDiscounts();
        sportsCart.printCart();
    }
    
    private static void demonstrateDiscountOptimization(Product[] products, Discount[] discounts) {
        System.out.println("🔧 DISCOUNT OPTIMIZATION");
        System.out.println("Stacking vs. exclusive discount strategies\n");
        
        ShoppingCart optimizationCart = new ShoppingCart("smart_shopper");
        
        // Add all products to test optimization
        for (Product product : products) {
            optimizationCart.addItem(product, 1);
        }
        
        // Test with stacking enabled
        System.out.println("🔗 Testing with discount stacking ENABLED:");
        optimizationCart.getDiscountEngine().setAllowDiscountStacking(true);
        
        for (Discount discount : discounts) {
            optimizationCart.getDiscountEngine().addDiscount(discount);
        }
        
        optimizationCart.calculateShipping();
        optimizationCart.applyBestDiscounts();
        optimizationCart.printCart();
        
        // Reset cart
        optimizationCart.clearCart();
        for (Product product : products) {
            optimizationCart.addItem(product, 1);
        }
        
        // Test with stacking disabled
        System.out.println("🚫 Testing with discount stacking DISABLED:");
        optimizationCart.getDiscountEngine().setAllowDiscountStacking(false);
        optimizationCart.calculateShipping();
        optimizationCart.applyBestDiscounts();
        optimizationCart.printCart();
    }
    
    private static void demonstrateRealWorldScenarios(Product[] products, Discount[] discounts) {
        System.out.println("🌍 REAL-WORLD E-COMMERCE SCENARIOS");
        System.out.println("Complex shopping situations and edge cases\n");
        
        // Scenario 1: Minimum Order Requirements
        System.out.println("Scenario 1: Minimum Order Requirements");
        ShoppingCart minOrderCart = new ShoppingCart("budget_shopper");
        
        FixedAmountDiscount highMinOrder = new FixedAmountDiscount("BIG20", "$20 Off Orders Over $200", 20.0);
        highMinOrder.setMinimumOrderAmount(200.0);
        minOrderCart.getDiscountEngine().addDiscount(highMinOrder);
        
        minOrderCart.addItem(products[3], 3); // 3x Smartphone Cases ($74.97)
        System.out.println("Order below minimum:");
        minOrderCart.applyBestDiscounts();
        minOrderCart.printCart();
        
        minOrderCart.addItem(products[0], 2); // Add 2x Headphones to reach minimum
        System.out.println("Order above minimum:");
        minOrderCart.applyBestDiscounts();
        minOrderCart.printCart();
        
        // Scenario 2: Usage Limits
        System.out.println("Scenario 2: Limited Use Discounts");
        PercentageDiscount limitedDiscount = new PercentageDiscount("LIMITED10", "Limited 10% Off", 10.0);
        limitedDiscount.setUsageLimit(2);
        
        ShoppingCart[] customers = {
            new ShoppingCart("customer1"),
            new ShoppingCart("customer2"),
            new ShoppingCart("customer3")
        };
        
        for (int i = 0; i < customers.length; i++) {
            customers[i].getDiscountEngine().addDiscount(limitedDiscount);
            customers[i].addItem(products[0], 1);
            
            System.out.println("Customer " + (i + 1) + " usage attempt:");
            customers[i].applyBestDiscounts();
            System.out.println("Discount usage count: " + limitedDiscount.getUsageCount() + "/" + limitedDiscount.getUsageLimit());
            System.out.println("Discount still valid: " + limitedDiscount.isValid());
            System.out.println();
        }
        
        System.out.println("🎉 E-commerce Discount System Demonstration Complete!");
        System.out.println("All abstraction concepts successfully demonstrated:");
        System.out.println("✅ Abstract discount classes and polymorphism");
        System.out.println("✅ Multiple discount calculation strategies");
        System.out.println("✅ Discount combination and optimization");
        System.out.println("✅ Shopping cart integration");
        System.out.println("✅ Real-world e-commerce scenarios");
        System.out.println("✅ Business rule enforcement");
    }
}
