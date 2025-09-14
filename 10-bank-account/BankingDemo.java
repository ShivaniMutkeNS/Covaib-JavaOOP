/**
 * Comprehensive demonstration of the banking system abstraction
 * Shows polymorphism, abstraction, and real-world banking scenarios
 */
public class BankingDemo {
    
    public static void main(String[] args) {
        System.out.println("🏦 Banking System Demonstration");
        System.out.println("===============================\n");
        
        // Create different types of bank accounts
        BankAccount[] accounts = {
            new SavingsAccount("SAV-001", "Alice Johnson", 5000.0),
            new CheckingAccount("CHK-001", "Bob Smith", 2500.0),
            new CreditAccount("CC-001", "Carol Davis", 10000.0),
            new InvestmentAccount("INV-001", "David Wilson", 15000.0)
        };
        
        // Demonstrate polymorphism with basic operations
        demonstratePolymorphism(accounts);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate account-specific features
        demonstrateAccountSpecificFeatures();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate monthly processing
        demonstrateMonthlyProcessing(accounts);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate transfer operations
        demonstrateTransfers();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate comprehensive scenarios
        demonstrateRealWorldScenarios();
    }
    
    private static void demonstratePolymorphism(BankAccount[] accounts) {
        System.out.println("🎯 POLYMORPHISM DEMONSTRATION");
        System.out.println("Same methods, different behaviors across account types\n");
        
        for (BankAccount account : accounts) {
            System.out.println("Account Type: " + account.getClass().getSimpleName());
            System.out.println("Initial Balance: $" + String.format("%.2f", account.getBalance()));
            
            // Same method calls, different implementations
            account.deposit(500.0, "Test deposit");
            account.withdraw(200.0, "Test withdrawal");
            
            System.out.println("Available Balance: $" + String.format("%.2f", account.getAvailableBalance()));
            System.out.println("Account Info: " + account.toString());
            System.out.println();
        }
    }
    
    private static void demonstrateAccountSpecificFeatures() {
        System.out.println("🌟 ACCOUNT-SPECIFIC FEATURES");
        System.out.println("Each account type has unique capabilities\n");
        
        // Savings Account Features
        System.out.println("💰 Savings Account Features:");
        SavingsAccount savings = new SavingsAccount("SAV-002", "Emma Brown", 10000.0);
        savings.enableCompoundInterest(true);
        savings.calculateInterest();
        System.out.println("Projected Annual Interest: $" + String.format("%.2f", savings.getAnnualInterestProjection()));
        System.out.println();
        
        // Checking Account Features
        System.out.println("📝 Checking Account Features:");
        CheckingAccount checking = new CheckingAccount("CHK-002", "Frank Miller", 3000.0);
        checking.writeCheck(150.0, "Electric Company");
        checking.atmWithdrawal(100.0);
        checking.onlinePurchase(75.50, "Amazon");
        checking.setOverdraftProtection(true);
        System.out.println();
        
        // Credit Account Features
        System.out.println("💳 Credit Account Features:");
        CreditAccount credit = new CreditAccount("CC-002", "Grace Lee", 5000.0);
        credit.charge(250.0, "Grocery shopping");
        credit.onlinePurchase(89.99, "Best Buy");
        credit.cashAdvance(300.0);
        System.out.println("Credit Utilization: " + String.format("%.1f%%", credit.getCreditUtilization()));
        credit.makeMinimumPayment();
        System.out.println();
        
        // Investment Account Features
        System.out.println("📊 Investment Account Features:");
        InvestmentAccount investment = new InvestmentAccount("INV-002", "Henry Taylor", 20000.0);
        investment.setRiskLevel("Aggressive");
        investment.buyInvestment(5000.0, "Tech Growth Fund");
        investment.rebalancePortfolio();
        investment.generatePerformanceReport();
        System.out.println();
    }
    
    private static void demonstrateMonthlyProcessing(BankAccount[] accounts) {
        System.out.println("📅 MONTHLY PROCESSING DEMONSTRATION");
        System.out.println("Interest calculations, fees, and maintenance\n");
        
        for (BankAccount account : accounts) {
            System.out.println("Processing: " + account.getAccountNumber() + " (" + 
                             account.getClass().getSimpleName() + ")");
            account.processMonthlyMaintenance();
            System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println();
        }
    }
    
    private static void demonstrateTransfers() {
        System.out.println("💸 TRANSFER OPERATIONS");
        System.out.println("Moving money between different account types\n");
        
        SavingsAccount savingsFrom = new SavingsAccount("SAV-003", "John Doe", 8000.0);
        CheckingAccount checkingTo = new CheckingAccount("CHK-003", "Jane Doe", 1500.0);
        
        System.out.println("Before Transfer:");
        System.out.println("Savings: $" + String.format("%.2f", savingsFrom.getBalance()));
        System.out.println("Checking: $" + String.format("%.2f", checkingTo.getBalance()));
        
        // Transfer from savings to checking
        savingsFrom.transfer(checkingTo, 1000.0, "Monthly budget transfer");
        
        System.out.println("\nAfter Transfer:");
        System.out.println("Savings: $" + String.format("%.2f", savingsFrom.getBalance()));
        System.out.println("Checking: $" + String.format("%.2f", checkingTo.getBalance()));
        System.out.println();
    }
    
    private static void demonstrateRealWorldScenarios() {
        System.out.println("🌍 REAL-WORLD BANKING SCENARIOS");
        System.out.println("Complex banking situations and edge cases\n");
        
        // Scenario 1: Overdraft Protection
        System.out.println("Scenario 1: Overdraft Protection Test");
        CheckingAccount overdraftTest = new CheckingAccount("CHK-004", "Mike Johnson", 100.0);
        overdraftTest.setOverdraftProtection(true);
        overdraftTest.withdraw(150.0, "Emergency expense"); // Should trigger overdraft
        overdraftTest.printStatement();
        
        // Scenario 2: Credit Card Payment Cycle
        System.out.println("Scenario 2: Credit Card Payment Cycle");
        CreditAccount creditCycle = new CreditAccount("CC-003", "Sarah Wilson", 2000.0);
        creditCycle.charge(500.0, "Monthly expenses");
        creditCycle.charge(200.0, "Gas and groceries");
        creditCycle.calculateInterest(); // Calculate interest and minimum payment
        System.out.println("Minimum Payment Due: $" + String.format("%.2f", creditCycle.getMinimumPayment()));
        creditCycle.deposit(100.0, "Partial payment");
        creditCycle.payOffBalance(); // Pay remaining balance
        
        // Scenario 3: Investment Volatility
        System.out.println("\nScenario 3: Investment Market Simulation");
        InvestmentAccount volatileInvestment = new InvestmentAccount("INV-003", "Robert Chen", 25000.0);
        volatileInvestment.setRiskLevel("Aggressive");
        
        // Simulate 3 months of market activity
        for (int month = 1; month <= 3; month++) {
            System.out.println("Month " + month + ":");
            volatileInvestment.calculateInterest();
            volatileInvestment.applyMonthlyFees();
            System.out.println("Portfolio Value: $" + String.format("%.2f", volatileInvestment.getPortfolioValue()));
        }
        
        // Scenario 4: Transaction Limits
        System.out.println("\nScenario 4: Transaction Limit Testing");
        SavingsAccount limitTest = new SavingsAccount("SAV-004", "Lisa Anderson", 3000.0);
        
        // Try to exceed monthly transaction limit
        for (int i = 1; i <= 8; i++) {
            boolean success = limitTest.withdraw(50.0, "Transaction #" + i);
            if (!success) {
                System.out.println("Transaction limit reached at transaction #" + i);
                break;
            }
        }
        
        System.out.println("\n🎉 Banking System Demonstration Complete!");
        System.out.println("All abstraction concepts successfully demonstrated:");
        System.out.println("✅ Abstract classes and methods");
        System.out.println("✅ Polymorphism across account types");
        System.out.println("✅ Encapsulation of account-specific data");
        System.out.println("✅ Inheritance hierarchies");
        System.out.println("✅ Real-world banking operations");
    }
}
