/**
 * Demo class to showcase notification system
 * Demonstrates inheritance vs strategy pattern
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class NotificationDemo {
    public static void main(String[] args) {
        System.out.println("📱 NOTIFICATION SYSTEM 📱");
        System.out.println("=" .repeat(50));
        
        // Create different types of notifications
        Notification[] notifications = {
            new EmailNotification("alice@email.com", "Welcome to our service", "Thank you for joining us!", 3, "noreply@company.com", "manager@company.com", "", true),
            new EmailNotification("bob@email.com", "Urgent: Account Security Alert", "Your account has been accessed from a new device.", 1, "security@company.com", "", "", false),
            new SMSNotification("+1234567890", "Order Confirmation", "Your order #12345 has been confirmed and will be delivered tomorrow.", 2, "+1", "Verizon", false, "COMPANY"),
            new SMSNotification("+919876543210", "Payment Reminder", "Your payment of $99.99 is due tomorrow.", 4, "+91", "Airtel", true, "PAYMENT"),
            new PushNotification("device_token_123", "New Message", "You have 3 new messages in your inbox.", 3, "device_token_123", "iOS", "com.company.app", "messages"),
            new PushNotification("device_token_456", "Breaking News", "Important news update available.", 1, "device_token_456", "Android", "com.news.app", "breaking_news")
        };
        
        // Display notification information
        System.out.println("\n📋 NOTIFICATION INFORMATION:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println(notification.getNotificationInfo());
        }
        
        // Demonstrate notification sending
        System.out.println("\n📤 NOTIFICATION SENDING DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println("\n" + notification.getNotificationType() + " Notification:");
            boolean sent = notification.send();
            System.out.println("Result: " + (sent ? "Success" : "Failed"));
        }
        
        // Display notification features
        System.out.println("\n🔧 NOTIFICATION FEATURES:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println("\n" + notification.getNotificationType() + " Notification:");
            System.out.println(notification.getNotificationFeatures());
        }
        
        // Demonstrate notification-specific behaviors
        System.out.println("\n📱 NOTIFICATION-SPECIFIC BEHAVIORS:");
        System.out.println("-".repeat(50));
        
        // Email Notification specific behaviors
        EmailNotification emailNotification = new EmailNotification("test@email.com", "Test Email", "This is a test email.", 3, "sender@company.com", "", "", true);
        System.out.println("Email Notification Behaviors:");
        System.out.println(emailNotification.getEmailSummary());
        System.out.println("Valid email format: " + emailNotification.isValidEmailFormat());
        emailNotification.addAttachment("document.pdf");
        emailNotification.addCC("cc@company.com");
        emailNotification.addBCC("bcc@company.com");
        emailNotification.send();
        System.out.println();
        
        // SMS Notification specific behaviors
        SMSNotification smsNotification = new SMSNotification("+1234567890", "Test SMS", "This is a test SMS message.", 3, "+1", "Verizon", false, "TEST");
        System.out.println("SMS Notification Behaviors:");
        System.out.println(smsNotification.getPhoneNumberInfo());
        System.out.println("Valid phone number: " + smsNotification.isValidPhoneNumber());
        System.out.println("Single SMS: " + smsNotification.isSingleSMS());
        System.out.println("Message segments: " + smsNotification.getMessageSegments());
        System.out.println("Character count: " + smsNotification.getCharacterCount() + "/" + smsNotification.getMaxCharacters());
        smsNotification.send();
        System.out.println();
        
        // Push Notification specific behaviors
        PushNotification pushNotification = new PushNotification("device_token_test", "Test Push", "This is a test push notification.", 3, "device_token_test", "iOS", "com.test.app", "test_channel");
        System.out.println("Push Notification Behaviors:");
        System.out.println(pushNotification.getDeviceInfo());
        System.out.println("Device online: " + pushNotification.isDeviceOnline());
        pushNotification.setSound("notification.wav");
        pushNotification.setIcon("app_icon.png");
        pushNotification.addImage("https://example.com/image.jpg");
        pushNotification.addActionUrl("https://example.com/action");
        pushNotification.setBadgeCount(5);
        pushNotification.setSilent(false);
        pushNotification.send();
        pushNotification.simulateClick();
        System.out.println();
        
        // Demonstrate delivery status tracking
        System.out.println("\n📊 DELIVERY STATUS TRACKING:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println(notification.getNotificationType() + " Notification:");
            System.out.println(notification.getDeliveryStatus());
            System.out.println();
        }
        
        // Demonstrate priority handling
        System.out.println("\n⚡ PRIORITY HANDLING:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println(notification.getNotificationType() + " Notification:");
            System.out.println("Priority: " + notification.getPriority() + " (" + notification.getPriorityDescription() + ")");
            System.out.println("Urgent: " + notification.isUrgent());
            System.out.println();
        }
        
        // Demonstrate validation
        System.out.println("\n✅ NOTIFICATION VALIDATION:");
        System.out.println("-".repeat(50));
        for (Notification notification : notifications) {
            System.out.println(notification.getNotificationType() + " Notification:");
            System.out.println("Valid: " + notification.validate());
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(notifications);
        
        // Demonstrate inheritance vs strategy pattern discussion
        System.out.println("\n🎯 INHERITANCE VS STRATEGY PATTERN DISCUSSION:");
        System.out.println("-".repeat(50));
        System.out.println("Inheritance Approach (Current Implementation):");
        System.out.println("- Each notification type extends Notification base class");
        System.out.println("- Common functionality in base class, specific behavior in subclasses");
        System.out.println("- Easy to add new notification types by extending base class");
        System.out.println("- Tight coupling between notification types and base class");
        System.out.println();
        System.out.println("Strategy Pattern Alternative:");
        System.out.println("- Notification class would contain a NotificationStrategy interface");
        System.out.println("- Different strategies (EmailStrategy, SMSStrategy, PushStrategy)");
        System.out.println("- Loose coupling, easy to change behavior at runtime");
        System.out.println("- More complex but more flexible for changing behavior");
        System.out.println();
        System.out.println("When to use Inheritance:");
        System.out.println("- When notification types share common structure and behavior");
        System.out.println("- When you need to add new notification types frequently");
        System.out.println("- When the relationship is truly 'is-a' (Email IS-A Notification)");
        System.out.println();
        System.out.println("When to use Strategy Pattern:");
        System.out.println("- When you need to change notification behavior at runtime");
        System.out.println("- When notification types have very different implementations");
        System.out.println("- When you want to avoid deep inheritance hierarchies");
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param notifications Array of Notification objects
     */
    public static void demonstratePolymorphism(Notification[] notifications) {
        System.out.println("Processing notifications using polymorphism:");
        for (int i = 0; i < notifications.length; i++) {
            Notification notification = notifications[i];
            System.out.println((i + 1) + ". " + notification.getNotificationType() + " Notification");
            System.out.println("   Recipient: " + notification.getRecipient());
            System.out.println("   Subject: " + notification.getSubject());
            System.out.println("   Priority: " + notification.getPriorityDescription());
            System.out.println("   Features: " + notification.getNotificationFeatures());
            System.out.println();
        }
    }
}
