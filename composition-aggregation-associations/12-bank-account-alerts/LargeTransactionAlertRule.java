package composition.banking;

/**
 * Large Transaction Alert Rule implementation
 */
public class LargeTransactionAlertRule implements AlertRule {
    private final double threshold;
    
    public LargeTransactionAlertRule(double threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public AlertEvaluation evaluate(BankAccount account, Transaction transaction) {
        if (transaction.getAmount() >= threshold) {
            String message = String.format("Large transaction alert: %s of $%.2f", 
                                          transaction.getType(), transaction.getAmount());
            AlertSeverity severity = transaction.getAmount() >= threshold * 5 ? AlertSeverity.HIGH : AlertSeverity.MEDIUM;
            return new AlertEvaluation(true, message, severity);
        }
        
        return new AlertEvaluation(false, null, AlertSeverity.INFO);
    }
    
    @Override
    public String getRuleName() {
        return "Large Transaction Alert Rule";
    }
}
