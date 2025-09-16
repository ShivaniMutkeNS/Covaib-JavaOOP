package composition.media;

/**
 * Playback Settings data class
 */
public class PlaybackSettings {
    private double volume;
    private EqualizerSettings equalizer;
    private boolean shuffleMode;
    private boolean repeatMode;
    
    public PlaybackSettings() {
        this.volume = 0.7;
        this.equalizer = null;
        this.shuffleMode = false;
        this.repeatMode = false;
    }
    
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = Math.max(0.0, Math.min(1.0, volume)); }
    
    public EqualizerSettings getEqualizer() { return equalizer; }
    public void setEqualizer(EqualizerSettings equalizer) { this.equalizer = equalizer; }
    
    public boolean isShuffleMode() { return shuffleMode; }
    public void setShuffleMode(boolean shuffleMode) { this.shuffleMode = shuffleMode; }
    
    public boolean isRepeatMode() { return repeatMode; }
    public void setRepeatMode(boolean repeatMode) { this.repeatMode = repeatMode; }
}
