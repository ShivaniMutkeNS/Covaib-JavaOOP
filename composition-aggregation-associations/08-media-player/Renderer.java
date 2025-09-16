package composition.media;

/**
 * Renderer Strategy Interface
 */
public interface Renderer {
    void initialize(PlaybackSettings settings);
    RenderResult render(DecodedAudio audio);
    void pause();
    void resume();
    void stop();
    void setVolume(double volume);
    void applyEqualizer(EqualizerSettings equalizer);
    String getType();
}
