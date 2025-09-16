package composition.banking;

/**
 * Alert Evaluation data class for alert rule evaluation results
 */
public class AlertEvaluation {
    private final boolean shouldAlert;
    private final String message;
    private final AlertSeverity severity;
    
    public AlertEvaluation(boolean shouldAlert, String message, AlertSeverity severity) {
        this.shouldAlert = shouldAlert;
        this.message = message;
        this.severity = severity;
    }
    
    public boolean shouldAlert() { return shouldAlert; }
    public String getMessage() { return message; }
    public AlertSeverity getSeverity() { return severity; }
}
