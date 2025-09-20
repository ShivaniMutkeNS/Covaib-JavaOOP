/**
 * Netflix streaming implementation
 * Focuses on premium content with high-quality streaming and offline downloads
 */
public class NetflixStream extends VideoStream {
    private String genre;
    private double imdbRating;
    private boolean isOriginalContent;
    private boolean downloadAvailable;
    private String maturityRating;
    private int seasonNumber;
    private int episodeNumber;
    
    public NetflixStream(String videoId, String title, String genre, double imdbRating, 
                        boolean isOriginalContent, VideoQuality defaultQuality, int duration) {
        super(videoId, title, defaultQuality, VideoCodec.H265); // Netflix prefers H.265
        this.genre = genre;
        this.imdbRating = imdbRating;
        this.isOriginalContent = isOriginalContent;
        this.downloadAvailable = true; // Most Netflix content is downloadable
        this.maturityRating = "PG-13";
        this.seasonNumber = 1;
        this.episodeNumber = 1;
        setDuration(duration);
    }
    
    public NetflixStream(String videoId, String title, String genre, 
                        int seasonNumber, int episodeNumber) {
        this(videoId, title, genre, 8.5, true, VideoQuality.FULL_HD_1080P, 2700); // 45 min episode
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }
    
    @Override
    public void initializeStream() {
        System.out.println("🎬 Initializing Netflix stream...");
        System.out.println("🎭 Genre: " + genre);
        System.out.println("⭐ IMDB Rating: " + imdbRating + "/10");
        if (isOriginalContent) {
            System.out.println("🅽 Netflix Original");
        }
        System.out.println("🔞 Rating: " + maturityRating);
        loadVideo();
    }
    
    @Override
    public void loadVideo() {
        System.out.println("🎥 Loading Netflix content: " + title);
        if (seasonNumber > 1 || episodeNumber > 1) {
            System.out.println("📺 S" + seasonNumber + "E" + episodeNumber);
        }
        
        // Netflix has sophisticated CDN, faster loading
        System.out.println("🌐 Connecting to nearest CDN server...");
        try {
            Thread.sleep(500); // Fast loading due to CDN
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        bufferContent(60); // Netflix buffers more aggressively
        System.out.println("✅ Content loaded with premium quality");
    }
    
    @Override
    public void bufferContent(int seconds) {
        System.out.println("⚡ Netflix smart buffering: " + seconds + " seconds");
        
        // Netflix has superior buffering technology
        boolean networkOk = checkNetworkConditions();
        if (networkOk) {
            System.out.println("📡 Intelligent buffering complete - smooth playback guaranteed");
        } else {
            System.out.println("🔄 Adaptive buffering - optimizing for your connection");
            // Netflix rarely has buffering issues due to advanced CDN
            if (Math.random() < 0.1) { // Only 10% chance of buffering issues
                metrics.recordBufferingEvent();
            }
        }
    }
    
    @Override
    public boolean checkNetworkConditions() {
        // Netflix has better network optimization
        double networkQuality = Math.random();
        return networkQuality > 0.2; // 80% chance of good connection
    }
    
    @Override
    public void adaptiveQualityAdjustment() {
        // Netflix's advanced algorithm
        if (!checkNetworkConditions()) {
            System.out.println("🧠 Netflix AI adjusting stream quality...");
            VideoQuality optimalQuality = calculateOptimalQuality();
            if (optimalQuality != currentQuality) {
                changeQuality(optimalQuality);
            }
        }
    }
    
    private VideoQuality calculateOptimalQuality() {
        // Simulate Netflix's smart quality selection
        double networkScore = Math.random();
        if (networkScore > 0.8) return VideoQuality.ULTRA_HD_4K;
        if (networkScore > 0.6) return VideoQuality.FULL_HD_1080P;
        if (networkScore > 0.4) return VideoQuality.HIGH_720P;
        if (networkScore > 0.2) return VideoQuality.MEDIUM_480P;
        return VideoQuality.LOW_240P;
    }
    
    public void downloadForOffline() {
        if (downloadAvailable) {
            System.out.println("⬇️ Downloading '" + title + "' for offline viewing...");
            long fileSize = codec.calculateFileSize(currentQuality, duration);
            System.out.println("📱 Download size: " + (fileSize / 1_000_000) + " MB");
            System.out.println("✅ Available offline for 30 days");
        } else {
            System.out.println("❌ This content is not available for download");
        }
    }
    
    public void addToMyList() {
        System.out.println("➕ Added '" + title + "' to My List");
    }
    
    public void rateContent(int stars) {
        if (stars >= 1 && stars <= 5) {
            System.out.println("⭐ Rated '" + title + "' " + stars + " stars");
            System.out.println("🤖 Netflix will use this to improve your recommendations");
        }
    }
    
    public void playNextEpisode() {
        if (episodeNumber < 10) { // Assume max 10 episodes per season
            episodeNumber++;
            System.out.println("▶️ Auto-playing next episode: S" + seasonNumber + "E" + episodeNumber);
            loadVideo();
        } else {
            System.out.println("🏁 Season " + seasonNumber + " complete!");
        }
    }
    
    public void enableSubtitles(String language) {
        System.out.println("📝 Enabled " + language + " subtitles");
    }
    
    public void enableAudioDescription() {
        System.out.println("🔊 Enabled audio description for accessibility");
    }
    
    // Getters
    public String getGenre() { return genre; }
    public double getImdbRating() { return imdbRating; }
    public boolean isOriginalContent() { return isOriginalContent; }
    public boolean isDownloadAvailable() { return downloadAvailable; }
    public String getMaturityRating() { return maturityRating; }
    public int getSeasonNumber() { return seasonNumber; }
    public int getEpisodeNumber() { return episodeNumber; }
    
    @Override
    public String getStreamInfo() {
        return super.getStreamInfo() + 
               "\n🎭 Genre: " + genre +
               "\n⭐ Rating: " + imdbRating + "/10" +
               "\n🅽 Original: " + (isOriginalContent ? "Yes" : "No") +
               "\n📱 Download: " + (downloadAvailable ? "Available" : "Not Available") +
               (seasonNumber > 1 || episodeNumber > 1 ? "\n📺 Episode: S" + seasonNumber + "E" + episodeNumber : "");
    }
}
