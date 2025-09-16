package composition.notification;

/**
 * SMS Notification Channel Implementation
 */
public class SMSChannel implements NotificationChannel {
    private final String apiKey;
    private final String provider;
    private boolean isAvailable;
    
    public SMSChannel(String apiKey, String provider) {
        this.apiKey = apiKey;
        this.provider = provider;
        this.isAvailable = true;
    }
    
    @Override
    public boolean sendMessage(NotificationMessage message) {
        if (!isAvailable) {
            throw new RuntimeException("SMS service unavailable");
        }
        
        // Simulate SMS sending with potential failure
        try {
            Thread.sleep(100); // Simulate API call delay
            
            // 95% success rate for SMS
            if (Math.random() < 0.95) {
                String truncatedContent = message.getContent().length() > 160 ? 
                    message.getContent().substring(0, 157) + "..." : 
                    message.getContent();
                
                System.out.println("📱 SMS sent to: " + message.getRecipient());
                System.out.println("   Message: " + truncatedContent);
                System.out.println("   Provider: " + provider);
                return true;
            } else {
                throw new RuntimeException("SMS gateway error");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("SMS sending interrupted");
        }
    }
    
    @Override
    public String getChannelType() {
        return "SMS";
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
