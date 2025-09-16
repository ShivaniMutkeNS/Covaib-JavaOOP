package composition.media;

/**
 * Bluetooth Renderer Implementation
 */
public class BluetoothRenderer implements Renderer {
    private boolean isInitialized;
    private double volume;
    private EqualizerSettings equalizer;
    private PlaybackState renderState;
    private final String deviceName;
    private boolean isConnected;
    
    public BluetoothRenderer(String deviceName) {
        this.volume = 0.7;
        this.renderState = PlaybackState.STOPPED;
        this.deviceName = deviceName;
        this.isConnected = false;
    }
    
    @Override
    public void initialize(PlaybackSettings settings) {
        System.out.println("Connecting to Bluetooth device: " + deviceName + "...");
        
        // Simulate connection process
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        this.isConnected = true;
        this.volume = settings.getVolume();
        this.equalizer = settings.getEqualizer();
        this.isInitialized = true;
        
        System.out.println("📶 Connected to " + deviceName);
    }
    
    @Override
    public RenderResult render(DecodedAudio audio) {
        if (!isInitialized || !isConnected) {
            return new RenderResult(false, "Bluetooth device not connected");
        }
        
        System.out.println("📶 Streaming to " + deviceName + ": " + audio.getTitle());
        System.out.printf("   Quality: %.1f%% | Wireless streaming\n", audio.getQualityScore());
        
        renderState = PlaybackState.PLAYING;
        
        // Simulate wireless streaming with slight delay
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new RenderResult(true, "Audio streamed to Bluetooth device");
    }
    
    @Override
    public void pause() {
        if (renderState == PlaybackState.PLAYING) {
            renderState = PlaybackState.PAUSED;
            System.out.println("📶 Bluetooth streaming paused");
        }
    }
    
    @Override
    public void resume() {
        if (renderState == PlaybackState.PAUSED) {
            renderState = PlaybackState.PLAYING;
            System.out.println("📶 Bluetooth streaming resumed");
        }
    }
    
    @Override
    public void stop() {
        renderState = PlaybackState.STOPPED;
        System.out.println("📶 Bluetooth streaming stopped");
    }
    
    @Override
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        System.out.println("📶 " + deviceName + " volume: " + (int)(this.volume * 100) + "%");
    }
    
    @Override
    public void applyEqualizer(EqualizerSettings equalizer) {
        this.equalizer = equalizer;
        if (equalizer != null) {
            System.out.println("📶 Equalizer applied to " + deviceName + ": " + equalizer.getPresetName());
        }
    }
    
    @Override
    public String getType() {
        return "Bluetooth (" + deviceName + ")";
    }
    
    public void disconnect() {
        isConnected = false;
        renderState = PlaybackState.STOPPED;
        System.out.println("📶 Disconnected from " + deviceName);
    }
    
    private enum PlaybackState {
        PLAYING, PAUSED, STOPPED
    }
}
