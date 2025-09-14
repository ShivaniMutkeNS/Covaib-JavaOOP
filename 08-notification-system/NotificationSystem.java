package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Notification System class defining the template for all notification channels
 * Uses Template Method pattern to enforce common notification workflow while allowing customization
 */
public abstract class NotificationSystem {
    
    protected String systemId;
    protected String systemName;
    protected NotificationChannel channel;
    protected NotificationState currentState;
    protected MessageProcessor messageProcessor;
    protected DeliveryTracker deliveryTracker;
    protected RetryManager retryManager;
    protected RateLimiter rateLimiter;
    protected Map<String, Object> configuration;
    
    public NotificationSystem(String systemId, String systemName, NotificationChannel channel, 
                             Map<String, Object> configuration) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.channel = channel;
        this.configuration = configuration;
        this.currentState = NotificationState.INITIALIZED;
        
        this.messageProcessor = createMessageProcessor();
        this.deliveryTracker = new DeliveryTracker();
        this.retryManager = new RetryManager();
        this.rateLimiter = new RateLimiter();
        
        initialize();
    }
    
    // Template method for sending notifications
    public final CompletableFuture<NotificationResult> sendNotification(NotificationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Pre-send validation
                ValidationResult preValidation = performPreSendValidation(request);
                if (!preValidation.isSuccess()) {
                    return NotificationResult.failure("Pre-send validation failed: " + preValidation.getMessage());
                }
                
                currentState = NotificationState.PROCESSING;
                
                // Rate limiting check
                RateLimitResult rateLimitCheck = rateLimiter.checkRateLimit(request.getRecipient());
                if (!rateLimitCheck.isAllowed()) {
                    currentState = NotificationState.RATE_LIMITED;
                    return NotificationResult.failure("Rate limit exceeded: " + rateLimitCheck.getMessage());
                }
                
                // Message processing
                ProcessingResult processingResult = processMessage(request.getMessage());
                if (!processingResult.isSuccess()) {
                    currentState = NotificationState.ERROR;
                    return NotificationResult.failure("Message processing failed: " + processingResult.getMessage());
                }
                
                // Channel-specific delivery (abstract method)
                NotificationResult deliveryResult = performChannelDelivery(request, processingResult.getProcessedMessage());
                if (!deliveryResult.isSuccess()) {
                    // Handle retry logic
                    return handleDeliveryFailure(request, deliveryResult);
                }
                
                // Post-delivery processing
                NotificationResult finalResult = performPostDelivery(deliveryResult, request);
                
                currentState = NotificationState.DELIVERED;
                return finalResult;
                
            } catch (Exception e) {
                currentState = NotificationState.ERROR;
                return NotificationResult.failure("Notification failed: " + e.getMessage());
            }
        });
    }
    
    // Template method for bulk notifications
    public final CompletableFuture<BulkNotificationResult> sendBulkNotification(BulkNotificationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                currentState = NotificationState.BULK_PROCESSING;
                
                BulkNotificationResult bulkResult = new BulkNotificationResult(request.getBulkId());
                
                for (NotificationRecipient recipient : request.getRecipients()) {
                    NotificationRequest singleRequest = new NotificationRequest(
                        "bulk_" + System.currentTimeMillis(),
                        recipient,
                        request.getMessage(),
                        request.getPriority(),
                        request.getScheduledTime(),
                        request.getRequestedBy()
                    );
                    
                    NotificationResult singleResult = sendNotification(singleRequest).get();
                    bulkResult.addResult(recipient.getRecipientId(), singleResult);
                    
                    // Update progress
                    bulkResult.updateProgress();
                    
                    // Respect rate limits between bulk sends
                    if (rateLimiter.requiresDelay()) {
                        Thread.sleep(rateLimiter.getDelayMillis());
                    }
                }
                
                currentState = NotificationState.DELIVERED;
                return bulkResult;
                
            } catch (Exception e) {
                currentState = NotificationState.ERROR;
                return BulkNotificationResult.failure("Bulk notification failed: " + e.getMessage());
            }
        });
    }
    
    // Abstract methods to be implemented by concrete notification systems
    protected abstract void initialize();
    protected abstract MessageProcessor createMessageProcessor();
    protected abstract NotificationResult performChannelDelivery(NotificationRequest request, ProcessedMessage message);
    protected abstract ValidationResult validateChannelSpecificOptions(NotificationOptions options);
    protected abstract String getChannelEndpoint();
    protected abstract Map<String, String> getChannelHeaders();
    
    // Concrete methods with default implementations
    protected ValidationResult performPreSendValidation(NotificationRequest request) {
        try {
            // Check recipient
            if (request.getRecipient() == null) {
                return ValidationResult.failure("Recipient is null");
            }
            
            // Check message
            if (request.getMessage() == null || request.getMessage().getContent() == null) {
                return ValidationResult.failure("Message content is null");
            }
            
            // Validate recipient for channel
            ValidationResult recipientValidation = validateRecipientForChannel(request.getRecipient());
            if (!recipientValidation.isSuccess()) {
                return recipientValidation;
            }
            
            // Validate notification options
            if (request.getOptions() != null) {
                ValidationResult optionsValidation = validateChannelSpecificOptions(request.getOptions());
                if (!optionsValidation.isSuccess()) {
                    return optionsValidation;
                }
            }
            
            // Check scheduled time
            if (request.getScheduledTime() != null && request.getScheduledTime().isBefore(LocalDateTime.now())) {
                return ValidationResult.failure("Scheduled time is in the past");
            }
            
            return ValidationResult.success("Pre-send validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Pre-send validation error: " + e.getMessage());
        }
    }
    
    protected ProcessingResult processMessage(NotificationMessage message) {
        try {
            return messageProcessor.processMessage(message);
        } catch (Exception e) {
            return ProcessingResult.failure("Message processing failed: " + e.getMessage());
        }
    }
    
    protected NotificationResult handleDeliveryFailure(NotificationRequest request, NotificationResult failedResult) {
        try {
            // Check if retry is allowed
            if (retryManager.shouldRetry(request.getRequestId(), failedResult.getErrorCode())) {
                // Schedule retry
                RetryAttempt retryAttempt = retryManager.scheduleRetry(request.getRequestId(), failedResult);
                
                // Update delivery tracking
                deliveryTracker.recordFailure(request.getRequestId(), failedResult.getMessage(), retryAttempt);
                
                return NotificationResult.retryScheduled(
                    "Delivery failed, retry scheduled",
                    retryAttempt.getRetryTime(),
                    retryAttempt.getAttemptNumber()
                );
            } else {
                // No more retries, mark as permanently failed
                deliveryTracker.recordPermanentFailure(request.getRequestId(), failedResult.getMessage());
                
                currentState = NotificationState.FAILED;
                return NotificationResult.failure("Delivery permanently failed: " + failedResult.getMessage());
            }
            
        } catch (Exception e) {
            return NotificationResult.failure("Error handling delivery failure: " + e.getMessage());
        }
    }
    
    protected NotificationResult performPostDelivery(NotificationResult deliveryResult, NotificationRequest request) {
        try {
            // Record successful delivery
            deliveryTracker.recordSuccess(request.getRequestId(), deliveryResult);
            
            // Update rate limiter
            rateLimiter.recordDelivery(request.getRecipient());
            
            // Add delivery metadata
            deliveryResult.addMetadata("delivery_time", LocalDateTime.now().toString());
            deliveryResult.addMetadata("channel", channel.toString());
            deliveryResult.addMetadata("system_id", systemId);
            
            // Send delivery confirmation if requested
            if (request.getOptions() != null && request.getOptions().isRequestDeliveryConfirmation()) {
                sendDeliveryConfirmation(request, deliveryResult);
            }
            
            return deliveryResult;
            
        } catch (Exception e) {
            return NotificationResult.failure("Post-delivery processing failed: " + e.getMessage());
        }
    }
    
    protected ValidationResult validateRecipientForChannel(NotificationRecipient recipient) {
        switch (channel) {
            case EMAIL:
                return validateEmailRecipient(recipient);
            case SMS:
                return validateSMSRecipient(recipient);
            case PUSH:
                return validatePushRecipient(recipient);
            case WEBHOOK:
                return validateWebhookRecipient(recipient);
            case SLACK:
                return validateSlackRecipient(recipient);
            default:
                return ValidationResult.success("No specific validation required");
        }
    }
    
    protected ValidationResult validateEmailRecipient(NotificationRecipient recipient) {
        String email = recipient.getContactInfo();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
            return ValidationResult.failure("Invalid email address: " + email);
        }
        return ValidationResult.success("Email recipient validated");
    }
    
    protected ValidationResult validateSMSRecipient(NotificationRecipient recipient) {
        String phone = recipient.getContactInfo();
        if (phone == null || !phone.matches("^\\+?[1-9]\\d{1,14}$")) {
            return ValidationResult.failure("Invalid phone number: " + phone);
        }
        return ValidationResult.success("SMS recipient validated");
    }
    
    protected ValidationResult validatePushRecipient(NotificationRecipient recipient) {
        String deviceToken = recipient.getContactInfo();
        if (deviceToken == null || deviceToken.length() < 10) {
            return ValidationResult.failure("Invalid device token");
        }
        return ValidationResult.success("Push recipient validated");
    }
    
    protected ValidationResult validateWebhookRecipient(NotificationRecipient recipient) {
        String url = recipient.getContactInfo();
        if (url == null || !url.matches("^https?://.*")) {
            return ValidationResult.failure("Invalid webhook URL: " + url);
        }
        return ValidationResult.success("Webhook recipient validated");
    }
    
    protected ValidationResult validateSlackRecipient(NotificationRecipient recipient) {
        String channel = recipient.getContactInfo();
        if (channel == null || (!channel.startsWith("#") && !channel.startsWith("@"))) {
            return ValidationResult.failure("Invalid Slack channel/user: " + channel);
        }
        return ValidationResult.success("Slack recipient validated");
    }
    
    protected void sendDeliveryConfirmation(NotificationRequest request, NotificationResult result) {
        // Send confirmation notification (simplified implementation)
        try {
            if (request.getRequestedBy() != null) {
                // Create confirmation message
                NotificationMessage confirmationMessage = new NotificationMessage(
                    "Delivery Confirmation",
                    "Your notification to " + request.getRecipient().getContactInfo() + " was delivered successfully.",
                    MessageType.INFO
                );
                
                // Send confirmation (this would typically use a different channel)
                System.out.println("Delivery confirmation sent to: " + request.getRequestedBy());
            }
        } catch (Exception e) {
            // Log error but don't fail the main notification
            System.err.println("Failed to send delivery confirmation: " + e.getMessage());
        }
    }
    
    // Status and monitoring methods
    public CompletableFuture<DeliveryStatus> getDeliveryStatus(String notificationId) {
        return CompletableFuture.supplyAsync(() -> {
            return deliveryTracker.getDeliveryStatus(notificationId);
        });
    }
    
    public CompletableFuture<NotificationMetrics> getMetrics() {
        return CompletableFuture.supplyAsync(() -> {
            return deliveryTracker.getMetrics();
        });
    }
    
    // Scheduling methods
    public CompletableFuture<ScheduleResult> scheduleNotification(NotificationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (request.getScheduledTime() == null) {
                    return ScheduleResult.failure("No scheduled time specified");
                }
                
                if (request.getScheduledTime().isBefore(LocalDateTime.now())) {
                    return ScheduleResult.failure("Scheduled time is in the past");
                }
                
                // Schedule the notification (simplified implementation)
                String scheduleId = "schedule_" + System.currentTimeMillis();
                
                // In a real implementation, this would use a job scheduler
                CompletableFuture.delayedExecutor(
                    java.time.Duration.between(LocalDateTime.now(), request.getScheduledTime()).toMillis(),
                    java.util.concurrent.TimeUnit.MILLISECONDS
                ).execute(() -> {
                    sendNotification(request);
                });
                
                return ScheduleResult.success("Notification scheduled successfully", scheduleId, request.getScheduledTime());
                
            } catch (Exception e) {
                return ScheduleResult.failure("Scheduling failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<CancelResult> cancelScheduledNotification(String scheduleId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Cancel scheduled notification (simplified implementation)
                return CancelResult.success("Scheduled notification cancelled", scheduleId);
            } catch (Exception e) {
                return CancelResult.failure("Cancellation failed: " + e.getMessage());
            }
        });
    }
    
    // Template and personalization
    public CompletableFuture<TemplateResult> createTemplate(TemplateRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return messageProcessor.createTemplate(request);
            } catch (Exception e) {
                return TemplateResult.failure("Template creation failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<PersonalizationResult> personalizeMessage(PersonalizationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return messageProcessor.personalizeMessage(request);
            } catch (Exception e) {
                return PersonalizationResult.failure("Message personalization failed: " + e.getMessage());
            }
        });
    }
    
    // Getters and status methods
    public String getSystemId() { return systemId; }
    public String getSystemName() { return systemName; }
    public NotificationChannel getChannel() { return channel; }
    public NotificationState getCurrentState() { return currentState; }
    
    public NotificationSystemStatus getStatus() {
        return new NotificationSystemStatus(
            systemId,
            systemName,
            channel,
            currentState,
            getChannelEndpoint(),
            LocalDateTime.now()
        );
    }
    
    public MessageProcessor getMessageProcessor() { return messageProcessor; }
    public DeliveryTracker getDeliveryTracker() { return deliveryTracker; }
    public RetryManager getRetryManager() { return retryManager; }
    public RateLimiter getRateLimiter() { return rateLimiter; }
    
    // Configuration
    public boolean supportsFeature(String feature) {
        return configuration.containsKey(feature) && 
               Boolean.TRUE.equals(configuration.get(feature));
    }
    
    public Object getConfigurationValue(String key) {
        return configuration.get(key);
    }
    
    // Health check
    public CompletableFuture<HealthCheckResult> performHealthCheck() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Check system connectivity
                boolean isConnected = checkConnectivity();
                
                // Check rate limiter status
                boolean rateLimiterHealthy = rateLimiter.isHealthy();
                
                // Check message processor status
                boolean processorHealthy = messageProcessor.isHealthy();
                
                if (isConnected && rateLimiterHealthy && processorHealthy) {
                    return HealthCheckResult.healthy("All systems operational");
                } else {
                    return HealthCheckResult.unhealthy("Some components are not healthy");
                }
                
            } catch (Exception e) {
                return HealthCheckResult.unhealthy("Health check failed: " + e.getMessage());
            }
        });
    }
    
    protected boolean checkConnectivity() {
        // Default implementation - override in concrete classes
        return true;
    }
}
