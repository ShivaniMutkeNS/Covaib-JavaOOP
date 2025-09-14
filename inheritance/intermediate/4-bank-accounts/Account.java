/**
 * Abstract base class for all bank accounts
 * Demonstrates overriding with rules and encapsulated balance handling
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected String accountType;
    protected boolean isActive;
    protected String openDate;
    protected double minimumBalance;
    
    /**
     * Constructor for Account
     * @param accountNumber Unique account identifier
     * @param accountHolderName Name of the account holder
     * @param initialBalance Initial balance
     * @param accountType Type of account
     * @param minimumBalance Minimum balance required
     */
    public Account(String accountNumber, String accountHolderName, double initialBalance, 
                   String accountType, double minimumBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.minimumBalance = minimumBalance;
        this.isActive = true;
        this.openDate = java.time.LocalDate.now().toString();
    }
    
    /**
     * Abstract method to withdraw money
     * Each account type has different withdrawal rules
     * @param amount Amount to withdraw
     * @return True if withdrawal successful
     */
    public abstract boolean withdraw(double amount);
    
    /**
     * Abstract method to calculate interest
     * Each account type has different interest rates
     * @return The interest amount
     */
    public abstract double calculateInterest();
    
    /**
     * Abstract method to get account features
     * Each account type has different features
     * @return String description of features
     */
    public abstract String getAccountFeatures();
    
    /**
     * Concrete method to deposit money
     * @param amount Amount to deposit
     * @return True if deposit successful
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Amount must be positive.");
            return false;
        }
        
        if (!isActive) {
            System.out.println("Account is not active. Cannot deposit.");
            return false;
        }
        
        balance += amount;
        System.out.println("Deposit successful. New balance: $" + String.format("%.2f", balance));
        return true;
    }
    
    /**
     * Concrete method to check balance
     * @return Current account balance
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * Concrete method to get account information
     * @return String with account details
     */
    public String getAccountInfo() {
        return String.format("Account: %s, Holder: %s, Type: %s, Balance: $%.2f, Active: %s", 
                           accountNumber, accountHolderName, accountType, balance, isActive);
    }
    
    /**
     * Concrete method to check if account is active
     * @return True if account is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Concrete method to deactivate account
     * @return True if deactivation successful
     */
    public boolean deactivateAccount() {
        if (balance > 0) {
            System.out.println("Cannot deactivate account with positive balance.");
            return false;
        }
        
        isActive = false;
        System.out.println("Account deactivated successfully.");
        return true;
    }
    
    /**
     * Concrete method to check minimum balance requirement
     * @return True if account meets minimum balance requirement
     */
    public boolean meetsMinimumBalance() {
        return balance >= minimumBalance;
    }
    
    /**
     * Concrete method to get minimum balance
     * @return Minimum balance required
     */
    public double getMinimumBalance() {
        return minimumBalance;
    }
    
    /**
     * Getter for account number
     * @return The account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
    
    /**
     * Getter for account holder name
     * @return The account holder name
     */
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    /**
     * Getter for account type
     * @return The account type
     */
    public String getAccountType() {
        return accountType;
    }
    
    /**
     * Getter for open date
     * @return The open date
     */
    public String getOpenDate() {
        return openDate;
    }
    
    /**
     * Override toString method
     * @return String representation of the account
     */
    @Override
    public String toString() {
        return getAccountInfo();
    }
}
