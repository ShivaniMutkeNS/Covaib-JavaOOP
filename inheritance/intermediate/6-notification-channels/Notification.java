/**
 * Abstract base class for all notification channels
 * Demonstrates inheritance vs strategy pattern
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Notification {
    protected String recipient;
    protected String subject;
    protected String message;
    protected String timestamp;
    protected boolean isSent;
    protected String notificationType;
    protected int priority;
    protected String status;
    
    /**
     * Constructor for Notification
     * @param recipient Recipient of the notification
     * @param subject Subject of the notification
     * @param message Message content
     * @param priority Priority level (1-5, 1 being highest)
     * @param notificationType Type of notification
     */
    public Notification(String recipient, String subject, String message, int priority, String notificationType) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.priority = Math.max(1, Math.min(5, priority)); // Clamp between 1-5
        this.notificationType = notificationType;
        this.isSent = false;
        this.status = "Pending";
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    /**
     * Abstract method to send notification
     * Each notification type has different sending logic
     * @return True if notification sent successfully
     */
    public abstract boolean send();
    
    /**
     * Abstract method to get delivery status
     * Each notification type has different delivery tracking
     * @return String with delivery status
     */
    public abstract String getDeliveryStatus();
    
    /**
     * Abstract method to get notification features
     * Each notification type has different features
     * @return String description of features
     */
    public abstract String getNotificationFeatures();
    
    /**
     * Concrete method to validate notification
     * @return True if notification is valid
     */
    public boolean validate() {
        if (recipient == null || recipient.trim().isEmpty()) {
            System.out.println("Error: Recipient cannot be empty");
            return false;
        }
        
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Error: Message cannot be empty");
            return false;
        }
        
        if (subject == null || subject.trim().isEmpty()) {
            System.out.println("Error: Subject cannot be empty");
            return false;
        }
        
        return true;
    }
    
    /**
     * Concrete method to get notification information
     * @return String with notification details
     */
    public String getNotificationInfo() {
        return String.format("Type: %s, Recipient: %s, Subject: %s, Priority: %d, Status: %s, Sent: %s", 
                           notificationType, recipient, subject, priority, status, isSent);
    }
    
    /**
     * Concrete method to get priority description
     * @return String with priority description
     */
    public String getPriorityDescription() {
        switch (priority) {
            case 1: return "Critical";
            case 2: return "High";
            case 3: return "Medium";
            case 4: return "Low";
            case 5: return "Very Low";
            default: return "Unknown";
        }
    }
    
    /**
     * Concrete method to check if notification is urgent
     * @return True if priority is 1 or 2
     */
    public boolean isUrgent() {
        return priority <= 2;
    }
    
    /**
     * Getter for recipient
     * @return The recipient
     */
    public String getRecipient() {
        return recipient;
    }
    
    /**
     * Getter for subject
     * @return The subject
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Getter for message
     * @return The message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Getter for timestamp
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    
    /**
     * Getter for sent status
     * @return True if notification is sent
     */
    public boolean isSent() {
        return isSent;
    }
    
    /**
     * Getter for notification type
     * @return The notification type
     */
    public String getNotificationType() {
        return notificationType;
    }
    
    /**
     * Getter for priority
     * @return The priority level
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Getter for status
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Override toString method
     * @return String representation of the notification
     */
    @Override
    public String toString() {
        return getNotificationInfo();
    }
}
