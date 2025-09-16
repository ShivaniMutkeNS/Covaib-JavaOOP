package composition.media;

/**
 * Render Result data class
 */
public class RenderResult {
    private final boolean success;
    private final String message;
    
    public RenderResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getErrorMessage() { return success ? null : message; }
}
