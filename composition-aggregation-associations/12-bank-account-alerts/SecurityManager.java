package composition.banking;

/**
 * Security Manager interface for transaction security validation
 */
public interface SecurityManager {
    SecurityValidationResult validateTransaction(String accountNumber, TransactionType type, double amount, String targetAccount);
    String getManagerName();
}
