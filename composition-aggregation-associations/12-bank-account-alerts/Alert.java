package composition.banking;

/**
 * Alert data class for bank account alerts
 */
public class Alert {
    private final String accountNumber;
    private final AlertType type;
    private final String message;
    private final AlertSeverity severity;
    private final long timestamp;
    
    public Alert(String accountNumber, AlertType type, String message, AlertSeverity severity, long timestamp) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.timestamp = timestamp;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public AlertType getType() { return type; }
    public String getMessage() { return message; }
    public AlertSeverity getSeverity() { return severity; }
    public long getTimestamp() { return timestamp; }
}
