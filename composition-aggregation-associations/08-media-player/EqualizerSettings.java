package composition.media;

/**
 * Equalizer Settings data class
 */
public class EqualizerSettings {
    private final String presetName;
    private final double[] frequencyBands; // dB adjustments for different frequency ranges
    
    public EqualizerSettings(String presetName, double[] frequencyBands) {
        this.presetName = presetName;
        this.frequencyBands = frequencyBands.clone();
    }
    
    public String getPresetName() { return presetName; }
    public double[] getFrequencyBands() { return frequencyBands.clone(); }
    
    // Common presets
    public static EqualizerSettings rock() {
        return new EqualizerSettings("Rock", new double[]{3.0, 2.0, -1.0, -0.5, 1.0, 2.5, 3.5, 3.0});
    }
    
    public static EqualizerSettings pop() {
        return new EqualizerSettings("Pop", new double[]{1.0, 2.0, 3.0, 2.0, 0.0, -1.0, 1.0, 2.0});
    }
    
    public static EqualizerSettings classical() {
        return new EqualizerSettings("Classical", new double[]{2.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0});
    }
    
    public static EqualizerSettings jazz() {
        return new EqualizerSettings("Jazz", new double[]{2.0, 1.0, 1.0, 0.0, -1.0, 1.0, 2.0, 2.5});
    }
}
