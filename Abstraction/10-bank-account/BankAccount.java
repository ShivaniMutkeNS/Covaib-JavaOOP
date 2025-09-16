import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Abstract base class for all bank account types
 * Defines core banking operations that all accounts must implement
 */
public abstract class BankAccount {
    protected String accountNumber;
    protected String accountHolderName;
    protected AccountType accountType;
    protected double balance;
    protected LocalDate openDate;
    protected List<Transaction> transactionHistory;
    protected int monthlyTransactionCount;
    protected LocalDate lastInterestDate;
    protected boolean isActive;
    protected double overdraftLimit;
    
    public BankAccount(String accountNumber, String accountHolderName, AccountType accountType, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.balance = 0.0;
        this.openDate = LocalDate.now();
        this.transactionHistory = new ArrayList<>();
        this.monthlyTransactionCount = 0;
        this.lastInterestDate = LocalDate.now();
        this.isActive = true;
        this.overdraftLimit = 0.0;
        
        // Process initial deposit
        if (initialDeposit > 0) {
            deposit(initialDeposit, "Initial deposit");
        }
        
        // Check minimum balance requirement
        validateMinimumBalance();
    }
    
    // Abstract methods that must be implemented by concrete classes
    public abstract boolean canWithdraw(double amount);
    public abstract void calculateInterest();
    public abstract void applyMonthlyFees();
    public abstract double getAvailableBalance();
    
    // Concrete methods with default implementation
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            System.out.println("❌ Deposit amount must be positive");
            return false;
        }
        
        if (!isActive) {
            System.out.println("❌ Account is inactive");
            return false;
        }
        
        balance += amount;
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, description, balance);
        transactionHistory.add(transaction);
        
        System.out.println("✅ Deposited $" + String.format("%.2f", amount) + " - " + description);
        return true;
    }
    
    public boolean withdraw(double amount, String description) {
        return withdraw(amount, description, TransactionType.WITHDRAWAL);
    }
    
    protected boolean withdraw(double amount, String description, TransactionType transactionType) {
        if (amount <= 0) {
            System.out.println("❌ Withdrawal amount must be positive");
            return false;
        }
        
        if (!isActive) {
            System.out.println("❌ Account is inactive");
            return false;
        }
        
        if (!canWithdraw(amount)) {
            System.out.println("❌ Insufficient funds or withdrawal not allowed");
            return false;
        }
        
        if (accountType.hasTransactionLimit() && transactionType.countsTowardLimit()) {
            if (monthlyTransactionCount >= accountType.getMonthlyTransactionLimit()) {
                System.out.println("❌ Monthly transaction limit exceeded");
                return false;
            }
            monthlyTransactionCount++;
        }
        
        balance -= amount;
        Transaction transaction = new Transaction(transactionType, amount, description, balance);
        transactionHistory.add(transaction);
        
        System.out.println("✅ Withdrew $" + String.format("%.2f", amount) + " - " + description);
        
        // Check for overdraft
        if (balance < 0 && overdraftLimit == 0) {
            applyOverdraftFee();
        }
        
        return true;
    }
    
    public boolean transfer(BankAccount targetAccount, double amount, String description) {
        if (targetAccount == null) {
            System.out.println("❌ Invalid target account");
            return false;
        }
        
        if (this.withdraw(amount, "Transfer to " + targetAccount.getAccountNumber(), TransactionType.TRANSFER_OUT)) {
            targetAccount.deposit(amount, "Transfer from " + this.getAccountNumber());
            
            // Add transfer in transaction to target account
            Transaction transferIn = new Transaction(TransactionType.TRANSFER_IN, amount, 
                "Transfer from " + this.getAccountNumber(), targetAccount.getBalance());
            targetAccount.transactionHistory.add(transferIn);
            
            System.out.println("💸 Transfer completed: $" + String.format("%.2f", amount) + 
                             " from " + this.getAccountNumber() + " to " + targetAccount.getAccountNumber());
            return true;
        }
        return false;
    }
    
    protected void applyOverdraftFee() {
        double overdraftFee = 35.0; // Standard overdraft fee
        balance -= overdraftFee;
        Transaction feeTransaction = new Transaction(TransactionType.OVERDRAFT_FEE, overdraftFee, 
            "Overdraft fee", balance);
        transactionHistory.add(feeTransaction);
        System.out.println("⚠️ Overdraft fee applied: $" + String.format("%.2f", overdraftFee));
    }
    
    protected void validateMinimumBalance() {
        if (accountType.hasMinimumBalance() && balance < accountType.getMinimumBalance()) {
            System.out.println("⚠️ Warning: Balance below minimum requirement of $" + 
                             String.format("%.2f", accountType.getMinimumBalance()));
        }
    }
    
    public void processMonthlyMaintenance() {
        System.out.println("🔄 Processing monthly maintenance for account: " + accountNumber);
        
        // Reset monthly transaction count
        monthlyTransactionCount = 0;
        
        // Apply monthly fees
        applyMonthlyFees();
        
        // Calculate and apply interest
        calculateInterest();
        
        // Validate minimum balance
        validateMinimumBalance();
        
        System.out.println("✅ Monthly maintenance completed");
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return copy to prevent modification
    }
    
    public List<Transaction> getRecentTransactions(int count) {
        List<Transaction> recent = new ArrayList<>();
        int start = Math.max(0, transactionHistory.size() - count);
        for (int i = start; i < transactionHistory.size(); i++) {
            recent.add(transactionHistory.get(i));
        }
        return recent;
    }
    
    public void printStatement() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 ACCOUNT STATEMENT");
        System.out.println("=".repeat(60));
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Account Type: " + accountType.getDisplayName());
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
        System.out.println("Available Balance: $" + String.format("%.2f", getAvailableBalance()));
        System.out.println("Account Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Opened: " + openDate);
        
        if (accountType.hasTransactionLimit()) {
            System.out.println("Monthly Transactions: " + monthlyTransactionCount + "/" + 
                             accountType.getMonthlyTransactionLimit());
        }
        
        System.out.println("\nRECENT TRANSACTIONS:");
        System.out.println("-".repeat(60));
        
        List<Transaction> recent = getRecentTransactions(10);
        if (recent.isEmpty()) {
            System.out.println("No transactions found");
        } else {
            for (Transaction transaction : recent) {
                System.out.println(transaction);
            }
        }
        System.out.println("=".repeat(60) + "\n");
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public AccountType getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public LocalDate getOpenDate() { return openDate; }
    public boolean isActive() { return isActive; }
    public int getMonthlyTransactionCount() { return monthlyTransactionCount; }
    public double getOverdraftLimit() { return overdraftLimit; }
    
    // Setters
    public void setActive(boolean active) { this.isActive = active; }
    protected void setOverdraftLimit(double limit) { this.overdraftLimit = limit; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s): $%.2f", 
            accountNumber, accountHolderName, accountType.getDisplayName(), balance);
    }
}
