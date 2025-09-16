package composition.media;

import java.util.*;

/**
 * MAANG-Level Media Player System using Composition
 * Demonstrates: Strategy Pattern, Chain of Responsibility, Factory Pattern, Plugin Architecture
 */
public class MediaPlayer {
    private Decoder decoder;
    private Renderer renderer;
    private final List<MediaFile> playlist;
    private int currentTrack;
    private PlayerState state;
    private final Map<String, Decoder> supportedDecoders;
    private final List<PlayerEventListener> listeners;
    private PlaybackSettings settings;
    
    public MediaPlayer() {
        this.playlist = new ArrayList<>();
        this.currentTrack = -1;
        this.state = PlayerState.STOPPED;
        this.supportedDecoders = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.settings = new PlaybackSettings();
        
        // Initialize default decoders
        registerDecoder("mp3", new MP3Decoder());
        registerDecoder("aac", new AACDecoder());
        registerDecoder("flac", new FLACDecoder());
        registerDecoder("wav", new WAVDecoder());
    }
    
    // Runtime decoder registration - plugin architecture
    public void registerDecoder(String format, Decoder decoder) {
        supportedDecoders.put(format.toLowerCase(), decoder);
        notifyListeners("Decoder registered for format: " + format.toUpperCase());
    }
    
    public void unregisterDecoder(String format) {
        if (supportedDecoders.remove(format.toLowerCase()) != null) {
            notifyListeners("Decoder unregistered for format: " + format.toUpperCase());
        }
    }
    
    // Runtime renderer swapping
    public void setRenderer(Renderer renderer) {
        Renderer oldRenderer = this.renderer;
        this.renderer = renderer;
        
        String message = oldRenderer == null ? 
            "Renderer set to " + renderer.getType() :
            "Renderer changed from " + oldRenderer.getType() + " to " + renderer.getType();
        
        notifyListeners(message);
        
        if (state == PlayerState.PLAYING) {
            // Seamlessly switch renderer during playback
            renderer.initialize(settings);
        }
    }
    
    public void addToPlaylist(MediaFile mediaFile) {
        playlist.add(mediaFile);
        notifyListeners("Added to playlist: " + mediaFile.getTitle());
    }
    
    public void removeFromPlaylist(int index) {
        if (index >= 0 && index < playlist.size()) {
            MediaFile removed = playlist.remove(index);
            if (currentTrack == index) {
                stop();
            } else if (currentTrack > index) {
                currentTrack--;
            }
            notifyListeners("Removed from playlist: " + removed.getTitle());
        }
    }
    
    public PlaybackResult play(int trackIndex) {
        if (trackIndex < 0 || trackIndex >= playlist.size()) {
            return new PlaybackResult(false, "Invalid track index", null);
        }
        
        MediaFile mediaFile = playlist.get(trackIndex);
        
        // Auto-select appropriate decoder based on file format
        String format = mediaFile.getFormat().toLowerCase();
        Decoder selectedDecoder = supportedDecoders.get(format);
        
        if (selectedDecoder == null) {
            return new PlaybackResult(false, "Unsupported format: " + format, null);
        }
        
        if (renderer == null) {
            return new PlaybackResult(false, "No renderer configured", null);
        }
        
        // Stop current playback if any
        if (state == PlayerState.PLAYING) {
            stop();
        }
        
        // Set components for this playback
        this.decoder = selectedDecoder;
        this.currentTrack = trackIndex;
        
        // Initialize components
        decoder.initialize();
        renderer.initialize(settings);
        
        // Start playback
        DecodedAudio decodedAudio = decoder.decode(mediaFile);
        if (decodedAudio == null) {
            return new PlaybackResult(false, "Failed to decode audio", null);
        }
        
        RenderResult renderResult = renderer.render(decodedAudio);
        if (!renderResult.isSuccess()) {
            return new PlaybackResult(false, "Rendering failed: " + renderResult.getErrorMessage(), null);
        }
        
        state = PlayerState.PLAYING;
        notifyListeners("Now playing: " + mediaFile.getTitle() + " (" + format.toUpperCase() + ")");
        
        return new PlaybackResult(true, "Playback started", new PlaybackInfo(mediaFile, decoder.getType(), renderer.getType()));
    }
    
    public PlaybackResult play() {
        if (currentTrack >= 0) {
            return play(currentTrack);
        } else if (!playlist.isEmpty()) {
            return play(0);
        }
        return new PlaybackResult(false, "No track selected", null);
    }
    
    public void pause() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
            if (renderer != null) {
                renderer.pause();
            }
            notifyListeners("Playback paused");
        }
    }
    
    public void resume() {
        if (state == PlayerState.PAUSED) {
            state = PlayerState.PLAYING;
            if (renderer != null) {
                renderer.resume();
            }
            notifyListeners("Playback resumed");
        }
    }
    
    public void stop() {
        if (state != PlayerState.STOPPED) {
            state = PlayerState.STOPPED;
            if (renderer != null) {
                renderer.stop();
            }
            notifyListeners("Playback stopped");
        }
    }
    
    public PlaybackResult next() {
        if (currentTrack < playlist.size() - 1) {
            return play(currentTrack + 1);
        }
        return new PlaybackResult(false, "Already at last track", null);
    }
    
    public PlaybackResult previous() {
        if (currentTrack > 0) {
            return play(currentTrack - 1);
        }
        return new PlaybackResult(false, "Already at first track", null);
    }
    
    public void setVolume(double volume) {
        settings.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        if (renderer != null) {
            renderer.setVolume(settings.getVolume());
        }
        notifyListeners("Volume set to " + (int)(settings.getVolume() * 100) + "%");
    }
    
    public void setEqualizer(EqualizerSettings equalizer) {
        settings.setEqualizer(equalizer);
        if (renderer != null) {
            renderer.applyEqualizer(equalizer);
        }
        notifyListeners("Equalizer settings updated");
    }
    
    public void addEventListener(PlayerEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(PlayerEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (PlayerEventListener listener : listeners) {
            listener.onPlayerEvent(message, state, getCurrentTrackInfo());
        }
    }
    
    private MediaFile getCurrentTrackInfo() {
        return (currentTrack >= 0 && currentTrack < playlist.size()) ? playlist.get(currentTrack) : null;
    }
    
    public void displayPlayerStatus() {
        System.out.println("\n=== Media Player Status ===");
        System.out.println("State: " + state);
        System.out.println("Playlist Size: " + playlist.size());
        System.out.println("Current Track: " + (currentTrack >= 0 ? (currentTrack + 1) + "/" + playlist.size() : "None"));
        
        if (currentTrack >= 0 && currentTrack < playlist.size()) {
            MediaFile current = playlist.get(currentTrack);
            System.out.println("Now Playing: " + current.getTitle() + " by " + current.getArtist());
        }
        
        System.out.println("Decoder: " + (decoder != null ? decoder.getType() : "None"));
        System.out.println("Renderer: " + (renderer != null ? renderer.getType() : "None"));
        System.out.println("Volume: " + (int)(settings.getVolume() * 100) + "%");
        System.out.println("Supported Formats: " + String.join(", ", supportedDecoders.keySet()));
    }
    
    public void displayPlaylist() {
        System.out.println("\n=== Playlist ===");
        for (int i = 0; i < playlist.size(); i++) {
            MediaFile file = playlist.get(i);
            String marker = (i == currentTrack) ? "► " : "  ";
            System.out.printf("%s%d. %s - %s (%s, %s)\n", 
                            marker, i + 1, file.getArtist(), file.getTitle(), 
                            file.getFormat().toUpperCase(), formatDuration(file.getDurationSeconds()));
        }
    }
    
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    
    // Getters
    public PlayerState getState() { return state; }
    public List<MediaFile> getPlaylist() { return new ArrayList<>(playlist); }
    public int getCurrentTrack() { return currentTrack; }
    public Set<String> getSupportedFormats() { return new HashSet<>(supportedDecoders.keySet()); }
}
