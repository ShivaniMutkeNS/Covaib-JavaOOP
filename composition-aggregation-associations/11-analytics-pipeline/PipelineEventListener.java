package composition.analytics;

/**
 * Pipeline Event Listener interface for observing pipeline events
 */
public interface PipelineEventListener {
    void onPipelineEvent(String pipelineId, String message, PipelineStatus status);
}
