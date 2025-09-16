package composition.media;

/**
 * Decoded Audio data class
 */
public class DecodedAudio {
    private final String title;
    private final int sampleRate;
    private final int bitDepth;
    private final int channels;
    private final int durationSeconds;
    private final String codecInfo;
    private final double qualityScore; // 0-100
    
    public DecodedAudio(String title, int sampleRate, int bitDepth, int channels, 
                       int durationSeconds, String codecInfo, double qualityScore) {
        this.title = title;
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
        this.durationSeconds = durationSeconds;
        this.codecInfo = codecInfo;
        this.qualityScore = qualityScore;
    }
    
    public String getTitle() { return title; }
    public int getSampleRate() { return sampleRate; }
    public int getBitDepth() { return bitDepth; }
    public int getChannels() { return channels; }
    public int getDurationSeconds() { return durationSeconds; }
    public String getCodecInfo() { return codecInfo; }
    public double getQualityScore() { return qualityScore; }
}
