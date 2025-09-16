package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Push Notification System - Concrete implementation for push notifications
 * Demonstrates mobile push notification integration and push-specific features
 */
public class PushNotificationSystem extends NotificationSystem {
    
    private PushServiceClient pushClient;
    private DeviceTokenManager tokenManager;
    private PushTemplateManager templateManager;
    private BadgeManager badgeManager;
    
    // Push-specific configuration
    private String serviceProvider;
    private String serverKey;
    private String applicationId;
    private boolean enableBadging;
    private boolean enableSound;
    private int maxPayloadSize;
    
    public PushNotificationSystem(String systemId, String systemName, Map<String, Object> configuration) {
        super(systemId, systemName, NotificationChannel.PUSH, configuration);
    }
    
    @Override
    protected void initialize() {
        // Initialize push-specific settings
        this.serviceProvider = (String) configuration.getOrDefault("service_provider", "fcm");
        this.serverKey = (String) configuration.getOrDefault("server_key", "");
        this.applicationId = (String) configuration.getOrDefault("application_id", "");
        this.enableBadging = (Boolean) configuration.getOrDefault("enable_badging", true);
        this.enableSound = (Boolean) configuration.getOrDefault("enable_sound", true);
        this.maxPayloadSize = (Integer) configuration.getOrDefault("max_payload_size", 4096);
        
        this.pushClient = new PushServiceClient(serviceProvider, serverKey, applicationId);
        this.tokenManager = new DeviceTokenManager();
        this.templateManager = new PushTemplateManager();
        this.badgeManager = new BadgeManager();
        
        // Configure push client
        configurePushClient();
    }
    
    @Override
    protected MessageProcessor createMessageProcessor() {
        return new PushMessageProcessor(maxPayloadSize);
    }
    
    @Override
    protected NotificationResult performChannelDelivery(NotificationRequest request, ProcessedMessage message) {
        try {
            // Create push notification
            PushNotification pushNotification = createPushNotification(request, message);
            
            // Send push notification
            PushResult pushResult = pushClient.sendPush(pushNotification);
            
            if (pushResult.isSuccess()) {
                return NotificationResult.success(
                    "Push notification sent successfully",
                    pushResult.getMessageId()
                );
            } else {
                NotificationResult result = NotificationResult.failure("Push delivery failed: " + pushResult.getErrorMessage());
                result.setErrorCode(pushResult.getErrorCode());
                return result;
            }
            
        } catch (Exception e) {
            NotificationResult result = NotificationResult.failure("Push delivery error: " + e.getMessage());
            result.setErrorCode("DELIVERY_ERROR");
            return result;
        }
    }
    
    @Override
    protected ValidationResult validateChannelSpecificOptions(NotificationOptions options) {
        try {
            if (options.getChannelSpecificOptions().containsKey("badge_count")) {
                Integer badgeCount = (Integer) options.getChannelSpecificOptions().get("badge_count");
                if (badgeCount != null && badgeCount < 0) {
                    return ValidationResult.failure("Badge count cannot be negative");
                }
            }
            
            if (options.getChannelSpecificOptions().containsKey("sound")) {
                String sound = (String) options.getChannelSpecificOptions().get("sound");
                if (sound != null && sound.length() > 50) {
                    return ValidationResult.failure("Sound filename too long");
                }
            }
            
            if (options.getChannelSpecificOptions().containsKey("custom_data")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> customData = (Map<String, Object>) options.getChannelSpecificOptions().get("custom_data");
                if (customData != null && customData.size() > 20) {
                    return ValidationResult.failure("Too many custom data fields (max 20)");
                }
            }
            
            return ValidationResult.success("Push options validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("Push options validation error: " + e.getMessage());
        }
    }
    
    @Override
    protected String getChannelEndpoint() {
        return pushClient.getServiceEndpoint();
    }
    
    @Override
    protected Map<String, String> getChannelHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Push-Service", serviceProvider);
        headers.put("X-System-ID", systemId);
        headers.put("X-Channel", "PUSH");
        headers.put("X-App-ID", applicationId);
        return headers;
    }
    
    private void configurePushClient() {
        Map<String, Object> clientConfig = new HashMap<>();
        clientConfig.put("connection_timeout", 30000);
        clientConfig.put("read_timeout", 60000);
        clientConfig.put("max_retries", 3);
        clientConfig.put("batch_size", 1000);
        clientConfig.put("enable_compression", true);
        
        pushClient.configure(clientConfig);
    }
    
    private PushNotification createPushNotification(NotificationRequest request, ProcessedMessage message) {
        PushNotification pushNotification = new PushNotification();
        
        // Basic push properties
        pushNotification.setDeviceToken(request.getRecipient().getContactInfo());
        pushNotification.setTitle(message.getProcessedSubject());
        pushNotification.setBody(message.getProcessedContent());
        pushNotification.setPriority(mapPriorityToPush(request.getPriority()));
        
        // Default settings
        if (enableBadging) {
            pushNotification.setBadge(1);
        }
        
        if (enableSound) {
            pushNotification.setSound("default");
        }
        
        // Add channel-specific options
        if (request.getOptions() != null) {
            Map<String, Object> channelOptions = request.getOptions().getChannelSpecificOptions();
            
            // Badge count
            if (channelOptions.containsKey("badge_count")) {
                Integer badgeCount = (Integer) channelOptions.get("badge_count");
                if (badgeCount != null) {
                    pushNotification.setBadge(badgeCount);
                }
            }
            
            // Sound
            if (channelOptions.containsKey("sound")) {
                String sound = (String) channelOptions.get("sound");
                if (sound != null && !sound.isEmpty()) {
                    pushNotification.setSound(sound);
                }
            }
            
            // Category/Action
            if (channelOptions.containsKey("category")) {
                pushNotification.setCategory((String) channelOptions.get("category"));
            }
            
            // Custom data
            if (channelOptions.containsKey("custom_data")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> customData = (Map<String, Object>) channelOptions.get("custom_data");
                pushNotification.setCustomData(customData);
            }
            
            // Time to live
            if (channelOptions.containsKey("ttl_seconds")) {
                Integer ttl = (Integer) channelOptions.get("ttl_seconds");
                if (ttl != null && ttl > 0) {
                    pushNotification.setTimeToLive(ttl);
                }
            }
        }
        
        return pushNotification;
    }
    
    private PushPriority mapPriorityToPush(NotificationPriority priority) {
        switch (priority) {
            case CRITICAL:
            case URGENT:
                return PushPriority.HIGH;
            case HIGH:
                return PushPriority.NORMAL;
            case NORMAL:
            case LOW:
            default:
                return PushPriority.LOW;
        }
    }
    
    @Override
    protected boolean checkConnectivity() {
        try {
            return pushClient.testConnection();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Push-specific methods
    public CompletableFuture<DeviceRegistrationResult> registerDevice(DeviceRegistrationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return tokenManager.registerDevice(request);
            } catch (Exception e) {
                return DeviceRegistrationResult.failure("Device registration failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<PushTemplateResult> createPushTemplate(PushTemplateRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return templateManager.createTemplate(request);
            } catch (Exception e) {
                return PushTemplateResult.failure("Push template creation failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<BadgeUpdateResult> updateBadgeCount(String deviceToken, int badgeCount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return badgeManager.updateBadge(deviceToken, badgeCount);
            } catch (Exception e) {
                return BadgeUpdateResult.failure("Badge update failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<PushAnalyticsResult> getPushAnalytics() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return pushClient.getAnalytics();
            } catch (Exception e) {
                return PushAnalyticsResult.failure("Analytics retrieval failed: " + e.getMessage());
            }
        });
    }
    
    // Getters for push-specific properties
    public String getServiceProvider() { return serviceProvider; }
    public String getServerKey() { return serverKey; }
    public String getApplicationId() { return applicationId; }
    public boolean isEnableBadging() { return enableBadging; }
    public boolean isEnableSound() { return enableSound; }
    public int getMaxPayloadSize() { return maxPayloadSize; }
    public PushServiceClient getPushClient() { return pushClient; }
    public DeviceTokenManager getTokenManager() { return tokenManager; }
    public PushTemplateManager getTemplateManager() { return templateManager; }
    public BadgeManager getBadgeManager() { return badgeManager; }
}

// Push-specific message processor
class PushMessageProcessor extends GenericMessageProcessor {
    private int maxPayloadSize;
    
    public PushMessageProcessor(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }
    
    @Override
    protected void initializeProcessor() {
        super.initializeProcessor();
        processingConfig.put("truncate_for_payload", true);
        processingConfig.put("optimize_for_mobile", true);
        processingConfig.put("add_action_buttons", false);
        processingConfig.put("localize_content", false);
    }
    
    @Override
    public ProcessingResult processMessage(NotificationMessage message) {
        // Call parent processing first
        ProcessingResult baseResult = super.processMessage(message);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        
        ProcessedMessage processedMessage = baseResult.getProcessedMessage();
        
        // Push-specific processing
        if (Boolean.TRUE.equals(processingConfig.get("optimize_for_mobile"))) {
            optimizeForMobile(processedMessage);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("truncate_for_payload"))) {
            truncateForPayload(processedMessage);
        }
        
        return ProcessingResult.success("Push message processing completed", processedMessage);
    }
    
    private void optimizeForMobile(ProcessedMessage message) {
        String content = message.getProcessedContent();
        
        // Optimize for mobile viewing
        content = content.replaceAll("\\s+", " "); // Normalize whitespace
        content = content.trim();
        
        // Ensure first sentence is impactful (for preview)
        if (content.length() > 100) {
            int firstSentenceEnd = content.indexOf(". ");
            if (firstSentenceEnd > 0 && firstSentenceEnd < 100) {
                // Good first sentence length
            } else {
                // Truncate to first 100 characters with ellipsis
                content = content.substring(0, 97) + "...";
            }
        }
        
        message.setProcessedContent(content);
        message.addProcessingStep(new ProcessingStep("mobile_optimization", "Content optimized for mobile", true));
    }
    
    private void truncateForPayload(ProcessedMessage message) {
        // Estimate payload size (simplified)
        String title = message.getProcessedSubject() != null ? message.getProcessedSubject() : "";
        String body = message.getProcessedContent() != null ? message.getProcessedContent() : "";
        
        int estimatedSize = title.length() + body.length() + 500; // 500 bytes for metadata
        
        if (estimatedSize > maxPayloadSize) {
            int availableForContent = maxPayloadSize - title.length() - 500;
            if (availableForContent > 0 && body.length() > availableForContent) {
                String truncated = body.substring(0, availableForContent - 3) + "...";
                message.setProcessedContent(truncated);
                message.addProcessingStep(new ProcessingStep("payload_truncate", "Content truncated for payload size", true));
            }
        } else {
            message.addProcessingStep(new ProcessingStep("payload_check", "Payload size within limits", true));
        }
    }
}
