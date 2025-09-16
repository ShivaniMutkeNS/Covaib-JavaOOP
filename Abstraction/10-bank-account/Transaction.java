import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a banking transaction with all relevant details
 */
public class Transaction {
    private static int nextTransactionId = 1000;
    
    private final int transactionId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String description;
    private final double balanceAfter;
    private final String reference;
    
    public Transaction(TransactionType type, double amount, String description, double balanceAfter) {
        this.transactionId = nextTransactionId++;
        this.type = type;
        this.amount = Math.abs(amount); // Store as positive value
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.reference = generateReference();
    }
    
    public Transaction(TransactionType type, double amount, String description, 
                      double balanceAfter, String reference) {
        this.transactionId = nextTransactionId++;
        this.type = type;
        this.amount = Math.abs(amount);
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.reference = reference;
    }
    
    private String generateReference() {
        return "TXN" + transactionId + timestamp.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    
    public double getEffectiveAmount() {
        return type.isCredit() ? amount : -amount;
    }
    
    // Getters
    public int getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getReference() { return reference; }
    
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s$%.2f - %s (Balance: $%.2f) [Ref: %s]",
            getFormattedTimestamp(),
            type.getDisplayName(),
            type.isCredit() ? "+" : "-",
            amount,
            description,
            balanceAfter,
            reference
        );
    }
    
    public String getShortDescription() {
        return String.format("%s %s$%.2f - %s",
            type.getDisplayName(),
            type.isCredit() ? "+" : "-",
            amount,
            description
        );
    }
}
