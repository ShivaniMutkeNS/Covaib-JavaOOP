package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Comprehensive demo class for the Notification System abstraction
 * Demonstrates polymorphic usage of different notification channels
 */
public class NotificationSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== NOTIFICATION SYSTEM ABSTRACTION DEMO ===\n");
        
        NotificationSystemDemo demo = new NotificationSystemDemo();
        
        try {
            // Test polymorphic notification systems
            demo.testPolymorphicNotificationSystems();
            
            // Test bulk notifications
            demo.testBulkNotifications();
            
            // Test notification scheduling
            demo.testNotificationScheduling();
            
            // Test channel-specific features
            demo.testChannelSpecificFeatures();
            
            // Test system health and monitoring
            demo.testSystemHealthAndMonitoring();
            
            // Test error handling and retry logic
            demo.testErrorHandlingAndRetry();
            
        } catch (Exception e) {
            System.err.println("Demo execution failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== NOTIFICATION SYSTEM DEMO COMPLETED ===");
    }
    
    private void testPolymorphicNotificationSystems() {
        System.out.println("1. TESTING POLYMORPHIC NOTIFICATION SYSTEMS");
        System.out.println("=" .repeat(50));
        
        // Create different notification systems
        List<NotificationSystem> systems = createNotificationSystems();
        
        // Test each system polymorphically
        for (NotificationSystem system : systems) {
            testNotificationSystem(system);
        }
        
        System.out.println();
    }
    
    private List<NotificationSystem> createNotificationSystems() {
        List<NotificationSystem> systems = new ArrayList<>();
        
        // Email notification system
        Map<String, Object> emailConfig = new HashMap<>();
        emailConfig.put("smtp_host", "smtp.example.com");
        emailConfig.put("smtp_port", 587);
        emailConfig.put("username", "notifications@example.com");
        emailConfig.put("password", "secure_password");
        emailConfig.put("use_tls", true);
        
        EmailNotificationSystem emailSystem = new EmailNotificationSystem(
            "email_sys_001", "Email Notification Service", emailConfig);
        systems.add(emailSystem);
        
        // SMS notification system
        Map<String, Object> smsConfig = new HashMap<>();
        smsConfig.put("gateway_provider", "twilio");
        smsConfig.put("api_key", "twilio_api_key");
        smsConfig.put("sender_id", "MyApp");
        smsConfig.put("enable_delivery_reports", true);
        smsConfig.put("max_message_length", 160);
        
        SMSNotificationSystem smsSystem = new SMSNotificationSystem(
            "sms_sys_001", "SMS Notification Service", smsConfig);
        systems.add(smsSystem);
        
        // Push notification system
        Map<String, Object> pushConfig = new HashMap<>();
        pushConfig.put("service_provider", "fcm");
        pushConfig.put("server_key", "fcm_server_key");
        pushConfig.put("application_id", "com.example.myapp");
        pushConfig.put("enable_badging", true);
        pushConfig.put("enable_sound", true);
        pushConfig.put("max_payload_size", 4096);
        
        PushNotificationSystem pushSystem = new PushNotificationSystem(
            "push_sys_001", "Push Notification Service", pushConfig);
        systems.add(pushSystem);
        
        return systems;
    }
    
    private void testNotificationSystem(NotificationSystem system) {
        System.out.println("Testing " + system.getSystemName() + " (" + system.getChannel() + ")");
        
        try {
            // Create test notification request
            NotificationRequest request = createTestNotificationRequest(system.getChannel());
            
            // Send notification
            CompletableFuture<NotificationResult> future = system.sendNotification(request);
            NotificationResult result = future.get();
            
            System.out.println("  Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Message: " + result.getMessage());
            if (result.getMessageId() != null) {
                System.out.println("  Message ID: " + result.getMessageId());
            }
            if (result.getErrorCode() != null) {
                System.out.println("  Error Code: " + result.getErrorCode());
            }
            
        } catch (Exception e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private NotificationRequest createTestNotificationRequest(NotificationChannel channel) {
        NotificationRecipient recipient = createTestRecipient(channel);
        NotificationMessage message = createTestMessage(channel);
        NotificationOptions options = createTestOptions(channel);
        
        return new NotificationRequest(
            "test_" + System.currentTimeMillis(),
            recipient,
            message,
            NotificationPriority.NORMAL,
            options
        );
    }
    
    private NotificationRecipient createTestRecipient(NotificationChannel channel) {
        String contactInfo;
        switch (channel) {
            case EMAIL:
                contactInfo = "test.user@example.com";
                break;
            case SMS:
                contactInfo = "+1234567890";
                break;
            case PUSH:
                contactInfo = "device_token_abc123xyz789_sample_fcm_token_for_testing_purposes_only";
                break;
            default:
                contactInfo = "test@example.com";
        }
        
        return new NotificationRecipient("user_123", "Test User", contactInfo, channel);
    }
    
    private NotificationMessage createTestMessage(NotificationChannel channel) {
        String subject, content;
        
        switch (channel) {
            case EMAIL:
                subject = "Welcome to Our Service";
                content = "Thank you for joining our service! We're excited to have you on board. " +
                         "You can now access all premium features and enjoy our comprehensive platform.";
                break;
            case SMS:
                subject = null; // SMS doesn't typically have subjects
                content = "Welcome to MyApp! Your account is now active. Reply STOP to opt out.";
                break;
            case PUSH:
                subject = "Welcome!";
                content = "Your account is ready. Tap to explore new features.";
                break;
            default:
                subject = "Test Notification";
                content = "This is a test notification message.";
        }
        
        return new NotificationMessage(
            "msg_" + System.currentTimeMillis(),
            subject,
            content,
            MessageType.TRANSACTIONAL
        );
    }
    
    private NotificationOptions createTestOptions(NotificationChannel channel) {
        NotificationOptions options = new NotificationOptions();
        
        switch (channel) {
            case EMAIL:
                options.addChannelSpecificOption("reply_to", "support@example.com");
                options.addChannelSpecificOption("html_content", 
                    "<h1>Welcome!</h1><p>Thank you for joining our service!</p>");
                break;
            case SMS:
                options.addChannelSpecificOption("unicode_enabled", false);
                options.addChannelSpecificOption("validity_period_hours", 24);
                break;
            case PUSH:
                options.addChannelSpecificOption("badge_count", 1);
                options.addChannelSpecificOption("sound", "default");
                options.addChannelSpecificOption("category", "welcome");
                Map<String, Object> customData = new HashMap<>();
                customData.put("action", "welcome");
                customData.put("user_id", "123");
                options.addChannelSpecificOption("custom_data", customData);
                break;
        }
        
        return options;
    }
    
    private void testBulkNotifications() {
        System.out.println("2. TESTING BULK NOTIFICATIONS");
        System.out.println("=" .repeat(50));
        
        // Create email system for bulk testing
        Map<String, Object> emailConfig = new HashMap<>();
        emailConfig.put("smtp_host", "smtp.example.com");
        emailConfig.put("smtp_port", 587);
        emailConfig.put("username", "bulk@example.com");
        emailConfig.put("password", "secure_password");
        
        EmailNotificationSystem emailSystem = new EmailNotificationSystem(
            "bulk_email_sys", "Bulk Email Service", emailConfig);
        
        try {
            // Create bulk notification request
            BulkNotificationRequest bulkRequest = createBulkNotificationRequest();
            
            System.out.println("Sending bulk notifications to " + bulkRequest.getRequests().size() + " recipients...");
            
            CompletableFuture<BulkNotificationResult> future = emailSystem.sendBulkNotifications(bulkRequest);
            BulkNotificationResult result = future.get();
            
            System.out.println("Bulk Status: " + (result.isSuccess() ? "SUCCESS" : "PARTIAL/FAILED"));
            System.out.println("Total Sent: " + result.getResults().size());
            
            long successCount = result.getResults().stream()
                .mapToLong(r -> r.isSuccess() ? 1 : 0)
                .sum();
            
            System.out.println("Successful: " + successCount);
            System.out.println("Failed: " + (result.getResults().size() - successCount));
            
            if (!result.getFailedRequests().isEmpty()) {
                System.out.println("Failed Request IDs: " + 
                    result.getFailedRequests().stream()
                        .map(NotificationRequest::getRequestId)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None"));
            }
            
        } catch (Exception e) {
            System.out.println("Bulk notification error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private BulkNotificationRequest createBulkNotificationRequest() {
        List<NotificationRequest> requests = new ArrayList<>();
        
        // Create multiple notification requests
        for (int i = 1; i <= 5; i++) {
            NotificationRecipient recipient = new NotificationRecipient(
                "user_" + i,
                "User " + i,
                "user" + i + "@example.com",
                NotificationChannel.EMAIL
            );
            
            NotificationMessage message = new NotificationMessage(
                "bulk_msg_" + i,
                "Newsletter #" + i,
                "This is newsletter content for user " + i + ". Thank you for subscribing!",
                MessageType.MARKETING
            );
            
            NotificationRequest request = new NotificationRequest(
                "bulk_req_" + i,
                recipient,
                message,
                NotificationPriority.NORMAL,
                new NotificationOptions()
            );
            
            requests.add(request);
        }
        
        return new BulkNotificationRequest("bulk_" + System.currentTimeMillis(), requests);
    }
    
    private void testNotificationScheduling() {
        System.out.println("3. TESTING NOTIFICATION SCHEDULING");
        System.out.println("=" .repeat(50));
        
        // Create SMS system for scheduling test
        Map<String, Object> smsConfig = new HashMap<>();
        smsConfig.put("gateway_provider", "test_provider");
        smsConfig.put("api_key", "test_key");
        
        SMSNotificationSystem smsSystem = new SMSNotificationSystem(
            "schedule_sms_sys", "Scheduled SMS Service", smsConfig);
        
        try {
            // Create scheduled notification request
            NotificationRequest request = createTestNotificationRequest(NotificationChannel.SMS);
            LocalDateTime scheduleTime = LocalDateTime.now().plusMinutes(5);
            
            System.out.println("Scheduling SMS notification for: " + scheduleTime);
            
            CompletableFuture<ScheduleResult> future = smsSystem.scheduleNotification(request, scheduleTime);
            ScheduleResult result = future.get();
            
            System.out.println("Schedule Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("Schedule Message: " + result.getMessage());
            if (result.getScheduleId() != null) {
                System.out.println("Schedule ID: " + result.getScheduleId());
            }
            
            // Test canceling the scheduled notification
            if (result.isSuccess() && result.getScheduleId() != null) {
                System.out.println("\nTesting schedule cancellation...");
                
                CompletableFuture<CancelResult> cancelFuture = smsSystem.cancelScheduledNotification(result.getScheduleId());
                CancelResult cancelResult = cancelFuture.get();
                
                System.out.println("Cancel Status: " + (cancelResult.isSuccess() ? "SUCCESS" : "FAILED"));
                System.out.println("Cancel Message: " + cancelResult.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Scheduling error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testChannelSpecificFeatures() {
        System.out.println("4. TESTING CHANNEL-SPECIFIC FEATURES");
        System.out.println("=" .repeat(50));
        
        testEmailSpecificFeatures();
        testSMSSpecificFeatures();
        testPushSpecificFeatures();
        
        System.out.println();
    }
    
    private void testEmailSpecificFeatures() {
        System.out.println("Email-Specific Features:");
        
        Map<String, Object> emailConfig = new HashMap<>();
        emailConfig.put("smtp_host", "smtp.example.com");
        
        EmailNotificationSystem emailSystem = new EmailNotificationSystem(
            "email_features_sys", "Email Features Test", emailConfig);
        
        try {
            // Test email template creation
            EmailTemplateRequest templateRequest = new EmailTemplateRequest(
                "welcome_email",
                "Welcome to {{service_name}}!",
                "Dear {{name}}, welcome to our service!",
                "<h1>Welcome {{name}}!</h1><p>Thank you for joining {{service_name}}.</p>"
            );
            
            CompletableFuture<EmailTemplateResult> templateFuture = emailSystem.createEmailTemplate(templateRequest);
            EmailTemplateResult templateResult = templateFuture.get();
            
            System.out.println("  Email Template Creation: " + 
                (templateResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Template Message: " + templateResult.getMessage());
            
            // Test attachment handling
            byte[] sampleData = "Sample attachment content".getBytes();
            AttachmentRequest attachmentRequest = new AttachmentRequest(
                "sample.txt", "text/plain", sampleData);
            
            CompletableFuture<AttachmentResult> attachmentFuture = emailSystem.addAttachment(attachmentRequest);
            AttachmentResult attachmentResult = attachmentFuture.get();
            
            System.out.println("  Attachment Processing: " + 
                (attachmentResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Attachment Message: " + attachmentResult.getMessage());
            
            // Test bounce handling
            CompletableFuture<BounceHandlingResult> bounceFuture = emailSystem.handleBounces();
            BounceHandlingResult bounceResult = bounceFuture.get();
            
            System.out.println("  Bounce Handling: " + 
                (bounceResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Bounces Processed: " + bounceResult.getBounces().size());
            
        } catch (Exception e) {
            System.out.println("  Email features error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testSMSSpecificFeatures() {
        System.out.println("SMS-Specific Features:");
        
        Map<String, Object> smsConfig = new HashMap<>();
        smsConfig.put("gateway_provider", "test_provider");
        
        SMSNotificationSystem smsSystem = new SMSNotificationSystem(
            "sms_features_sys", "SMS Features Test", smsConfig);
        
        try {
            // Test phone number validation
            CompletableFuture<PhoneValidationResult> validationFuture = 
                smsSystem.validatePhoneNumber("+1234567890");
            PhoneValidationResult validationResult = validationFuture.get();
            
            System.out.println("  Phone Validation: " + 
                (validationResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Validation Message: " + validationResult.getMessage());
            
            // Test message segmentation
            String longMessage = "This is a very long SMS message that will definitely exceed the standard 160 character limit for SMS messages and will need to be split into multiple segments for proper delivery.";
            
            CompletableFuture<MessageSegmentResult> segmentFuture = 
                smsSystem.analyzeMessageSegments(longMessage);
            MessageSegmentResult segmentResult = segmentFuture.get();
            
            System.out.println("  Message Segmentation: " + 
                (segmentResult.isSuccess() ? "SUCCESS" : "FAILED"));
            if (segmentResult.isSuccess()) {
                MessageSegmentInfo info = segmentResult.getSegmentInfo();
                System.out.println("  Message Length: " + info.getLength());
                System.out.println("  Segment Count: " + info.getSegmentCount());
                System.out.println("  Unicode: " + info.isUnicode());
            }
            
            // Test SMS template creation
            SMSTemplateRequest templateRequest = new SMSTemplateRequest(
                "otp_sms", "Your OTP is {{otp_code}}. Valid for {{validity}} minutes.");
            
            CompletableFuture<SMSTemplateResult> templateFuture = smsSystem.createSMSTemplate(templateRequest);
            SMSTemplateResult templateResult = templateFuture.get();
            
            System.out.println("  SMS Template Creation: " + 
                (templateResult.isSuccess() ? "SUCCESS" : "FAILED"));
            
            // Test delivery reports
            CompletableFuture<DeliveryReportResult> reportFuture = smsSystem.getDeliveryReports();
            DeliveryReportResult reportResult = reportFuture.get();
            
            System.out.println("  Delivery Reports: " + 
                (reportResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Reports Retrieved: " + reportResult.getReports().size());
            
        } catch (Exception e) {
            System.out.println("  SMS features error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testPushSpecificFeatures() {
        System.out.println("Push-Specific Features:");
        
        Map<String, Object> pushConfig = new HashMap<>();
        pushConfig.put("service_provider", "fcm");
        pushConfig.put("server_key", "test_key");
        pushConfig.put("application_id", "com.test.app");
        
        PushNotificationSystem pushSystem = new PushNotificationSystem(
            "push_features_sys", "Push Features Test", pushConfig);
        
        try {
            // Test device registration
            DeviceRegistrationRequest deviceRequest = new DeviceRegistrationRequest(
                "sample_device_token_for_testing_purposes_only_123456789",
                "user_123",
                "iOS",
                "1.0.0",
                "iPhone 12"
            );
            
            CompletableFuture<DeviceRegistrationResult> deviceFuture = 
                pushSystem.registerDevice(deviceRequest);
            DeviceRegistrationResult deviceResult = deviceFuture.get();
            
            System.out.println("  Device Registration: " + 
                (deviceResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Registration Message: " + deviceResult.getMessage());
            
            // Test push template creation
            PushTemplateRequest templateRequest = new PushTemplateRequest(
                "news_push",
                "{{category}} News",
                "{{headline}} - Tap to read more",
                "news"
            );
            
            CompletableFuture<PushTemplateResult> templateFuture = 
                pushSystem.createPushTemplate(templateRequest);
            PushTemplateResult templateResult = templateFuture.get();
            
            System.out.println("  Push Template Creation: " + 
                (templateResult.isSuccess() ? "SUCCESS" : "FAILED"));
            
            // Test badge management
            String deviceToken = "sample_device_token_for_testing_purposes_only_123456789";
            CompletableFuture<BadgeUpdateResult> badgeFuture = 
                pushSystem.updateBadgeCount(deviceToken, 5);
            BadgeUpdateResult badgeResult = badgeFuture.get();
            
            System.out.println("  Badge Update: " + 
                (badgeResult.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("  Badge Count: " + badgeResult.getBadgeCount());
            
            // Test push analytics
            CompletableFuture<PushAnalyticsResult> analyticsFuture = pushSystem.getPushAnalytics();
            PushAnalyticsResult analyticsResult = analyticsFuture.get();
            
            System.out.println("  Push Analytics: " + 
                (analyticsResult.isSuccess() ? "SUCCESS" : "FAILED"));
            if (analyticsResult.isSuccess()) {
                PushAnalytics analytics = analyticsResult.getAnalytics();
                System.out.println("  Total Sent: " + analytics.getTotalSent());
                System.out.println("  Delivery Rate: " + String.format("%.1f%%", analytics.getDeliveryRate()));
                System.out.println("  Open Rate: " + String.format("%.1f%%", analytics.getOpenRate()));
            }
            
        } catch (Exception e) {
            System.out.println("  Push features error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private void testSystemHealthAndMonitoring() {
        System.out.println("5. TESTING SYSTEM HEALTH AND MONITORING");
        System.out.println("=" .repeat(50));
        
        List<NotificationSystem> systems = createNotificationSystems();
        
        for (NotificationSystem system : systems) {
            try {
                System.out.println("Health Check: " + system.getSystemName());
                
                CompletableFuture<HealthCheckResult> future = system.performHealthCheck();
                HealthCheckResult result = future.get();
                
                System.out.println("  Overall Health: " + 
                    (result.isHealthy() ? "HEALTHY" : "UNHEALTHY"));
                System.out.println("  Status Message: " + result.getMessage());
                
                if (result.getComponentStatuses() != null) {
                    System.out.println("  Component Health:");
                    result.getComponentStatuses().forEach((component, status) -> 
                        System.out.println("    " + component + ": " + status));
                }
                
                // Get system status
                NotificationSystemStatus status = system.getSystemStatus();
                System.out.println("  System Status: " + status.getStatus());
                System.out.println("  Uptime: " + status.getUptime() + " seconds");
                
                // Get metrics
                NotificationMetrics metrics = system.getMetrics();
                System.out.println("  Metrics:");
                System.out.println("    Total Sent: " + metrics.getTotalSent());
                System.out.println("    Total Delivered: " + metrics.getTotalDelivered());
                System.out.println("    Delivery Rate: " + String.format("%.1f%%", metrics.getDeliveryRate()));
                
            } catch (Exception e) {
                System.out.println("  Health check error: " + e.getMessage());
            }
            
            System.out.println();
        }
    }
    
    private void testErrorHandlingAndRetry() {
        System.out.println("6. TESTING ERROR HANDLING AND RETRY LOGIC");
        System.out.println("=" .repeat(50));
        
        // Create a system that might fail
        Map<String, Object> testConfig = new HashMap<>();
        testConfig.put("smtp_host", "invalid.smtp.server");
        testConfig.put("smtp_port", 9999);
        
        EmailNotificationSystem testSystem = new EmailNotificationSystem(
            "error_test_sys", "Error Test System", testConfig);
        
        try {
            // Create a notification that might fail
            NotificationRequest request = createTestNotificationRequest(NotificationChannel.EMAIL);
            
            System.out.println("Sending notification with potential failure...");
            
            CompletableFuture<NotificationResult> future = testSystem.sendNotification(request);
            NotificationResult result = future.get();
            
            System.out.println("Result Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
            System.out.println("Result Message: " + result.getMessage());
            
            if (!result.isSuccess()) {
                System.out.println("Error Code: " + result.getErrorCode());
                
                // Demonstrate retry logic would be triggered
                System.out.println("Retry logic would be automatically triggered for retryable errors");
            }
            
        } catch (Exception e) {
            System.out.println("Expected error occurred: " + e.getMessage());
            System.out.println("This demonstrates proper error handling in the system");
        }
        
        System.out.println();
    }
}
