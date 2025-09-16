package composition.banking;

/**
 * Balance Alert Rule implementation for low balance alerts
 */
public class BalanceAlertRule implements AlertRule {
    private final double threshold;
    
    public BalanceAlertRule(double threshold) {
        this.threshold = threshold;
    }
    
    @Override
    public AlertEvaluation evaluate(BankAccount account, Transaction transaction) {
        if (account.getBalance() <= threshold) {
            String message = String.format("Low balance alert: $%.2f (threshold: $%.2f)", 
                                          account.getBalance(), threshold);
            AlertSeverity severity = account.getBalance() <= threshold * 0.5 ? AlertSeverity.HIGH : AlertSeverity.MEDIUM;
            return new AlertEvaluation(true, message, severity);
        }
        
        return new AlertEvaluation(false, null, AlertSeverity.INFO);
    }
    
    @Override
    public String getRuleName() {
        return "Balance Alert Rule";
    }
}
