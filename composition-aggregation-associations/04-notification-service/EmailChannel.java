package composition.notification;

/**
 * Email Notification Channel Implementation
 */
public class EmailChannel implements NotificationChannel {
    private final String smtpServer;
    private final int port;
    private final String username;
    private boolean isAvailable;
    
    public EmailChannel(String smtpServer, int port, String username) {
        this.smtpServer = smtpServer;
        this.port = port;
        this.username = username;
        this.isAvailable = true;
    }
    
    @Override
    public boolean sendMessage(NotificationMessage message) {
        if (!isAvailable) {
            throw new RuntimeException("Email service unavailable");
        }
        
        // Simulate email sending with potential failure
        try {
            Thread.sleep(200); // Simulate network delay
            
            // 90% success rate
            if (Math.random() < 0.9) {
                System.out.println("📧 Email sent to: " + message.getRecipient());
                System.out.println("   Subject: " + message.getSubject());
                System.out.println("   Content: " + message.getContent().substring(0, Math.min(50, message.getContent().length())) + "...");
                return true;
            } else {
                throw new RuntimeException("SMTP server timeout");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Email sending interrupted");
        }
    }
    
    @Override
    public String getChannelType() {
        return "EMAIL";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public int getMaxRetries() {
        return 3;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
