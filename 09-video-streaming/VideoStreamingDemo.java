/**
 * Demonstration class showcasing the video streaming abstraction system
 * Shows polymorphism and abstraction in action with different streaming platforms
 */
public class VideoStreamingDemo {
    
    public static void main(String[] args) {
        System.out.println("🎬 Video Streaming Platform Demonstration");
        System.out.println("==========================================\n");
        
        // Create different streaming platform instances
        VideoStream[] streams = {
            new YouTubeStream("YT001", "Java Programming Tutorial", "CodeMaster", 
                            VideoQuality.FULL_HD_1080P, VideoCodec.H264, 1800),
            new NetflixStream("NF001", "Stranger Things", "Sci-Fi", 4, 1),
            new TwitchStream("TW001", "Epic Gaming Session", "ProGamer123", "Just Chatting", true)
        };
        
        // Demonstrate polymorphism - same interface, different behaviors
        demonstrateBasicPlayback(streams);
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demonstrate platform-specific features
        demonstratePlatformSpecificFeatures();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demonstrate adaptive streaming and quality management
        demonstrateAdaptiveStreaming();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demonstrate metrics and analytics
        demonstrateStreamingMetrics();
    }
    
    private static void demonstrateBasicPlayback(VideoStream[] streams) {
        System.out.println("🎯 BASIC PLAYBACK DEMONSTRATION");
        System.out.println("Showing polymorphism - same methods, different implementations\n");
        
        for (VideoStream stream : streams) {
            System.out.println("Platform: " + stream.getClass().getSimpleName());
            stream.initializeStream();
            stream.play();
            
            // Simulate some playback time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            stream.seek(30);
            stream.pause();
            System.out.println(stream.getStreamInfo());
            stream.stop();
            System.out.println();
        }
    }
    
    private static void demonstratePlatformSpecificFeatures() {
        System.out.println("🌟 PLATFORM-SPECIFIC FEATURES");
        System.out.println("Each platform has unique capabilities\n");
        
        // YouTube specific features
        System.out.println("📺 YouTube Features:");
        YouTubeStream youtube = new YouTubeStream("YT002", "Cooking Masterclass", 
                                                 "ChefExpert", false);
        youtube.initializeStream();
        youtube.play();
        youtube.likeVideo();
        youtube.subscribeToChannel();
        youtube.addToPlaylist("Learning");
        youtube.stop();
        System.out.println();
        
        // Netflix specific features
        System.out.println("🎬 Netflix Features:");
        NetflixStream netflix = new NetflixStream("NF002", "The Crown", "Drama", 2, 3);
        netflix.initializeStream();
        netflix.play();
        netflix.addToMyList();
        netflix.rateContent(5);
        netflix.enableSubtitles("Spanish");
        netflix.downloadForOffline();
        netflix.playNextEpisode();
        netflix.stop();
        System.out.println();
        
        // Twitch specific features
        System.out.println("🎮 Twitch Features:");
        TwitchStream twitch = new TwitchStream("TW002", "Speedrun Attempt", 
                                             "SpeedRunner99", "Super Mario Bros", true);
        twitch.initializeStream();
        twitch.play();
        twitch.followStreamer();
        twitch.sendChatMessage("Amazing run! PogChamp");
        twitch.enableLowLatencyMode();
        twitch.clipMoment(120, 30);
        twitch.subscribeToChannel();
        twitch.stop();
        System.out.println();
    }
    
    private static void demonstrateAdaptiveStreaming() {
        System.out.println("🔄 ADAPTIVE STREAMING DEMONSTRATION");
        System.out.println("Quality adjustment based on network conditions\n");
        
        YouTubeStream adaptiveStream = new YouTubeStream("YT003", "4K Nature Documentary", 
                                                        "NatureChannel", VideoQuality.ULTRA_HD_4K, 
                                                        VideoCodec.H265, 3600);
        
        System.out.println("Starting with 4K quality...");
        adaptiveStream.initializeStream();
        adaptiveStream.play();
        
        // Simulate network quality changes
        System.out.println("\n🔄 Simulating network quality changes:");
        
        // Simulate poor network - should trigger quality reduction
        System.out.println("📉 Poor network detected...");
        adaptiveStream.adaptiveQualityAdjustment();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Manual quality change
        System.out.println("\n🎛️ Manual quality adjustment:");
        adaptiveStream.changeQuality(VideoQuality.HIGH_720P);
        
        System.out.println("\nFinal stream info:");
        System.out.println(adaptiveStream.getStreamInfo());
        adaptiveStream.stop();
    }
    
    private static void demonstrateStreamingMetrics() {
        System.out.println("📊 STREAMING METRICS DEMONSTRATION");
        System.out.println("Analytics and performance tracking\n");
        
        NetflixStream metricsStream = new NetflixStream("NF003", "Data Science Explained", 
                                                       "Documentary", 8.7, true, 
                                                       VideoQuality.FULL_HD_1080P, 5400);
        
        metricsStream.initializeStream();
        metricsStream.play();
        
        // Let it run for a bit to collect metrics
        System.out.println("⏱️ Collecting streaming data for 5 seconds...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate some quality changes and buffering
        metricsStream.changeQuality(VideoQuality.HIGH_720P);
        metricsStream.changeQuality(VideoQuality.FULL_HD_1080P);
        
        metricsStream.stop();
        
        // Display comprehensive metrics
        System.out.println("\n📈 STREAMING SESSION ANALYTICS:");
        System.out.println(metricsStream.getMetrics().getMetricsSummary());
        
        // Show codec efficiency comparison
        System.out.println("\n🎞️ CODEC COMPARISON:");
        demonstrateCodecComparison(5400); // 90 minutes
    }
    
    private static void demonstrateCodecComparison(int durationSeconds) {
        VideoQuality quality = VideoQuality.FULL_HD_1080P;
        
        System.out.println("File size comparison for " + quality.getDisplayName() + 
                         " content (" + (durationSeconds/60) + " minutes):");
        
        for (VideoCodec codec : VideoCodec.values()) {
            long fileSize = codec.calculateFileSize(quality, durationSeconds);
            System.out.printf("%-15s: %,d MB (%s)\n", 
                codec.getDisplayName(), 
                fileSize / 1_000_000,
                codec.toString().split(" - ")[1]); // Get compression info
        }
    }
}
