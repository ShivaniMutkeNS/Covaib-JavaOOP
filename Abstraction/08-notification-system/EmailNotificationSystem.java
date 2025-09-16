package abstraction.notificationsystem;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Email Notification System - Concrete implementation for email notifications
 * Demonstrates SMTP integration and email-specific features
 */
public class EmailNotificationSystem extends NotificationSystem {
    
    private SMTPClient smtpClient;
    private EmailTemplateEngine templateEngine;
    private AttachmentManager attachmentManager;
    private EmailValidator emailValidator;
    
    // Email-specific configuration
    private String smtpHost;
    private int smtpPort;
    private String username;
    private String password;
    private boolean useSSL;
    private boolean useTLS;
    
    public EmailNotificationSystem(String systemId, String systemName, Map<String, Object> configuration) {
        super(systemId, systemName, NotificationChannel.EMAIL, configuration);
    }
    
    @Override
    protected void initialize() {
        // Initialize email-specific settings
        this.smtpHost = (String) configuration.getOrDefault("smtp_host", "smtp.gmail.com");
        this.smtpPort = (Integer) configuration.getOrDefault("smtp_port", 587);
        this.username = (String) configuration.getOrDefault("username", "");
        this.password = (String) configuration.getOrDefault("password", "");
        this.useSSL = (Boolean) configuration.getOrDefault("use_ssl", false);
        this.useTLS = (Boolean) configuration.getOrDefault("use_tls", true);
        
        this.smtpClient = new SMTPClient(smtpHost, smtpPort, username, password, useSSL, useTLS);
        this.templateEngine = new EmailTemplateEngine();
        this.attachmentManager = new AttachmentManager();
        this.emailValidator = new EmailValidator();
        
        // Configure SMTP client
        configureSMTPClient();
    }
    
    @Override
    protected MessageProcessor createMessageProcessor() {
        return new EmailMessageProcessor();
    }
    
    @Override
    protected NotificationResult performChannelDelivery(NotificationRequest request, ProcessedMessage message) {
        try {
            // Create email message
            EmailMessage emailMessage = createEmailMessage(request, message);
            
            // Send email
            SMTPResult smtpResult = smtpClient.sendEmail(emailMessage);
            
            if (smtpResult.isSuccess()) {
                return NotificationResult.success(
                    "Email sent successfully",
                    smtpResult.getMessageId()
                );
            } else {
                NotificationResult result = NotificationResult.failure("Email delivery failed: " + smtpResult.getErrorMessage());
                result.setErrorCode(smtpResult.getErrorCode());
                return result;
            }
            
        } catch (Exception e) {
            NotificationResult result = NotificationResult.failure("Email delivery error: " + e.getMessage());
            result.setErrorCode("DELIVERY_ERROR");
            return result;
        }
    }
    
    @Override
    protected ValidationResult validateChannelSpecificOptions(NotificationOptions options) {
        try {
            if (options.getChannelSpecificOptions().containsKey("cc_recipients")) {
                @SuppressWarnings("unchecked")
                List<String> ccRecipients = (List<String>) options.getChannelSpecificOptions().get("cc_recipients");
                for (String email : ccRecipients) {
                    if (!emailValidator.isValidEmail(email)) {
                        return ValidationResult.failure("Invalid CC email address: " + email);
                    }
                }
            }
            
            if (options.getChannelSpecificOptions().containsKey("bcc_recipients")) {
                @SuppressWarnings("unchecked")
                List<String> bccRecipients = (List<String>) options.getChannelSpecificOptions().get("bcc_recipients");
                for (String email : bccRecipients) {
                    if (!emailValidator.isValidEmail(email)) {
                        return ValidationResult.failure("Invalid BCC email address: " + email);
                    }
                }
            }
            
            if (options.getChannelSpecificOptions().containsKey("reply_to")) {
                String replyTo = (String) options.getChannelSpecificOptions().get("reply_to");
                if (!emailValidator.isValidEmail(replyTo)) {
                    return ValidationResult.failure("Invalid reply-to email address: " + replyTo);
                }
            }
            
            return ValidationResult.success("Email options validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("Email options validation error: " + e.getMessage());
        }
    }
    
    @Override
    protected String getChannelEndpoint() {
        return smtpHost + ":" + smtpPort;
    }
    
    @Override
    protected Map<String, String> getChannelHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Mailer", "NotificationSystem v1.0");
        headers.put("X-System-ID", systemId);
        headers.put("X-Channel", "EMAIL");
        return headers;
    }
    
    private void configureSMTPClient() {
        Map<String, Object> clientConfig = new HashMap<>();
        clientConfig.put("connection_timeout", 30000);
        clientConfig.put("read_timeout", 60000);
        clientConfig.put("max_retries", 3);
        clientConfig.put("enable_debug", false);
        
        smtpClient.configure(clientConfig);
    }
    
    private EmailMessage createEmailMessage(NotificationRequest request, ProcessedMessage message) {
        EmailMessage emailMessage = new EmailMessage();
        
        // Basic email properties
        emailMessage.setFrom(username);
        emailMessage.setTo(request.getRecipient().getContactInfo());
        emailMessage.setSubject(message.getProcessedSubject());
        emailMessage.setContent(message.getProcessedContent());
        emailMessage.setPriority(mapPriorityToEmail(request.getPriority()));
        
        // Add channel-specific options
        if (request.getOptions() != null) {
            Map<String, Object> channelOptions = request.getOptions().getChannelSpecificOptions();
            
            // CC recipients
            if (channelOptions.containsKey("cc_recipients")) {
                @SuppressWarnings("unchecked")
                List<String> ccRecipients = (List<String>) channelOptions.get("cc_recipients");
                emailMessage.setCcRecipients(ccRecipients);
            }
            
            // BCC recipients
            if (channelOptions.containsKey("bcc_recipients")) {
                @SuppressWarnings("unchecked")
                List<String> bccRecipients = (List<String>) channelOptions.get("bcc_recipients");
                emailMessage.setBccRecipients(bccRecipients);
            }
            
            // Reply-to
            if (channelOptions.containsKey("reply_to")) {
                emailMessage.setReplyTo((String) channelOptions.get("reply_to"));
            }
            
            // HTML content
            if (channelOptions.containsKey("html_content")) {
                emailMessage.setHtmlContent((String) channelOptions.get("html_content"));
            }
            
            // Attachments
            if (channelOptions.containsKey("attachments")) {
                @SuppressWarnings("unchecked")
                List<EmailAttachment> attachments = (List<EmailAttachment>) channelOptions.get("attachments");
                emailMessage.setAttachments(attachments);
            }
        }
        
        // Add system headers
        Map<String, String> headers = getChannelHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            emailMessage.addHeader(header.getKey(), header.getValue());
        }
        
        return emailMessage;
    }
    
    private EmailPriority mapPriorityToEmail(NotificationPriority priority) {
        switch (priority) {
            case CRITICAL:
            case URGENT:
                return EmailPriority.HIGH;
            case HIGH:
                return EmailPriority.NORMAL;
            case NORMAL:
            case LOW:
            default:
                return EmailPriority.LOW;
        }
    }
    
    @Override
    protected boolean checkConnectivity() {
        try {
            return smtpClient.testConnection();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Email-specific methods
    public CompletableFuture<EmailTemplateResult> createEmailTemplate(EmailTemplateRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return templateEngine.createTemplate(request);
            } catch (Exception e) {
                return EmailTemplateResult.failure("Email template creation failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<AttachmentResult> addAttachment(AttachmentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return attachmentManager.addAttachment(request);
            } catch (Exception e) {
                return AttachmentResult.failure("Attachment processing failed: " + e.getMessage());
            }
        });
    }
    
    public CompletableFuture<BounceHandlingResult> handleBounces() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return smtpClient.processBounces();
            } catch (Exception e) {
                return BounceHandlingResult.failure("Bounce handling failed: " + e.getMessage());
            }
        });
    }
    
    // Getters for email-specific properties
    public String getSmtpHost() { return smtpHost; }
    public int getSmtpPort() { return smtpPort; }
    public String getUsername() { return username; }
    public boolean isUseSSL() { return useSSL; }
    public boolean isUseTLS() { return useTLS; }
    public SMTPClient getSmtpClient() { return smtpClient; }
    public EmailTemplateEngine getTemplateEngine() { return templateEngine; }
    public AttachmentManager getAttachmentManager() { return attachmentManager; }
    public EmailValidator getEmailValidator() { return emailValidator; }
}

// Email-specific message processor
class EmailMessageProcessor extends GenericMessageProcessor {
    
    @Override
    protected void initializeProcessor() {
        super.initializeProcessor();
        processingConfig.put("convert_to_html", true);
        processingConfig.put("add_unsubscribe_link", true);
        processingConfig.put("track_opens", false);
        processingConfig.put("track_clicks", false);
    }
    
    @Override
    public ProcessingResult processMessage(NotificationMessage message) {
        // Call parent processing first
        ProcessingResult baseResult = super.processMessage(message);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        
        ProcessedMessage processedMessage = baseResult.getProcessedMessage();
        
        // Email-specific processing
        if (Boolean.TRUE.equals(processingConfig.get("convert_to_html"))) {
            convertToHTML(processedMessage);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("add_unsubscribe_link"))) {
            addUnsubscribeLink(processedMessage);
        }
        
        return ProcessingResult.success("Email message processing completed", processedMessage);
    }
    
    private void convertToHTML(ProcessedMessage message) {
        String content = message.getProcessedContent();
        
        // Simple text to HTML conversion
        content = content.replace("\n\n", "</p><p>");
        content = content.replace("\n", "<br>");
        content = "<p>" + content + "</p>";
        
        message.getProcessingMetadata().put("html_content", content);
        message.addProcessingStep(new ProcessingStep("html_conversion", "Content converted to HTML", true));
    }
    
    private void addUnsubscribeLink(ProcessedMessage message) {
        String content = message.getProcessedContent();
        String unsubscribeLink = "\n\n---\nTo unsubscribe from these notifications, click here: [Unsubscribe]";
        
        message.setProcessedContent(content + unsubscribeLink);
        message.addProcessingStep(new ProcessingStep("unsubscribe_link", "Unsubscribe link added", true));
    }
}
