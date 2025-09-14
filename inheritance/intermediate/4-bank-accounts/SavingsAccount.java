/**
 * Savings Account class extending Account
 * Demonstrates overriding with rules and encapsulated balance handling
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
    private static final double WITHDRAWAL_LIMIT = 1000.0; // Daily withdrawal limit
    private static final int MAX_WITHDRAWALS = 6; // Maximum withdrawals per month
    private int monthlyWithdrawals;
    private double dailyWithdrawn;
    private String lastWithdrawalDate;
    
    /**
     * Constructor for SavingsAccount
     * @param accountNumber Unique account identifier
     * @param accountHolderName Name of the account holder
     * @param initialBalance Initial balance
     */
    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance, "Savings", 100.0);
        this.monthlyWithdrawals = 0;
        this.dailyWithdrawn = 0.0;
        this.lastWithdrawalDate = "";
    }
    
    /**
     * Override withdraw method with savings account rules
     * @param amount Amount to withdraw
     * @return True if withdrawal successful
     */
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Amount must be positive.");
            return false;
        }
        
        if (!isActive) {
            System.out.println("Account is not active. Cannot withdraw.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Insufficient funds. Available balance: $" + String.format("%.2f", balance));
            return false;
        }
        
        if (amount > WITHDRAWAL_LIMIT) {
            System.out.println("Withdrawal amount exceeds daily limit of $" + String.format("%.2f", WITHDRAWAL_LIMIT));
            return false;
        }
        
        if (monthlyWithdrawals >= MAX_WITHDRAWALS) {
            System.out.println("Monthly withdrawal limit reached. Maximum " + MAX_WITHDRAWALS + " withdrawals allowed.");
            return false;
        }
        
        if (balance - amount < minimumBalance) {
            System.out.println("Withdrawal would violate minimum balance requirement of $" + String.format("%.2f", minimumBalance));
            return false;
        }
        
        // Check daily withdrawal limit
        String today = java.time.LocalDate.now().toString();
        if (!today.equals(lastWithdrawalDate)) {
            dailyWithdrawn = 0.0;
            lastWithdrawalDate = today;
        }
        
        if (dailyWithdrawn + amount > WITHDRAWAL_LIMIT) {
            System.out.println("Daily withdrawal limit exceeded. Remaining today: $" + String.format("%.2f", WITHDRAWAL_LIMIT - dailyWithdrawn));
            return false;
        }
        
        balance -= amount;
        dailyWithdrawn += amount;
        monthlyWithdrawals++;
        
        System.out.println("Withdrawal successful. Amount: $" + String.format("%.2f", amount));
        System.out.println("New balance: $" + String.format("%.2f", balance));
        System.out.println("Monthly withdrawals used: " + monthlyWithdrawals + "/" + MAX_WITHDRAWALS);
        
        return true;
    }
    
    /**
     * Override calculateInterest method with savings account interest rate
     * @return The interest amount
     */
    @Override
    public double calculateInterest() {
        if (!isActive) {
            return 0.0;
        }
        
        double interest = balance * INTEREST_RATE;
        System.out.println("Interest calculated: $" + String.format("%.2f", interest) + " (Rate: " + String.format("%.1f", INTEREST_RATE * 100) + "%)");
        return interest;
    }
    
    /**
     * Override getAccountFeatures method with savings account features
     * @return String description of savings account features
     */
    @Override
    public String getAccountFeatures() {
        return "Savings Account Features: " +
               "2% annual interest, " +
               "Daily withdrawal limit: $" + String.format("%.2f", WITHDRAWAL_LIMIT) + ", " +
               "Monthly withdrawal limit: " + MAX_WITHDRAWALS + " transactions, " +
               "Minimum balance: $" + String.format("%.2f", minimumBalance) + ", " +
               "No monthly fees, " +
               "Online banking access";
    }
    
    /**
     * Savings account specific method to reset monthly withdrawals
     */
    public void resetMonthlyWithdrawals() {
        monthlyWithdrawals = 0;
        System.out.println("Monthly withdrawal counter reset.");
    }
    
    /**
     * Savings account specific method to get withdrawal history
     * @return String with withdrawal history
     */
    public String getWithdrawalHistory() {
        return String.format("Monthly withdrawals: %d/%d, Daily withdrawn: $%.2f", 
                           monthlyWithdrawals, MAX_WITHDRAWALS, dailyWithdrawn);
    }
    
    /**
     * Savings account specific method to check withdrawal eligibility
     * @param amount Amount to check
     * @return True if withdrawal is allowed
     */
    public boolean canWithdraw(double amount) {
        if (amount <= 0 || amount > balance || amount > WITHDRAWAL_LIMIT) {
            return false;
        }
        
        if (monthlyWithdrawals >= MAX_WITHDRAWALS) {
            return false;
        }
        
        if (balance - amount < minimumBalance) {
            return false;
        }
        
        String today = java.time.LocalDate.now().toString();
        if (!today.equals(lastWithdrawalDate)) {
            return true; // New day, reset daily limit
        }
        
        return dailyWithdrawn + amount <= WITHDRAWAL_LIMIT;
    }
    
    /**
     * Getter for monthly withdrawals
     * @return Number of monthly withdrawals
     */
    public int getMonthlyWithdrawals() {
        return monthlyWithdrawals;
    }
    
    /**
     * Getter for daily withdrawn amount
     * @return Amount withdrawn today
     */
    public double getDailyWithdrawn() {
        return dailyWithdrawn;
    }
    
    /**
     * Getter for withdrawal limit
     * @return Daily withdrawal limit
     */
    public double getWithdrawalLimit() {
        return WITHDRAWAL_LIMIT;
    }
    
    /**
     * Getter for maximum withdrawals
     * @return Maximum monthly withdrawals
     */
    public int getMaxWithdrawals() {
        return MAX_WITHDRAWALS;
    }
    
    /**
     * Override toString to include savings account specific details
     * @return String representation of the savings account
     */
    @Override
    public String toString() {
        return super.toString() + " [Withdrawals: " + monthlyWithdrawals + "/" + MAX_WITHDRAWALS + 
               ", Daily: $" + String.format("%.2f", dailyWithdrawn) + "]";
    }
}
