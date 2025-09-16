package composition.banking;

/**
 * Unusual Activity Alert Rule implementation
 */
public class UnusualActivityAlertRule implements AlertRule {
    private final int transactionCountThreshold;
    private final long timeWindowMs;
    
    public UnusualActivityAlertRule(int transactionCountThreshold, long timeWindowMs) {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindowMs = timeWindowMs;
    }
    
    @Override
    public AlertEvaluation evaluate(BankAccount account, Transaction transaction) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - timeWindowMs;
        
        long recentTransactionCount = account.getTransactionHistory().stream()
            .filter(t -> t.getTimestamp() >= windowStart)
            .count();
        
        if (recentTransactionCount >= transactionCountThreshold) {
            String message = String.format("Unusual activity: %d transactions in %d minutes", 
                                          recentTransactionCount, timeWindowMs / 60000);
            return new AlertEvaluation(true, message, AlertSeverity.HIGH);
        }
        
        return new AlertEvaluation(false, null, AlertSeverity.INFO);
    }
    
    @Override
    public String getRuleName() {
        return "Unusual Activity Alert Rule";
    }
}
