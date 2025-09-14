/**
 * Current Account class extending Account
 * Demonstrates overriding with rules and encapsulated balance handling
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class CurrentAccount extends Account {
    private static final double INTEREST_RATE = 0.01; // 1% annual interest
    private static final double OVERDRAFT_LIMIT = 5000.0; // Overdraft limit
    private static final double MONTHLY_FEE = 10.0; // Monthly maintenance fee
    private double overdraftUsed;
    private boolean hasOverdraft;
    private String lastFeeDate;
    
    /**
     * Constructor for CurrentAccount
     * @param accountNumber Unique account identifier
     * @param accountHolderName Name of the account holder
     * @param initialBalance Initial balance
     * @param hasOverdraft Whether account has overdraft facility
     */
    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance, boolean hasOverdraft) {
        super(accountNumber, accountHolderName, initialBalance, "Current", 0.0);
        this.overdraftUsed = 0.0;
        this.hasOverdraft = hasOverdraft;
        this.lastFeeDate = "";
    }
    
    /**
     * Override withdraw method with current account rules
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
        
        // Check if withdrawal is within available balance + overdraft
        double availableBalance = balance + (hasOverdraft ? (OVERDRAFT_LIMIT - overdraftUsed) : 0);
        
        if (amount > availableBalance) {
            System.out.println("Insufficient funds. Available balance: $" + String.format("%.2f", availableBalance));
            if (hasOverdraft) {
                System.out.println("Overdraft available: $" + String.format("%.2f", OVERDRAFT_LIMIT - overdraftUsed));
            }
            return false;
        }
        
        // Process withdrawal
        if (amount <= balance) {
            // Normal withdrawal
            balance -= amount;
            System.out.println("Withdrawal successful. Amount: $" + String.format("%.2f", amount));
        } else {
            // Overdraft withdrawal
            double overdraftNeeded = amount - balance;
            balance = 0;
            overdraftUsed += overdraftNeeded;
            System.out.println("Withdrawal successful. Amount: $" + String.format("%.2f", amount));
            System.out.println("Overdraft used: $" + String.format("%.2f", overdraftNeeded));
            System.out.println("Total overdraft used: $" + String.format("%.2f", overdraftUsed));
        }
        
        System.out.println("New balance: $" + String.format("%.2f", balance));
        if (hasOverdraft) {
            System.out.println("Overdraft remaining: $" + String.format("%.2f", OVERDRAFT_LIMIT - overdraftUsed));
        }
        
        return true;
    }
    
    /**
     * Override calculateInterest method with current account interest rate
     * @return The interest amount
     */
    @Override
    public double calculateInterest() {
        if (!isActive) {
            return 0.0;
        }
        
        // Only calculate interest on positive balance
        double interest = Math.max(0, balance) * INTEREST_RATE;
        System.out.println("Interest calculated: $" + String.format("%.2f", interest) + " (Rate: " + String.format("%.1f", INTEREST_RATE * 100) + "%)");
        return interest;
    }
    
    /**
     * Override getAccountFeatures method with current account features
     * @return String description of current account features
     */
    @Override
    public String getAccountFeatures() {
        return "Current Account Features: " +
               "1% annual interest on positive balance, " +
               "No minimum balance requirement, " +
               "Monthly maintenance fee: $" + String.format("%.2f", MONTHLY_FEE) + ", " +
               "Unlimited transactions, " +
               "Overdraft facility: " + (hasOverdraft ? "$" + String.format("%.2f", OVERDRAFT_LIMIT) : "Not available") + ", " +
               "Online banking access, " +
               "Check book facility";
    }
    
    /**
     * Current account specific method to charge monthly fee
     * @return True if fee charged successfully
     */
    public boolean chargeMonthlyFee() {
        String today = java.time.LocalDate.now().toString();
        if (today.equals(lastFeeDate)) {
            System.out.println("Monthly fee already charged today.");
            return false;
        }
        
        if (balance >= MONTHLY_FEE) {
            balance -= MONTHLY_FEE;
            lastFeeDate = today;
            System.out.println("Monthly fee charged: $" + String.format("%.2f", MONTHLY_FEE));
            System.out.println("New balance: $" + String.format("%.2f", balance));
            return true;
        } else {
            System.out.println("Insufficient funds to charge monthly fee.");
            return false;
        }
    }
    
    /**
     * Current account specific method to pay overdraft
     * @param amount Amount to pay towards overdraft
     * @return True if payment successful
     */
    public boolean payOverdraft(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid payment amount. Amount must be positive.");
            return false;
        }
        
        if (overdraftUsed <= 0) {
            System.out.println("No overdraft to pay.");
            return false;
        }
        
        double paymentAmount = Math.min(amount, overdraftUsed);
        overdraftUsed -= paymentAmount;
        balance += paymentAmount;
        
        System.out.println("Overdraft payment: $" + String.format("%.2f", paymentAmount));
        System.out.println("Remaining overdraft: $" + String.format("%.2f", overdraftUsed));
        System.out.println("New balance: $" + String.format("%.2f", balance));
        
        return true;
    }
    
    /**
     * Current account specific method to check overdraft status
     * @return String with overdraft status
     */
    public String getOverdraftStatus() {
        if (!hasOverdraft) {
            return "Overdraft facility not available";
        }
        
        return String.format("Overdraft used: $%.2f, Available: $%.2f", 
                           overdraftUsed, OVERDRAFT_LIMIT - overdraftUsed);
    }
    
    /**
     * Current account specific method to get available balance
     * @return Available balance including overdraft
     */
    public double getAvailableBalance() {
        return balance + (hasOverdraft ? (OVERDRAFT_LIMIT - overdraftUsed) : 0);
    }
    
    /**
     * Getter for overdraft used
     * @return Amount of overdraft used
     */
    public double getOverdraftUsed() {
        return overdraftUsed;
    }
    
    /**
     * Getter for overdraft limit
     * @return Overdraft limit
     */
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }
    
    /**
     * Getter for monthly fee
     * @return Monthly maintenance fee
     */
    public double getMonthlyFee() {
        return MONTHLY_FEE;
    }
    
    /**
     * Getter for overdraft availability
     * @return True if overdraft is available
     */
    public boolean hasOverdraft() {
        return hasOverdraft;
    }
    
    /**
     * Override toString to include current account specific details
     * @return String representation of the current account
     */
    @Override
    public String toString() {
        return super.toString() + " [Overdraft: " + (hasOverdraft ? "$" + String.format("%.2f", overdraftUsed) + "/$" + String.format("%.2f", OVERDRAFT_LIMIT) : "N/A") + "]";
    }
}
