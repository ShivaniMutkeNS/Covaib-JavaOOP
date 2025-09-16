package composition.banking;

/**
 * Transaction Validator interface for validating transactions
 */
public interface TransactionValidator {
    ValidationResult validateTransaction(BankAccount account, TransactionType type, double amount, String description);
    String getValidatorName();
}
