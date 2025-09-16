package composition.media;

/**
 * MAANG-Level Demo: Media Player System with Dynamic Decoder/Renderer Swapping
 * Demonstrates composition flexibility, strategy pattern, and plugin architecture
 */
public class MediaPlayerDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Media Player System Demo ===\n");
        
        // Create media player
        MediaPlayer player = new MediaPlayer();
        
        // Add event listeners
        AudioVisualizerListener visualizer = new AudioVisualizerListener();
        LastFmScrobblerListener scrobbler = new LastFmScrobblerListener("user123");
        
        player.addEventListener(visualizer);
        player.addEventListener(scrobbler);
        
        // Create sample media files
        System.out.println("--- Building Music Library ---");
        MediaFile song1 = new MediaFile("Bohemian Rhapsody", "Queen", "A Night at the Opera", 
                                       "mp3", 355, 8500000, 320, "/music/queen/bohemian_rhapsody.mp3");
        MediaFile song2 = new MediaFile("Hotel California", "Eagles", "Hotel California", 
                                       "flac", 391, 45000000, 1411, "/music/eagles/hotel_california.flac");
        MediaFile song3 = new MediaFile("Billie Jean", "Michael Jackson", "Thriller", 
                                       "aac", 294, 7200000, 256, "/music/mj/billie_jean.m4a");
        MediaFile song4 = new MediaFile("Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", 
                                       "wav", 482, 52000000, 1411, "/music/lz/stairway.wav");
        
        player.addToPlaylist(song1);
        player.addToPlaylist(song2);
        player.addToPlaylist(song3);
        player.addToPlaylist(song4);
        
        player.displayPlaylist();
        
        // Test different renderers
        System.out.println("\n--- Testing Different Output Devices ---");
        
        // Start with speakers
        SpeakerRenderer speakers = new SpeakerRenderer();
        player.setRenderer(speakers);
        
        PlaybackResult result1 = player.play(0); // Play MP3
        System.out.println("MP3 Playback: " + (result1.isSuccess() ? "Success" : result1.getMessage()));
        
        player.stop();
        
        // Switch to headphones for high-quality FLAC
        HeadphoneRenderer headphones = new HeadphoneRenderer(true);
        player.setRenderer(headphones);
        player.setEqualizer(EqualizerSettings.classical());
        
        PlaybackResult result2 = player.play(1); // Play FLAC
        System.out.println("FLAC Playback: " + (result2.isSuccess() ? "Success" : result2.getMessage()));
        
        player.pause();
        player.resume();
        player.stop();
        
        // Switch to Bluetooth for mobile listening
        BluetoothRenderer bluetooth = new BluetoothRenderer("AirPods Pro");
        player.setRenderer(bluetooth);
        player.setEqualizer(EqualizerSettings.pop());
        
        PlaybackResult result3 = player.play(2); // Play AAC
        System.out.println("AAC Bluetooth Playback: " + (result3.isSuccess() ? "Success" : result3.getMessage()));
        
        // Test volume control
        System.out.println("\n--- Testing Volume Control ---");
        player.setVolume(0.3);
        player.setVolume(0.8);
        player.setVolume(1.0);
        
        player.stop();
        
        // Test unsupported format scenario
        System.out.println("\n--- Testing Unsupported Format ---");
        MediaFile unsupportedFile = new MediaFile("Test Song", "Test Artist", "Test Album", 
                                                 "ogg", 180, 4500000, 192, "/music/test.ogg");
        player.addToPlaylist(unsupportedFile);
        
        PlaybackResult unsupportedResult = player.play(4);
        System.out.println("OGG Playback: " + unsupportedResult.getMessage());
        
        // Add OGG decoder plugin
        System.out.println("\n--- Installing OGG Decoder Plugin ---");
        player.registerDecoder("ogg", new OGGDecoder());
        
        PlaybackResult oggResult = player.play(4);
        System.out.println("OGG Playback after plugin: " + (oggResult.isSuccess() ? "Success" : oggResult.getMessage()));
        
        player.stop();
        
        // Test playlist navigation
        System.out.println("\n--- Testing Playlist Navigation ---");
        player.setRenderer(speakers);
        player.setEqualizer(EqualizerSettings.rock());
        
        player.play(0);
        player.next();
        player.next();
        player.previous();
        
        // Test different equalizer presets
        System.out.println("\n--- Testing Equalizer Presets ---");
        player.setEqualizer(EqualizerSettings.jazz());
        player.setEqualizer(EqualizerSettings.classical());
        player.setEqualizer(EqualizerSettings.rock());
        
        // Test seamless renderer switching during playback
        System.out.println("\n--- Testing Seamless Renderer Switching ---");
        player.play(3); // Play WAV file
        
        System.out.println("Switching from speakers to headphones during playback...");
        player.setRenderer(headphones);
        
        System.out.println("Switching from headphones to Bluetooth during playback...");
        player.setRenderer(bluetooth);
        
        player.stop();
        
        // Test decoder performance comparison
        System.out.println("\n--- Decoder Performance Comparison ---");
        testDecoderPerformance(player, song1, "MP3");
        testDecoderPerformance(player, song2, "FLAC");
        testDecoderPerformance(player, song3, "AAC");
        testDecoderPerformance(player, song4, "WAV");
        
        // Test plugin management
        System.out.println("\n--- Testing Plugin Management ---");
        player.displayPlayerStatus();
        
        System.out.println("Unregistering MP3 decoder...");
        player.unregisterDecoder("mp3");
        
        PlaybackResult noMp3Result = player.play(0);
        System.out.println("MP3 Playback after unregistering: " + noMp3Result.getMessage());
        
        System.out.println("Re-registering MP3 decoder...");
        player.registerDecoder("mp3", new MP3Decoder());
        
        PlaybackResult mp3BackResult = player.play(0);
        System.out.println("MP3 Playback after re-registering: " + (mp3BackResult.isSuccess() ? "Success" : mp3BackResult.getMessage()));
        
        player.stop();
        player.displayPlayerStatus();
        
        System.out.println("\n=== Demo Complete: Media player adapted to different formats and output devices without modification ===");
    }
    
    private static void testDecoderPerformance(MediaPlayer player, MediaFile file, String format) {
        System.out.println("Testing " + format + " decoder performance...");
        long startTime = System.currentTimeMillis();
        
        PlaybackResult result = player.play(player.getPlaylist().indexOf(file));
        
        long endTime = System.currentTimeMillis();
        long decodingTime = endTime - startTime;
        
        System.out.printf("%s decoding time: %dms\n", format, decodingTime);
        player.stop();
    }
    
    // Audio Visualizer Listener
    static class AudioVisualizerListener implements PlayerEventListener {
        @Override
        public void onPlayerEvent(String message, PlayerState state, MediaFile currentTrack) {
            if (state == PlayerState.PLAYING && currentTrack != null) {
                System.out.println("🎵 Visualizer: Displaying waveform for " + currentTrack.getTitle());
            }
        }
    }
    
    // Last.fm Scrobbler Listener
    static class LastFmScrobblerListener implements PlayerEventListener {
        private final String username;
        
        public LastFmScrobblerListener(String username) {
            this.username = username;
        }
        
        @Override
        public void onPlayerEvent(String message, PlayerState state, MediaFile currentTrack) {
            if (state == PlayerState.PLAYING && currentTrack != null) {
                System.out.println("📊 Last.fm: Scrobbling " + currentTrack.getTitle() + " for " + username);
            }
        }
    }
    
    // Custom OGG Decoder for plugin demonstration
    static class OGGDecoder implements Decoder {
        private boolean isInitialized;
        
        @Override
        public void initialize() {
            System.out.println("Initializing OGG Vorbis decoder...");
            isInitialized = true;
        }
        
        @Override
        public DecodedAudio decode(MediaFile mediaFile) {
            if (!isInitialized) {
                throw new IllegalStateException("Decoder not initialized");
            }
            
            System.out.println("Decoding OGG: " + mediaFile.getTitle());
            
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            return new DecodedAudio(mediaFile.getTitle(), 44100, 16, 2, 
                                  mediaFile.getDurationSeconds(), "OGG Vorbis Decoded", 88.0);
        }
        
        @Override
        public String getType() {
            return "OGG Vorbis Decoder";
        }
        
        @Override
        public String[] getSupportedFormats() {
            return new String[]{"ogg"};
        }
        
        @Override
        public boolean canDecode(String format) {
            return "ogg".equalsIgnoreCase(format);
        }
    }
}
