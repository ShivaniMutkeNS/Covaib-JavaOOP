/**
 * Abstract base class for all video streaming implementations
 * Defines the core streaming functionality that all platforms must implement
 */
public abstract class VideoStream {
    protected String videoId;
    protected String title;
    protected VideoQuality currentQuality;
    protected VideoCodec codec;
    protected StreamingMetrics metrics;
    protected boolean isPlaying;
    protected boolean isPaused;
    protected int currentPosition; // in seconds
    protected int duration; // in seconds
    
    public VideoStream(String videoId, String title, VideoQuality defaultQuality, VideoCodec codec) {
        this.videoId = videoId;
        this.title = title;
        this.currentQuality = defaultQuality;
        this.codec = codec;
        this.metrics = new StreamingMetrics();
        this.isPlaying = false;
        this.isPaused = false;
        this.currentPosition = 0;
    }
    
    // Abstract methods that must be implemented by concrete classes
    public abstract void initializeStream();
    public abstract void loadVideo();
    public abstract void bufferContent(int seconds);
    public abstract boolean checkNetworkConditions();
    public abstract void adaptiveQualityAdjustment();
    
    // Concrete methods with default implementation
    public void play() {
        if (!isPlaying && !isPaused) {
            loadVideo();
        }
        isPlaying = true;
        isPaused = false;
        System.out.println("▶️ Playing: " + title);
        startMetricsTracking();
    }
    
    public void pause() {
        isPlaying = false;
        isPaused = true;
        System.out.println("⏸️ Paused: " + title);
    }
    
    public void stop() {
        isPlaying = false;
        isPaused = false;
        currentPosition = 0;
        metrics.stopTracking();
        System.out.println("⏹️ Stopped: " + title);
    }
    
    public void seek(int positionSeconds) {
        if (positionSeconds >= 0 && positionSeconds <= duration) {
            this.currentPosition = positionSeconds;
            bufferContent(10); // Buffer 10 seconds ahead
            System.out.println("⏭️ Seeked to: " + formatTime(positionSeconds));
        }
    }
    
    public void changeQuality(VideoQuality newQuality) {
        if (newQuality != this.currentQuality) {
            VideoQuality oldQuality = this.currentQuality;
            this.currentQuality = newQuality;
            metrics.recordQualityChange();
            System.out.println("🔄 Quality changed from " + oldQuality.getDisplayName() + 
                             " to " + newQuality.getDisplayName());
            
            if (isPlaying) {
                bufferContent(5); // Re-buffer with new quality
            }
        }
    }
    
    protected void startMetricsTracking() {
        // Simulate streaming data
        new Thread(() -> {
            while (isPlaying) {
                try {
                    Thread.sleep(1000); // Update every second
                    if (isPlaying) {
                        metrics.recordViewTime(1);
                        long bytesPerSecond = currentQuality.getBitrate() / 8;
                        metrics.recordDataStreamed(bytesPerSecond);
                        currentPosition++;
                        
                        // Simulate occasional buffering
                        if (Math.random() < 0.05) { // 5% chance
                            metrics.recordBufferingEvent();
                        }
                        
                        // Update buffer health
                        metrics.updateBufferHealth(85 + Math.random() * 15);
                        
                        // Check if we need adaptive quality
                        if (Math.random() < 0.1) { // 10% chance to check
                            adaptiveQualityAdjustment();
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
    
    protected String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%d:%02d", minutes, secs);
        }
    }
    
    // Getters
    public String getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public VideoQuality getCurrentQuality() { return currentQuality; }
    public VideoCodec getCodec() { return codec; }
    public StreamingMetrics getMetrics() { return metrics; }
    public boolean isPlaying() { return isPlaying; }
    public boolean isPaused() { return isPaused; }
    public int getCurrentPosition() { return currentPosition; }
    public int getDuration() { return duration; }
    
    protected void setDuration(int duration) { this.duration = duration; }
    
    public String getStreamInfo() {
        return String.format(
            "🎬 %s\n" +
            "📺 Quality: %s\n" +
            "🎞️ Codec: %s\n" +
            "⏱️ Position: %s / %s\n" +
            "📊 Status: %s",
            title,
            currentQuality.getDisplayName(),
            codec.getDisplayName(),
            formatTime(currentPosition),
            formatTime(duration),
            isPlaying ? "Playing" : (isPaused ? "Paused" : "Stopped")
        );
    }
}
