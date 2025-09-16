package composition.banking;

/**
 * Transaction data class for bank transactions
 */
public class Transaction {
    private final String transactionId;
    private final TransactionType type;
    private final double amount;
    private final String description;
    private final long timestamp;
    private final String targetAccount;
    private final double fee;
    
    public Transaction(String transactionId, TransactionType type, double amount, 
                      String description, long timestamp) {
        this(transactionId, type, amount, description, timestamp, null, 0.0);
    }
    
    public Transaction(String transactionId, TransactionType type, double amount, 
                      String description, long timestamp, String targetAccount, double fee) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.targetAccount = targetAccount;
        this.fee = fee;
    }
    
    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public long getTimestamp() { return timestamp; }
    public String getTargetAccount() { return targetAccount; }
    public double getFee() { return fee; }
}
