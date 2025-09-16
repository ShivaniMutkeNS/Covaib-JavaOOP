package composition.notification;

/**
 * Notification Channel Strategy Interface
 */
public interface NotificationChannel {
    boolean sendMessage(NotificationMessage message);
    String getChannelType();
    boolean isAvailable();
    int getMaxRetries();
}
