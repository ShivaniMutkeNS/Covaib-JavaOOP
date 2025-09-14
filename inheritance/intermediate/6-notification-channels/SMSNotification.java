/**
 * SMS Notification class extending Notification
 * Demonstrates inheritance vs strategy pattern
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class SMSNotification extends Notification {
    private String phoneNumber;
    private String countryCode;
    private String carrier;
    private boolean isInternational;
    private String smsFormat;
    private int characterCount;
    private int maxCharacters;
    private String deliveryStatus;
    private double cost;
    private String senderId;
    
    /**
     * Constructor for SMSNotification
     * @param recipient Recipient phone number
     * @param subject Subject of the SMS (optional)
     * @param message Message content
     * @param priority Priority level (1-5)
     * @param countryCode Country code
     * @param carrier Mobile carrier
     * @param isInternational Whether it's an international SMS
     * @param senderId Sender ID
     */
    public SMSNotification(String recipient, String subject, String message, int priority, 
                          String countryCode, String carrier, boolean isInternational, String senderId) {
        super(recipient, subject, message, priority, "SMS");
        this.phoneNumber = recipient;
        this.countryCode = countryCode;
        this.carrier = carrier;
        this.isInternational = isInternational;
        this.smsFormat = "Text";
        this.characterCount = message.length();
        this.maxCharacters = 160; // Standard SMS length
        this.deliveryStatus = "";
        this.cost = 0.0;
        this.senderId = senderId;
    }
    
    /**
     * Override send method with SMS-specific logic
     * @return True if SMS sent successfully
     */
    @Override
    public boolean send() {
        if (!validate()) {
            return false;
        }
        
        // Check message length
        if (characterCount > maxCharacters) {
            System.out.println("Error: SMS message too long (" + characterCount + " characters, max " + maxCharacters + ")");
            return false;
        }
        
        // Simulate SMS sending process
        System.out.println("Sending SMS notification...");
        System.out.println("To: " + countryCode + phoneNumber);
        System.out.println("Carrier: " + carrier);
        System.out.println("Sender ID: " + senderId);
        System.out.println("Message: " + message);
        System.out.println("Characters: " + characterCount + "/" + maxCharacters);
        System.out.println("International: " + (isInternational ? "Yes" : "No"));
        
        // Calculate cost
        calculateCost();
        System.out.println("Cost: $" + String.format("%.4f", cost));
        
        // Simulate sending delay based on priority
        try {
            Thread.sleep(priority * 50); // Higher priority = faster sending
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success/failure based on priority and international status
        boolean success = priority <= 4 && (!isInternational || priority <= 2);
        
        if (success) {
            isSent = true;
            status = "Delivered";
            deliveryStatus = "SMS delivered successfully to " + countryCode + phoneNumber;
            System.out.println("✅ SMS sent successfully!");
        } else {
            status = "Failed";
            deliveryStatus = "SMS delivery failed - " + (isInternational ? "international delivery issue" : "carrier issue");
            System.out.println("❌ SMS sending failed!");
        }
        
        return success;
    }
    
    /**
     * Override getDeliveryStatus method with SMS-specific tracking
     * @return String with delivery status
     */
    @Override
    public String getDeliveryStatus() {
        if (!isSent) {
            return "SMS not sent yet";
        }
        
        return String.format("SMS Status: %s, Delivery Report: %s, Carrier: %s, Cost: $%.4f, Timestamp: %s", 
                           status, deliveryStatus, carrier, cost, timestamp);
    }
    
    /**
     * Override getNotificationFeatures method with SMS features
     * @return String description of SMS features
     */
    @Override
    public String getNotificationFeatures() {
        return "SMS Features: " +
               "Format: " + smsFormat + ", " +
               "Character Count: " + characterCount + "/" + maxCharacters + ", " +
               "Carrier: " + carrier + ", " +
               "International: " + (isInternational ? "Yes" : "No") + ", " +
               "Cost: $" + String.format("%.4f", cost) + ", " +
               "Sender ID: " + senderId + ", " +
               "Priority: " + getPriorityDescription();
    }
    
    /**
     * SMS-specific method to calculate cost
     */
    private void calculateCost() {
        double baseCost = 0.01; // Base cost per SMS
        if (isInternational) {
            baseCost *= 2.0; // Double cost for international
        }
        
        // Cost increases with message length
        if (characterCount > 80) {
            baseCost *= 1.5; // 50% more for long messages
        }
        
        // Priority affects cost
        if (priority <= 2) {
            baseCost *= 2.0; // Double cost for high priority
        }
        
        this.cost = baseCost;
    }
    
    /**
     * SMS-specific method to check if message fits in single SMS
     * @return True if message fits in single SMS
     */
    public boolean isSingleSMS() {
        return characterCount <= maxCharacters;
    }
    
    /**
     * SMS-specific method to get message segments
     * @return Number of SMS segments needed
     */
    public int getMessageSegments() {
        return (int) Math.ceil((double) characterCount / maxCharacters);
    }
    
    /**
     * SMS-specific method to truncate message
     * @param maxLength Maximum length allowed
     * @return Truncated message
     */
    public String truncateMessage(int maxLength) {
        if (message.length() <= maxLength) {
            return message;
        }
        
        String truncated = message.substring(0, maxLength - 3) + "...";
        this.message = truncated;
        this.characterCount = truncated.length();
        return truncated;
    }
    
    /**
     * SMS-specific method to get phone number info
     * @return String with phone number information
     */
    public String getPhoneNumberInfo() {
        return String.format("Phone: %s%s, Carrier: %s, International: %s", 
                           countryCode, phoneNumber, carrier, isInternational ? "Yes" : "No");
    }
    
    /**
     * SMS-specific method to check if phone number is valid
     * @return True if phone number format is valid
     */
    public boolean isValidPhoneNumber() {
        return phoneNumber.matches("\\d{10}") && countryCode.matches("\\+\\d{1,3}");
    }
    
    /**
     * Getter for phone number
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Getter for country code
     * @return The country code
     */
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * Getter for carrier
     * @return The mobile carrier
     */
    public String getCarrier() {
        return carrier;
    }
    
    /**
     * Getter for international status
     * @return True if SMS is international
     */
    public boolean isInternational() {
        return isInternational;
    }
    
    /**
     * Getter for SMS format
     * @return The SMS format
     */
    public String getSmsFormat() {
        return smsFormat;
    }
    
    /**
     * Getter for character count
     * @return The character count
     */
    public int getCharacterCount() {
        return characterCount;
    }
    
    /**
     * Getter for max characters
     * @return The maximum characters allowed
     */
    public int getMaxCharacters() {
        return maxCharacters;
    }
    
    /**
     * Getter for delivery status
     * @return The delivery status
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    /**
     * Getter for cost
     * @return The SMS cost
     */
    public double getCost() {
        return cost;
    }
    
    /**
     * Getter for sender ID
     * @return The sender ID
     */
    public String getSenderId() {
        return senderId;
    }
    
    /**
     * Override toString to include SMS-specific details
     * @return String representation of the SMS notification
     */
    @Override
    public String toString() {
        return super.toString() + " [Phone: " + countryCode + phoneNumber + ", Carrier: " + carrier + ", Cost: $" + String.format("%.4f", cost) + "]";
    }
}
