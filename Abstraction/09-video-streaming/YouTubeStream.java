/**
 * YouTube streaming implementation
 * Focuses on user-generated content with adaptive streaming
 */
public class YouTubeStream extends VideoStream {
    private String channelName;
    private int viewCount;
    private boolean isLiveStream;
    private double bufferHealth;
    
    public YouTubeStream(String videoId, String title, String channelName, 
                        VideoQuality defaultQuality, VideoCodec codec, int duration) {
        super(videoId, title, defaultQuality, codec);
        this.channelName = channelName;
        this.viewCount = 0;
        this.isLiveStream = false;
        this.bufferHealth = 100.0;
        setDuration(duration);
    }
    
    public YouTubeStream(String videoId, String title, String channelName, boolean isLiveStream) {
        this(videoId, title, channelName, VideoQuality.HIGH_720P, VideoCodec.H264, 
             isLiveStream ? Integer.MAX_VALUE : 600); // Live streams have no fixed duration
        this.isLiveStream = isLiveStream;
    }
    
    @Override
    public void initializeStream() {
        System.out.println("🔴 Initializing YouTube stream...");
        System.out.println("📺 Channel: " + channelName);
        if (isLiveStream) {
            System.out.println("🔴 LIVE STREAM");
        }
        loadVideo();
    }
    
    @Override
    public void loadVideo() {
        System.out.println("📡 Loading YouTube video: " + title);
        System.out.println("🎯 Target quality: " + currentQuality.getDisplayName());
        
        // Simulate loading time based on quality
        try {
            Thread.sleep(currentQuality.getBitrate() / 1_000_000); // Higher quality = longer load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        bufferContent(30); // YouTube typically buffers 30 seconds ahead
        System.out.println("✅ Video loaded successfully");
    }
    
    @Override
    public void bufferContent(int seconds) {
        System.out.println("⏳ Buffering " + seconds + " seconds of content...");
        
        // Simulate buffering based on network conditions
        boolean goodConnection = checkNetworkConditions();
        if (goodConnection) {
            bufferHealth = Math.min(100.0, bufferHealth + seconds * 2);
            System.out.println("📶 Buffer health: " + String.format("%.1f%%", bufferHealth));
        } else {
            bufferHealth = Math.max(0, bufferHealth - 10);
            System.out.println("⚠️ Poor connection - buffer health: " + String.format("%.1f%%", bufferHealth));
            if (bufferHealth < 20) {
                metrics.recordBufferingEvent();
            }
        }
    }
    
    @Override
    public boolean checkNetworkConditions() {
        // Simulate network condition check
        double networkQuality = Math.random();
        return networkQuality > 0.3; // 70% chance of good connection
    }
    
    @Override
    public void adaptiveQualityAdjustment() {
        if (!checkNetworkConditions() && bufferHealth < 30) {
            // Downgrade quality for better streaming
            VideoQuality newQuality = getDowngradedQuality();
            if (newQuality != currentQuality) {
                System.out.println("📉 Auto-adjusting quality due to network conditions");
                changeQuality(newQuality);
            }
        } else if (checkNetworkConditions() && bufferHealth > 80) {
            // Upgrade quality if conditions allow
            VideoQuality newQuality = getUpgradedQuality();
            if (newQuality != currentQuality) {
                System.out.println("📈 Auto-upgrading quality - good network detected");
                changeQuality(newQuality);
            }
        }
    }
    
    private VideoQuality getDowngradedQuality() {
        VideoQuality[] qualities = VideoQuality.values();
        for (int i = 0; i < qualities.length; i++) {
            if (qualities[i] == currentQuality && i > 0) {
                return qualities[i - 1];
            }
        }
        return currentQuality;
    }
    
    private VideoQuality getUpgradedQuality() {
        VideoQuality[] qualities = VideoQuality.values();
        for (int i = 0; i < qualities.length; i++) {
            if (qualities[i] == currentQuality && i < qualities.length - 1) {
                return qualities[i + 1];
            }
        }
        return currentQuality;
    }
    
    public void likeVideo() {
        System.out.println("👍 Liked: " + title);
    }
    
    public void subscribeToChannel() {
        System.out.println("🔔 Subscribed to: " + channelName);
    }
    
    public void addToPlaylist(String playlistName) {
        System.out.println("➕ Added '" + title + "' to playlist: " + playlistName);
    }
    
    // Getters
    public String getChannelName() { return channelName; }
    public int getViewCount() { return viewCount; }
    public boolean isLiveStream() { return isLiveStream; }
    public double getBufferHealth() { return bufferHealth; }
    
    @Override
    public String getStreamInfo() {
        return super.getStreamInfo() + 
               "\n📺 Channel: " + channelName +
               "\n👀 Views: " + viewCount +
               "\n🔴 Live: " + (isLiveStream ? "Yes" : "No") +
               "\n📊 Buffer: " + String.format("%.1f%%", bufferHealth);
    }
}
