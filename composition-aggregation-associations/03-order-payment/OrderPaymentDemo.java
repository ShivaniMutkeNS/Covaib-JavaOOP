package composition.order;

/**
 * MAANG-Level Demo: Order Payment System with Dynamic Payment Methods
 * Demonstrates composition flexibility, strategy pattern, and observer pattern
 */
public class OrderPaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Order Payment System Demo ===\n");
        
        // Create order
        Order order = new Order("ORD-2024-001");
        
        // Add observer for customer notifications
        CustomerNotificationObserver customerObserver = 
            new CustomerNotificationObserver("customer@email.com", "+1-555-0123");
        order.addObserver(customerObserver);
        
        // Add items to order
        System.out.println("--- Building Order ---");
        order.addItem("LAPTOP-001", "MacBook Pro 16\"", 2499.99, 1);
        order.addItem("MOUSE-001", "Magic Mouse", 79.99, 1);
        order.addItem("KEYBOARD-001", "Magic Keyboard", 199.99, 1);
        
        order.displayOrderSummary();
        
        // Try to pay without payment method
        System.out.println("\n--- Attempting Payment Without Payment Method ---");
        PaymentResult result = order.processPayment();
        System.out.println("Result: " + (result.isSuccess() ? "Success" : "Failed - " + result.getErrorMessage()));
        
        // Set credit card payment method
        System.out.println("\n--- Setting Credit Card Payment ---");
        CreditCardPayment creditCard = new CreditCardPayment(
            "4532123456789012", "John Doe", "12/25", "123");
        order.setPaymentMethod(creditCard);
        
        result = order.processPayment();
        System.out.println("Payment Result: " + (result.isSuccess() ? 
            "Success - Transaction ID: " + result.getTransactionId() : 
            "Failed - " + result.getErrorMessage()));
        
        // Create new order and test UPI payment
        System.out.println("\n\n--- New Order with UPI Payment ---");
        Order order2 = new Order("ORD-2024-002");
        order2.addObserver(customerObserver);
        
        order2.addItem("PHONE-001", "iPhone 15 Pro", 999.99, 1);
        order2.addItem("CASE-001", "Leather Case", 59.99, 1);
        
        UPIPayment upiPayment = new UPIPayment("john.doe@paytm", "1234");
        order2.setPaymentMethod(upiPayment);
        
        result = order2.processPayment();
        System.out.println("UPI Payment Result: " + (result.isSuccess() ? 
            "Success - Transaction ID: " + result.getTransactionId() : 
            "Failed - " + result.getErrorMessage()));
        
        // Test wallet payment with insufficient balance
        System.out.println("\n--- Testing Wallet Payment (Insufficient Balance) ---");
        Order order3 = new Order("ORD-2024-003");
        order3.addObserver(customerObserver);
        
        order3.addItem("TABLET-001", "iPad Pro", 1099.99, 1);
        
        WalletPayment wallet = new WalletPayment("WALLET123", 500.0, "PayPal");
        order3.setPaymentMethod(wallet);
        
        result = order3.processPayment();
        System.out.println("Wallet Payment Result: " + (result.isSuccess() ? 
            "Success" : "Failed - " + result.getErrorMessage()));
        
        // Recharge wallet and retry
        System.out.println("\n--- Recharging Wallet and Retrying ---");
        wallet.addBalance(700.0);
        
        result = order3.processPayment();
        System.out.println("Wallet Payment Result: " + (result.isSuccess() ? 
            "Success - Transaction ID: " + result.getTransactionId() : 
            "Failed - " + result.getErrorMessage()));
        
        // Runtime payment method switching
        System.out.println("\n--- Runtime Payment Method Switching ---");
        Order order4 = new Order("ORD-2024-004");
        order4.addObserver(customerObserver);
        
        order4.addItem("HEADPHONES-001", "AirPods Pro", 249.99, 1);
        
        // Start with credit card
        order4.setPaymentMethod(creditCard);
        
        // Switch to UPI
        order4.setPaymentMethod(upiPayment);
        
        // Switch to wallet
        order4.setPaymentMethod(wallet);
        
        result = order4.processPayment();
        order4.displayOrderSummary();
        
        System.out.println("\n=== Demo Complete: Order system adapted to different payment methods without modification ===");
    }
}
