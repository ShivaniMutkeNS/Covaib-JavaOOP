package composition.analytics;

/**
 * Pipeline State enum for pipeline lifecycle
 */
public enum PipelineState {
    STOPPED,
    STARTING,
    RUNNING,
    PAUSED,
    STOPPING,
    ERROR
}
