package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;

/**
 * SMS Notification System - Concrete implementation for SMS notifications
 * Demonstrates SMS gateway integration and SMS-specific features
 */
public class SMSNotificationSystem extends NotificationSystem {
    
    private SMSGateway smsGateway;
    private PhoneNumberValidator phoneValidator;
    private SMSTemplateManager templateManager;
    private MessageSegmentManager segmentManager;
    
    // SMS-specific configuration
    private String gatewayProvider;
    private String apiKey;
    private String senderId;
    private boolean enableDeliveryReports;
    private int maxMessageLength;
    
    public SMSNotificationSystem(String systemId, String systemName, Map<String, Object> configuration) {
        super(systemId, systemName, NotificationChannel.SMS, configuration);
    }
    
    @Override
    protected void initialize() {
        // Initialize SMS-specific settings
        this.gatewayProvider = (String) configuration.getOrDefault("gateway_provider", "twilio");
        this.apiKey = (String) configuration.getOrDefault("api_key", "");
        this.senderId = (String) configuration.getOrDefault("sender_id", "NotifyApp");
        this.enableDeliveryReports = (Boolean) configuration.getOrDefault("enable_delivery_reports", true);
        this.maxMessageLength = (Integer) configuration.getOrDefault("max_message_length", 160);
        
        this.smsGateway = new SMSGateway(gatewayProvider, apiKey, senderId);
        this.phoneValidator = new PhoneNumberValidator();
        this.templateManager = new SMSTemplateManager();
        this.segmentManager = new MessageSegmentManager(maxMessageLength);
        
        // Configure SMS gateway
        configureSMSGateway();
    }
    
    @Override
    protected MessageProcessor createMessageProcessor() {
        return new SMSMessageProcessor(maxMessageLength);
    }
    
    @Override
    protected NotificationResult performChannelDelivery(NotificationRequest request, ProcessedMessage message) {
        try {
            // Create SMS message
            SMSMessage smsMessage = createSMSMessage(request, message);
            
            // Send SMS
            SMSResult smsResult = smsGateway.sendSMS(smsMessage);
            
            if (smsResult.isSuccess()) {
                return NotificationResult.success(
                    "SMS sent successfully",
                    smsResult.getMessageId()
                );
            } else {
                NotificationResult result = NotificationResult.failure("SMS delivery failed: " + smsResult.getErrorMessage());
                result.setErrorCode(smsResult.getErrorCode());
                return result;
            }
            
        } catch (Exception e) {
            NotificationResult result = NotificationResult.failure("SMS delivery error: " + e.getMessage());
            result.setErrorCode("DELIVERY_ERROR");
            return result;
        }
    }
    
    @Override
    protected ValidationResult validateChannelSpecificOptions(NotificationOptions options) {
        try {
            if (options.getChannelSpecificOptions().containsKey("sender_id")) {
                String customSenderId = (String) options.getChannelSpecificOptions().get("sender_id");
                if (customSenderId != null && customSenderId.length() > 11) {
                    return ValidationResult.failure("Sender ID cannot exceed 11 characters");
                }
            }
            
            if (options.getChannelSpecificOptions().containsKey("unicode_enabled")) {
                Boolean unicodeEnabled = (Boolean) options.getChannelSpecificOptions().get("unicode_enabled");
                if (Boolean.TRUE.equals(unicodeEnabled)) {
                    // Unicode messages have different length limits
                    // This would be handled in message processing
                }
            }
            
            return ValidationResult.success("SMS options validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("SMS options validation error: " + e.getMessage());
        }
    }
    
    @Override
    protected String getChannelEndpoint() {
        return smsGateway.getGatewayEndpoint();
    }
    
    @Override
    protected Map<String, String> getChannelHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-SMS-Gateway", gatewayProvider);
        headers.put("X-System-ID", systemId);
        headers.put("X-Channel", "SMS");
        headers.put("X-Sender-ID", senderId);
        return headers;
    }
    
    private void configureSMSGateway() {
        Map<String, Object> gatewayConfig = new HashMap<>();
        gatewayConfig.put("connection_timeout", 30000);
        gatewayConfig.put("read_timeout", 60000);
        gatewayConfig.put("max_retries", 3);
        gatewayConfig.put("enable_delivery_reports", enableDeliveryReports);
        gatewayConfig.put("rate_limit_per_second", 10);
        
        smsGateway.configure(gatewayConfig);
    }
    
    private SMSMessage createSMSMessage(NotificationRequest request, ProcessedMessage message) {
        SMSMessage smsMessage = new SMSMessage();
        
        // Basic SMS properties
        smsMessage.setTo(request.getRecipient().getContactInfo());
        smsMessage.setContent(message.getProcessedContent());
        smsMessage.setSenderId(senderId);
        smsMessage.setPriority(mapPriorityToSMS(request.getPriority()));
        smsMessage.setEnableDeliveryReport(enableDeliveryReports);
        
        // Add channel-specific options
        if (request.getOptions() != null) {
            Map<String, Object> channelOptions = request.getOptions().getChannelSpecificOptions();
            
            // Custom sender ID
            if (channelOptions.containsKey("sender_id")) {
                String customSenderId = (String) channelOptions.get("sender_id");
                if (customSenderId != null && !customSenderId.isEmpty()) {
                    smsMessage.setSenderId(customSenderId);
                }
            }
            
            // Unicode support
            if (channelOptions.containsKey("unicode_enabled")) {
                Boolean unicodeEnabled = (Boolean) channelOptions.get("unicode_enabled");
                smsMessage.setUnicodeEnabled(Boolean.TRUE.equals(unicodeEnabled));
            }
            
            // Flash SMS
            if (channelOptions.containsKey("flash_sms")) {
                Boolean flashSms = (Boolean) channelOptions.get("flash_sms");
                smsMessage.setFlashSms(Boolean.TRUE.equals(flashSms));
            }
            
            // Validity period
            if (channelOptions.containsKey("validity_period_hours")) {
                Integer validityHours = (Integer) channelOptions.get("validity_period_hours");
                if (validityHours != null && validityHours > 0) {
                    smsMessage.setValidityPeriod(validityHours);
                }
            }
        }
        
        return smsMessage;
    }
    
    private SMSPriority mapPriorityToSMS(NotificationPriority priority) {
        switch (priority) {
            case CRITICAL:
            case URGENT:
                return SMSPriority.HIGH;
            case HIGH:
                return SMSPriority.NORMAL;
            case NORMAL:
            case LOW:
            default:
                return SMSPriority.LOW;
        }
    }
    
    @Override
    protected boolean checkConnectivity() {
        try {
            return smsGateway.testConnection();
        } catch (Exception e) {
            return false;
        }
    }
    
    // SMS-specific methods
    public CompletableFuture<SMSTemplateResult> createSMSTemplate(SMSTemplateRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return templateManager.createTemplate(request);
            } catch (Exception e) {
                return SMSTemplateResult.failure("SMS template creation failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<MessageSegmentResult> analyzeMessageSegments(String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return segmentManager.analyzeMessage(content);
            } catch (Exception e) {
                return MessageSegmentResult.failure("Message analysis failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<DeliveryReportResult> getDeliveryReports() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return smsGateway.getDeliveryReports();
            } catch (Exception e) {
                return DeliveryReportResult.failure("Delivery report retrieval failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<PhoneValidationResult> validatePhoneNumber(String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return phoneValidator.validatePhoneNumber(phoneNumber);
            } catch (Exception e) {
                return PhoneValidationResult.failure("Phone validation failed: " + e.getMessage());
            }
        });
    }
    
    // Getters for SMS-specific properties
    public String getGatewayProvider() { return gatewayProvider; }
    public String getApiKey() { return apiKey; }
    public String getSenderId() { return senderId; }
    public boolean isEnableDeliveryReports() { return enableDeliveryReports; }
    public int getMaxMessageLength() { return maxMessageLength; }
    public SMSGateway getSmsGateway() { return smsGateway; }
    public PhoneNumberValidator getPhoneValidator() { return phoneValidator; }
    public SMSTemplateManager getTemplateManager() { return templateManager; }
    public MessageSegmentManager getSegmentManager() { return segmentManager; }
}

// SMS-specific message processor
class SMSMessageProcessor extends GenericMessageProcessor {
    private int maxLength;
    
    public SMSMessageProcessor(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    protected void initializeProcessor() {
        super.initializeProcessor();
        processingConfig.put("truncate_long_messages", true);
        processingConfig.put("remove_special_chars", true);
        processingConfig.put("normalize_whitespace", true);
        processingConfig.put("add_opt_out", true);
    }
    
    @Override
    public ProcessingResult processMessage(NotificationMessage message) {
        // Call parent processing first
        ProcessingResult baseResult = super.processMessage(message);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        
        ProcessedMessage processedMessage = baseResult.getProcessedMessage();
        
        // SMS-specific processing
        if (Boolean.TRUE.equals(processingConfig.get("remove_special_chars"))) {
            removeSpecialCharacters(processedMessage);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("normalize_whitespace"))) {
            normalizeWhitespace(processedMessage);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("truncate_long_messages"))) {
            truncateMessage(processedMessage);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("add_opt_out"))) {
            addOptOutMessage(processedMessage);
        }
        
        return ProcessingResult.success("SMS message processing completed", processedMessage);
    }
    
    private void removeSpecialCharacters(ProcessedMessage message) {
        String content = message.getProcessedContent();
        
        // Remove characters that might cause issues in SMS
        content = content.replaceAll("[\\u2018\\u2019]", "'"); // Smart quotes to regular quotes
        content = content.replaceAll("[\\u201C\\u201D]", "\""); // Smart quotes to regular quotes
        content = content.replaceAll("[\\u2013\\u2014]", "-"); // Em/En dashes to hyphens
        content = content.replaceAll("[^\\x00-\\x7F]", ""); // Remove non-ASCII characters (for non-unicode SMS)
        
        message.setProcessedContent(content);
        message.addProcessingStep(new ProcessingStep("remove_special_chars", "Special characters removed", true));
    }
    
    private void normalizeWhitespace(ProcessedMessage message) {
        String content = message.getProcessedContent();
        
        // Normalize whitespace
        content = content.replaceAll("\\s+", " "); // Multiple spaces to single space
        content = content.trim();
        
        message.setProcessedContent(content);
        message.addProcessingStep(new ProcessingStep("normalize_whitespace", "Whitespace normalized", true));
    }
    
    private void truncateMessage(ProcessedMessage message) {
        String content = message.getProcessedContent();
        
        if (content.length() > maxLength) {
            // Leave space for "..." at the end
            String truncated = content.substring(0, maxLength - 3) + "...";
            message.setProcessedContent(truncated);
            message.addProcessingStep(new ProcessingStep("truncate", "Message truncated to " + maxLength + " characters", true));
            message.getProcessingMetadata().put("original_length", String.valueOf(content.length()));
            message.getProcessingMetadata().put("truncated", "true");
        } else {
            message.addProcessingStep(new ProcessingStep("truncate", "No truncation needed", true));
        }
    }
    
    private void addOptOutMessage(ProcessedMessage message) {
        String content = message.getProcessedContent();
        String optOutText = " Reply STOP to opt out.";
        
        // Only add if there's space and it's not already present
        if (!content.toLowerCase().contains("stop") && 
            (content.length() + optOutText.length()) <= maxLength) {
            message.setProcessedContent(content + optOutText);
            message.addProcessingStep(new ProcessingStep("opt_out", "Opt-out message added", true));
        } else {
            message.addProcessingStep(new ProcessingStep("opt_out", "Opt-out message skipped (no space or already present)", true));
        }
    }
}
