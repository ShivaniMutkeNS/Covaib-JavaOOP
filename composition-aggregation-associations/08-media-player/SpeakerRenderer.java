package composition.media;

/**
 * Speaker Renderer Implementation
 */
public class SpeakerRenderer implements Renderer {
    private boolean isInitialized;
    private double volume;
    private EqualizerSettings equalizer;
    private PlaybackState renderState;
    
    public SpeakerRenderer() {
        this.volume = 0.7;
        this.renderState = PlaybackState.STOPPED;
    }
    
    @Override
    public void initialize(PlaybackSettings settings) {
        System.out.println("Initializing speaker output...");
        this.volume = settings.getVolume();
        this.equalizer = settings.getEqualizer();
        this.isInitialized = true;
    }
    
    @Override
    public RenderResult render(DecodedAudio audio) {
        if (!isInitialized) {
            return new RenderResult(false, "Renderer not initialized");
        }
        
        System.out.println("🔊 Playing through speakers: " + audio.getTitle());
        System.out.printf("   Quality: %.1f%% | Sample Rate: %d Hz | Bit Depth: %d-bit\n",
                         audio.getQualityScore(), audio.getSampleRate(), audio.getBitDepth());
        
        renderState = PlaybackState.PLAYING;
        
        // Simulate audio rendering
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new RenderResult(true, "Audio rendered to speakers");
    }
    
    @Override
    public void pause() {
        if (renderState == PlaybackState.PLAYING) {
            renderState = PlaybackState.PAUSED;
            System.out.println("🔊 Speaker output paused");
        }
    }
    
    @Override
    public void resume() {
        if (renderState == PlaybackState.PAUSED) {
            renderState = PlaybackState.PLAYING;
            System.out.println("🔊 Speaker output resumed");
        }
    }
    
    @Override
    public void stop() {
        renderState = PlaybackState.STOPPED;
        System.out.println("🔊 Speaker output stopped");
    }
    
    @Override
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("🔊 Speaker volume: " + (int)(this.volume * 100) + "%");
    }
    
    @Override
    public void applyEqualizer(EqualizerSettings equalizer) {
        this.equalizer = equalizer;
        if (equalizer != null) {
            System.out.println("🔊 Equalizer applied to speakers: " + equalizer.getPresetName());
        }
    }
    
    @Override
    public String getType() {
        return "Speaker Output";
    }
    
    private enum PlaybackState {
        PLAYING, PAUSED, STOPPED
    }
}
