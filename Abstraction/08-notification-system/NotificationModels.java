package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Data models and enums for the Notification System abstraction
 */

// Notification channel enumeration
enum NotificationChannel {
    EMAIL, SMS, PUSH, WEBHOOK, SLACK, TEAMS, DISCORD, TELEGRAM
}

// Notification state enumeration
enum NotificationState {
    INITIALIZED, PROCESSING, BULK_PROCESSING, DELIVERED, FAILED, RATE_LIMITED, ERROR
}

// Message type enumeration
enum MessageType {
    INFO, WARNING, ERROR, SUCCESS, MARKETING, TRANSACTIONAL, REMINDER
}

// Priority enumeration
enum NotificationPriority {
    LOW, NORMAL, HIGH, URGENT, CRITICAL
}

// Notification recipient class
class NotificationRecipient {
    private String recipientId;
    private String name;
    private String contactInfo;
    private RecipientType recipientType;
    private Map<String, String> preferences;
    private boolean isActive;
    
    public NotificationRecipient(String recipientId, String name, String contactInfo, RecipientType recipientType) {
        this.recipientId = recipientId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.recipientType = recipientType;
        this.preferences = new HashMap<>();
        this.isActive = true;
    }
    
    // Getters and setters
    public String getRecipientId() { return recipientId; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }
    public RecipientType getRecipientType() { return recipientType; }
    public Map<String, String> getPreferences() { return preferences; }
    public boolean isActive() { return isActive; }
    
    public void setName(String name) { this.name = name; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setActive(boolean active) { this.isActive = active; }
    public void addPreference(String key, String value) { this.preferences.put(key, value); }
}

// Recipient type enumeration
enum RecipientType {
    INDIVIDUAL, GROUP, ROLE, DEPARTMENT, EXTERNAL
}

// Notification message class
class NotificationMessage {
    private String subject;
    private String content;
    private MessageType messageType;
    private Map<String, Object> attachments;
    private Map<String, String> variables;
    private LocalDateTime createdAt;
    
    public NotificationMessage(String subject, String content, MessageType messageType) {
        this.subject = subject;
        this.content = content;
        this.messageType = messageType;
        this.attachments = new HashMap<>();
        this.variables = new HashMap<>();
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public MessageType getMessageType() { return messageType; }
    public Map<String, Object> getAttachments() { return attachments; }
    public Map<String, String> getVariables() { return variables; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setSubject(String subject) { this.subject = subject; }
    public void setContent(String content) { this.content = content; }
    public void addAttachment(String name, Object attachment) { this.attachments.put(name, attachment); }
    public void addVariable(String key, String value) { this.variables.put(key, value); }
}

// Processed message class
class ProcessedMessage {
    private NotificationMessage originalMessage;
    private String processedSubject;
    private String processedContent;
    private Map<String, Object> processingMetadata;
    private List<ProcessingStep> processingSteps;
    
    public ProcessedMessage(NotificationMessage originalMessage) {
        this.originalMessage = originalMessage;
        this.processedSubject = originalMessage.getSubject();
        this.processedContent = originalMessage.getContent();
        this.processingMetadata = new HashMap<>();
        this.processingSteps = new ArrayList<>();
    }
    
    // Getters and setters
    public NotificationMessage getOriginalMessage() { return originalMessage; }
    public String getProcessedSubject() { return processedSubject; }
    public String getProcessedContent() { return processedContent; }
    public Map<String, Object> getProcessingMetadata() { return processingMetadata; }
    public List<ProcessingStep> getProcessingSteps() { return processingSteps; }
    
    public void setProcessedSubject(String processedSubject) { this.processedSubject = processedSubject; }
    public void setProcessedContent(String processedContent) { this.processedContent = processedContent; }
    public void addProcessingStep(ProcessingStep step) { this.processingSteps.add(step); }
}

// Processing step class
class ProcessingStep {
    private String stepName;
    private String description;
    private LocalDateTime timestamp;
    private boolean successful;
    
    public ProcessingStep(String stepName, String description, boolean successful) {
        this.stepName = stepName;
        this.description = description;
        this.successful = successful;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getStepName() { return stepName; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isSuccessful() { return successful; }
}

// Notification options class
class NotificationOptions {
    private boolean requestDeliveryConfirmation = false;
    private boolean requestReadReceipt = false;
    private int retryAttempts = 3;
    private long retryDelayMillis = 5000;
    private Map<String, Object> channelSpecificOptions;
    
    public NotificationOptions() {
        this.channelSpecificOptions = new HashMap<>();
    }
    
    // Getters and setters
    public boolean isRequestDeliveryConfirmation() { return requestDeliveryConfirmation; }
    public boolean isRequestReadReceipt() { return requestReadReceipt; }
    public int getRetryAttempts() { return retryAttempts; }
    public long getRetryDelayMillis() { return retryDelayMillis; }
    public Map<String, Object> getChannelSpecificOptions() { return channelSpecificOptions; }
    
    public void setRequestDeliveryConfirmation(boolean requestDeliveryConfirmation) { 
        this.requestDeliveryConfirmation = requestDeliveryConfirmation; 
    }
    public void setRequestReadReceipt(boolean requestReadReceipt) { 
        this.requestReadReceipt = requestReadReceipt; 
    }
    public void setRetryAttempts(int retryAttempts) { this.retryAttempts = retryAttempts; }
    public void setRetryDelayMillis(long retryDelayMillis) { this.retryDelayMillis = retryDelayMillis; }
    public void addChannelOption(String key, Object value) { this.channelSpecificOptions.put(key, value); }
}

// Notification request class
class NotificationRequest {
    private String requestId;
    private NotificationRecipient recipient;
    private NotificationMessage message;
    private NotificationPriority priority;
    private LocalDateTime scheduledTime;
    private String requestedBy;
    private NotificationOptions options;
    private LocalDateTime requestTime;
    
    public NotificationRequest(String requestId, NotificationRecipient recipient, NotificationMessage message,
                              NotificationPriority priority, LocalDateTime scheduledTime, String requestedBy) {
        this.requestId = requestId;
        this.recipient = recipient;
        this.message = message;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
        this.requestedBy = requestedBy;
        this.requestTime = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public NotificationRecipient getRecipient() { return recipient; }
    public NotificationMessage getMessage() { return message; }
    public NotificationPriority getPriority() { return priority; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public String getRequestedBy() { return requestedBy; }
    public NotificationOptions getOptions() { return options; }
    public LocalDateTime getRequestTime() { return requestTime; }
    
    public void setOptions(NotificationOptions options) { this.options = options; }
}

// Notification result class
class NotificationResult {
    private boolean success;
    private String message;
    private String notificationId;
    private LocalDateTime deliveryTime;
    private String errorCode;
    private Map<String, String> metadata;
    private DeliveryStatus deliveryStatus;
    
    private NotificationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.metadata = new HashMap<>();
        this.deliveryTime = LocalDateTime.now();
    }
    
    public static NotificationResult success(String message, String notificationId) {
        NotificationResult result = new NotificationResult(true, message);
        result.notificationId = notificationId;
        result.deliveryStatus = DeliveryStatus.DELIVERED;
        return result;
    }
    
    public static NotificationResult failure(String message) {
        NotificationResult result = new NotificationResult(false, message);
        result.deliveryStatus = DeliveryStatus.FAILED;
        return result;
    }
    
    public static NotificationResult retryScheduled(String message, LocalDateTime retryTime, int attemptNumber) {
        NotificationResult result = new NotificationResult(false, message);
        result.deliveryStatus = DeliveryStatus.RETRY_SCHEDULED;
        result.addMetadata("retry_time", retryTime.toString());
        result.addMetadata("attempt_number", String.valueOf(attemptNumber));
        return result;
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getNotificationId() { return notificationId; }
    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public String getErrorCode() { return errorCode; }
    public Map<String, String> getMetadata() { return metadata; }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void addMetadata(String key, String value) { this.metadata.put(key, value); }
}

// Delivery status enumeration
enum DeliveryStatus {
    PENDING, PROCESSING, DELIVERED, FAILED, RETRY_SCHEDULED, CANCELLED
}

// Bulk notification request class
class BulkNotificationRequest {
    private String bulkId;
    private List<NotificationRecipient> recipients;
    private NotificationMessage message;
    private NotificationPriority priority;
    private LocalDateTime scheduledTime;
    private String requestedBy;
    private NotificationOptions options;
    
    public BulkNotificationRequest(String bulkId, List<NotificationRecipient> recipients, 
                                  NotificationMessage message, NotificationPriority priority, String requestedBy) {
        this.bulkId = bulkId;
        this.recipients = recipients;
        this.message = message;
        this.priority = priority;
        this.requestedBy = requestedBy;
    }
    
    // Getters and setters
    public String getBulkId() { return bulkId; }
    public List<NotificationRecipient> getRecipients() { return recipients; }
    public NotificationMessage getMessage() { return message; }
    public NotificationPriority getPriority() { return priority; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public String getRequestedBy() { return requestedBy; }
    public NotificationOptions getOptions() { return options; }
    
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public void setOptions(NotificationOptions options) { this.options = options; }
}

// Bulk notification result class
class BulkNotificationResult {
    private String bulkId;
    private boolean success;
    private String message;
    private Map<String, NotificationResult> results;
    private int totalRecipients;
    private int processedRecipients;
    private int successfulDeliveries;
    private int failedDeliveries;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public BulkNotificationResult(String bulkId) {
        this.bulkId = bulkId;
        this.results = new HashMap<>();
        this.startTime = LocalDateTime.now();
        this.processedRecipients = 0;
        this.successfulDeliveries = 0;
        this.failedDeliveries = 0;
    }
    
    public static BulkNotificationResult failure(String message) {
        BulkNotificationResult result = new BulkNotificationResult("failed_bulk");
        result.success = false;
        result.message = message;
        result.endTime = LocalDateTime.now();
        return result;
    }
    
    public void addResult(String recipientId, NotificationResult notificationResult) {
        results.put(recipientId, notificationResult);
        if (notificationResult.isSuccess()) {
            successfulDeliveries++;
        } else {
            failedDeliveries++;
        }
    }
    
    public void updateProgress() {
        processedRecipients = results.size();
        if (processedRecipients == totalRecipients) {
            success = failedDeliveries == 0;
            message = success ? "Bulk notification completed successfully" : 
                     "Bulk notification completed with " + failedDeliveries + " failures";
            endTime = LocalDateTime.now();
        }
    }
    
    // Getters and setters
    public String getBulkId() { return bulkId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Map<String, NotificationResult> getResults() { return results; }
    public int getTotalRecipients() { return totalRecipients; }
    public int getProcessedRecipients() { return processedRecipients; }
    public int getSuccessfulDeliveries() { return successfulDeliveries; }
    public int getFailedDeliveries() { return failedDeliveries; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    
    public void setTotalRecipients(int totalRecipients) { this.totalRecipients = totalRecipients; }
    
    public double getProgressPercentage() {
        return totalRecipients > 0 ? (double) processedRecipients / totalRecipients * 100.0 : 0.0;
    }
}

// Result classes for various operations
class ValidationResult {
    private boolean success;
    private String message;
    
    private ValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }
    
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class ProcessingResult {
    private boolean success;
    private String message;
    private ProcessedMessage processedMessage;
    
    private ProcessingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ProcessingResult success(String message, ProcessedMessage processedMessage) {
        ProcessingResult result = new ProcessingResult(true, message);
        result.processedMessage = processedMessage;
        return result;
    }
    
    public static ProcessingResult failure(String message) {
        return new ProcessingResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ProcessedMessage getProcessedMessage() { return processedMessage; }
}

// Rate limit result class
class RateLimitResult {
    private boolean allowed;
    private String message;
    private long resetTimeMillis;
    private int remainingRequests;
    
    private RateLimitResult(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }
    
    public static RateLimitResult allowed(int remainingRequests) {
        RateLimitResult result = new RateLimitResult(true, "Request allowed");
        result.remainingRequests = remainingRequests;
        return result;
    }
    
    public static RateLimitResult denied(String message, long resetTimeMillis) {
        RateLimitResult result = new RateLimitResult(false, message);
        result.resetTimeMillis = resetTimeMillis;
        return result;
    }
    
    // Getters
    public boolean isAllowed() { return allowed; }
    public String getMessage() { return message; }
    public long getResetTimeMillis() { return resetTimeMillis; }
    public int getRemainingRequests() { return remainingRequests; }
}

// Retry attempt class
class RetryAttempt {
    private String requestId;
    private int attemptNumber;
    private LocalDateTime retryTime;
    private String reason;
    
    public RetryAttempt(String requestId, int attemptNumber, LocalDateTime retryTime, String reason) {
        this.requestId = requestId;
        this.attemptNumber = attemptNumber;
        this.retryTime = retryTime;
        this.reason = reason;
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public int getAttemptNumber() { return attemptNumber; }
    public LocalDateTime getRetryTime() { return retryTime; }
    public String getReason() { return reason; }
}

// Notification system status class
class NotificationSystemStatus {
    private String systemId;
    private String systemName;
    private NotificationChannel channel;
    private NotificationState currentState;
    private String channelEndpoint;
    private LocalDateTime statusTime;
    
    public NotificationSystemStatus(String systemId, String systemName, NotificationChannel channel,
                                   NotificationState currentState, String channelEndpoint, LocalDateTime statusTime) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.channel = channel;
        this.currentState = currentState;
        this.channelEndpoint = channelEndpoint;
        this.statusTime = statusTime;
    }
    
    // Getters
    public String getSystemId() { return systemId; }
    public String getSystemName() { return systemName; }
    public NotificationChannel getChannel() { return channel; }
    public NotificationState getCurrentState() { return currentState; }
    public String getChannelEndpoint() { return channelEndpoint; }
    public LocalDateTime getStatusTime() { return statusTime; }
}

// Notification metrics class
class NotificationMetrics {
    private long totalSent;
    private long totalDelivered;
    private long totalFailed;
    private double deliveryRate;
    private double averageDeliveryTimeMs;
    private Map<String, Long> channelMetrics;
    private LocalDateTime lastUpdated;
    
    public NotificationMetrics() {
        this.channelMetrics = new HashMap<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and setters
    public long getTotalSent() { return totalSent; }
    public long getTotalDelivered() { return totalDelivered; }
    public long getTotalFailed() { return totalFailed; }
    public double getDeliveryRate() { return deliveryRate; }
    public double getAverageDeliveryTimeMs() { return averageDeliveryTimeMs; }
    public Map<String, Long> getChannelMetrics() { return channelMetrics; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    public void setTotalSent(long totalSent) { this.totalSent = totalSent; }
    public void setTotalDelivered(long totalDelivered) { this.totalDelivered = totalDelivered; }
    public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }
    public void setDeliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; }
    public void setAverageDeliveryTimeMs(double averageDeliveryTimeMs) { this.averageDeliveryTimeMs = averageDeliveryTimeMs; }
    public void updateLastUpdated() { this.lastUpdated = LocalDateTime.now(); }
}

// Schedule result class
class ScheduleResult {
    private boolean success;
    private String message;
    private String scheduleId;
    private LocalDateTime scheduledTime;
    
    private ScheduleResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ScheduleResult success(String message, String scheduleId, LocalDateTime scheduledTime) {
        ScheduleResult result = new ScheduleResult(true, message);
        result.scheduleId = scheduleId;
        result.scheduledTime = scheduledTime;
        return result;
    }
    
    public static ScheduleResult failure(String message) {
        return new ScheduleResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getScheduleId() { return scheduleId; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
}

// Cancel result class
class CancelResult {
    private boolean success;
    private String message;
    private String scheduleId;
    
    private CancelResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static CancelResult success(String message, String scheduleId) {
        CancelResult result = new CancelResult(true, message);
        result.scheduleId = scheduleId;
        return result;
    }
    
    public static CancelResult failure(String message) {
        return new CancelResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getScheduleId() { return scheduleId; }
}

// Template request class
class TemplateRequest {
    private String templateId;
    private String templateName;
    private String templateContent;
    private Map<String, String> placeholders;
    private String createdBy;
    
    public TemplateRequest(String templateId, String templateName, String templateContent, String createdBy) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateContent = templateContent;
        this.createdBy = createdBy;
        this.placeholders = new HashMap<>();
    }
    
    // Getters and setters
    public String getTemplateId() { return templateId; }
    public String getTemplateName() { return templateName; }
    public String getTemplateContent() { return templateContent; }
    public Map<String, String> getPlaceholders() { return placeholders; }
    public String getCreatedBy() { return createdBy; }
    
    public void addPlaceholder(String key, String description) { this.placeholders.put(key, description); }
}

// Template result class
class TemplateResult {
    private boolean success;
    private String message;
    private String templateId;
    
    private TemplateResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static TemplateResult success(String message, String templateId) {
        TemplateResult result = new TemplateResult(true, message);
        result.templateId = templateId;
        return result;
    }
    
    public static TemplateResult failure(String message) {
        return new TemplateResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getTemplateId() { return templateId; }
}

// Personalization request class
class PersonalizationRequest {
    private String templateId;
    private Map<String, String> personalData;
    private NotificationRecipient recipient;
    
    public PersonalizationRequest(String templateId, NotificationRecipient recipient) {
        this.templateId = templateId;
        this.recipient = recipient;
        this.personalData = new HashMap<>();
    }
    
    // Getters and setters
    public String getTemplateId() { return templateId; }
    public Map<String, String> getPersonalData() { return personalData; }
    public NotificationRecipient getRecipient() { return recipient; }
    
    public void addPersonalData(String key, String value) { this.personalData.put(key, value); }
}

// Personalization result class
class PersonalizationResult {
    private boolean success;
    private String message;
    private String personalizedContent;
    
    private PersonalizationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static PersonalizationResult success(String message, String personalizedContent) {
        PersonalizationResult result = new PersonalizationResult(true, message);
        result.personalizedContent = personalizedContent;
        return result;
    }
    
    public static PersonalizationResult failure(String message) {
        return new PersonalizationResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getPersonalizedContent() { return personalizedContent; }
}

// Health check result class
class HealthCheckResult {
    private boolean healthy;
    private String message;
    private Map<String, String> componentStatus;
    private LocalDateTime checkTime;
    
    private HealthCheckResult(boolean healthy, String message) {
        this.healthy = healthy;
        this.message = message;
        this.componentStatus = new HashMap<>();
        this.checkTime = LocalDateTime.now();
    }
    
    public static HealthCheckResult healthy(String message) {
        return new HealthCheckResult(true, message);
    }
    
    public static HealthCheckResult unhealthy(String message) {
        return new HealthCheckResult(false, message);
    }
    
    // Getters
    public boolean isHealthy() { return healthy; }
    public String getMessage() { return message; }
    public Map<String, String> getComponentStatus() { return componentStatus; }
    public LocalDateTime getCheckTime() { return checkTime; }
    
    public void addComponentStatus(String component, String status) {
        this.componentStatus.put(component, status);
    }
}

// Custom exception for notification operations
class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }
    
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
