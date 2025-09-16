package composition.order;

import java.time.LocalDateTime;
import java.util.*;

/**
 * MAANG-Level Order System with Dynamic Payment Strategy
 * Demonstrates: Strategy Pattern, Observer Pattern, State Management
 */
public class Order {
    private final String orderId;
    private final List<OrderItem> items;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private double totalAmount;
    private final List<OrderObserver> observers;
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = 0.0;
        this.observers = new ArrayList<>();
    }
    
    public void addItem(String productId, String name, double price, int quantity) {
        OrderItem item = new OrderItem(productId, name, price, quantity);
        items.add(item);
        totalAmount += item.getTotalPrice();
        notifyObservers("Item added: " + name + " (Qty: " + quantity + ")");
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> {
            if (item.getProductId().equals(productId)) {
                totalAmount -= item.getTotalPrice();
                notifyObservers("Item removed: " + item.getName());
                return true;
            }
            return false;
        });
    }
    
    // Runtime payment method swapping - core composition flexibility
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        PaymentMethod oldMethod = this.paymentMethod;
        this.paymentMethod = paymentMethod;
        
        String message = oldMethod == null ? 
            "Payment method set to " + paymentMethod.getType() :
            "Payment method changed from " + oldMethod.getType() + " to " + paymentMethod.getType();
        
        notifyObservers(message);
    }
    
    public PaymentResult processPayment() {
        if (paymentMethod == null) {
            return new PaymentResult(false, "No payment method selected", null);
        }
        
        if (items.isEmpty()) {
            return new PaymentResult(false, "Order is empty", null);
        }
        
        status = OrderStatus.PROCESSING_PAYMENT;
        notifyObservers("Processing payment of $" + String.format("%.2f", totalAmount));
        
        PaymentResult result = paymentMethod.processPayment(totalAmount, orderId);
        
        if (result.isSuccess()) {
            status = OrderStatus.PAID;
            notifyObservers("Payment successful via " + paymentMethod.getType());
        } else {
            status = OrderStatus.PAYMENT_FAILED;
            notifyObservers("Payment failed: " + result.getErrorMessage());
        }
        
        return result;
    }
    
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String message) {
        for (OrderObserver observer : observers) {
            observer.onOrderUpdate(orderId, message, status);
        }
    }
    
    public void displayOrderSummary() {
        System.out.println("\n=== Order Summary ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status);
        System.out.println("Created: " + createdAt);
        System.out.println("Payment Method: " + (paymentMethod != null ? paymentMethod.getType() : "Not set"));
        
        System.out.println("\nItems:");
        for (OrderItem item : items) {
            System.out.printf("- %s (x%d) @ $%.2f = $%.2f\n", 
                            item.getName(), item.getQuantity(), item.getPrice(), item.getTotalPrice());
        }
        
        System.out.printf("Total Amount: $%.2f\n", totalAmount);
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
}
