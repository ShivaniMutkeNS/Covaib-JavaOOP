package composition.media;

/**
 * Player Event Listener Interface for Observer Pattern
 */
public interface PlayerEventListener {
    void onPlayerEvent(String message, PlayerState state, MediaFile currentTrack);
}
