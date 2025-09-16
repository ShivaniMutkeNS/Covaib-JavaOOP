package composition.banking;

/**
 * Security Alert Rule implementation
 */
public class SecurityAlertRule implements AlertRule {
    
    @Override
    public AlertEvaluation evaluate(BankAccount account, Transaction transaction) {
        // Check for suspicious patterns
        if (isNightTransaction(transaction)) {
            return new AlertEvaluation(true, "Transaction during unusual hours", AlertSeverity.MEDIUM);
        }
        
        if (isRoundAmountTransaction(transaction)) {
            return new AlertEvaluation(true, "Suspicious round amount transaction", AlertSeverity.LOW);
        }
        
        return new AlertEvaluation(false, null, AlertSeverity.INFO);
    }
    
    private boolean isNightTransaction(Transaction transaction) {
        // Check if transaction is between 11 PM and 6 AM
        long hour = (transaction.getTimestamp() / (1000 * 60 * 60)) % 24;
        return hour >= 23 || hour <= 6;
    }
    
    private boolean isRoundAmountTransaction(Transaction transaction) {
        // Check if amount is a round number (e.g., 1000.00, 5000.00)
        double amount = transaction.getAmount();
        return amount >= 1000 && amount % 1000 == 0;
    }
    
    @Override
    public String getRuleName() {
        return "Security Alert Rule";
    }
}
