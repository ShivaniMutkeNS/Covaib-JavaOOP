package composition.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Notification Metrics tracking class
 */
public class NotificationMetrics {
    private final Map<String, Integer> successCounts;
    private final Map<String, Integer> totalCounts;
    
    public NotificationMetrics() {
        this.successCounts = new HashMap<>();
        this.totalCounts = new HashMap<>();
    }
    
    public void recordAttempt(String channelType, boolean success) {
        totalCounts.put(channelType, totalCounts.getOrDefault(channelType, 0) + 1);
        if (success) {
            successCounts.put(channelType, successCounts.getOrDefault(channelType, 0) + 1);
        }
    }
    
    public double getSuccessRate(String channelType) {
        int total = totalCounts.getOrDefault(channelType, 0);
        if (total == 0) return 0.0;
        
        int success = successCounts.getOrDefault(channelType, 0);
        return (success * 100.0) / total;
    }
    
    public int getTotalAttempts() {
        return totalCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getOverallSuccessRate() {
        int totalSuccess = successCounts.values().stream().mapToInt(Integer::intValue).sum();
        int totalAttempts = getTotalAttempts();
        
        if (totalAttempts == 0) return 0.0;
        return (totalSuccess * 100.0) / totalAttempts;
    }
}
