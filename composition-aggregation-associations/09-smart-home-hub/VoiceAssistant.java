package composition.smarthome;

/**
 * Voice Assistant Device Implementation
 */
public class VoiceAssistant implements SmartDevice {
    private final String deviceId;
    private final String name;
    private SmartHomeHub hub;
    private boolean isOnline;
    private boolean isListening;
    private double volume;
    private String language;
    private boolean microphoneMuted;
    
    public VoiceAssistant(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOnline = false;
        this.isListening = false;
        this.volume = 0.7;
        this.language = "en-US";
        this.microphoneMuted = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("🎤 Initializing voice assistant: " + name);
        isOnline = true;
        isListening = true;
    }
    
    @Override
    public void disconnect() {
        System.out.println("🎤 Disconnecting voice assistant: " + name);
        isOnline = false;
        isListening = false;
    }
    
    @Override
    public DeviceControlResult executeCommand(String command, Object... parameters) {
        if (!isOnline) {
            return new DeviceControlResult(false, "Device offline", null);
        }
        
        switch (command.toLowerCase()) {
            case "start_listening":
                if (!microphoneMuted) {
                    isListening = true;
                    System.out.println("🎤 " + name + " started listening");
                    return new DeviceControlResult(true, "Started listening", true);
                }
                return new DeviceControlResult(false, "Microphone is muted", false);
                
            case "stop_listening":
                isListening = false;
                System.out.println("🎤 " + name + " stopped listening");
                return new DeviceControlResult(true, "Stopped listening", false);
                
            case "set_volume":
                if (parameters.length > 0 && parameters[0] instanceof Number) {
                    volume = Math.max(0.0, Math.min(1.0, ((Number) parameters[0]).doubleValue()));
                    System.out.println("🎤 " + name + " volume set to " + (int)(volume * 100) + "%");
                    return new DeviceControlResult(true, "Volume set", volume);
                }
                return new DeviceControlResult(false, "Invalid volume value", null);
                
            case "mute_microphone":
                microphoneMuted = true;
                isListening = false;
                System.out.println("🎤 " + name + " microphone muted");
                return new DeviceControlResult(true, "Microphone muted", true);
                
            case "unmute_microphone":
                microphoneMuted = false;
                isListening = true;
                System.out.println("🎤 " + name + " microphone unmuted");
                return new DeviceControlResult(true, "Microphone unmuted", false);
                
            case "set_language":
                if (parameters.length > 0 && parameters[0] instanceof String) {
                    language = (String) parameters[0];
                    System.out.println("🎤 " + name + " language set to " + language);
                    return new DeviceControlResult(true, "Language set", language);
                }
                return new DeviceControlResult(false, "Invalid language", null);
                
            case "process_voice":
                if (parameters.length > 0 && parameters[0] instanceof String && isListening && !microphoneMuted) {
                    String voiceInput = (String) parameters[0];
                    System.out.println("🎤 " + name + " processing: \"" + voiceInput + "\"");
                    
                    // Forward to hub for processing
                    if (hub != null) {
                        DeviceControlResult result = hub.processVoiceCommand(voiceInput);
                        return new DeviceControlResult(true, "Voice command processed", result);
                    }
                    return new DeviceControlResult(false, "Hub not connected", null);
                }
                return new DeviceControlResult(false, "Cannot process voice - not listening or muted", null);
                
            default:
                return new DeviceControlResult(false, "Unknown command: " + command, null);
        }
    }
    
    @Override
    public String getDeviceId() { return deviceId; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getType() { return "Voice Assistant"; }
    
    @Override
    public String getStatus() {
        if (!isOnline) return "Offline";
        
        String status = isListening ? "Listening" : "Idle";
        if (microphoneMuted) status += " (Muted)";
        status += " - Vol: " + (int)(volume * 100) + "%";
        
        return status;
    }
    
    @Override
    public boolean isOnline() { return isOnline; }
    
    @Override
    public void setHub(SmartHomeHub hub) { this.hub = hub; }
    
    @Override
    public DeviceCapabilities getCapabilities() {
        return new DeviceCapabilities(
            new String[]{"start_listening", "stop_listening", "set_volume", "mute_microphone", "unmute_microphone", "set_language", "process_voice"},
            new String[]{"listening_status", "volume", "language", "microphone_status"}
        );
    }
}
