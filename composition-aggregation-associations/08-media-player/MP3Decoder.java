package composition.media;

/**
 * MP3 Decoder Implementation
 */
public class MP3Decoder implements Decoder {
    private boolean isInitialized;
    
    @Override
    public void initialize() {
        System.out.println("Initializing MP3 decoder...");
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
        
        // Simulate MP3 decoding
        System.out.println("Decoding MP3: " + mediaFile.getTitle());
        
        try {
            Thread.sleep(100); // Simulate decoding time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // MP3 typically has good compression but lossy quality
        return new DecodedAudio(mediaFile.getTitle(), 44100, 16, 2, 
                              mediaFile.getDurationSeconds(), "MP3 Decoded", 85.0);
    }
    
    @Override
    public String getType() {
        return "MP3 Decoder";
    }
    
    @Override
    public String[] getSupportedFormats() {
        return new String[]{"mp3"};
    }
    
    @Override
    public boolean canDecode(String format) {
        return "mp3".equalsIgnoreCase(format);
    }
}
