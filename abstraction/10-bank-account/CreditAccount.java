/**
 * Credit Account implementation with credit limit and interest charges
 */
public class CreditAccount extends BankAccount {
    private double creditLimit;
    private double availableCredit;
    private double minimumPayment;
    private double interestCharges;
    private int gracePeriodDays;
    private boolean inGracePeriod;
    
    public CreditAccount(String accountNumber, String accountHolderName, double creditLimit) {
        super(accountNumber, accountHolderName, AccountType.CREDIT, 0.0);
        this.creditLimit = creditLimit;
        this.availableCredit = creditLimit;
        this.minimumPayment = 0.0;
        this.interestCharges = 0.0;
        this.gracePeriodDays = 25;
        this.inGracePeriod = true;
        this.balance = 0.0; // Credit accounts start at $0 balance (no debt)
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // For credit accounts, "withdrawal" is actually charging/borrowing
        return amount <= availableCredit;
    }
    
    @Override
    public void calculateInterest() {
        // Credit accounts charge interest on outstanding balance
        if (balance > 0 && !inGracePeriod) { // Positive balance means debt
            double monthlyInterest = balance * accountType.getMonthlyInterestRate();
            balance += monthlyInterest;
            interestCharges += monthlyInterest;
            availableCredit = creditLimit - balance;
            
            Transaction interestTransaction = new Transaction(TransactionType.FEE_DEBIT, 
                monthlyInterest, "Interest charge on outstanding balance", balance);
            transactionHistory.add(interestTransaction);
            
            System.out.println("💸 Interest charged: $" + String.format("%.2f", monthlyInterest));
        }
        
        // Calculate minimum payment (2% of balance or $25, whichever is higher)
        if (balance > 0) {
            minimumPayment = Math.max(balance * 0.02, 25.0);
            if (minimumPayment > balance) {
                minimumPayment = balance;
            }
        } else {
            minimumPayment = 0.0;
        }
    }
    
    @Override
    public void applyMonthlyFees() {
        // Credit cards may have annual fees (applied monthly)
        if (creditLimit > 5000) { // Premium cards have annual fees
            double monthlyAnnualFee = 99.0 / 12.0; // $99 annual fee
            balance += monthlyAnnualFee;
            availableCredit = creditLimit - balance;
            
            Transaction feeTransaction = new Transaction(TransactionType.MAINTENANCE_FEE, 
                monthlyAnnualFee, "Monthly portion of annual fee", balance);
            transactionHistory.add(feeTransaction);
            
            System.out.println("💸 Annual fee (monthly): $" + String.format("%.2f", monthlyAnnualFee));
        }
        
        // Late payment fee if minimum payment not made
        if (minimumPayment > 0) {
            System.out.println("⚠️ Minimum payment due: $" + String.format("%.2f", minimumPayment));
        }
    }
    
    @Override
    public double getAvailableBalance() {
        return availableCredit; // Available credit for spending
    }
    
    // Credit-specific deposit (payment toward balance)
    @Override
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            System.out.println("❌ Payment amount must be positive");
            return false;
        }
        
        if (!isActive) {
            System.out.println("❌ Account is inactive");
            return false;
        }
        
        balance -= amount; // Reduce debt
        if (balance < 0) {
            balance = 0; // Can't have negative debt (overpayment becomes credit)
        }
        
        availableCredit = creditLimit - balance;
        
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, 
            "Payment - " + description, balance);
        transactionHistory.add(transaction);
        
        System.out.println("✅ Payment received: $" + String.format("%.2f", amount));
        
        // Check if minimum payment is satisfied
        if (amount >= minimumPayment) {
            minimumPayment = 0;
            System.out.println("✅ Minimum payment satisfied");
        }
        
        return true;
    }
    
    // Credit-specific withdrawal (charge/purchase)
    public boolean charge(double amount, String description) {
        if (amount <= 0) {
            System.out.println("❌ Charge amount must be positive");
            return false;
        }
        
        if (!isActive) {
            System.out.println("❌ Account is inactive");
            return false;
        }
        
        if (!canWithdraw(amount)) {
            System.out.println("❌ Insufficient credit limit");
            return false;
        }
        
        balance += amount; // Increase debt
        availableCredit = creditLimit - balance;
        
        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, 
            "Charge - " + description, balance);
        transactionHistory.add(transaction);
        
        System.out.println("💳 Charged: $" + String.format("%.2f", amount) + " - " + description);
        return true;
    }
    
    public boolean onlinePurchase(double amount, String merchant) {
        return charge(amount, "Online purchase - " + merchant);
    }
    
    public boolean cashAdvance(double amount) {
        double cashAdvanceFee = amount * 0.03; // 3% fee
        double totalAmount = amount + cashAdvanceFee;
        
        if (charge(totalAmount, "Cash advance + fee")) {
            System.out.println("💰 Cash advance: $" + String.format("%.2f", amount) + 
                             " (Fee: $" + String.format("%.2f", cashAdvanceFee) + ")");
            return true;
        }
        return false;
    }
    
    public void makeMinimumPayment() {
        if (minimumPayment > 0) {
            deposit(minimumPayment, "Minimum payment");
        } else {
            System.out.println("ℹ️ No minimum payment due");
        }
    }
    
    public void payOffBalance() {
        if (balance > 0) {
            deposit(balance, "Full balance payment");
            System.out.println("🎉 Credit card balance paid in full!");
        } else {
            System.out.println("ℹ️ No balance to pay off");
        }
    }
    
    public double getCreditUtilization() {
        return (balance / creditLimit) * 100.0;
    }
    
    // Getters
    public double getCreditLimit() { return creditLimit; }
    public double getAvailableCredit() { return availableCredit; }
    public double getMinimumPayment() { return minimumPayment; }
    public double getInterestCharges() { return interestCharges; }
    public boolean isInGracePeriod() { return inGracePeriod; }
    
    @Override
    public void printStatement() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💳 CREDIT CARD STATEMENT");
        System.out.println("=".repeat(60));
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Account Type: " + accountType.getDisplayName());
        System.out.println("Credit Limit: $" + String.format("%.2f", creditLimit));
        System.out.println("Available Credit: $" + String.format("%.2f", availableCredit));
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
        System.out.println("Minimum Payment: $" + String.format("%.2f", minimumPayment));
        System.out.println("Credit Utilization: " + String.format("%.1f%%", getCreditUtilization()));
        System.out.println("Total Interest Charges: $" + String.format("%.2f", interestCharges));
        System.out.println("APR: " + String.format("%.1f%%", accountType.getAnnualInterestRate() * 100));
        
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
}
