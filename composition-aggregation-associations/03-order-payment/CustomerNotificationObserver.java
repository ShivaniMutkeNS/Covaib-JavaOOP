package composition.order;

/**
 * Customer Notification Observer Implementation
 */
public class CustomerNotificationObserver implements OrderObserver {
    private final String customerEmail;
    private final String customerPhone;
    
    public CustomerNotificationObserver(String customerEmail, String customerPhone) {
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }
    
    @Override
    public void onOrderUpdate(String orderId, String message, OrderStatus status) {
        System.out.println("\n📧 Customer Notification:");
        System.out.println("To: " + customerEmail);
        System.out.println("Order: " + orderId);
        System.out.println("Update: " + message);
        System.out.println("Status: " + status);
        
        // Send different notifications based on status
        switch (status) {
            case PAID:
                sendSMS("Order " + orderId + " payment confirmed. Thank you!");
                break;
            case PAYMENT_FAILED:
                sendSMS("Order " + orderId + " payment failed. Please try again.");
                break;
            case SHIPPED:
                sendEmail("Your order has been shipped!");
                break;
        }
    }
    
    private void sendEmail(String message) {
        System.out.println("📧 Email sent to " + customerEmail + ": " + message);
    }
    
    private void sendSMS(String message) {
        System.out.println("📱 SMS sent to " + customerPhone + ": " + message);
    }
}
