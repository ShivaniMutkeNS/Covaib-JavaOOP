package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Supporting components for the Notification System abstraction
 */

// Abstract message processor
abstract class MessageProcessor {
    protected Map<String, Object> processingConfig;
    protected Map<String, String> templates;
    
    public MessageProcessor() {
        this.processingConfig = new HashMap<>();
        this.templates = new ConcurrentHashMap<>();
        initializeProcessor();
    }
    
    protected abstract void initializeProcessor();
    public abstract ProcessingResult processMessage(NotificationMessage message);
    public abstract boolean supportsMessageType(MessageType messageType);
    
    protected ProcessingResult validateMessage(NotificationMessage message) {
        if (message == null) {
            return ProcessingResult.failure("Message is null");
        }
        
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            return ProcessingResult.failure("Message content is empty");
        }
        
        if (!supportsMessageType(message.getMessageType())) {
            return ProcessingResult.failure("Message type " + message.getMessageType() + " not supported");
        }
        
        return ProcessingResult.success("Message validation passed", null);
    }
    
    protected String sanitizeContent(String content) {
        if (content == null) return "";
        
        // Remove potentially harmful content
        content = content.replaceAll("<script[^>]*>.*?</script>", "");
        content = content.replaceAll("javascript:", "");
        content = content.replaceAll("on\\w+\\s*=", "");
        
        return content.trim();
    }
    
    public TemplateResult createTemplate(TemplateRequest request) {
        try {
            if (request.getTemplateId() == null || request.getTemplateContent() == null) {
                return TemplateResult.failure("Template ID and content are required");
            }
            
            templates.put(request.getTemplateId(), request.getTemplateContent());
            return TemplateResult.success("Template created successfully", request.getTemplateId());
            
        } catch (Exception e) {
            return TemplateResult.failure("Template creation failed: " + e.getMessage());
        }
    }
    
    public PersonalizationResult personalizeMessage(PersonalizationRequest request) {
        try {
            String template = templates.get(request.getTemplateId());
            if (template == null) {
                return PersonalizationResult.failure("Template not found: " + request.getTemplateId());
            }
            
            String personalizedContent = template;
            
            // Replace placeholders with personal data
            for (Map.Entry<String, String> entry : request.getPersonalData().entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                personalizedContent = personalizedContent.replace(placeholder, entry.getValue());
            }
            
            // Add recipient-specific data
            if (request.getRecipient() != null) {
                personalizedContent = personalizedContent.replace("{{name}}", request.getRecipient().getName());
                personalizedContent = personalizedContent.replace("{{recipient_id}}", request.getRecipient().getRecipientId());
            }
            
            return PersonalizationResult.success("Message personalized successfully", personalizedContent);
            
        } catch (Exception e) {
            return PersonalizationResult.failure("Personalization failed: " + e.getMessage());
        }
    }
    
    public boolean isHealthy() {
        return true; // Default implementation
    }
}

// Generic message processor implementation
class GenericMessageProcessor extends MessageProcessor {
    
    @Override
    protected void initializeProcessor() {
        processingConfig.put("sanitize_content", true);
        processingConfig.put("validate_links", true);
        processingConfig.put("check_spam", true);
        processingConfig.put("format_content", true);
    }
    
    @Override
    public ProcessingResult processMessage(NotificationMessage message) {
        try {
            // Validate message
            ProcessingResult validation = validateMessage(message);
            if (!validation.isSuccess()) {
                return validation;
            }
            
            ProcessedMessage processedMessage = new ProcessedMessage(message);
            
            // Sanitize content if enabled
            if (Boolean.TRUE.equals(processingConfig.get("sanitize_content"))) {
                String sanitizedContent = sanitizeContent(message.getContent());
                processedMessage.setProcessedContent(sanitizedContent);
                processedMessage.addProcessingStep(new ProcessingStep("sanitize", "Content sanitized", true));
            }
            
            // Validate links if enabled
            if (Boolean.TRUE.equals(processingConfig.get("validate_links"))) {
                boolean linksValid = validateLinks(message.getContent());
                processedMessage.addProcessingStep(new ProcessingStep("validate_links", "Links validated", linksValid));
            }
            
            // Check for spam if enabled
            if (Boolean.TRUE.equals(processingConfig.get("check_spam"))) {
                boolean isSpam = checkSpam(message.getContent());
                if (isSpam) {
                    return ProcessingResult.failure("Message flagged as spam");
                }
                processedMessage.addProcessingStep(new ProcessingStep("spam_check", "Spam check passed", true));
            }
            
            // Format content if enabled
            if (Boolean.TRUE.equals(processingConfig.get("format_content"))) {
                String formattedContent = formatContent(processedMessage.getProcessedContent());
                processedMessage.setProcessedContent(formattedContent);
                processedMessage.addProcessingStep(new ProcessingStep("format", "Content formatted", true));
            }
            
            return ProcessingResult.success("Message processed successfully", processedMessage);
            
        } catch (Exception e) {
            return ProcessingResult.failure("Message processing failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean supportsMessageType(MessageType messageType) {
        // Generic processor supports all message types
        return true;
    }
    
    private boolean validateLinks(String content) {
        // Simple link validation
        if (content.contains("http://") || content.contains("https://")) {
            // In real implementation, would validate actual URLs
            return !content.contains("malicious-site.com");
        }
        return true;
    }
    
    private boolean checkSpam(String content) {
        // Simple spam detection
        String lowerContent = content.toLowerCase();
        String[] spamKeywords = {"free money", "click here now", "urgent action required", "congratulations winner"};
        
        for (String keyword : spamKeywords) {
            if (lowerContent.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    private String formatContent(String content) {
        if (content == null) return "";
        
        // Basic formatting
        content = content.replaceAll("\\s+", " "); // Normalize whitespace
        content = content.trim();
        
        return content;
    }
}

// Delivery tracker class
class DeliveryTracker {
    private Map<String, DeliveryRecord> deliveryRecords;
    private NotificationMetrics metrics;
    
    public DeliveryTracker() {
        this.deliveryRecords = new ConcurrentHashMap<>();
        this.metrics = new NotificationMetrics();
    }
    
    public void recordSuccess(String notificationId, NotificationResult result) {
        DeliveryRecord record = new DeliveryRecord(notificationId, DeliveryStatus.DELIVERED);
        record.setDeliveryTime(result.getDeliveryTime());
        record.setMessage("Delivered successfully");
        
        deliveryRecords.put(notificationId, record);
        updateMetrics();
    }
    
    public void recordFailure(String notificationId, String errorMessage, RetryAttempt retryAttempt) {
        DeliveryRecord record = deliveryRecords.getOrDefault(notificationId, 
            new DeliveryRecord(notificationId, DeliveryStatus.FAILED));
        
        record.setStatus(DeliveryStatus.RETRY_SCHEDULED);
        record.setMessage(errorMessage);
        record.addRetryAttempt(retryAttempt);
        
        deliveryRecords.put(notificationId, record);
        updateMetrics();
    }
    
    public void recordPermanentFailure(String notificationId, String errorMessage) {
        DeliveryRecord record = deliveryRecords.getOrDefault(notificationId, 
            new DeliveryRecord(notificationId, DeliveryStatus.FAILED));
        
        record.setStatus(DeliveryStatus.FAILED);
        record.setMessage(errorMessage);
        
        deliveryRecords.put(notificationId, record);
        updateMetrics();
    }
    
    public DeliveryStatus getDeliveryStatus(String notificationId) {
        DeliveryRecord record = deliveryRecords.get(notificationId);
        return record != null ? record.getStatus() : DeliveryStatus.PENDING;
    }
    
    public NotificationMetrics getMetrics() {
        updateMetrics();
        return metrics;
    }
    
    private void updateMetrics() {
        long totalSent = deliveryRecords.size();
        long totalDelivered = deliveryRecords.values().stream()
            .mapToLong(record -> record.getStatus() == DeliveryStatus.DELIVERED ? 1 : 0)
            .sum();
        long totalFailed = deliveryRecords.values().stream()
            .mapToLong(record -> record.getStatus() == DeliveryStatus.FAILED ? 1 : 0)
            .sum();
        
        metrics.setTotalSent(totalSent);
        metrics.setTotalDelivered(totalDelivered);
        metrics.setTotalFailed(totalFailed);
        metrics.setDeliveryRate(totalSent > 0 ? (double) totalDelivered / totalSent * 100.0 : 0.0);
        metrics.updateLastUpdated();
    }
}

// Delivery record class
class DeliveryRecord {
    private String notificationId;
    private DeliveryStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime deliveryTime;
    private String message;
    private List<RetryAttempt> retryAttempts;
    
    public DeliveryRecord(String notificationId, DeliveryStatus status) {
        this.notificationId = notificationId;
        this.status = status;
        this.createdTime = LocalDateTime.now();
        this.retryAttempts = new ArrayList<>();
    }
    
    // Getters and setters
    public String getNotificationId() { return notificationId; }
    public DeliveryStatus getStatus() { return status; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public String getMessage() { return message; }
    public List<RetryAttempt> getRetryAttempts() { return retryAttempts; }
    
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }
    public void setMessage(String message) { this.message = message; }
    public void addRetryAttempt(RetryAttempt attempt) { this.retryAttempts.add(attempt); }
}

// Retry manager class
class RetryManager {
    private Map<String, List<RetryAttempt>> retryHistory;
    private Map<String, Object> retryConfig;
    
    public RetryManager() {
        this.retryHistory = new ConcurrentHashMap<>();
        this.retryConfig = new HashMap<>();
        initializeRetryConfig();
    }
    
    private void initializeRetryConfig() {
        retryConfig.put("max_attempts", 3);
        retryConfig.put("base_delay_ms", 5000);
        retryConfig.put("max_delay_ms", 300000); // 5 minutes
        retryConfig.put("exponential_backoff", true);
        retryConfig.put("jitter", true);
    }
    
    public boolean shouldRetry(String requestId, String errorCode) {
        List<RetryAttempt> attempts = retryHistory.getOrDefault(requestId, new ArrayList<>());
        Integer maxAttempts = (Integer) retryConfig.get("max_attempts");
        
        if (attempts.size() >= maxAttempts) {
            return false;
        }
        
        // Don't retry for certain error codes
        if (isNonRetryableError(errorCode)) {
            return false;
        }
        
        return true;
    }
    
    public RetryAttempt scheduleRetry(String requestId, NotificationResult failedResult) {
        List<RetryAttempt> attempts = retryHistory.getOrDefault(requestId, new ArrayList<>());
        int attemptNumber = attempts.size() + 1;
        
        long delayMs = calculateRetryDelay(attemptNumber);
        LocalDateTime retryTime = LocalDateTime.now().plusNanos(delayMs * 1_000_000);
        
        RetryAttempt attempt = new RetryAttempt(requestId, attemptNumber, retryTime, failedResult.getMessage());
        attempts.add(attempt);
        retryHistory.put(requestId, attempts);
        
        return attempt;
    }
    
    private long calculateRetryDelay(int attemptNumber) {
        Long baseDelay = (Long) retryConfig.get("base_delay_ms");
        Long maxDelay = (Long) retryConfig.get("max_delay_ms");
        Boolean exponentialBackoff = (Boolean) retryConfig.get("exponential_backoff");
        Boolean jitter = (Boolean) retryConfig.get("jitter");
        
        long delay = baseDelay;
        
        if (exponentialBackoff) {
            delay = baseDelay * (long) Math.pow(2, attemptNumber - 1);
        }
        
        delay = Math.min(delay, maxDelay);
        
        if (jitter) {
            // Add random jitter (±25%)
            double jitterFactor = 0.75 + (Math.random() * 0.5);
            delay = (long) (delay * jitterFactor);
        }
        
        return delay;
    }
    
    private boolean isNonRetryableError(String errorCode) {
        // Errors that should not be retried
        String[] nonRetryableErrors = {"INVALID_RECIPIENT", "PERMISSION_DENIED", "MALFORMED_REQUEST"};
        
        if (errorCode != null) {
            for (String nonRetryable : nonRetryableErrors) {
                if (errorCode.contains(nonRetryable)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public List<RetryAttempt> getRetryHistory(String requestId) {
        return retryHistory.getOrDefault(requestId, new ArrayList<>());
    }
}

// Rate limiter class
class RateLimiter {
    private Map<String, RateLimitBucket> buckets;
    private Map<String, Object> rateLimitConfig;
    
    public RateLimiter() {
        this.buckets = new ConcurrentHashMap<>();
        this.rateLimitConfig = new HashMap<>();
        initializeRateLimitConfig();
    }
    
    private void initializeRateLimitConfig() {
        rateLimitConfig.put("requests_per_minute", 60);
        rateLimitConfig.put("requests_per_hour", 1000);
        rateLimitConfig.put("burst_limit", 10);
        rateLimitConfig.put("global_limit", 10000);
    }
    
    public RateLimitResult checkRateLimit(NotificationRecipient recipient) {
        String recipientKey = recipient.getRecipientId();
        RateLimitBucket bucket = buckets.computeIfAbsent(recipientKey, k -> new RateLimitBucket());
        
        Integer requestsPerMinute = (Integer) rateLimitConfig.get("requests_per_minute");
        
        if (bucket.canConsume(1, requestsPerMinute, 60)) {
            bucket.consume(1);
            return RateLimitResult.allowed(bucket.getRemainingTokens());
        } else {
            long resetTime = bucket.getResetTime();
            return RateLimitResult.denied("Rate limit exceeded", resetTime);
        }
    }
    
    public void recordDelivery(NotificationRecipient recipient) {
        // Update delivery statistics for rate limiting
        String recipientKey = recipient.getRecipientId();
        RateLimitBucket bucket = buckets.get(recipientKey);
        if (bucket != null) {
            bucket.recordDelivery();
        }
    }
    
    public boolean requiresDelay() {
        // Check if global rate limiting requires delay
        return false; // Simplified implementation
    }
    
    public long getDelayMillis() {
        return 100; // 100ms delay between bulk sends
    }
    
    public boolean isHealthy() {
        return buckets.size() < 10000; // Prevent memory issues
    }
}

// Rate limit bucket class
class RateLimitBucket {
    private int tokens;
    private long lastRefill;
    private int capacity;
    private long refillRate;
    private int deliveryCount;
    
    public RateLimitBucket() {
        this.capacity = 60; // Default: 60 requests per minute
        this.tokens = capacity;
        this.lastRefill = System.currentTimeMillis();
        this.refillRate = 1000; // Refill every second
        this.deliveryCount = 0;
    }
    
    public synchronized boolean canConsume(int tokensRequested, int maxTokens, int windowSeconds) {
        refillTokens(maxTokens, windowSeconds);
        return tokens >= tokensRequested;
    }
    
    public synchronized void consume(int tokensRequested) {
        if (tokens >= tokensRequested) {
            tokens -= tokensRequested;
        }
    }
    
    private void refillTokens(int maxTokens, int windowSeconds) {
        long now = System.currentTimeMillis();
        long timePassed = now - lastRefill;
        
        if (timePassed >= refillRate) {
            int tokensToAdd = (int) (timePassed / refillRate);
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefill = now;
        }
    }
    
    public void recordDelivery() {
        deliveryCount++;
    }
    
    public int getRemainingTokens() {
        return tokens;
    }
    
    public long getResetTime() {
        return lastRefill + (capacity * refillRate);
    }
    
    public int getDeliveryCount() {
        return deliveryCount;
    }
}

// Notification queue manager
class NotificationQueueManager {
    private Map<NotificationPriority, Queue<NotificationRequest>> priorityQueues;
    private Map<String, Object> queueConfig;
    
    public NotificationQueueManager() {
        this.priorityQueues = new HashMap<>();
        this.queueConfig = new HashMap<>();
        initializeQueues();
    }
    
    private void initializeQueues() {
        for (NotificationPriority priority : NotificationPriority.values()) {
            priorityQueues.put(priority, new LinkedList<>());
        }
        
        queueConfig.put("max_queue_size", 10000);
        queueConfig.put("batch_size", 100);
        queueConfig.put("processing_interval_ms", 1000);
    }
    
    public boolean enqueue(NotificationRequest request) {
        Queue<NotificationRequest> queue = priorityQueues.get(request.getPriority());
        Integer maxSize = (Integer) queueConfig.get("max_queue_size");
        
        if (queue.size() >= maxSize) {
            return false; // Queue full
        }
        
        return queue.offer(request);
    }
    
    public List<NotificationRequest> dequeue(int batchSize) {
        List<NotificationRequest> batch = new ArrayList<>();
        
        // Process by priority order (highest first)
        NotificationPriority[] priorities = {
            NotificationPriority.CRITICAL,
            NotificationPriority.URGENT,
            NotificationPriority.HIGH,
            NotificationPriority.NORMAL,
            NotificationPriority.LOW
        };
        
        for (NotificationPriority priority : priorities) {
            Queue<NotificationRequest> queue = priorityQueues.get(priority);
            
            while (batch.size() < batchSize && !queue.isEmpty()) {
                NotificationRequest request = queue.poll();
                if (request != null) {
                    batch.add(request);
                }
            }
            
            if (batch.size() >= batchSize) {
                break;
            }
        }
        
        return batch;
    }
    
    public int getQueueSize(NotificationPriority priority) {
        return priorityQueues.get(priority).size();
    }
    
    public int getTotalQueueSize() {
        return priorityQueues.values().stream()
            .mapToInt(Queue::size)
            .sum();
    }
    
    public Map<NotificationPriority, Integer> getQueueSizes() {
        Map<NotificationPriority, Integer> sizes = new HashMap<>();
        for (Map.Entry<NotificationPriority, Queue<NotificationRequest>> entry : priorityQueues.entrySet()) {
            sizes.put(entry.getKey(), entry.getValue().size());
        }
        return sizes;
    }
}

// Channel connection manager
class ChannelConnectionManager {
    private Map<NotificationChannel, ConnectionStatus> connectionStatus;
    private Map<NotificationChannel, LocalDateTime> lastHealthCheck;
    
    public ChannelConnectionManager() {
        this.connectionStatus = new HashMap<>();
        this.lastHealthCheck = new HashMap<>();
        
        // Initialize all channels as disconnected
        for (NotificationChannel channel : NotificationChannel.values()) {
            connectionStatus.put(channel, ConnectionStatus.DISCONNECTED);
        }
    }
    
    public boolean isChannelHealthy(NotificationChannel channel) {
        ConnectionStatus status = connectionStatus.get(channel);
        return status == ConnectionStatus.CONNECTED;
    }
    
    public void updateConnectionStatus(NotificationChannel channel, ConnectionStatus status) {
        connectionStatus.put(channel, status);
        lastHealthCheck.put(channel, LocalDateTime.now());
    }
    
    public ConnectionStatus getConnectionStatus(NotificationChannel channel) {
        return connectionStatus.get(channel);
    }
    
    public Map<NotificationChannel, ConnectionStatus> getAllConnectionStatuses() {
        return new HashMap<>(connectionStatus);
    }
    
    public boolean performHealthCheck(NotificationChannel channel) {
        try {
            // Simulate health check (in real implementation, would ping the service)
            boolean healthy = Math.random() > 0.1; // 90% success rate
            
            ConnectionStatus status = healthy ? ConnectionStatus.CONNECTED : ConnectionStatus.ERROR;
            updateConnectionStatus(channel, status);
            
            return healthy;
        } catch (Exception e) {
            updateConnectionStatus(channel, ConnectionStatus.ERROR);
            return false;
        }
    }
}

// Connection status enumeration
enum ConnectionStatus {
    CONNECTED, DISCONNECTED, ERROR, RECONNECTING
}
