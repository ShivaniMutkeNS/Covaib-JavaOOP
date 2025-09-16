package composition.banking;

/**
 * Account Analytics data class
 */
public class AccountAnalytics {
    private final AccountMetrics metrics;
    private final double totalDeposits;
    private final double totalWithdrawals;
    private final double averageBalance;
    private final int transactionCount;
    
    public AccountAnalytics(AccountMetrics metrics, double totalDeposits, double totalWithdrawals, 
                           double averageBalance, int transactionCount) {
        this.metrics = metrics;
        this.totalDeposits = totalDeposits;
        this.totalWithdrawals = totalWithdrawals;
        this.averageBalance = averageBalance;
        this.transactionCount = transactionCount;
    }
    
    public AccountMetrics getMetrics() { return metrics; }
    public double getTotalDeposits() { return totalDeposits; }
    public double getTotalWithdrawals() { return totalWithdrawals; }
    public double getAverageBalance() { return averageBalance; }
    public int getTransactionCount() { return transactionCount; }
}
