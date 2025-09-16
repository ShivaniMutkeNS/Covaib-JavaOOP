package composition.banking;

import java.util.*;

/**
 * MAANG-Level Bank Account with Alerts Demo
 * Demonstrates runtime strategy swapping, alert system, and comprehensive banking operations
 */
public class BankAccountDemo {
    
    public static void main(String[] args) {
        System.out.println("🏦 MAANG-Level Bank Account with Alerts System Demo");
        System.out.println("==================================================");
        
        // Create bank account
        BankAccount account = new BankAccount("ACC_123456789", "CUST_001", AccountType.CHECKING, 1000.0);
        
        // Add alert listeners
        account.addAlertListener(new AlertListener() {
            @Override
            public void onAlert(Alert alert) {
                String severityIcon = getSeverityIcon(alert.getSeverity());
                System.out.printf("🚨 %s [%s] %s: %s\n", 
                    severityIcon, alert.getType(), alert.getSeverity(), alert.getMessage());
            }
        });
        
        System.out.println("\n=== 1. Initial Account Setup ===");
        account.displayAccountSummary();
        
        System.out.println("\n=== 2. Basic Banking Operations ===");
        
        // Perform various transactions
        TransactionResult result1 = account.deposit(500.0, "Salary deposit");
        System.out.println("Deposit result: " + (result1.isSuccess() ? "✅ Success" : "❌ Failed"));
        
        TransactionResult result2 = account.withdraw(200.0, "ATM withdrawal");
        System.out.println("Withdrawal result: " + (result2.isSuccess() ? "✅ Success" : "❌ Failed"));
        
        TransactionResult result3 = account.transfer(300.0, "Transfer to savings", "ACC_987654321");
        System.out.println("Transfer result: " + (result3.isSuccess() ? "✅ Success" : "❌ Failed"));
        
        account.displayAccountSummary();
        
        System.out.println("\n=== 3. Alert System Testing ===");
        
        // Trigger low balance alert
        System.out.println("\nTriggering low balance alert...");
        account.withdraw(950.0, "Large withdrawal");
        
        // Trigger large transaction alert
        System.out.println("\nTriggering large transaction alert...");
        account.deposit(2000.0, "Large deposit");
        
        // Trigger unusual activity alert
        System.out.println("\nTriggering unusual activity alert...");
        for (int i = 0; i < 6; i++) {
            account.withdraw(10.0, "Small withdrawal " + (i + 1));
            try {
                Thread.sleep(100); // Small delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("\n=== 4. Strategy Pattern - Component Swapping ===");
        
        // Switch to premium fee calculator
        System.out.println("\nSwitching to premium fee calculator (no fees)...");
        account.setFeeCalculator(new FeeCalculator() {
            @Override
            public double calculateFee(BankAccount account, TransactionType type, double amount) {
                return 0.0; // Premium accounts have no fees
            }
            
            @Override
            public String getCalculatorName() {
                return "Premium Fee Calculator";
            }
        });
        
        account.withdraw(100.0, "Premium withdrawal");
        
        // Switch to high-yield interest calculator
        System.out.println("\nSwitching to high-yield interest calculator...");
        account.setInterestCalculator(new InterestCalculator() {
            @Override
            public double calculateInterest(BankAccount account) {
                return account.getBalance() * 0.05 / 365; // 5% annual rate
            }
            
            @Override
            public String getCalculatorName() {
                return "High-Yield Interest Calculator";
            }
        });
        
        InterestResult interestResult = account.calculateInterest();
        System.out.printf("Daily interest: $%.4f\n", interestResult.getInterestAmount());
        account.applyInterest();
        
        System.out.println("\n=== 5. Custom Alert Rules ===");
        
        // Add custom alert rule
        account.setAlertRule(AlertType.TRANSACTION, new AlertRule() {
            @Override
            public AlertEvaluation evaluate(BankAccount account, Transaction transaction) {
                if (transaction.getType() == TransactionType.WITHDRAWAL && 
                    transaction.getAmount() > account.getBalance() * 0.1) {
                    return new AlertEvaluation(true, 
                        "Withdrawal exceeds 10% of balance", AlertSeverity.MEDIUM);
                }
                return new AlertEvaluation(false, null, AlertSeverity.INFO);
            }
            
            @Override
            public String getRuleName() {
                return "Large Withdrawal Percentage Rule";
            }
        });
        
        System.out.println("Added custom alert rule");
        account.withdraw(250.0, "Test custom rule");
        
        System.out.println("\n=== 6. Transaction Filtering and Search ===");
        
        // Search for specific transactions
        List<Transaction> deposits = account.searchTransactions(transaction -> 
            transaction.getType() == TransactionType.DEPOSIT);
        
        System.out.println("\nDeposit transactions:");
        for (Transaction transaction : deposits) {
            System.out.printf("   %s: $%.2f - %s\n", 
                new Date(transaction.getTimestamp()), 
                transaction.getAmount(), 
                transaction.getDescription());
        }
        
        // Search for large transactions
        List<Transaction> largeTransactions = account.searchTransactions(transaction -> 
            transaction.getAmount() > 500.0);
        
        System.out.println("\nLarge transactions (>$500):");
        for (Transaction transaction : largeTransactions) {
            System.out.printf("   %s: %s $%.2f - %s\n", 
                new Date(transaction.getTimestamp()), 
                transaction.getType(),
                transaction.getAmount(), 
                transaction.getDescription());
        }
        
        System.out.println("\n=== 7. Account Statement Generation ===");
        
        long now = System.currentTimeMillis();
        long oneHourAgo = now - (60 * 60 * 1000);
        
        AccountStatement statement = account.generateStatement(oneHourAgo, now);
        System.out.println("\nAccount Statement (Last Hour):");
        System.out.printf("Period: %s to %s\n", new Date(statement.getStartDate()), new Date(statement.getEndDate()));
        System.out.printf("Starting Balance: $%.2f\n", statement.getStartBalance());
        System.out.printf("Ending Balance: $%.2f\n", statement.getEndBalance());
        System.out.println("Transactions: " + statement.getTransactions().size());
        
        System.out.println("\n=== 8. Security Testing ===");
        
        // Test security validations
        System.out.println("\nTesting rapid transactions (should be blocked)...");
        account.withdraw(50.0, "First rapid transaction");
        TransactionResult rapidResult = account.withdraw(50.0, "Second rapid transaction");
        System.out.println("Rapid transaction result: " + rapidResult.getMessage());
        
        try {
            Thread.sleep(1100); // Wait to reset velocity check
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nTesting very large transaction (should be blocked)...");
        TransactionResult largeResult = account.withdraw(60000.0, "Extremely large withdrawal");
        System.out.println("Large transaction result: " + largeResult.getMessage());
        
        System.out.println("\n=== 9. Account Management ===");
        
        // Test account freezing
        System.out.println("\nFreezing account...");
        account.freezeAccount("Suspicious activity detected");
        
        TransactionResult frozenResult = account.withdraw(10.0, "Test frozen account");
        System.out.println("Transaction on frozen account: " + frozenResult.getMessage());
        
        System.out.println("\nUnfreezing account...");
        account.unfreezeAccount();
        
        TransactionResult unfrozenResult = account.withdraw(10.0, "Test unfrozen account");
        System.out.println("Transaction on unfrozen account: " + (unfrozenResult.isSuccess() ? "✅ Success" : "❌ Failed"));
        
        System.out.println("\n=== 10. Analytics and Reporting ===");
        
        AccountAnalytics analytics = account.getAnalytics();
        System.out.println("\n📊 Account Analytics (30 days):");
        System.out.printf("   Total Deposits: $%.2f\n", analytics.getTotalDeposits());
        System.out.printf("   Total Withdrawals: $%.2f\n", analytics.getTotalWithdrawals());
        System.out.printf("   Average Balance: $%.2f\n", analytics.getAverageBalance());
        System.out.println("   Transaction Count: " + analytics.getTransactionCount());
        
        AccountMetrics metrics = analytics.getMetrics();
        System.out.println("\n📈 Lifetime Metrics:");
        System.out.println("   Total Deposits: " + metrics.getTotalDeposits());
        System.out.println("   Total Withdrawals: " + metrics.getTotalWithdrawals());
        System.out.println("   Total Transfers: " + metrics.getTotalTransfers());
        System.out.printf("   Total Transaction Amount: $%.2f\n", metrics.getTotalTransactionAmount());
        System.out.printf("   Total Fees Paid: $%.2f\n", metrics.getTotalFeeAmount());
        
        System.out.println("\n=== 11. Final Account Summary ===");
        account.displayAccountSummary();
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("✅ Bank Account with Alerts successfully demonstrated:");
        System.out.println("   • Runtime strategy swapping (validators, calculators, security)");
        System.out.println("   • Comprehensive alert system with custom rules");
        System.out.println("   • Real-time transaction monitoring and validation");
        System.out.println("   • Security features and fraud detection");
        System.out.println("   • Account lifecycle management");
        System.out.println("   • Transaction filtering and search capabilities");
        System.out.println("   • Statement generation and reporting");
        System.out.println("   • Analytics and metrics collection");
        System.out.println("   • Event-driven architecture with observers");
        System.out.println("   • Asynchronous alert processing");
        
        // Cleanup
        account.shutdown();
    }
    
    private static String getSeverityIcon(AlertSeverity severity) {
        switch (severity) {
            case CRITICAL: return "🔴";
            case HIGH: return "🟠";
            case MEDIUM: return "🟡";
            case LOW: return "🟢";
            case INFO: return "ℹ️";
            default: return "📢";
        }
    }
}
