/**
 * Class to track and manage streaming metrics and analytics
 */
public class StreamingMetrics {
    private long totalBytesStreamed;
    private long totalViewTime; // in seconds
    private int bufferingEvents;
    private double averageBufferHealth; // percentage
    private int qualityChanges;
    private long startTime;
    private boolean isActive;
    
    public StreamingMetrics() {
        this.startTime = System.currentTimeMillis();
        this.isActive = true;
        this.averageBufferHealth = 100.0;
    }
    
    public void recordDataStreamed(long bytes) {
        this.totalBytesStreamed += bytes;
    }
    
    public void recordViewTime(long seconds) {
        this.totalViewTime += seconds;
    }
    
    public void recordBufferingEvent() {
        this.bufferingEvents++;
    }
    
    public void updateBufferHealth(double bufferPercentage) {
        this.averageBufferHealth = (this.averageBufferHealth + bufferPercentage) / 2.0;
    }
    
    public void recordQualityChange() {
        this.qualityChanges++;
    }
    
    public void stopTracking() {
        this.isActive = false;
    }
    
    // Getters
    public long getTotalBytesStreamed() { return totalBytesStreamed; }
    public long getTotalViewTime() { return totalViewTime; }
    public int getBufferingEvents() { return bufferingEvents; }
    public double getAverageBufferHealth() { return averageBufferHealth; }
    public int getQualityChanges() { return qualityChanges; }
    public boolean isActive() { return isActive; }
    
    public double getAverageBitrate() {
        if (totalViewTime == 0) return 0;
        return (totalBytesStreamed * 8.0) / totalViewTime; // bits per second
    }
    
    public long getSessionDuration() {
        return (System.currentTimeMillis() - startTime) / 1000; // seconds
    }
    
    public String getMetricsSummary() {
        return String.format(
            "Streaming Metrics:\n" +
            "- Total Data: %.2f MB\n" +
            "- View Time: %d seconds\n" +
            "- Buffering Events: %d\n" +
            "- Avg Buffer Health: %.1f%%\n" +
            "- Quality Changes: %d\n" +
            "- Avg Bitrate: %.1f kbps\n" +
            "- Session Duration: %d seconds",
            totalBytesStreamed / 1_000_000.0,
            totalViewTime,
            bufferingEvents,
            averageBufferHealth,
            qualityChanges,
            getAverageBitrate() / 1000.0,
            getSessionDuration()
        );
    }
}
