package composition.banking;

/**
 * Standard Transaction Validator implementation
 */
public class StandardTransactionValidator implements TransactionValidator {
    private static final double MAX_DAILY_WITHDRAWAL = 5000.0;
    private static final double MAX_SINGLE_TRANSACTION = 10000.0;
    private static final double MIN_TRANSACTION_AMOUNT = 0.01;
    
    @Override
    public ValidationResult validateTransaction(BankAccount account, TransactionType type, double amount, String description) {
        // Basic amount validation
        if (amount < MIN_TRANSACTION_AMOUNT) {
            return new ValidationResult(false, "Transaction amount too small");
        }
        
        if (amount > MAX_SINGLE_TRANSACTION) {
            return new ValidationResult(false, "Transaction amount exceeds single transaction limit");
        }
        
        // Withdrawal limits
        if (type == TransactionType.WITHDRAWAL || type == TransactionType.TRANSFER) {
            double dailyWithdrawals = calculateDailyWithdrawals(account);
            if (dailyWithdrawals + amount > MAX_DAILY_WITHDRAWAL) {
                return new ValidationResult(false, "Daily withdrawal limit exceeded");
            }
        }
        
        // Description validation
        if (description == null || description.trim().isEmpty()) {
            return new ValidationResult(false, "Transaction description is required");
        }
        
        return new ValidationResult(true, "Transaction valid");
    }
    
    private double calculateDailyWithdrawals(BankAccount account) {
        long oneDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        
        return account.getTransactionHistory().stream()
            .filter(t -> t.getTimestamp() >= oneDayAgo)
            .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    @Override
    public String getValidatorName() {
        return "Standard Transaction Validator";
    }
}
