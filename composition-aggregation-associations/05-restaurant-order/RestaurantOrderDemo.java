package composition.restaurant;

/**
 * MAANG-Level Demo: Restaurant Order System with Dynamic Composition
 * Demonstrates composition flexibility, strategy pattern, and state management
 */
public class RestaurantOrderDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Restaurant Order System Demo ===\n");
        
        // Create order
        RestaurantOrder order = new RestaurantOrder("ORD-REST-001", "Table-5");
        
        // Add state listener for kitchen and customer notifications
        KitchenNotificationListener kitchenListener = new KitchenNotificationListener();
        CustomerNotificationListener customerListener = new CustomerNotificationListener("customer@email.com");
        
        order.addStateListener(kitchenListener);
        order.addStateListener(customerListener);
        
        // Build order with menu items
        System.out.println("--- Building Order ---");
        MenuItem pasta = new MenuItem("PASTA-001", "Truffle Pasta", 28.99, "Main Course", 
                                    "Fresh pasta with truffle sauce", true, 15);
        MenuItem steak = new MenuItem("STEAK-001", "Ribeye Steak", 45.99, "Main Course", 
                                    "Premium ribeye with garlic butter", false, 20);
        MenuItem wine = new MenuItem("WINE-001", "Cabernet Sauvignon", 12.99, "Beverage", 
                                   "House red wine", true, 2);
        MenuItem dessert = new MenuItem("DESSERT-001", "Chocolate Cake", 8.99, "Dessert", 
                                      "Rich chocolate cake with berries", true, 5);
        
        order.addMenuItem(pasta);
        order.addMenuItem(steak);
        order.addMenuItem(wine);
        order.addMenuItem(dessert);
        
        order.displayOrderSummary();
        
        // Test discount strategies
        System.out.println("\n--- Applying Discounts ---");
        PercentageDiscount happyHour = new PercentageDiscount(15.0, "Happy Hour Special", 50.0);
        FixedAmountDiscount loyaltyDiscount = new FixedAmountDiscount(10.0, "Loyalty Member", 30.0);
        
        order.addDiscount(happyHour);
        order.addDiscount(loyaltyDiscount);
        
        // Confirm order
        System.out.println("\n--- Confirming Order ---");
        order.confirmOrder();
        
        // Test payment method swapping
        System.out.println("\n--- Setting Payment Methods ---");
        CashPayment cashPayment = new CashPayment();
        CardPayment cardPayment = new CardPayment("4532123456789012", "Visa");
        
        // Start with cash
        order.setPaymentProcessor(cashPayment);
        
        // Switch to card
        order.setPaymentProcessor(cardPayment);
        
        // Process payment
        System.out.println("\n--- Processing Payment ---");
        PaymentResult result = order.processPayment();
        System.out.println("Payment Result: " + (result.isSuccess() ? 
            "Success - Transaction ID: " + result.getTransactionId() : 
            "Failed - " + result.getErrorMessage()));
        
        // Kitchen workflow
        System.out.println("\n--- Kitchen Workflow ---");
        order.startPreparation();
        
        // Simulate preparation time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        order.markReady();
        order.completeOrder();
        
        // Test order modification scenarios
        System.out.println("\n\n--- New Order with Modifications ---");
        RestaurantOrder order2 = new RestaurantOrder("ORD-REST-002", "Table-3");
        order2.addStateListener(kitchenListener);
        order2.addStateListener(customerListener);
        
        // Add items
        order2.addMenuItem(new MenuItem("BURGER-001", "Gourmet Burger", 18.99, "Main Course", 
                                      "Wagyu beef burger with truffle fries", false, 12));
        order2.addMenuItem(new MenuItem("SALAD-001", "Caesar Salad", 12.99, "Appetizer", 
                                      "Fresh romaine with parmesan", true, 8));
        
        // Remove an item
        order2.removeMenuItem("SALAD-001");
        
        // Add different discount
        PercentageDiscount studentDiscount = new PercentageDiscount(20.0, "Student Discount", 15.0);
        order2.addDiscount(studentDiscount);
        
        // Remove discount
        order2.removeDiscount(studentDiscount);
        
        // Add back different discount
        FixedAmountDiscount firstTimeDiscount = new FixedAmountDiscount(5.0, "First Time Customer", 10.0);
        order2.addDiscount(firstTimeDiscount);
        
        order2.confirmOrder();
        order2.setPaymentProcessor(cashPayment);
        
        PaymentResult result2 = order2.processPayment();
        System.out.println("Payment Result: " + (result2.isSuccess() ? "Success" : "Failed"));
        
        order2.displayOrderSummary();
        
        System.out.println("\n=== Demo Complete: Restaurant order system adapted to different payment methods and discounts without modification ===");
    }
    
    // Kitchen notification listener
    static class KitchenNotificationListener implements OrderStateListener {
        @Override
        public void onStateChange(String orderId, OrderState newState, String message) {
            if (newState == OrderState.PAID || newState == OrderState.PREPARING || newState == OrderState.READY) {
                System.out.println("🍳 Kitchen Alert [" + orderId + "]: " + message);
            }
        }
    }
    
    // Customer notification listener
    static class CustomerNotificationListener implements OrderStateListener {
        private final String customerEmail;
        
        public CustomerNotificationListener(String customerEmail) {
            this.customerEmail = customerEmail;
        }
        
        @Override
        public void onStateChange(String orderId, OrderState newState, String message) {
            switch (newState) {
                case CONFIRMED:
                case PAID:
                case READY:
                case COMPLETED:
                    System.out.println("📧 Customer Notification [" + customerEmail + "]: " + message);
                    break;
            }
        }
    }
}
