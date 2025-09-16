package composition.banking;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Account Metrics data class for tracking account statistics
 */
public class AccountMetrics {
    private final AtomicLong totalDeposits;
    private final AtomicLong totalWithdrawals;
    private final AtomicLong totalTransfers;
    private double totalTransactionAmount;
    private double totalFeeAmount;
    
    public AccountMetrics() {
        this.totalDeposits = new AtomicLong(0);
        this.totalWithdrawals = new AtomicLong(0);
        this.totalTransfers = new AtomicLong(0);
        this.totalTransactionAmount = 0.0;
        this.totalFeeAmount = 0.0;
    }
    
    public void incrementTransactionCount(TransactionType type) {
        switch (type) {
            case DEPOSIT:
                totalDeposits.incrementAndGet();
                break;
            case WITHDRAWAL:
                totalWithdrawals.incrementAndGet();
                break;
            case TRANSFER:
                totalTransfers.incrementAndGet();
                break;
        }
    }
    
    public void addTransactionAmount(double amount) {
        totalTransactionAmount += amount;
    }
    
    public void addFeeAmount(double amount) {
        totalFeeAmount += amount;
    }
    
    public long getTotalDeposits() { return totalDeposits.get(); }
    public long getTotalWithdrawals() { return totalWithdrawals.get(); }
    public long getTotalTransfers() { return totalTransfers.get(); }
    public double getTotalTransactionAmount() { return totalTransactionAmount; }
    public double getTotalFeeAmount() { return totalFeeAmount; }
}
