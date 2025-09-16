package composition.notification;

import java.util.List;

/**
 * Notification Result containing results from all channels
 */
public class NotificationResult {
    private final String messageId;
    private final List<ChannelResult> channelResults;
    
    public NotificationResult(String messageId, List<ChannelResult> channelResults) {
        this.messageId = messageId;
        this.channelResults = channelResults;
    }
    
    public boolean isAnySuccessful() {
        return channelResults.stream().anyMatch(ChannelResult::isSuccess);
    }
    
    public boolean isAllSuccessful() {
        return channelResults.stream().allMatch(ChannelResult::isSuccess);
    }
    
    public int getSuccessCount() {
        return (int) channelResults.stream().filter(ChannelResult::isSuccess).count();
    }
    
    public String getMessageId() { return messageId; }
    public List<ChannelResult> getChannelResults() { return channelResults; }
}
