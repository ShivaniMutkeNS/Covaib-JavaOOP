package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * SMS-specific components and supporting classes
 */

// SMS Gateway for message delivery
class SMSGateway {
    private String provider;
    private String apiKey;
    private String senderId;
    private Map<String, Object> configuration;
    private boolean connected;
    private String gatewayEndpoint;
    
    public SMSGateway(String provider, String apiKey, String senderId) {
        this.provider = provider;
        this.apiKey = apiKey;
        this.senderId = senderId;
        this.configuration = new HashMap<>();
        this.connected = false;
        this.gatewayEndpoint = "https://api." + provider.toLowerCase() + ".com/sms";
    }
    
    public void configure(Map<String, Object> config) {
        this.configuration.putAll(config);
    }
    
    public boolean testConnection() {
        try {
            // Simulate connection test
            Thread.sleep(100); // Simulate network delay
            
            // Basic validation
            if (apiKey == null || apiKey.isEmpty()) {
                return false;
            }
            
            if (provider == null || provider.isEmpty()) {
                return false;
            }
            
            // Simulate connection success (90% success rate)
            boolean success = Math.random() > 0.1;
            this.connected = success;
            return success;
            
        } catch (Exception e) {
            this.connected = false;
            return false;
        }
    }
    
    public SMSResult sendSMS(SMSMessage message) {
        try {
            if (!connected && !testConnection()) {
                return SMSResult.failure("SMS gateway connection failed", "CONNECTION_ERROR");
            }
            
            // Validate SMS message
            SMSResult validation = validateSMSMessage(message);
            if (!validation.isSuccess()) {
                return validation;
            }
            
            // Check rate limiting
            if (isRateLimited()) {
                return SMSResult.failure("Rate limit exceeded", "RATE_LIMIT_ERROR");
            }
            
            // Simulate SMS sending
            Thread.sleep(300); // Simulate sending delay
            
            // Generate message ID
            String messageId = generateMessageId();
            
            // Simulate sending success (92% success rate)
            if (Math.random() > 0.08) {
                return SMSResult.success("SMS sent successfully", messageId);
            } else {
                return SMSResult.failure("SMS gateway error", "GATEWAY_ERROR");
            }
            
        } catch (Exception e) {
            return SMSResult.failure("SMS sending failed: " + e.getMessage(), "SEND_ERROR");
        }
    }
    
    private SMSResult validateSMSMessage(SMSMessage message) {
        if (message == null) {
            return SMSResult.failure("SMS message is null", "VALIDATION_ERROR");
        }
        
        if (message.getTo() == null || message.getTo().isEmpty()) {
            return SMSResult.failure("Recipient phone number is required", "VALIDATION_ERROR");
        }
        
        if (message.getContent() == null || message.getContent().isEmpty()) {
            return SMSResult.failure("Message content is required", "VALIDATION_ERROR");
        }
        
        if (message.getContent().length() > 1600) { // Max for concatenated SMS
            return SMSResult.failure("Message content too long", "VALIDATION_ERROR");
        }
        
        return SMSResult.success("SMS message validated", null);
    }
    
    private boolean isRateLimited() {
        // Simple rate limiting simulation
        Integer rateLimit = (Integer) configuration.get("rate_limit_per_second");
        if (rateLimit != null && rateLimit > 0) {
            // In real implementation, would check actual rate limiting
            return Math.random() < 0.02; // 2% chance of rate limiting
        }
        return false;
    }
    
    private String generateMessageId() {
        return "sms_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public DeliveryReportResult getDeliveryReports() {
        try {
            // Simulate delivery report retrieval
            List<DeliveryReport> reports = new ArrayList<>();
            
            // Generate some sample delivery reports
            int reportCount = (int) (Math.random() * 10);
            for (int i = 0; i < reportCount; i++) {
                DeliveryReport report = new DeliveryReport(
                    "sms_" + i,
                    "+1234567890" + i,
                    DeliveryStatus.DELIVERED,
                    "Message delivered successfully",
                    LocalDateTime.now().minusMinutes(i * 5)
                );
                reports.add(report);
            }
            
            return DeliveryReportResult.success("Retrieved " + reports.size() + " delivery reports", reports);
            
        } catch (Exception e) {
            return DeliveryReportResult.failure("Delivery report retrieval failed: " + e.getMessage());
        }
    }
    
    public boolean isConnected() { return connected; }
    public String getProvider() { return provider; }
    public String getApiKey() { return apiKey; }
    public String getSenderId() { return senderId; }
    public String getGatewayEndpoint() { return gatewayEndpoint; }
}

// SMS message class
class SMSMessage {
    private String to;
    private String content;
    private String senderId;
    private SMSPriority priority;
    private boolean enableDeliveryReport;
    private boolean unicodeEnabled;
    private boolean flashSms;
    private Integer validityPeriod; // in hours
    private LocalDateTime createdTime;
    private Map<String, String> customParameters;
    
    public SMSMessage() {
        this.priority = SMSPriority.NORMAL;
        this.enableDeliveryReport = false;
        this.unicodeEnabled = false;
        this.flashSms = false;
        this.createdTime = LocalDateTime.now();
        this.customParameters = new HashMap<>();
    }
    
    // Getters and setters
    public String getTo() { return to; }
    public String getContent() { return content; }
    public String getSenderId() { return senderId; }
    public SMSPriority getPriority() { return priority; }
    public boolean isEnableDeliveryReport() { return enableDeliveryReport; }
    public boolean isUnicodeEnabled() { return unicodeEnabled; }
    public boolean isFlashSms() { return flashSms; }
    public Integer getValidityPeriod() { return validityPeriod; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public Map<String, String> getCustomParameters() { return customParameters; }
    
    public void setTo(String to) { this.to = to; }
    public void setContent(String content) { this.content = content; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setPriority(SMSPriority priority) { this.priority = priority; }
    public void setEnableDeliveryReport(boolean enableDeliveryReport) { this.enableDeliveryReport = enableDeliveryReport; }
    public void setUnicodeEnabled(boolean unicodeEnabled) { this.unicodeEnabled = unicodeEnabled; }
    public void setFlashSms(boolean flashSms) { this.flashSms = flashSms; }
    public void setValidityPeriod(Integer validityPeriod) { this.validityPeriod = validityPeriod; }
    
    public void addCustomParameter(String key, String value) {
        customParameters.put(key, value);
    }
}

// SMS priority enumeration
enum SMSPriority {
    HIGH, NORMAL, LOW
}

// SMS result class
class SMSResult {
    private boolean success;
    private String message;
    private String messageId;
    private String errorCode;
    private LocalDateTime timestamp;
    
    private SMSResult(boolean success, String message, String messageId, String errorCode) {
        this.success = success;
        this.message = message;
        this.messageId = messageId;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
    
    public static SMSResult success(String message, String messageId) {
        return new SMSResult(true, message, messageId, null);
    }
    
    public static SMSResult failure(String message, String errorCode) {
        return new SMSResult(false, message, null, errorCode);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getMessageId() { return messageId; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

// Phone number validator
class PhoneNumberValidator {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        
        // Remove common formatting characters
        String cleanNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)\\.]", "");
        
        // Basic E.164 format validation
        return PHONE_PATTERN.matcher(cleanNumber).matches();
    }
    
    public PhoneValidationResult validatePhoneNumber(String phoneNumber) {
        try {
            if (!isValidPhoneNumber(phoneNumber)) {
                return PhoneValidationResult.failure("Invalid phone number format: " + phoneNumber);
            }
            
            String cleanNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)\\.]", "");
            
            // Additional validations
            if (cleanNumber.length() < 7) {
                return PhoneValidationResult.failure("Phone number too short");
            }
            
            if (cleanNumber.length() > 15) {
                return PhoneValidationResult.failure("Phone number too long");
            }
            
            // Extract country code and format
            PhoneNumberInfo info = parsePhoneNumber(cleanNumber);
            
            return PhoneValidationResult.success("Phone number is valid", info);
            
        } catch (Exception e) {
            return PhoneValidationResult.failure("Phone validation error: " + e.getMessage());
        }
    }
    
    private PhoneNumberInfo parsePhoneNumber(String phoneNumber) {
        PhoneNumberInfo info = new PhoneNumberInfo();
        
        if (phoneNumber.startsWith("+")) {
            info.setInternationalFormat(phoneNumber);
            info.setNationalFormat(phoneNumber.substring(1));
        } else {
            info.setNationalFormat(phoneNumber);
            info.setInternationalFormat("+" + phoneNumber);
        }
        
        // Simple country code extraction (first 1-3 digits)
        String withoutPlus = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
        
        if (withoutPlus.startsWith("1")) {
            info.setCountryCode("1");
            info.setCountryName("US/Canada");
        } else if (withoutPlus.startsWith("44")) {
            info.setCountryCode("44");
            info.setCountryName("United Kingdom");
        } else if (withoutPlus.startsWith("91")) {
            info.setCountryCode("91");
            info.setCountryName("India");
        } else {
            info.setCountryCode("Unknown");
            info.setCountryName("Unknown");
        }
        
        return info;
    }
}

// Phone number information class
class PhoneNumberInfo {
    private String nationalFormat;
    private String internationalFormat;
    private String countryCode;
    private String countryName;
    
    // Getters and setters
    public String getNationalFormat() { return nationalFormat; }
    public String getInternationalFormat() { return internationalFormat; }
    public String getCountryCode() { return countryCode; }
    public String getCountryName() { return countryName; }
    
    public void setNationalFormat(String nationalFormat) { this.nationalFormat = nationalFormat; }
    public void setInternationalFormat(String internationalFormat) { this.internationalFormat = internationalFormat; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
}

// Phone validation result
class PhoneValidationResult {
    private boolean success;
    private String message;
    private PhoneNumberInfo phoneInfo;
    
    private PhoneValidationResult(boolean success, String message, PhoneNumberInfo phoneInfo) {
        this.success = success;
        this.message = message;
        this.phoneInfo = phoneInfo;
    }
    
    public static PhoneValidationResult success(String message, PhoneNumberInfo phoneInfo) {
        return new PhoneValidationResult(true, message, phoneInfo);
    }
    
    public static PhoneValidationResult failure(String message) {
        return new PhoneValidationResult(false, message, null);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PhoneNumberInfo getPhoneInfo() { return phoneInfo; }
}

// SMS template manager
class SMSTemplateManager {
    private Map<String, SMSTemplate> templates;
    
    public SMSTemplateManager() {
        this.templates = new HashMap<>();
        initializeDefaultTemplates();
    }
    
    private void initializeDefaultTemplates() {
        // OTP template
        SMSTemplate otpTemplate = new SMSTemplate(
            "otp",
            "Your {{service_name}} verification code is {{otp_code}}. Valid for {{validity_minutes}} minutes. Do not share this code."
        );
        templates.put("otp", otpTemplate);
        
        // Welcome template
        SMSTemplate welcomeTemplate = new SMSTemplate(
            "welcome",
            "Welcome to {{service_name}}, {{name}}! Your account is now active. Reply STOP to opt out."
        );
        templates.put("welcome", welcomeTemplate);
        
        // Alert template
        SMSTemplate alertTemplate = new SMSTemplate(
            "alert",
            "ALERT: {{alert_message}} Time: {{timestamp}}. For support, contact {{support_number}}."
        );
        templates.put("alert", alertTemplate);
    }
    
    public SMSTemplateResult createTemplate(SMSTemplateRequest request) {
        try {
            if (request.getTemplateId() == null || request.getTemplateId().isEmpty()) {
                return SMSTemplateResult.failure("Template ID is required");
            }
            
            if (request.getContent() == null || request.getContent().isEmpty()) {
                return SMSTemplateResult.failure("Template content is required");
            }
            
            SMSTemplate template = new SMSTemplate(request.getTemplateId(), request.getContent());
            templates.put(request.getTemplateId(), template);
            
            return SMSTemplateResult.success("SMS template created successfully", template);
            
        } catch (Exception e) {
            return SMSTemplateResult.failure("SMS template creation failed: " + e.getMessage());
        }
    }
    
    public SMSTemplate getTemplate(String templateId) {
        return templates.get(templateId);
    }
    
    public String renderTemplate(String templateId, Map<String, String> variables) {
        SMSTemplate template = templates.get(templateId);
        if (template == null) {
            return null;
        }
        
        String rendered = template.getContent();
        
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            rendered = rendered.replace(placeholder, entry.getValue());
        }
        
        return rendered;
    }
}

// SMS template class
class SMSTemplate {
    private String templateId;
    private String content;
    private LocalDateTime createdTime;
    
    public SMSTemplate(String templateId, String content) {
        this.templateId = templateId;
        this.content = content;
        this.createdTime = LocalDateTime.now();
    }
    
    // Getters
    public String getTemplateId() { return templateId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedTime() { return createdTime; }
}

// SMS template request
class SMSTemplateRequest {
    private String templateId;
    private String content;
    
    public SMSTemplateRequest(String templateId, String content) {
        this.templateId = templateId;
        this.content = content;
    }
    
    // Getters
    public String getTemplateId() { return templateId; }
    public String getContent() { return content; }
}

// SMS template result
class SMSTemplateResult {
    private boolean success;
    private String message;
    private SMSTemplate template;
    
    private SMSTemplateResult(boolean success, String message, SMSTemplate template) {
        this.success = success;
        this.message = message;
        this.template = template;
    }
    
    public static SMSTemplateResult success(String message, SMSTemplate template) {
        return new SMSTemplateResult(true, message, template);
    }
    
    public static SMSTemplateResult failure(String message) {
        return new SMSTemplateResult(false, message, null);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public SMSTemplate getTemplate() { return template; }
}

// Message segment manager
class MessageSegmentManager {
    private int maxSingleSMSLength;
    private int maxConcatenatedSMSLength;
    private int unicodeSingleSMSLength;
    private int unicodeConcatenatedSMSLength;
    
    public MessageSegmentManager(int maxLength) {
        this.maxSingleSMSLength = maxLength;
        this.maxConcatenatedSMSLength = 153; // For concatenated SMS
        this.unicodeSingleSMSLength = 70;
        this.unicodeConcatenatedSMSLength = 67;
    }
    
    public MessageSegmentResult analyzeMessage(String content) {
        try {
            if (content == null || content.isEmpty()) {
                return MessageSegmentResult.failure("Message content is empty");
            }
            
            boolean isUnicode = containsUnicodeCharacters(content);
            int singleSMSLimit = isUnicode ? unicodeSingleSMSLength : maxSingleSMSLength;
            int concatenatedSMSLimit = isUnicode ? unicodeConcatenatedSMSLength : maxConcatenatedSMSLength;
            
            MessageSegmentInfo segmentInfo = new MessageSegmentInfo();
            segmentInfo.setContent(content);
            segmentInfo.setLength(content.length());
            segmentInfo.setUnicode(isUnicode);
            segmentInfo.setSingleSMSLimit(singleSMSLimit);
            segmentInfo.setConcatenatedSMSLimit(concatenatedSMSLimit);
            
            if (content.length() <= singleSMSLimit) {
                segmentInfo.setSegmentCount(1);
                segmentInfo.setRemainingCharacters(singleSMSLimit - content.length());
            } else {
                int segmentCount = (int) Math.ceil((double) content.length() / concatenatedSMSLimit);
                segmentInfo.setSegmentCount(segmentCount);
                segmentInfo.setRemainingCharacters(concatenatedSMSLimit - (content.length() % concatenatedSMSLimit));
            }
            
            // Calculate segments
            List<String> segments = splitIntoSegments(content, singleSMSLimit, concatenatedSMSLimit);
            segmentInfo.setSegments(segments);
            
            return MessageSegmentResult.success("Message analyzed successfully", segmentInfo);
            
        } catch (Exception e) {
            return MessageSegmentResult.failure("Message analysis failed: " + e.getMessage());
        }
    }
    
    private boolean containsUnicodeCharacters(String content) {
        return !content.matches("^[\\x00-\\x7F]*$");
    }
    
    private List<String> splitIntoSegments(String content, int singleLimit, int concatenatedLimit) {
        List<String> segments = new ArrayList<>();
        
        if (content.length() <= singleLimit) {
            segments.add(content);
        } else {
            int pos = 0;
            while (pos < content.length()) {
                int endPos = Math.min(pos + concatenatedLimit, content.length());
                segments.add(content.substring(pos, endPos));
                pos = endPos;
            }
        }
        
        return segments;
    }
}

// Message segment information
class MessageSegmentInfo {
    private String content;
    private int length;
    private boolean unicode;
    private int segmentCount;
    private int remainingCharacters;
    private int singleSMSLimit;
    private int concatenatedSMSLimit;
    private List<String> segments;
    
    public MessageSegmentInfo() {
        this.segments = new ArrayList<>();
    }
    
    // Getters and setters
    public String getContent() { return content; }
    public int getLength() { return length; }
    public boolean isUnicode() { return unicode; }
    public int getSegmentCount() { return segmentCount; }
    public int getRemainingCharacters() { return remainingCharacters; }
    public int getSingleSMSLimit() { return singleSMSLimit; }
    public int getConcatenatedSMSLimit() { return concatenatedSMSLimit; }
    public List<String> getSegments() { return segments; }
    
    public void setContent(String content) { this.content = content; }
    public void setLength(int length) { this.length = length; }
    public void setUnicode(boolean unicode) { this.unicode = unicode; }
    public void setSegmentCount(int segmentCount) { this.segmentCount = segmentCount; }
    public void setRemainingCharacters(int remainingCharacters) { this.remainingCharacters = remainingCharacters; }
    public void setSingleSMSLimit(int singleSMSLimit) { this.singleSMSLimit = singleSMSLimit; }
    public void setConcatenatedSMSLimit(int concatenatedSMSLimit) { this.concatenatedSMSLimit = concatenatedSMSLimit; }
    public void setSegments(List<String> segments) { this.segments = segments; }
}

// Message segment result
class MessageSegmentResult {
    private boolean success;
    private String message;
    private MessageSegmentInfo segmentInfo;
    
    private MessageSegmentResult(boolean success, String message, MessageSegmentInfo segmentInfo) {
        this.success = success;
        this.message = message;
        this.segmentInfo = segmentInfo;
    }
    
    public static MessageSegmentResult success(String message, MessageSegmentInfo segmentInfo) {
        return new MessageSegmentResult(true, message, segmentInfo);
    }
    
    public static MessageSegmentResult failure(String message) {
        return new MessageSegmentResult(false, message, null);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public MessageSegmentInfo getSegmentInfo() { return segmentInfo; }
}

// Delivery report classes
class DeliveryReport {
    private String messageId;
    private String phoneNumber;
    private DeliveryStatus status;
    private String statusMessage;
    private LocalDateTime deliveryTime;
    
    public DeliveryReport(String messageId, String phoneNumber, DeliveryStatus status, String statusMessage, LocalDateTime deliveryTime) {
        this.messageId = messageId;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.statusMessage = statusMessage;
        this.deliveryTime = deliveryTime;
    }
    
    // Getters
    public String getMessageId() { return messageId; }
    public String getPhoneNumber() { return phoneNumber; }
    public DeliveryStatus getStatus() { return status; }
    public String getStatusMessage() { return statusMessage; }
    public LocalDateTime getDeliveryTime() { return deliveryTime; }
}

class DeliveryReportResult {
    private boolean success;
    private String message;
    private List<DeliveryReport> reports;
    
    private DeliveryReportResult(boolean success, String message, List<DeliveryReport> reports) {
        this.success = success;
        this.message = message;
        this.reports = reports;
    }
    
    public static DeliveryReportResult success(String message, List<DeliveryReport> reports) {
        return new DeliveryReportResult(true, message, reports);
    }
    
    public static DeliveryReportResult failure(String message) {
        return new DeliveryReportResult(false, message, new ArrayList<>());
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<DeliveryReport> getReports() { return reports; }
}
