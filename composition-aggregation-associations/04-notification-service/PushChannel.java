package composition.notification;

/**
 * Push Notification Channel Implementation
 */
public class PushChannel implements NotificationChannel {
    private final String fcmServerKey;
    private final String platform;
    private boolean isAvailable;
    
    public PushChannel(String fcmServerKey, String platform) {
        this.fcmServerKey = fcmServerKey;
        this.platform = platform;
        this.isAvailable = true;
    }
    
    @Override
    public boolean sendMessage(NotificationMessage message) {
        if (!isAvailable) {
            throw new RuntimeException("Push notification service unavailable");
        }
        
        // Simulate push notification sending
        try {
            Thread.sleep(150); // Simulate FCM/APNS delay
            
            // 92% success rate for push notifications
            if (Math.random() < 0.92) {
                System.out.println("🔔 Push notification sent to: " + message.getRecipient());
                System.out.println("   Title: " + message.getSubject());
                System.out.println("   Body: " + message.getContent().substring(0, Math.min(100, message.getContent().length())) + "...");
                System.out.println("   Platform: " + platform);
                return true;
            } else {
                throw new RuntimeException("Push service delivery failed");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Push notification interrupted");
        }
    }
    
    @Override
    public String getChannelType() {
        return "PUSH";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public int getMaxRetries() {
        return 2;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
