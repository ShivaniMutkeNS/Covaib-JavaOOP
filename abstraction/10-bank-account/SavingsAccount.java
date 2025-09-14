/**
 * Savings Account implementation with interest earning and transaction limits
 */
public class SavingsAccount extends BankAccount {
    private double interestEarned;
    private boolean compoundInterest;
    
    public SavingsAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, AccountType.SAVINGS, initialDeposit);
        this.interestEarned = 0.0;
        this.compoundInterest = true;
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Savings accounts cannot go negative
        return (balance - amount) >= 0;
    }
    
    @Override
    public void calculateInterest() {
        if (balance > 0) {
            double monthlyInterest = balance * accountType.getMonthlyInterestRate();
            balance += monthlyInterest;
            interestEarned += monthlyInterest;
            
            Transaction interestTransaction = new Transaction(TransactionType.INTEREST_CREDIT, 
                monthlyInterest, "Monthly interest credit", balance);
            transactionHistory.add(interestTransaction);
            
            System.out.println("💰 Interest credited: $" + String.format("%.2f", monthlyInterest));
        }
    }
    
    @Override
    public void applyMonthlyFees() {
        // Savings accounts typically don't have monthly fees
        if (balance < accountType.getMinimumBalance()) {
            double lowBalanceFee = 5.0;
            balance -= lowBalanceFee;
            
            Transaction feeTransaction = new Transaction(TransactionType.MAINTENANCE_FEE, 
                lowBalanceFee, "Low balance fee", balance);
            transactionHistory.add(feeTransaction);
            
            System.out.println("💸 Low balance fee applied: $" + String.format("%.2f", lowBalanceFee));
        }
    }
    
    @Override
    public double getAvailableBalance() {
        return balance; // Full balance is available for savings
    }
    
    public void enableCompoundInterest(boolean enable) {
        this.compoundInterest = enable;
        System.out.println("🔄 Compound interest " + (enable ? "enabled" : "disabled"));
    }
    
    public double getTotalInterestEarned() {
        return interestEarned;
    }
    
    public double getAnnualInterestProjection() {
        return balance * accountType.getAnnualInterestRate();
    }
    
    @Override
    public void printStatement() {
        super.printStatement();
        System.out.println("💰 Total Interest Earned: $" + String.format("%.2f", interestEarned));
        System.out.println("📈 Projected Annual Interest: $" + String.format("%.2f", getAnnualInterestProjection()));
        System.out.println("🔄 Compound Interest: " + (compoundInterest ? "Enabled" : "Disabled"));
    }
}
