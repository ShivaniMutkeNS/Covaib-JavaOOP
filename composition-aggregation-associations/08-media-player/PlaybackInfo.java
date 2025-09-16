package composition.media;

/**
 * Playback Information data class
 */
public class PlaybackInfo {
    private final MediaFile mediaFile;
    private final String decoderType;
    private final String rendererType;
    
    public PlaybackInfo(MediaFile mediaFile, String decoderType, String rendererType) {
        this.mediaFile = mediaFile;
        this.decoderType = decoderType;
        this.rendererType = rendererType;
    }
    
    public MediaFile getMediaFile() { return mediaFile; }
    public String getDecoderType() { return decoderType; }
    public String getRendererType() { return rendererType; }
}
