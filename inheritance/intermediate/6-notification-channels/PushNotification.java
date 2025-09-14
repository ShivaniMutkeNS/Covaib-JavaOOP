/**
 * Push Notification class extending Notification
 * Demonstrates inheritance vs strategy pattern
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class PushNotification extends Notification {
    private String deviceToken;
    private String platform;
    private String appId;
    private String channelId;
    private String sound;
    private String icon;
    private String imageUrl;
    private String actionUrl;
    private boolean isSilent;
    private int badgeCount;
    private String deliveryStatus;
    private String clickStatus;
    private long deliveryTime;
    
    /**
     * Constructor for PushNotification
     * @param recipient Recipient device token
     * @param subject Subject of the push notification
     * @param message Message content
     * @param priority Priority level (1-5)
     * @param deviceToken Device token for push delivery
     * @param platform Platform (iOS, Android, Web)
     * @param appId Application ID
     * @param channelId Notification channel ID
     */
    public PushNotification(String recipient, String subject, String message, int priority, 
                           String deviceToken, String platform, String appId, String channelId) {
        super(recipient, subject, message, priority, "Push");
        this.deviceToken = deviceToken;
        this.platform = platform;
        this.appId = appId;
        this.channelId = channelId;
        this.sound = "default";
        this.icon = "default";
        this.imageUrl = "";
        this.actionUrl = "";
        this.isSilent = false;
        this.badgeCount = 1;
        this.deliveryStatus = "";
        this.clickStatus = "";
        this.deliveryTime = 0;
    }
    
    /**
     * Override send method with push notification-specific logic
     * @return True if push notification sent successfully
     */
    @Override
    public boolean send() {
        if (!validate()) {
            return false;
        }
        
        // Validate device token
        if (deviceToken == null || deviceToken.trim().isEmpty()) {
            System.out.println("Error: Device token cannot be empty");
            return false;
        }
        
        // Simulate push notification sending process
        System.out.println("Sending push notification...");
        System.out.println("Device Token: " + deviceToken.substring(0, Math.min(20, deviceToken.length())) + "...");
        System.out.println("Platform: " + platform);
        System.out.println("App ID: " + appId);
        System.out.println("Channel: " + channelId);
        System.out.println("Title: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Sound: " + sound);
        System.out.println("Icon: " + icon);
        System.out.println("Badge Count: " + badgeCount);
        System.out.println("Silent: " + (isSilent ? "Yes" : "No"));
        
        // Simulate sending delay based on priority
        try {
            Thread.sleep(priority * 25); // Higher priority = faster sending
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success/failure based on priority and platform
        boolean success = priority <= 4 && (platform.equals("iOS") || platform.equals("Android") || platform.equals("Web"));
        
        if (success) {
            isSent = true;
            status = "Delivered";
            deliveryStatus = "Push notification delivered to " + platform + " device";
            deliveryTime = System.currentTimeMillis();
            System.out.println("✅ Push notification sent successfully!");
        } else {
            status = "Failed";
            deliveryStatus = "Push notification delivery failed - invalid platform or device token";
            System.out.println("❌ Push notification sending failed!");
        }
        
        return success;
    }
    
    /**
     * Override getDeliveryStatus method with push notification-specific tracking
     * @return String with delivery status
     */
    @Override
    public String getDeliveryStatus() {
        if (!isSent) {
            return "Push notification not sent yet";
        }
        
        return String.format("Push Status: %s, Delivery Report: %s, Platform: %s, Click Status: %s, Timestamp: %s", 
                           status, deliveryStatus, platform, clickStatus.isEmpty() ? "Not clicked" : clickStatus, timestamp);
    }
    
    /**
     * Override getNotificationFeatures method with push notification features
     * @return String description of push notification features
     */
    @Override
    public String getNotificationFeatures() {
        return "Push Notification Features: " +
               "Platform: " + platform + ", " +
               "App ID: " + appId + ", " +
               "Channel: " + channelId + ", " +
               "Sound: " + sound + ", " +
               "Icon: " + icon + ", " +
               "Image: " + (imageUrl.isEmpty() ? "No" : "Yes") + ", " +
               "Action URL: " + (actionUrl.isEmpty() ? "No" : "Yes") + ", " +
               "Silent: " + (isSilent ? "Yes" : "No") + ", " +
               "Badge Count: " + badgeCount + ", " +
               "Priority: " + getPriorityDescription();
    }
    
    /**
     * Push notification-specific method to set sound
     * @param sound Sound file name
     * @return True if sound set successfully
     */
    public boolean setSound(String sound) {
        if (sound == null || sound.trim().isEmpty()) {
            System.out.println("Error: Sound cannot be empty");
            return false;
        }
        
        this.sound = sound;
        System.out.println("Sound set to: " + sound);
        return true;
    }
    
    /**
     * Push notification-specific method to set icon
     * @param icon Icon file name
     * @return True if icon set successfully
     */
    public boolean setIcon(String icon) {
        if (icon == null || icon.trim().isEmpty()) {
            System.out.println("Error: Icon cannot be empty");
            return false;
        }
        
        this.icon = icon;
        System.out.println("Icon set to: " + icon);
        return true;
    }
    
    /**
     * Push notification-specific method to add image
     * @param imageUrl URL of the image
     * @return True if image added successfully
     */
    public boolean addImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            System.out.println("Error: Image URL cannot be empty");
            return false;
        }
        
        this.imageUrl = imageUrl;
        System.out.println("Image added: " + imageUrl);
        return true;
    }
    
    /**
     * Push notification-specific method to add action URL
     * @param actionUrl URL for the action
     * @return True if action URL added successfully
     */
    public boolean addActionUrl(String actionUrl) {
        if (actionUrl == null || actionUrl.trim().isEmpty()) {
            System.out.println("Error: Action URL cannot be empty");
            return false;
        }
        
        this.actionUrl = actionUrl;
        System.out.println("Action URL added: " + actionUrl);
        return true;
    }
    
    /**
     * Push notification-specific method to set badge count
     * @param badgeCount Badge count to display
     * @return True if badge count set successfully
     */
    public boolean setBadgeCount(int badgeCount) {
        if (badgeCount < 0) {
            System.out.println("Error: Badge count cannot be negative");
            return false;
        }
        
        this.badgeCount = badgeCount;
        System.out.println("Badge count set to: " + badgeCount);
        return true;
    }
    
    /**
     * Push notification-specific method to set silent mode
     * @param isSilent Whether notification should be silent
     * @return True if silent mode set successfully
     */
    public boolean setSilent(boolean isSilent) {
        this.isSilent = isSilent;
        System.out.println("Silent mode set to: " + (isSilent ? "Yes" : "No"));
        return true;
    }
    
    /**
     * Push notification-specific method to simulate click
     * @return True if click simulated successfully
     */
    public boolean simulateClick() {
        if (!isSent) {
            System.out.println("Error: Push notification not sent yet");
            return false;
        }
        
        clickStatus = "Clicked at " + java.time.LocalDateTime.now().toString();
        System.out.println("Push notification clicked!");
        System.out.println("Click time: " + clickStatus);
        
        return true;
    }
    
    /**
     * Push notification-specific method to get device info
     * @return String with device information
     */
    public String getDeviceInfo() {
        return String.format("Device: %s, Platform: %s, App: %s, Channel: %s", 
                           deviceToken.substring(0, Math.min(10, deviceToken.length())) + "...", 
                           platform, appId, channelId);
    }
    
    /**
     * Push notification-specific method to check if device is online
     * @return True if device is online
     */
    public boolean isDeviceOnline() {
        // Simulate device online status
        return priority <= 3; // Higher priority = more likely to be online
    }
    
    /**
     * Getter for device token
     * @return The device token
     */
    public String getDeviceToken() {
        return deviceToken;
    }
    
    /**
     * Getter for platform
     * @return The platform
     */
    public String getPlatform() {
        return platform;
    }
    
    /**
     * Getter for app ID
     * @return The app ID
     */
    public String getAppId() {
        return appId;
    }
    
    /**
     * Getter for channel ID
     * @return The channel ID
     */
    public String getChannelId() {
        return channelId;
    }
    
    /**
     * Getter for sound
     * @return The sound file name
     */
    public String getSound() {
        return sound;
    }
    
    /**
     * Getter for icon
     * @return The icon file name
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * Getter for image URL
     * @return The image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }
    
    /**
     * Getter for action URL
     * @return The action URL
     */
    public String getActionUrl() {
        return actionUrl;
    }
    
    /**
     * Getter for silent status
     * @return True if notification is silent
     */
    public boolean isSilent() {
        return isSilent;
    }
    
    /**
     * Getter for badge count
     * @return The badge count
     */
    public int getBadgeCount() {
        return badgeCount;
    }
    
    /**
     * Getter for delivery status
     * @return The delivery status
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    /**
     * Getter for click status
     * @return The click status
     */
    public String getClickStatus() {
        return clickStatus;
    }
    
    /**
     * Getter for delivery time
     * @return The delivery time
     */
    public long getDeliveryTime() {
        return deliveryTime;
    }
    
    /**
     * Override toString to include push notification-specific details
     * @return String representation of the push notification
     */
    @Override
    public String toString() {
        return super.toString() + " [Platform: " + platform + ", App: " + appId + ", Channel: " + channelId + ", Silent: " + isSilent + "]";
    }
}
