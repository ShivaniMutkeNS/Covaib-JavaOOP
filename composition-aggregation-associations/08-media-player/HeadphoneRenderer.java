package composition.media;

/**
 * Headphone Renderer Implementation
 */
public class HeadphoneRenderer implements Renderer {
    private boolean isInitialized;
    private double volume;
    private EqualizerSettings equalizer;
    private PlaybackState renderState;
    private final boolean hasNoiseCancellation;
    
    public HeadphoneRenderer(boolean hasNoiseCancellation) {
        this.volume = 0.6; // Lower default volume for headphones
        this.renderState = PlaybackState.STOPPED;
        this.hasNoiseCancellation = hasNoiseCancellation;
    }
    
    @Override
    public void initialize(PlaybackSettings settings) {
        System.out.println("Initializing headphone output" + 
                         (hasNoiseCancellation ? " with noise cancellation..." : "..."));
        this.volume = settings.getVolume();
        this.equalizer = settings.getEqualizer();
        this.isInitialized = true;
    }
    
    @Override
    public RenderResult render(DecodedAudio audio) {
        if (!isInitialized) {
            return new RenderResult(false, "Renderer not initialized");
        }
        
        System.out.println("🎧 Playing through headphones: " + audio.getTitle());
        System.out.printf("   Quality: %.1f%% | Intimate listening experience\n", audio.getQualityScore());
        
        if (hasNoiseCancellation) {
            System.out.println("   🔇 Noise cancellation active");
        }
        
        renderState = PlaybackState.PLAYING;
        
        // Simulate audio rendering with enhanced detail for headphones
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new RenderResult(true, "Audio rendered to headphones");
    }
    
    @Override
    public void pause() {
        if (renderState == PlaybackState.PLAYING) {
            renderState = PlaybackState.PAUSED;
            System.out.println("🎧 Headphone output paused");
        }
    }
    
    @Override
    public void resume() {
        if (renderState == PlaybackState.PAUSED) {
            renderState = PlaybackState.PLAYING;
            System.out.println("🎧 Headphone output resumed");
        }
    }
    
    @Override
    public void stop() {
        renderState = PlaybackState.STOPPED;
        System.out.println("🎧 Headphone output stopped");
    }
    
    @Override
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("🎧 Headphone volume: " + (int)(this.volume * 100) + "%");
        
        // Warn about high volume levels
        if (this.volume > 0.8) {
            System.out.println("⚠️  High volume warning - protect your hearing!");
        }
    }
    
    @Override
    public void applyEqualizer(EqualizerSettings equalizer) {
        this.equalizer = equalizer;
        if (equalizer != null) {
            System.out.println("🎧 Equalizer applied to headphones: " + equalizer.getPresetName());
        }
    }
    
    @Override
    public String getType() {
        return hasNoiseCancellation ? "Noise-Cancelling Headphones" : "Headphones";
    }
    
    private enum PlaybackState {
        PLAYING, PAUSED, STOPPED
    }
}
