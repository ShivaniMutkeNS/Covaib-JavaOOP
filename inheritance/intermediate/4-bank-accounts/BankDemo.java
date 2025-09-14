/**
 * Demo class to showcase bank account system
 * Demonstrates inheritance, method overriding, and encapsulated balance handling
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class BankDemo {
    public static void main(String[] args) {
        System.out.println("🏦 BANK ACCOUNT SYSTEM 🏦");
        System.out.println("=" .repeat(50));
        
        // Create different types of accounts
        Account[] accounts = {
            new SavingsAccount("SA001", "Alice Johnson", 1000.0),
            new SavingsAccount("SA002", "Bob Smith", 500.0),
            new CurrentAccount("CA001", "Carol Davis", 2000.0, true),
            new CurrentAccount("CA002", "David Wilson", 1500.0, false),
            new FixedDepositAccount("FD001", "Eva Brown", 10000.0, 12, true),
            new FixedDepositAccount("FD002", "Frank Miller", 5000.0, 24, false)
        };
        
        // Display account information
        System.out.println("\n📋 ACCOUNT INFORMATION:");
        System.out.println("-".repeat(50));
        for (Account account : accounts) {
            System.out.println(account.getAccountInfo());
        }
        
        // Demonstrate deposits
        System.out.println("\n💰 DEPOSIT DEMONSTRATION:");
        System.out.println("-".repeat(50));
        double[] depositAmounts = {200.0, 300.0, 500.0, 1000.0, 2000.0, 3000.0};
        
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("\n" + accounts[i].getAccountHolderName() + " (" + accounts[i].getAccountType() + "):");
            accounts[i].deposit(depositAmounts[i]);
        }
        
        // Demonstrate withdrawals with different rules
        System.out.println("\n💸 WITHDRAWAL DEMONSTRATION:");
        System.out.println("-".repeat(50));
        double[] withdrawalAmounts = {100.0, 200.0, 500.0, 1000.0, 2000.0, 3000.0};
        
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("\n" + accounts[i].getAccountHolderName() + " (" + accounts[i].getAccountType() + "):");
            accounts[i].withdraw(withdrawalAmounts[i]);
        }
        
        // Demonstrate interest calculation
        System.out.println("\n📈 INTEREST CALCULATION:");
        System.out.println("-".repeat(50));
        for (Account account : accounts) {
            System.out.println("\n" + account.getAccountHolderName() + " (" + account.getAccountType() + "):");
            double interest = account.calculateInterest();
            System.out.println("Interest earned: $" + String.format("%.2f", interest));
        }
        
        // Display account features
        System.out.println("\n🏠 ACCOUNT FEATURES:");
        System.out.println("-".repeat(50));
        for (Account account : accounts) {
            System.out.println("\n" + account.getAccountHolderName() + " (" + account.getAccountType() + "):");
            System.out.println(account.getAccountFeatures());
        }
        
        // Demonstrate account-specific behaviors
        System.out.println("\n🔧 ACCOUNT-SPECIFIC BEHAVIORS:");
        System.out.println("-".repeat(50));
        
        // Savings Account specific behaviors
        SavingsAccount savingsAccount = new SavingsAccount("SA003", "Grace Lee", 2000.0);
        System.out.println("Savings Account Behaviors:");
        System.out.println(savingsAccount.getWithdrawalHistory());
        System.out.println("Can withdraw $500: " + savingsAccount.canWithdraw(500.0));
        System.out.println("Can withdraw $2000: " + savingsAccount.canWithdraw(2000.0));
        savingsAccount.withdraw(100.0);
        System.out.println(savingsAccount.getWithdrawalHistory());
        System.out.println();
        
        // Current Account specific behaviors
        CurrentAccount currentAccount = new CurrentAccount("CA003", "Henry Chen", 1000.0, true);
        System.out.println("Current Account Behaviors:");
        System.out.println(currentAccount.getOverdraftStatus());
        System.out.println("Available balance: $" + String.format("%.2f", currentAccount.getAvailableBalance()));
        currentAccount.withdraw(1500.0); // Test overdraft
        System.out.println(currentAccount.getOverdraftStatus());
        currentAccount.payOverdraft(200.0);
        System.out.println(currentAccount.getOverdraftStatus());
        currentAccount.chargeMonthlyFee();
        System.out.println();
        
        // Fixed Deposit specific behaviors
        FixedDepositAccount fixedDeposit = new FixedDepositAccount("FD003", "Ivy Rodriguez", 15000.0, 18, true);
        System.out.println("Fixed Deposit Behaviors:");
        System.out.println(fixedDeposit.getMaturityInfo());
        System.out.println(fixedDeposit.getTermInfo());
        System.out.println("Maturity value: $" + String.format("%.2f", fixedDeposit.calculateMaturityValue()));
        fixedDeposit.withdraw(5000.0); // Test early withdrawal
        System.out.println(fixedDeposit.getMaturityInfo());
        System.out.println();
        
        // Demonstrate minimum balance requirements
        System.out.println("\n⚖️ MINIMUM BALANCE REQUIREMENTS:");
        System.out.println("-".repeat(50));
        for (Account account : accounts) {
            System.out.println(account.getAccountHolderName() + " (" + account.getAccountType() + "):");
            System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("Minimum required: $" + String.format("%.2f", account.getMinimumBalance()));
            System.out.println("Meets requirement: " + account.meetsMinimumBalance());
            System.out.println();
        }
        
        // Demonstrate account deactivation
        System.out.println("\n🚫 ACCOUNT DEACTIVATION:");
        System.out.println("-".repeat(50));
        for (Account account : accounts) {
            System.out.println(account.getAccountHolderName() + " (" + account.getAccountType() + "):");
            if (account.getBalance() == 0) {
                account.deactivateAccount();
            } else {
                System.out.println("Cannot deactivate account with positive balance.");
            }
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(accounts);
        
        // Demonstrate balance comparison
        System.out.println("\n💰 BALANCE COMPARISON:");
        System.out.println("-".repeat(50));
        System.out.println("Account Type\t\tBalance\t\tInterest Rate\tInterest Earned");
        System.out.println("-".repeat(50));
        
        for (Account account : accounts) {
            double interest = account.calculateInterest();
            String interestRate = "";
            
            if (account instanceof SavingsAccount) {
                interestRate = "2.0%";
            } else if (account instanceof CurrentAccount) {
                interestRate = "1.0%";
            } else if (account instanceof FixedDepositAccount) {
                interestRate = "5.0%";
            }
            
            System.out.printf("%-15s\t$%.2f\t\t%s\t\t$%.2f%n", 
                            account.getAccountType(), account.getBalance(), interestRate, interest);
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param accounts Array of Account objects
     */
    public static void demonstratePolymorphism(Account[] accounts) {
        System.out.println("Processing accounts using polymorphism:");
        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            System.out.println((i + 1) + ". " + account.getAccountHolderName() + " (" + account.getAccountType() + ")");
            System.out.println("   Balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("   Features: " + account.getAccountFeatures());
            System.out.println();
        }
    }
}
