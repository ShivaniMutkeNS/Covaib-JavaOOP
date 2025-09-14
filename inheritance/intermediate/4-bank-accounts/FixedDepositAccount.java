/**
 * Fixed Deposit Account class extending Account
 * Demonstrates overriding with rules and encapsulated balance handling
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class FixedDepositAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% annual interest
    private static final int MINIMUM_TERM_MONTHS = 6; // Minimum 6 months
    private static final int MAXIMUM_TERM_MONTHS = 60; // Maximum 60 months
    private int termMonths;
    private String maturityDate;
    private boolean isMatured;
    private double penaltyRate;
    private boolean earlyWithdrawalAllowed;
    
    /**
     * Constructor for FixedDepositAccount
     * @param accountNumber Unique account identifier
     * @param accountHolderName Name of the account holder
     * @param initialBalance Initial balance
     * @param termMonths Term in months
     * @param earlyWithdrawalAllowed Whether early withdrawal is allowed
     */
    public FixedDepositAccount(String accountNumber, String accountHolderName, double initialBalance, 
                              int termMonths, boolean earlyWithdrawalAllowed) {
        super(accountNumber, accountHolderName, initialBalance, "Fixed Deposit", initialBalance);
        this.termMonths = Math.max(MINIMUM_TERM_MONTHS, Math.min(MAXIMUM_TERM_MONTHS, termMonths));
        this.earlyWithdrawalAllowed = earlyWithdrawalAllowed;
        this.isMatured = false;
        this.penaltyRate = 0.02; // 2% penalty for early withdrawal
        this.maturityDate = calculateMaturityDate();
    }
    
    /**
     * Override withdraw method with fixed deposit rules
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
        
        // Check if account is matured
        if (isMatured) {
            balance -= amount;
            System.out.println("Withdrawal successful. Amount: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", balance));
            return true;
        }
        
        // Check if early withdrawal is allowed
        if (!earlyWithdrawalAllowed) {
            System.out.println("Early withdrawal not allowed. Maturity date: " + maturityDate);
            return false;
        }
        
        // Calculate penalty for early withdrawal
        double penalty = amount * penaltyRate;
        double totalDeduction = amount + penalty;
        
        if (totalDeduction > balance) {
            System.out.println("Insufficient funds to cover withdrawal and penalty.");
            return false;
        }
        
        balance -= totalDeduction;
        System.out.println("Early withdrawal successful. Amount: $" + String.format("%.2f", amount));
        System.out.println("Penalty charged: $" + String.format("%.2f", penalty));
        System.out.println("Total deduction: $" + String.format("%.2f", totalDeduction));
        System.out.println("New balance: $" + String.format("%.2f", balance));
        
        return true;
    }
    
    /**
     * Override calculateInterest method with fixed deposit interest rate
     * @return The interest amount
     */
    @Override
    public double calculateInterest() {
        if (!isActive) {
            return 0.0;
        }
        
        // Calculate interest based on term
        double termMultiplier = (double) termMonths / 12.0;
        double interest = balance * INTEREST_RATE * termMultiplier;
        
        System.out.println("Interest calculated: $" + String.format("%.2f", interest) + 
                          " (Rate: " + String.format("%.1f", INTEREST_RATE * 100) + "%, Term: " + termMonths + " months)");
        return interest;
    }
    
    /**
     * Override getAccountFeatures method with fixed deposit features
     * @return String description of fixed deposit features
     */
    @Override
    public String getAccountFeatures() {
        return "Fixed Deposit Features: " +
               "5% annual interest, " +
               "Term: " + termMonths + " months, " +
               "Maturity date: " + maturityDate + ", " +
               "Early withdrawal: " + (earlyWithdrawalAllowed ? "Allowed with penalty" : "Not allowed") + ", " +
               "Penalty rate: " + String.format("%.1f", penaltyRate * 100) + "%, " +
               "No monthly fees, " +
               "Higher interest rate";
    }
    
    /**
     * Fixed deposit specific method to check maturity
     * @return True if account is matured
     */
    public boolean checkMaturity() {
        if (isMatured) {
            return true;
        }
        
        String today = java.time.LocalDate.now().toString();
        if (today.compareTo(maturityDate) >= 0) {
            isMatured = true;
            System.out.println("Account has matured! You can now withdraw without penalty.");
            return true;
        }
        
        return false;
    }
    
    /**
     * Fixed deposit specific method to get maturity information
     * @return String with maturity information
     */
    public String getMaturityInfo() {
        if (isMatured) {
            return "Account is matured. You can withdraw without penalty.";
        }
        
        String today = java.time.LocalDate.now().toString();
        long daysToMaturity = java.time.LocalDate.parse(maturityDate).toEpochDay() - 
                             java.time.LocalDate.parse(today).toEpochDay();
        
        return String.format("Maturity date: %s, Days remaining: %d", maturityDate, daysToMaturity);
    }
    
    /**
     * Fixed deposit specific method to renew account
     * @param newTermMonths New term in months
     * @return True if renewal successful
     */
    public boolean renewAccount(int newTermMonths) {
        if (!isMatured) {
            System.out.println("Account must be matured to renew.");
            return false;
        }
        
        this.termMonths = Math.max(MINIMUM_TERM_MONTHS, Math.min(MAXIMUM_TERM_MONTHS, newTermMonths));
        this.maturityDate = calculateMaturityDate();
        this.isMatured = false;
        
        System.out.println("Account renewed for " + termMonths + " months.");
        System.out.println("New maturity date: " + maturityDate);
        
        return true;
    }
    
    /**
     * Fixed deposit specific method to calculate maturity value
     * @return The maturity value including interest
     */
    public double calculateMaturityValue() {
        double interest = calculateInterest();
        return balance + interest;
    }
    
    /**
     * Fixed deposit specific method to get term information
     * @return String with term information
     */
    public String getTermInfo() {
        return String.format("Term: %d months, Maturity date: %s, Matured: %s", 
                           termMonths, maturityDate, isMatured);
    }
    
    /**
     * Calculate maturity date based on term
     * @return Maturity date as string
     */
    private String calculateMaturityDate() {
        return java.time.LocalDate.now().plusMonths(termMonths).toString();
    }
    
    /**
     * Getter for term months
     * @return Term in months
     */
    public int getTermMonths() {
        return termMonths;
    }
    
    /**
     * Getter for maturity date
     * @return Maturity date
     */
    public String getMaturityDate() {
        return maturityDate;
    }
    
    /**
     * Getter for maturity status
     * @return True if account is matured
     */
    public boolean isMatured() {
        return isMatured;
    }
    
    /**
     * Getter for penalty rate
     * @return Penalty rate for early withdrawal
     */
    public double getPenaltyRate() {
        return penaltyRate;
    }
    
    /**
     * Getter for early withdrawal allowance
     * @return True if early withdrawal is allowed
     */
    public boolean isEarlyWithdrawalAllowed() {
        return earlyWithdrawalAllowed;
    }
    
    /**
     * Override toString to include fixed deposit specific details
     * @return String representation of the fixed deposit account
     */
    @Override
    public String toString() {
        return super.toString() + " [Term: " + termMonths + " months, Maturity: " + maturityDate + 
               ", Matured: " + isMatured + "]";
    }
}
