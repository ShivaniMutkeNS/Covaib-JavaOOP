package composition.media;

/**
 * WAV Decoder Implementation
 */
public class WAVDecoder implements Decoder {
    private boolean isInitialized;
    
    @Override
    public void initialize() {
        System.out.println("Initializing WAV decoder...");
        isInitialized = true;
    }
    
    @Override
    public DecodedAudio decode(MediaFile mediaFile) {
        if (!isInitialized) {
            throw new IllegalStateException("Decoder not initialized");
        }
        
        if (!canDecode(mediaFile.getFormat())) {
            return null;
        }
        
        // Simulate WAV decoding
        System.out.println("Decoding WAV: " + mediaFile.getTitle());
        
        try {
            Thread.sleep(50); // WAV decodes very quickly as it's uncompressed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // WAV provides uncompressed quality
        return new DecodedAudio(mediaFile.getTitle(), 44100, 16, 2, 
                              mediaFile.getDurationSeconds(), "WAV Decoded", 100.0);
    }
    
    @Override
    public String getType() {
        return "WAV Decoder";
    }
    
    @Override
    public String[] getSupportedFormats() {
        return new String[]{"wav"};
    }
    
    @Override
    public boolean canDecode(String format) {
        return "wav".equalsIgnoreCase(format);
    }
}
