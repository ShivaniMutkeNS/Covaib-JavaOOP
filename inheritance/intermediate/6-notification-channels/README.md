# 📱 Notification Channels System - Inheritance vs Strategy Pattern

## Problem Statement
Create a notification system with different channels. Implement inheritance with base class `Notification` and subclasses `EmailNotification`, `SMSNotification`, and `PushNotification`. Each notification type should override the abstract methods `send()`, `getDeliveryStatus()`, and `getNotificationFeatures()` with their specific business logic.

## Learning Objectives
- **Inheritance vs Strategy Pattern**: Understanding when to use each approach
- **Method Overriding**: Each notification type has different sending logic
- **Abstract Methods**: Force subclasses to implement specific behaviors
- **Real-world Business Logic**: Notification delivery and tracking

## Class Hierarchy

```
Notification (Abstract)
├── EmailNotification
├── SMSNotification
└── PushNotification
```

## Key Concepts Demonstrated

### 1. Inheritance vs Strategy Pattern
- **Inheritance Approach**: Each notification type extends base class
- **Strategy Pattern Alternative**: Notification class contains NotificationStrategy interface
- **When to use each**: Inheritance for 'is-a' relationships, Strategy for runtime behavior changes

### 2. Method Overriding
- Each notification type provides its own implementation of abstract methods
- Different sending logic for each notification type
- Different delivery tracking and features

### 3. Abstract Methods
- Force subclasses to implement specific behaviors
- Ensure consistent interface across all notification types
- Enable polymorphic method calls

## Code Structure

### Notification.java (Abstract Base Class)
```java
public abstract class Notification {
    protected String recipient;
    protected String subject;
    protected String message;
    protected String timestamp;
    protected boolean isSent;
    protected String notificationType;
    protected int priority;
    protected String status;
    
    // Abstract methods - must be implemented by subclasses
    public abstract boolean send();
    public abstract String getDeliveryStatus();
    public abstract String getNotificationFeatures();
    
    // Concrete methods - shared by all notifications
    public boolean validate() { ... }
    public String getNotificationInfo() { ... }
}
```

### EmailNotification.java (Concrete Subclass)
```java
public class EmailNotification extends Notification {
    private String fromEmail;
    private String toEmail;
    private String ccEmails;
    private String bccEmails;
    private boolean hasAttachment;
    private String attachmentPath;
    private String emailFormat;
    private boolean isHtml;
    private String deliveryReport;
    
    @Override
    public boolean send() {
        // Email-specific sending logic
        System.out.println("Sending email notification...");
        System.out.println("From: " + fromEmail);
        System.out.println("To: " + toEmail);
        // ... email sending logic
    }
    
    @Override
    public String getDeliveryStatus() {
        return String.format("Email Status: %s, Delivery Report: %s, Format: %s", 
                           status, deliveryReport, emailFormat);
    }
    
    @Override
    public String getNotificationFeatures() {
        return "Email Features: Format: " + emailFormat + ", CC: Yes, BCC: Yes, ...";
    }
}
```

### SMSNotification.java (Concrete Subclass)
```java
public class SMSNotification extends Notification {
    private String phoneNumber;
    private String countryCode;
    private String carrier;
    private boolean isInternational;
    private String smsFormat;
    private int characterCount;
    private int maxCharacters;
    private String deliveryStatus;
    private double cost;
    private String senderId;
    
    @Override
    public boolean send() {
        // SMS-specific sending logic
        System.out.println("Sending SMS notification...");
        System.out.println("To: " + countryCode + phoneNumber);
        System.out.println("Carrier: " + carrier);
        // ... SMS sending logic
    }
    
    @Override
    public String getDeliveryStatus() {
        return String.format("SMS Status: %s, Delivery Report: %s, Carrier: %s, Cost: $%.4f", 
                           status, deliveryStatus, carrier, cost);
    }
    
    @Override
    public String getNotificationFeatures() {
        return "SMS Features: Format: " + smsFormat + ", Character Count: " + characterCount + 
               "/" + maxCharacters + ", Carrier: " + carrier + ", ...";
    }
}
```

### PushNotification.java (Concrete Subclass)
```java
public class PushNotification extends Notification {
    private String deviceToken;
    private String platform;
    private String appId;
    private String channelId;
    private String sound;
    private String icon;
    private String imageUrl;
    private String actionUrl;
    private boolean isSilent;
    private int badgeCount;
    private String deliveryStatus;
    private String clickStatus;
    private long deliveryTime;
    
    @Override
    public boolean send() {
        // Push notification-specific sending logic
        System.out.println("Sending push notification...");
        System.out.println("Device Token: " + deviceToken.substring(0, 20) + "...");
        System.out.println("Platform: " + platform);
        // ... push notification sending logic
    }
    
    @Override
    public String getDeliveryStatus() {
        return String.format("Push Status: %s, Delivery Report: %s, Platform: %s, Click Status: %s", 
                           status, deliveryStatus, platform, clickStatus.isEmpty() ? "Not clicked" : clickStatus);
    }
    
    @Override
    public String getNotificationFeatures() {
        return "Push Notification Features: Platform: " + platform + ", App ID: " + appId + 
               ", Channel: " + channelId + ", Sound: " + sound + ", ...";
    }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java NotificationDemo
```

## Expected Output

```
📱 NOTIFICATION SYSTEM 📱
==================================================

📋 NOTIFICATION INFORMATION:
--------------------------------------------------
Type: Email, Recipient: alice@email.com, Subject: Welcome to our service, Priority: 3, Status: Pending, Sent: false
Type: Email, Recipient: bob@email.com, Subject: Urgent: Account Security Alert, Priority: 1, Status: Pending, Sent: false
Type: SMS, Recipient: +1234567890, Subject: Order Confirmation, Priority: 2, Status: Pending, Sent: false
Type: SMS, Recipient: +919876543210, Subject: Payment Reminder, Priority: 4, Status: Pending, Sent: false
Type: Push, Recipient: device_token_123, Subject: New Message, Priority: 3, Status: Pending, Sent: false
Type: Push, Recipient: device_token_456, Subject: Breaking News, Priority: 1, Status: Pending, Sent: false

📤 NOTIFICATION SENDING DEMONSTRATION:
--------------------------------------------------

Email Notification:
Sending email notification...
From: noreply@company.com
To: alice@email.com
CC: manager@company.com
Subject: Welcome to our service
Format: HTML
✅ Email sent successfully!
Result: Success

Email Notification:
Sending email notification...
From: security@company.com
To: bob@email.com
Subject: Urgent: Account Security Alert
Format: Plain Text
✅ Email sent successfully!
Result: Success

SMS Notification:
Sending SMS notification...
To: +1+1234567890
Carrier: Verizon
Sender ID: COMPANY
Message: Your order #12345 has been confirmed and will be delivered tomorrow.
Characters: 75/160
International: No
Cost: $0.0100
✅ SMS sent successfully!
Result: Success

SMS Notification:
Sending SMS notification...
To: +91+919876543210
Carrier: Airtel
Sender ID: PAYMENT
Message: Your payment of $99.99 is due tomorrow.
Characters: 45/160
International: Yes
Cost: $0.0200
✅ SMS sent successfully!
Result: Success

Push Notification:
Sending push notification...
Device Token: device_token_123...
Platform: iOS
App ID: com.company.app
Channel: messages
Title: New Message
Message: You have 3 new messages in your inbox.
Sound: default
Icon: default
Badge Count: 1
Silent: No
✅ Push notification sent successfully!
Result: Success

Push Notification:
Sending push notification...
Device Token: device_token_456...
Platform: Android
App ID: com.news.app
Channel: breaking_news
Title: Breaking News
Message: Important news update available.
Sound: default
Icon: default
Badge Count: 1
Silent: No
✅ Push notification sent successfully!
Result: Success

🔧 NOTIFICATION FEATURES:
--------------------------------------------------

Email Notification:
Email Features: Format: HTML, CC: Yes, BCC: No, Attachment: No, Delivery Report: Available, Priority: Medium

Email Notification:
Email Features: Format: Plain Text, CC: No, BCC: No, Attachment: No, Delivery Report: Available, Priority: Critical

SMS Notification:
SMS Features: Format: Text, Character Count: 75/160, Carrier: Verizon, International: No, Cost: $0.0100, Sender ID: COMPANY, Priority: High

SMS Notification:
SMS Features: Format: Text, Character Count: 45/160, Carrier: Airtel, International: Yes, Cost: $0.0200, Sender ID: PAYMENT, Priority: Low

Push Notification:
Push Notification Features: Platform: iOS, App ID: com.company.app, Channel: messages, Sound: default, Icon: default, Image: No, Action URL: No, Silent: No, Badge Count: 1, Priority: Medium

Push Notification:
Push Notification Features: Platform: Android, App ID: com.news.app, Channel: breaking_news, Sound: default, Icon: default, Image: No, Action URL: No, Silent: No, Badge Count: 1, Priority: Critical

📱 NOTIFICATION-SPECIFIC BEHAVIORS:
--------------------------------------------------
Email Notification Behaviors:
Email Summary: noreply@company.com -> test@email.com, Subject: Test Email, Format: HTML, Attachment: No
Valid email format: true
Attachment added: document.pdf
CC recipients added: cc@company.com
BCC recipients added: bcc@company.com
Sending email notification...
From: sender@company.com
To: test@email.com
CC: cc@company.com
BCC: bcc@company.com
Subject: Test Email
Format: HTML
Attachment: document.pdf
✅ Email sent successfully!

SMS Notification Behaviors:
Phone: +1+1234567890, Carrier: Verizon, International: No
Valid phone number: true
Single SMS: true
Message segments: 1
Character count: 20/160
Sending SMS notification...
To: +1+1234567890
Carrier: Verizon
Sender ID: TEST
Message: This is a test SMS message.
Characters: 20/160
International: No
Cost: $0.0100
✅ SMS sent successfully!

Push Notification Behaviors:
Device: device_token..., Platform: iOS, App: com.test.app, Channel: test_channel
Device online: true
Sound set to: notification.wav
Icon set to: app_icon.png
Image added: https://example.com/image.jpg
Action URL added: https://example.com/action
Badge count set to: 5
Silent mode set to: No
Sending push notification...
Device Token: device_token_test...
Platform: iOS
App ID: com.test.app
Channel: test_channel
Title: Test Push
Message: This is a test push notification.
Sound: notification.wav
Icon: app_icon.png
Badge Count: 5
Silent: No
✅ Push notification sent successfully!
Push notification clicked!
Click time: Clicked at 2024-01-14T17:07:00.123

📊 DELIVERY STATUS TRACKING:
--------------------------------------------------
Email Notification:
Email Status: Delivered, Delivery Report: Email delivered successfully to alice@email.com, Format: HTML, Timestamp: 2024-01-14T17:07:00.123

Email Notification:
Email Status: Delivered, Delivery Report: Email delivered successfully to bob@email.com, Format: Plain Text, Timestamp: 2024-01-14T17:07:00.123

SMS Notification:
SMS Status: Delivered, Delivery Report: SMS delivered successfully to +1+1234567890, Carrier: Verizon, Cost: $0.0100, Timestamp: 2024-01-14T17:07:00.123

SMS Notification:
SMS Status: Delivered, Delivery Report: SMS delivered successfully to +91+919876543210, Carrier: Airtel, Cost: $0.0200, Timestamp: 2024-01-14T17:07:00.123

Push Notification:
Push Status: Delivered, Delivery Report: Push notification delivered to iOS device, Platform: iOS, Click Status: Not clicked, Timestamp: 2024-01-14T17:07:00.123

Push Notification:
Push Status: Delivered, Delivery Report: Push notification delivered to Android device, Platform: Android, Click Status: Not clicked, Timestamp: 2024-01-14T17:07:00.123

⚡ PRIORITY HANDLING:
--------------------------------------------------
Email Notification:
Priority: 3 (Medium)
Urgent: false

Email Notification:
Priority: 1 (Critical)
Urgent: true

SMS Notification:
Priority: 2 (High)
Urgent: true

SMS Notification:
Priority: 4 (Low)
Urgent: false

Push Notification:
Priority: 3 (Medium)
Urgent: false

Push Notification:
Priority: 1 (Critical)
Urgent: true

✅ NOTIFICATION VALIDATION:
--------------------------------------------------
Email Notification:
Valid: true

Email Notification:
Valid: true

SMS Notification:
Valid: true

SMS Notification:
Valid: true

Push Notification:
Valid: true

Push Notification:
Valid: true

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing notifications using polymorphism:
1. Email Notification
   Recipient: alice@email.com
   Subject: Welcome to our service
   Priority: Medium
   Features: Email Features: Format: HTML, CC: Yes, BCC: No, Attachment: No, Delivery Report: Available, Priority: Medium

2. Email Notification
   Recipient: bob@email.com
   Subject: Urgent: Account Security Alert
   Priority: Critical
   Features: Email Features: Format: Plain Text, CC: No, BCC: No, Attachment: No, Delivery Report: Available, Priority: Critical

3. SMS Notification
   Recipient: +1234567890
   Subject: Order Confirmation
   Priority: High
   Features: SMS Features: Format: Text, Character Count: 75/160, Carrier: Verizon, International: No, Cost: $0.0100, Sender ID: COMPANY, Priority: High

4. SMS Notification
   Recipient: +919876543210
   Subject: Payment Reminder
   Priority: Low
   Features: SMS Features: Format: Text, Character Count: 45/160, Carrier: Airtel, International: Yes, Cost: $0.0200, Sender ID: PAYMENT, Priority: Low

5. Push Notification
   Recipient: device_token_123
   Subject: New Message
   Priority: Medium
   Features: Push Notification Features: Platform: iOS, App ID: com.company.app, Channel: messages, Sound: default, Icon: default, Image: No, Action URL: No, Silent: No, Badge Count: 1, Priority: Medium

6. Push Notification
   Recipient: device_token_456
   Subject: Breaking News
   Priority: Critical
   Features: Push Notification Features: Platform: Android, App ID: com.news.app, Channel: breaking_news, Sound: default, Icon: default, Image: No, Action URL: No, Silent: No, Badge Count: 1, Priority: Critical

🎯 INHERITANCE VS STRATEGY PATTERN DISCUSSION:
--------------------------------------------------
Inheritance Approach (Current Implementation):
- Each notification type extends Notification base class
- Common functionality in base class, specific behavior in subclasses
- Easy to add new notification types by extending base class
- Tight coupling between notification types and base class

Strategy Pattern Alternative:
- Notification class would contain a NotificationStrategy interface
- Different strategies (EmailStrategy, SMSStrategy, PushStrategy)
- Loose coupling, easy to change behavior at runtime
- More complex but more flexible for changing behavior

When to use Inheritance:
- When notification types share common structure and behavior
- When you need to add new notification types frequently
- When the relationship is truly 'is-a' (Email IS-A Notification)

When to use Strategy Pattern:
- When you need to change notification behavior at runtime
- When notification types have very different implementations
- When you want to avoid deep inheritance hierarchies

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Inheritance vs Strategy Pattern**: Understanding when to use each approach
2. **Method Overriding**: Each notification type provides its own implementation
3. **Abstract Methods**: Force subclasses to implement specific behaviors
4. **Real-world Business Logic**: Notification delivery and tracking
5. **Polymorphism**: Same method call produces different results based on object type
6. **Design Patterns**: Understanding trade-offs between different approaches

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world notification delivery and tracking
- **Design Patterns**: Inheritance vs Strategy pattern comparison

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Alternative approach for changing behavior at runtime
3. **Polymorphism**: Single interface for different notification types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.
