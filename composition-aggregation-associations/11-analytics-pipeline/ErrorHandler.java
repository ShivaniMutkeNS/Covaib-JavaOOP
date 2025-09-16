package composition.analytics;

/**
 * Error Handler interface for handling pipeline errors
 */
public interface ErrorHandler {
    void handleError(PipelineError error);
    String getHandlerName();
}
