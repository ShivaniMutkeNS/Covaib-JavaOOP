package composition.media;

/**
 * Playback Result data class
 */
public class PlaybackResult {
    private final boolean success;
    private final String message;
    private final PlaybackInfo playbackInfo;
    
    public PlaybackResult(boolean success, String message, PlaybackInfo playbackInfo) {
        this.success = success;
        this.message = message;
        this.playbackInfo = playbackInfo;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PlaybackInfo getPlaybackInfo() { return playbackInfo; }
}
