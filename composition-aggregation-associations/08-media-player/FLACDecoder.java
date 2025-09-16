package composition.media;

/**
 * FLAC Decoder Implementation
 */
public class FLACDecoder implements Decoder {
    private boolean isInitialized;
    
    @Override
    public void initialize() {
        System.out.println("Initializing FLAC decoder...");
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
        
        // Simulate FLAC decoding
        System.out.println("Decoding FLAC: " + mediaFile.getTitle());
        
        try {
            Thread.sleep(150); // FLAC takes longer to decode but provides lossless quality
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // FLAC provides lossless quality
        return new DecodedAudio(mediaFile.getTitle(), 44100, 24, 2, 
                              mediaFile.getDurationSeconds(), "FLAC Decoded", 100.0);
    }
    
    @Override
    public String getType() {
        return "FLAC Decoder";
    }
    
    @Override
    public String[] getSupportedFormats() {
        return new String[]{"flac"};
    }
    
    @Override
    public boolean canDecode(String format) {
        return "flac".equalsIgnoreCase(format);
    }
}
