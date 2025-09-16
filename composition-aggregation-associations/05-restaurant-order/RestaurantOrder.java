package composition.restaurant;

import java.time.LocalDateTime;
import java.util.*;

/**
 * MAANG-Level Restaurant Order System using Composition
 * Demonstrates: Strategy Pattern, Decorator Pattern, State Pattern, Command Pattern
 */
public class RestaurantOrder {
    private final String orderId;
    private final List<MenuItem> menuItems;
    private Bill bill;
    private PaymentProcessor paymentProcessor;
    private final List<DiscountStrategy> discounts;
    private OrderState state;
    private final LocalDateTime orderTime;
    private final String tableNumber;
    private final List<OrderStateListener> listeners;
    
    public RestaurantOrder(String orderId, String tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.menuItems = new ArrayList<>();
        this.discounts = new ArrayList<>();
        this.state = OrderState.CREATED;
        this.orderTime = LocalDateTime.now();
        this.listeners = new ArrayList<>();
        this.bill = new Bill(orderId);
    }
    
    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
        bill.addItem(item);
        notifyStateChange("Item added: " + item.getName());
        System.out.println("Added to order: " + item.getName() + " - $" + String.format("%.2f", item.getPrice()));
    }
    
    public void removeMenuItem(String itemId) {
        menuItems.removeIf(item -> {
            if (item.getId().equals(itemId)) {
                bill.removeItem(item);
                notifyStateChange("Item removed: " + item.getName());
                System.out.println("Removed from order: " + item.getName());
                return true;
            }
            return false;
        });
    }
    
    // Runtime discount strategy composition
    public void addDiscount(DiscountStrategy discount) {
        discounts.add(discount);
        bill.applyDiscount(discount);
        notifyStateChange("Discount applied: " + discount.getDescription());
        System.out.println("Discount applied: " + discount.getDescription());
    }
    
    public void removeDiscount(DiscountStrategy discount) {
        if (discounts.remove(discount)) {
            bill.removeDiscount(discount);
            notifyStateChange("Discount removed: " + discount.getDescription());
            System.out.println("Discount removed: " + discount.getDescription());
        }
    }
    
    // Runtime payment processor swapping
    public void setPaymentProcessor(PaymentProcessor processor) {
        PaymentProcessor oldProcessor = this.paymentProcessor;
        this.paymentProcessor = processor;
        
        String message = oldProcessor == null ? 
            "Payment processor set to " + processor.getType() :
            "Payment processor changed from " + oldProcessor.getType() + " to " + processor.getType();
        
        notifyStateChange(message);
    }
    
    public void confirmOrder() {
        if (menuItems.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        
        state = OrderState.CONFIRMED;
        bill.finalize();
        notifyStateChange("Order confirmed");
        System.out.println("Order " + orderId + " confirmed for table " + tableNumber);
    }
    
    public PaymentResult processPayment() {
        if (state != OrderState.CONFIRMED) {
            return new PaymentResult(false, "Order must be confirmed before payment", null);
        }
        
        if (paymentProcessor == null) {
            return new PaymentResult(false, "No payment processor set", null);
        }
        
        state = OrderState.PROCESSING_PAYMENT;
        notifyStateChange("Processing payment");
        
        double totalAmount = bill.getTotalAmount();
        PaymentResult result = paymentProcessor.processPayment(totalAmount, orderId);
        
        if (result.isSuccess()) {
            state = OrderState.PAID;
            notifyStateChange("Payment successful");
        } else {
            state = OrderState.PAYMENT_FAILED;
            notifyStateChange("Payment failed: " + result.getErrorMessage());
        }
        
        return result;
    }
    
    public void startPreparation() {
        if (state != OrderState.PAID) {
            throw new IllegalStateException("Order must be paid before preparation");
        }
        
        state = OrderState.PREPARING;
        notifyStateChange("Kitchen started preparing order");
        System.out.println("Kitchen notification: Start preparing order " + orderId);
    }
    
    public void markReady() {
        if (state != OrderState.PREPARING) {
            throw new IllegalStateException("Order must be in preparation to mark ready");
        }
        
        state = OrderState.READY;
        notifyStateChange("Order ready for pickup/delivery");
        System.out.println("Order " + orderId + " is ready!");
    }
    
    public void completeOrder() {
        if (state != OrderState.READY) {
            throw new IllegalStateException("Order must be ready to complete");
        }
        
        state = OrderState.COMPLETED;
        notifyStateChange("Order completed");
        System.out.println("Order " + orderId + " completed successfully");
    }
    
    public void addStateListener(OrderStateListener listener) {
        listeners.add(listener);
    }
    
    private void notifyStateChange(String message) {
        for (OrderStateListener listener : listeners) {
            listener.onStateChange(orderId, state, message);
        }
    }
    
    public void displayOrderSummary() {
        System.out.println("\n=== Restaurant Order Summary ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Table: " + tableNumber);
        System.out.println("Status: " + state);
        System.out.println("Order Time: " + orderTime);
        System.out.println("Payment Method: " + (paymentProcessor != null ? paymentProcessor.getType() : "Not set"));
        
        System.out.println("\nMenu Items:");
        for (MenuItem item : menuItems) {
            System.out.printf("- %s: $%.2f (%s)\n", item.getName(), item.getPrice(), item.getCategory());
        }
        
        System.out.println("\nBill Details:");
        bill.displayBill();
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public OrderState getState() { return state; }
    public double getTotalAmount() { return bill.getTotalAmount(); }
    public List<MenuItem> getMenuItems() { return new ArrayList<>(menuItems); }
    public Bill getBill() { return bill; }
}
