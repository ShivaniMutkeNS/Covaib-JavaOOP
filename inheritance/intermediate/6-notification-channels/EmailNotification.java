/**
 * Email Notification class extending Notification
 * Demonstrates inheritance vs strategy pattern
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class EmailNotification extends Notification {
    private String fromEmail;
    private String toEmail;
    private String ccEmails;
    private String bccEmails;
    private boolean hasAttachment;
    private String attachmentPath;
    private String emailFormat;
    private boolean isHtml;
    private String deliveryReport;
    
    /**
     * Constructor for EmailNotification
     * @param recipient Recipient email address
     * @param subject Subject of the email
     * @param message Message content
     * @param priority Priority level (1-5)
     * @param fromEmail Sender email address
     * @param ccEmails CC email addresses (comma-separated)
     * @param bccEmails BCC email addresses (comma-separated)
     * @param isHtml Whether email is HTML format
     */
    public EmailNotification(String recipient, String subject, String message, int priority, 
                           String fromEmail, String ccEmails, String bccEmails, boolean isHtml) {
        super(recipient, subject, message, priority, "Email");
        this.fromEmail = fromEmail;
        this.toEmail = recipient;
        this.ccEmails = ccEmails;
        this.bccEmails = bccEmails;
        this.hasAttachment = false;
        this.attachmentPath = "";
        this.isHtml = isHtml;
        this.emailFormat = isHtml ? "HTML" : "Plain Text";
        this.deliveryReport = "";
    }
    
    /**
     * Override send method with email-specific logic
     * @return True if email sent successfully
     */
    @Override
    public boolean send() {
        if (!validate()) {
            return false;
        }
        
        // Simulate email sending process
        System.out.println("Sending email notification...");
        System.out.println("From: " + fromEmail);
        System.out.println("To: " + toEmail);
        if (ccEmails != null && !ccEmails.trim().isEmpty()) {
            System.out.println("CC: " + ccEmails);
        }
        if (bccEmails != null && !bccEmails.trim().isEmpty()) {
            System.out.println("BCC: " + bccEmails);
        }
        System.out.println("Subject: " + subject);
        System.out.println("Format: " + emailFormat);
        if (hasAttachment) {
            System.out.println("Attachment: " + attachmentPath);
        }
        
        // Simulate sending delay based on priority
        try {
            Thread.sleep(priority * 100); // Higher priority = faster sending
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success/failure based on priority
        boolean success = priority <= 3; // Higher priority = more likely to succeed
        
        if (success) {
            isSent = true;
            status = "Delivered";
            deliveryReport = "Email delivered successfully to " + toEmail;
            System.out.println("✅ Email sent successfully!");
        } else {
            status = "Failed";
            deliveryReport = "Email delivery failed - server timeout";
            System.out.println("❌ Email sending failed!");
        }
        
        return success;
    }
    
    /**
     * Override getDeliveryStatus method with email-specific tracking
     * @return String with delivery status
     */
    @Override
    public String getDeliveryStatus() {
        if (!isSent) {
            return "Email not sent yet";
        }
        
        return String.format("Email Status: %s, Delivery Report: %s, Format: %s, Timestamp: %s", 
                           status, deliveryReport, emailFormat, timestamp);
    }
    
    /**
     * Override getNotificationFeatures method with email features
     * @return String description of email features
     */
    @Override
    public String getNotificationFeatures() {
        return "Email Features: " +
               "Format: " + emailFormat + ", " +
               "CC: " + (ccEmails != null && !ccEmails.trim().isEmpty() ? "Yes" : "No") + ", " +
               "BCC: " + (bccEmails != null && !bccEmails.trim().isEmpty() ? "Yes" : "No") + ", " +
               "Attachment: " + (hasAttachment ? "Yes" : "No") + ", " +
               "Delivery Report: " + (deliveryReport.isEmpty() ? "Not available" : "Available") + ", " +
               "Priority: " + getPriorityDescription();
    }
    
    /**
     * Email-specific method to add attachment
     * @param attachmentPath Path to the attachment file
     * @return True if attachment added successfully
     */
    public boolean addAttachment(String attachmentPath) {
        if (attachmentPath == null || attachmentPath.trim().isEmpty()) {
            System.out.println("Error: Attachment path cannot be empty");
            return false;
        }
        
        this.attachmentPath = attachmentPath;
        this.hasAttachment = true;
        System.out.println("Attachment added: " + attachmentPath);
        return true;
    }
    
    /**
     * Email-specific method to add CC recipients
     * @param ccEmails CC email addresses (comma-separated)
     * @return True if CC added successfully
     */
    public boolean addCC(String ccEmails) {
        if (ccEmails == null || ccEmails.trim().isEmpty()) {
            System.out.println("Error: CC emails cannot be empty");
            return false;
        }
        
        this.ccEmails = ccEmails;
        System.out.println("CC recipients added: " + ccEmails);
        return true;
    }
    
    /**
     * Email-specific method to add BCC recipients
     * @param bccEmails BCC email addresses (comma-separated)
     * @return True if BCC added successfully
     */
    public boolean addBCC(String bccEmails) {
        if (bccEmails == null || bccEmails.trim().isEmpty()) {
            System.out.println("Error: BCC emails cannot be empty");
            return false;
        }
        
        this.bccEmails = bccEmails;
        System.out.println("BCC recipients added: " + bccEmails);
        return true;
    }
    
    /**
     * Email-specific method to get email summary
     * @return String with email summary
     */
    public String getEmailSummary() {
        return String.format("Email Summary: %s -> %s, Subject: %s, Format: %s, Attachment: %s", 
                           fromEmail, toEmail, subject, emailFormat, hasAttachment ? "Yes" : "No");
    }
    
    /**
     * Email-specific method to check if email is valid
     * @return True if email format is valid
     */
    public boolean isValidEmailFormat() {
        return toEmail.contains("@") && toEmail.contains(".");
    }
    
    /**
     * Getter for from email
     * @return The sender email
     */
    public String getFromEmail() {
        return fromEmail;
    }
    
    /**
     * Getter for to email
     * @return The recipient email
     */
    public String getToEmail() {
        return toEmail;
    }
    
    /**
     * Getter for CC emails
     * @return The CC email addresses
     */
    public String getCcEmails() {
        return ccEmails;
    }
    
    /**
     * Getter for BCC emails
     * @return The BCC email addresses
     */
    public String getBccEmails() {
        return bccEmails;
    }
    
    /**
     * Getter for attachment status
     * @return True if email has attachment
     */
    public boolean hasAttachment() {
        return hasAttachment;
    }
    
    /**
     * Getter for attachment path
     * @return The attachment path
     */
    public String getAttachmentPath() {
        return attachmentPath;
    }
    
    /**
     * Getter for email format
     * @return The email format
     */
    public String getEmailFormat() {
        return emailFormat;
    }
    
    /**
     * Getter for HTML status
     * @return True if email is HTML format
     */
    public boolean isHtml() {
        return isHtml;
    }
    
    /**
     * Getter for delivery report
     * @return The delivery report
     */
    public String getDeliveryReport() {
        return deliveryReport;
    }
    
    /**
     * Override toString to include email-specific details
     * @return String representation of the email notification
     */
    @Override
    public String toString() {
        return super.toString() + " [From: " + fromEmail + ", Format: " + emailFormat + ", Attachment: " + hasAttachment + "]";
    }
}
