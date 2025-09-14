/**
 * Twitch streaming implementation
 * Focuses on live gaming content with real-time interaction
 */
public class TwitchStream extends VideoStream {
    private String streamerName;
    private String gameCategory;
    private int viewerCount;
    private boolean isLive;
    private int followerCount;
    private boolean isPartner;
    private String streamLanguage;
    private boolean chatEnabled;
    
    public TwitchStream(String videoId, String title, String streamerName, 
                       String gameCategory, boolean isLive) {
        super(videoId, title, VideoQuality.HIGH_720P, VideoCodec.H264);
        this.streamerName = streamerName;
        this.gameCategory = gameCategory;
        this.isLive = isLive;
        this.viewerCount = (int)(Math.random() * 10000) + 100; // Random viewer count
        this.followerCount = (int)(Math.random() * 50000) + 1000;
        this.isPartner = Math.random() > 0.7; // 30% chance of being partner
        this.streamLanguage = "English";
        this.chatEnabled = true;
        setDuration(isLive ? Integer.MAX_VALUE : 7200); // Live or 2-hour VOD
    }
    
    @Override
    public void initializeStream() {
        System.out.println("🎮 Initializing Twitch stream...");
        System.out.println("👤 Streamer: " + streamerName);
        System.out.println("🎯 Category: " + gameCategory);
        System.out.println("👥 Viewers: " + viewerCount);
        if (isPartner) {
            System.out.println("✅ Twitch Partner");
        }
        if (isLive) {
            System.out.println("🔴 LIVE NOW");
        } else {
            System.out.println("📼 VOD (Video on Demand)");
        }
        loadVideo();
    }
    
    @Override
    public void loadVideo() {
        System.out.println("🚀 Loading Twitch stream: " + title);
        
        if (isLive) {
            System.out.println("⚡ Connecting to live stream...");
            // Live streams load faster but with potential latency
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("📹 Loading VOD...");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        bufferContent(isLive ? 5 : 15); // Live streams buffer less to reduce latency
        System.out.println("✅ Stream ready");
    }
    
    @Override
    public void bufferContent(int seconds) {
        if (isLive) {
            System.out.println("⚡ Low-latency buffering: " + seconds + " seconds");
            // Live streams prioritize low latency over buffer size
            if (seconds > 10) {
                System.out.println("⚠️ Reducing buffer to minimize stream delay");
                seconds = Math.min(seconds, 8);
            }
        } else {
            System.out.println("📼 VOD buffering: " + seconds + " seconds");
        }
        
        boolean networkOk = checkNetworkConditions();
        if (!networkOk && isLive) {
            System.out.println("🔄 Stream may experience frame drops due to network issues");
            metrics.recordBufferingEvent();
        }
    }
    
    @Override
    public boolean checkNetworkConditions() {
        // Twitch streaming is more sensitive to network conditions
        double networkQuality = Math.random();
        return networkQuality > 0.4; // 60% chance of good connection
    }
    
    @Override
    public void adaptiveQualityAdjustment() {
        if (isLive) {
            // Live streams need more aggressive quality adjustment
            if (!checkNetworkConditions()) {
                System.out.println("📉 Reducing quality to maintain live stream stability");
                VideoQuality lowerQuality = getLowerQualityForLive();
                if (lowerQuality != currentQuality) {
                    changeQuality(lowerQuality);
                }
            }
        } else {
            // VODs can buffer more, so less aggressive adjustment
            if (!checkNetworkConditions()) {
                System.out.println("🔄 Adjusting VOD quality for smoother playback");
                changeQuality(VideoQuality.MEDIUM_480P);
            }
        }
    }
    
    private VideoQuality getLowerQualityForLive() {
        // For live streams, prioritize stability
        switch (currentQuality) {
            case ULTRA_HD_4K:
            case ULTRA_HD_8K:
                return VideoQuality.FULL_HD_1080P;
            case FULL_HD_1080P:
                return VideoQuality.HIGH_720P;
            case HIGH_720P:
                return VideoQuality.MEDIUM_480P;
            default:
                return VideoQuality.LOW_240P;
        }
    }
    
    public void sendChatMessage(String message) {
        if (chatEnabled && isLive) {
            System.out.println("💬 [You]: " + message);
        } else {
            System.out.println("❌ Chat is disabled or stream is not live");
        }
    }
    
    public void followStreamer() {
        System.out.println("❤️ Followed " + streamerName);
        followerCount++;
        System.out.println("👥 " + streamerName + " now has " + followerCount + " followers");
    }
    
    public void subscribeToChannel() {
        if (isPartner) {
            System.out.println("⭐ Subscribed to " + streamerName + " for $4.99/month");
            System.out.println("🎁 Subscriber perks unlocked!");
        } else {
            System.out.println("❌ " + streamerName + " is not a Twitch Partner - subscriptions not available");
        }
    }
    
    public void enableLowLatencyMode() {
        if (isLive) {
            System.out.println("⚡ Low Latency Mode enabled - reduced stream delay");
            // Reduce buffer size for lower latency
            bufferContent(3);
        }
    }
    
    public void clipMoment(int startTime, int duration) {
        if (isLive) {
            System.out.println("✂️ Creating clip: " + duration + " seconds starting from " + 
                             formatTime(startTime));
            System.out.println("🎬 Clip will be available in your clips library");
        }
    }
    
    public void raidChannel(String targetStreamer) {
        if (isLive) {
            System.out.println("🚀 Raiding " + targetStreamer + " with " + viewerCount + " viewers!");
        }
    }
    
    public void toggleChatMode(boolean enabled) {
        this.chatEnabled = enabled;
        System.out.println("💬 Chat " + (enabled ? "enabled" : "disabled"));
    }
    
    // Getters
    public String getStreamerName() { return streamerName; }
    public String getGameCategory() { return gameCategory; }
    public int getViewerCount() { return viewerCount; }
    public boolean isLive() { return isLive; }
    public int getFollowerCount() { return followerCount; }
    public boolean isPartner() { return isPartner; }
    public String getStreamLanguage() { return streamLanguage; }
    public boolean isChatEnabled() { return chatEnabled; }
    
    @Override
    public String getStreamInfo() {
        return super.getStreamInfo() + 
               "\n👤 Streamer: " + streamerName +
               "\n🎮 Game: " + gameCategory +
               "\n👥 Viewers: " + viewerCount +
               "\n❤️ Followers: " + followerCount +
               "\n🔴 Live: " + (isLive ? "Yes" : "No") +
               "\n⭐ Partner: " + (isPartner ? "Yes" : "No") +
               "\n💬 Chat: " + (chatEnabled ? "Enabled" : "Disabled");
    }
}
