package composition.notification;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification Message data class
 */
public class NotificationMessage {
    private final String messageId;
    private final String recipient;
    private final String subject;
    private final String content;
    private final NotificationPriority priority;
    private final LocalDateTime timestamp;
    private final Map<String, String> metadata;
    
    public NotificationMessage(String messageId, String recipient, String subject, 
                             String content, NotificationPriority priority, 
                             Map<String, String> metadata) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
        this.metadata = metadata;
    }
    
    public String getMessageId() { return messageId; }
    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public NotificationPriority getPriority() { return priority; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, String> getMetadata() { return metadata; }
}
