package composition.media;

/**
 * Media File data class
 */
public class MediaFile {
    private final String title;
    private final String artist;
    private final String album;
    private final String format;
    private final int durationSeconds;
    private final long fileSize; // in bytes
    private final int bitrate; // in kbps
    private final String filePath;
    
    public MediaFile(String title, String artist, String album, String format, 
                    int durationSeconds, long fileSize, int bitrate, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.format = format;
        this.durationSeconds = durationSeconds;
        this.fileSize = fileSize;
        this.bitrate = bitrate;
        this.filePath = filePath;
    }
    
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getFormat() { return format; }
    public int getDurationSeconds() { return durationSeconds; }
    public long getFileSize() { return fileSize; }
    public int getBitrate() { return bitrate; }
    public String getFilePath() { return filePath; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", artist, title, format.toUpperCase());
    }
}
