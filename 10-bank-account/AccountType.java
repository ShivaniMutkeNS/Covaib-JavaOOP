/**
 * Enumeration representing different types of bank accounts
 * Each type has specific characteristics and rules
 */
public enum AccountType {
    SAVINGS("Savings Account", 0.02, 0.0, 1000.0, 6),
    CHECKING("Checking Account", 0.001, 2.50, 0.0, -1),
    CREDIT("Credit Card Account", 0.18, 0.0, 0.0, -1),
    INVESTMENT("Investment Account", 0.05, 10.0, 5000.0, 12),
    BUSINESS("Business Account", 0.015, 5.0, 2500.0, -1),
    STUDENT("Student Account", 0.01, 0.0, 100.0, 10);
    
    private final String displayName;
    private final double annualInterestRate;
    private final double monthlyFee;
    private final double minimumBalance;
    private final int monthlyTransactionLimit; // -1 means unlimited
    
    AccountType(String displayName, double annualInterestRate, double monthlyFee, 
                double minimumBalance, int monthlyTransactionLimit) {
        this.displayName = displayName;
        this.annualInterestRate = annualInterestRate;
        this.monthlyFee = monthlyFee;
        this.minimumBalance = minimumBalance;
        this.monthlyTransactionLimit = monthlyTransactionLimit;
    }
    
    public String getDisplayName() { return displayName; }
    public double getAnnualInterestRate() { return annualInterestRate; }
    public double getMonthlyInterestRate() { return annualInterestRate / 12.0; }
    public double getMonthlyFee() { return monthlyFee; }
    public double getMinimumBalance() { return minimumBalance; }
    public int getMonthlyTransactionLimit() { return monthlyTransactionLimit; }
    
    public boolean hasTransactionLimit() {
        return monthlyTransactionLimit > 0;
    }
    
    public boolean hasMinimumBalance() {
        return minimumBalance > 0;
    }
    
    public boolean hasMonthlyFee() {
        return monthlyFee > 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.2f%% APR, $%.2f fee, $%.2f min balance)", 
            displayName, annualInterestRate * 100, monthlyFee, minimumBalance);
    }
}
