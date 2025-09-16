package composition.banking;

import java.util.List;

/**
 * Account Statement data class
 */
public class AccountStatement {
    private final String accountNumber;
    private final long startDate;
    private final long endDate;
    private final double startBalance;
    private final double endBalance;
    private final List<Transaction> transactions;
    
    public AccountStatement(String accountNumber, long startDate, long endDate, 
                           double startBalance, double endBalance, List<Transaction> transactions) {
        this.accountNumber = accountNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startBalance = startBalance;
        this.endBalance = endBalance;
        this.transactions = transactions;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public long getStartDate() { return startDate; }
    public long getEndDate() { return endDate; }
    public double getStartBalance() { return startBalance; }
    public double getEndBalance() { return endBalance; }
    public List<Transaction> getTransactions() { return transactions; }
}
