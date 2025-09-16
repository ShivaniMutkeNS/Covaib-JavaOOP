package composition.media;

/**
 * AAC Decoder Implementation
 */
public class AACDecoder implements Decoder {
    private boolean isInitialized;
    
    @Override
    public void initialize() {
        System.out.println("Initializing AAC decoder...");
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
        
        // Simulate AAC decoding
        System.out.println("Decoding AAC: " + mediaFile.getTitle());
        
        try {
            Thread.sleep(80); // AAC decodes slightly faster than MP3
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // AAC typically has better quality than MP3 at same bitrate
        return new DecodedAudio(mediaFile.getTitle(), 44100, 16, 2, 
                              mediaFile.getDurationSeconds(), "AAC Decoded", 90.0);
    }
    
    @Override
    public String getType() {
        return "AAC Decoder";
    }
    
    @Override
    public String[] getSupportedFormats() {
        return new String[]{"aac", "m4a"};
    }
    
    @Override
    public boolean canDecode(String format) {
        return "aac".equalsIgnoreCase(format) || "m4a".equalsIgnoreCase(format);
    }
}
