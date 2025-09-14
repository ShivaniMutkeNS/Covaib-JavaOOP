/**
 * Enumeration representing different video codecs
 * Each codec has different compression efficiency and compatibility
 */
public enum VideoCodec {
    H264("H.264/AVC", "mp4", 1.0, true),
    H265("H.265/HEVC", "mp4", 0.5, true),
    VP9("VP9", "webm", 0.6, false),
    AV1("AV1", "mp4", 0.4, false),
    MPEG2("MPEG-2", "mpg", 2.0, true);
    
    private final String displayName;
    private final String containerFormat;
    private final double compressionRatio; // Relative to H.264 baseline
    private final boolean hardwareAccelerated;
    
    VideoCodec(String displayName, String containerFormat, 
               double compressionRatio, boolean hardwareAccelerated) {
        this.displayName = displayName;
        this.containerFormat = containerFormat;
        this.compressionRatio = compressionRatio;
        this.hardwareAccelerated = hardwareAccelerated;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getContainerFormat() {
        return containerFormat;
    }
    
    public double getCompressionRatio() {
        return compressionRatio;
    }
    
    public boolean isHardwareAccelerated() {
        return hardwareAccelerated;
    }
    
    public long calculateFileSize(VideoQuality quality, int durationSeconds) {
        long baseSizeBytes = (long) quality.getBitrate() * durationSeconds / 8;
        return (long) (baseSizeBytes * compressionRatio);
    }
    
    @Override
    public String toString() {
        return String.format("%s (.%s) - %s compression, %s HW acceleration", 
            displayName, containerFormat, 
            compressionRatio < 1.0 ? "High" : "Standard",
            hardwareAccelerated ? "with" : "without");
    }
}
