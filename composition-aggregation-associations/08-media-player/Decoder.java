package composition.media;

/**
 * Decoder Strategy Interface
 */
public interface Decoder {
    void initialize();
    DecodedAudio decode(MediaFile mediaFile);
    String getType();
    String[] getSupportedFormats();
    boolean canDecode(String format);
}
