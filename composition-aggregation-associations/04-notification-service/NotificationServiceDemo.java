package composition.notification;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * MAANG-Level Demo: Notification Service with Multiple Channels
 * Demonstrates composition flexibility, async processing, and circuit breaker pattern
 */
public class NotificationServiceDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Notification Service Demo ===\n");
        
        // Create notification service
        NotificationService service = new NotificationService();
        
        // Add multiple channels
        System.out.println("--- Setting up Notification Channels ---");
        EmailChannel emailChannel = new EmailChannel("smtp.gmail.com", 587, "service@company.com");
        SMSChannel smsChannel = new SMSChannel("API_KEY_123", "Twilio");
        PushChannel pushChannel = new PushChannel("FCM_KEY_456", "Android/iOS");
        
        service.addChannel(emailChannel);
        service.addChannel(smsChannel);
        service.addChannel(pushChannel);
        
        // Create test message
        Map<String, String> metadata = new HashMap<>();
        metadata.put("campaign", "welcome_series");
        metadata.put("user_segment", "premium");
        
        NotificationMessage message = new NotificationMessage(
            "MSG-001",
            "user@example.com",
            "Welcome to Our Service!",
            "Thank you for joining our premium service. You now have access to exclusive features and priority support.",
            NotificationPriority.HIGH,
            metadata
        );
        
        // Test 1: Send to all channels
        System.out.println("\n--- Test 1: Broadcasting to All Channels ---");
        CompletableFuture<NotificationResult> future1 = service.sendToAll(message);
        
        NotificationResult result1 = future1.join();
        System.out.println("Broadcast Result: " + result1.getSuccessCount() + "/" + result1.getChannelResults().size() + " channels successful");
        
        // Test 2: Send via specific channel
        System.out.println("\n--- Test 2: Sending via Specific Channel (SMS) ---");
        CompletableFuture<ChannelResult> future2 = service.sendViaChannel(message, "SMS");
        
        ChannelResult result2 = future2.join();
        System.out.println("SMS Result: " + (result2.isSuccess() ? "Success" : "Failed - " + result2.getErrorMessage()));
        
        // Test 3: Fallback strategy
        System.out.println("\n--- Test 3: Fallback Strategy (Push → SMS → Email) ---");
        List<String> fallbackOrder = Arrays.asList("PUSH", "SMS", "EMAIL");
        
        CompletableFuture<ChannelResult> future3 = service.sendWithFallback(message, fallbackOrder);
        ChannelResult result3 = future3.join();
        System.out.println("Fallback Result: " + result3.getChannelType() + " - " + 
                         (result3.isSuccess() ? "Success" : "Failed"));
        
        // Test 4: Simulate channel failures to trigger circuit breaker
        System.out.println("\n--- Test 4: Simulating Channel Failures ---");
        emailChannel.setAvailable(false); // Make email unavailable
        
        // Send multiple messages to trigger circuit breaker
        for (int i = 0; i < 8; i++) {
            NotificationMessage testMsg = new NotificationMessage(
                "MSG-" + (i + 2),
                "test" + i + "@example.com",
                "Test Message " + i,
                "This is a test message to trigger circuit breaker.",
                NotificationPriority.MEDIUM,
                new HashMap<>()
            );
            
            service.sendToAll(testMsg).join();
            
            if (i == 3) {
                System.out.println("--- Checking metrics after some failures ---");
                service.displayMetrics();
            }
        }
        
        // Test 5: Runtime channel management
        System.out.println("\n--- Test 5: Runtime Channel Management ---");
        
        // Add new channel type
        SlackChannel slackChannel = new SlackChannel("SLACK_WEBHOOK_URL", "#alerts");
        service.addChannel(slackChannel);
        
        NotificationMessage alertMessage = new NotificationMessage(
            "ALERT-001",
            "#system-alerts",
            "System Alert",
            "High CPU usage detected on production servers.",
            NotificationPriority.CRITICAL,
            Map.of("alert_type", "system", "severity", "high")
        );
        
        service.sendToAll(alertMessage).join();
        
        // Remove a channel
        service.removeChannel(pushChannel);
        
        System.out.println("\n--- Final Metrics ---");
        service.displayMetrics();
        
        // Cleanup
        service.shutdown();
        
        System.out.println("\n=== Demo Complete: Notification service adapted to different channels and failure scenarios ===");
    }
    
    // Additional channel implementation for demo
    static class SlackChannel implements NotificationChannel {
        private final String webhookUrl;
        private final String channel;
        
        public SlackChannel(String webhookUrl, String channel) {
            this.webhookUrl = webhookUrl;
            this.channel = channel;
        }
        
        @Override
        public boolean sendMessage(NotificationMessage message) {
            try {
                Thread.sleep(100);
                System.out.println("💬 Slack message sent to: " + channel);
                System.out.println("   Alert: " + message.getSubject());
                System.out.println("   Details: " + message.getContent());
                return Math.random() < 0.88; // 88% success rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Slack notification interrupted");
            }
        }
        
        @Override
        public String getChannelType() { return "SLACK"; }
        
        @Override
        public boolean isAvailable() { return true; }
        
        @Override
        public int getMaxRetries() { return 2; }
    }
}
