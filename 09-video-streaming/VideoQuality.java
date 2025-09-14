/**
 * Enumeration representing different video quality levels
 * Used across all streaming platforms for consistent quality management
 */
public enum VideoQuality {
    LOW_240P("240p", 240, 400_000),
    MEDIUM_480P("480p", 480, 1_000_000),
    HIGH_720P("720p", 720, 2_500_000),
    FULL_HD_1080P("1080p", 1080, 5_000_000),
    ULTRA_HD_4K("4K", 2160, 15_000_000),
    ULTRA_HD_8K("8K", 4320, 50_000_000);
    
    private final String displayName;
    private final int verticalResolution;
    private final int bitrate; // bits per second
    
    VideoQuality(String displayName, int verticalResolution, int bitrate) {
        this.displayName = displayName;
        this.verticalResolution = verticalResolution;
        this.bitrate = bitrate;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getVerticalResolution() {
        return verticalResolution;
    }
    
    public int getBitrate() {
        return bitrate;
    }
    
    public double getBandwidthMbps() {
        return bitrate / 1_000_000.0;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%d x %d, %.1f Mbps)", 
            displayName, getHorizontalResolution(), verticalResolution, getBandwidthMbps());
    }
    
    private int getHorizontalResolution() {
        return (int) (verticalResolution * 16.0 / 9.0); // Assuming 16:9 aspect ratio
    }
}
