package composition.banking;

import java.util.*;

/**
 * Default Security Manager implementation
 */
public class DefaultSecurityManager implements SecurityManager {
    private final Set<String> suspiciousAccounts;
    private final Map<String, Long> lastTransactionTime;
    
    public DefaultSecurityManager() {
        this.suspiciousAccounts = new HashSet<>();
        this.lastTransactionTime = new HashMap<>();
    }
    
    @Override
    public SecurityValidationResult validateTransaction(String accountNumber, TransactionType type, double amount, String targetAccount) {
        // Check for suspicious accounts
        if (suspiciousAccounts.contains(accountNumber)) {
            return new SecurityValidationResult(false, "Account flagged for suspicious activity");
        }
        
        // Check for rapid transactions (velocity check)
        Long lastTime = lastTransactionTime.get(accountNumber);
        long currentTime = System.currentTimeMillis();
        
        if (lastTime != null && (currentTime - lastTime) < 1000) { // Less than 1 second
            return new SecurityValidationResult(false, "Transaction velocity too high");
        }
        
        // Check for large amounts
        if (amount > 50000.0) {
            return new SecurityValidationResult(false, "Large transaction requires additional verification");
        }
        
        // Check for suspicious transfer patterns
        if (type == TransactionType.TRANSFER && targetAccount != null) {
            if (targetAccount.equals(accountNumber)) {
                return new SecurityValidationResult(false, "Cannot transfer to same account");
            }
        }
        
        // Update last transaction time
        lastTransactionTime.put(accountNumber, currentTime);
        
        return new SecurityValidationResult(true, "Security validation passed");
    }
    
    @Override
    public String getManagerName() {
        return "Default Security Manager";
    }
}
