package composition.notification;

/**
 * Channel Result data class
 */
public class ChannelResult {
    private final String channelType;
    private final boolean success;
    private final String errorMessage;
    
    public ChannelResult(String channelType, boolean success, String errorMessage) {
        this.channelType = channelType;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    public String getChannelType() { return channelType; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}
