/**
 * Checking Account implementation with overdraft protection and unlimited transactions
 */
public class CheckingAccount extends BankAccount {
    private boolean overdraftProtection;
    private double overdraftFeeRate;
    private int freeTransactionsPerMonth;
    private double transactionFeeAfterLimit;
    
    public CheckingAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, AccountType.CHECKING, initialDeposit);
        this.overdraftProtection = true;
        this.overdraftFeeRate = 35.0;
        this.freeTransactionsPerMonth = 25;
        this.transactionFeeAfterLimit = 1.0;
        setOverdraftLimit(500.0); // $500 overdraft limit
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Checking accounts can go negative up to overdraft limit
        return (balance - amount) >= -overdraftLimit;
    }
    
    @Override
    public void calculateInterest() {
        // Checking accounts typically earn very little interest
        if (balance > 1000) { // Only if balance is above $1000
            double monthlyInterest = balance * accountType.getMonthlyInterestRate();
            balance += monthlyInterest;
            
            Transaction interestTransaction = new Transaction(TransactionType.INTEREST_CREDIT, 
                monthlyInterest, "Monthly interest credit", balance);
            transactionHistory.add(interestTransaction);
            
            System.out.println("💰 Interest credited: $" + String.format("%.2f", monthlyInterest));
        }
    }
    
    @Override
    public void applyMonthlyFees() {
        // Apply monthly maintenance fee
        if (accountType.hasMonthlyFee()) {
            balance -= accountType.getMonthlyFee();
            
            Transaction feeTransaction = new Transaction(TransactionType.MAINTENANCE_FEE, 
                accountType.getMonthlyFee(), "Monthly maintenance fee", balance);
            transactionHistory.add(feeTransaction);
            
            System.out.println("💸 Monthly maintenance fee: $" + String.format("%.2f", accountType.getMonthlyFee()));
        }
        
        // Apply transaction fees if over limit
        if (monthlyTransactionCount > freeTransactionsPerMonth) {
            int excessTransactions = monthlyTransactionCount - freeTransactionsPerMonth;
            double totalTransactionFees = excessTransactions * transactionFeeAfterLimit;
            
            balance -= totalTransactionFees;
            
            Transaction feeTransaction = new Transaction(TransactionType.FEE_DEBIT, 
                totalTransactionFees, "Excess transaction fees (" + excessTransactions + " transactions)", balance);
            transactionHistory.add(feeTransaction);
            
            System.out.println("💸 Excess transaction fees: $" + String.format("%.2f", totalTransactionFees));
        }
    }
    
    @Override
    public double getAvailableBalance() {
        return balance + overdraftLimit; // Include overdraft limit
    }
    
    public boolean atmWithdrawal(double amount) {
        return withdraw(amount, "ATM withdrawal", TransactionType.ATM_WITHDRAWAL);
    }
    
    public boolean onlinePurchase(double amount, String merchant) {
        return withdraw(amount, "Online purchase - " + merchant, TransactionType.ONLINE_PURCHASE);
    }
    
    public boolean writeCheck(double amount, String payee) {
        if (withdraw(amount, "Check payment to " + payee, TransactionType.WITHDRAWAL)) {
            System.out.println("📝 Check written to " + payee + " for $" + String.format("%.2f", amount));
            return true;
        }
        return false;
    }
    
    public void setOverdraftProtection(boolean enabled) {
        this.overdraftProtection = enabled;
        if (!enabled) {
            setOverdraftLimit(0.0);
        } else {
            setOverdraftLimit(500.0);
        }
        System.out.println("🛡️ Overdraft protection " + (enabled ? "enabled" : "disabled"));
    }
    
    public void setOverdraftLimit(double limit) {
        super.setOverdraftLimit(limit);
        System.out.println("💳 Overdraft limit set to $" + String.format("%.2f", limit));
    }
    
    public boolean isOverdrawn() {
        return balance < 0;
    }
    
    public double getOverdrawnAmount() {
        return balance < 0 ? Math.abs(balance) : 0.0;
    }
    
    // Getters
    public boolean hasOverdraftProtection() { return overdraftProtection; }
    public double getOverdraftFeeRate() { return overdraftFeeRate; }
    public int getFreeTransactionsPerMonth() { return freeTransactionsPerMonth; }
    public double getTransactionFeeAfterLimit() { return transactionFeeAfterLimit; }
    
    @Override
    public void printStatement() {
        super.printStatement();
        System.out.println("🛡️ Overdraft Protection: " + (overdraftProtection ? "Enabled" : "Disabled"));
        System.out.println("💳 Overdraft Limit: $" + String.format("%.2f", overdraftLimit));
        System.out.println("🆓 Free Transactions: " + freeTransactionsPerMonth + "/month");
        if (isOverdrawn()) {
            System.out.println("⚠️ Account Overdrawn: $" + String.format("%.2f", getOverdrawnAmount()));
        }
    }
}
